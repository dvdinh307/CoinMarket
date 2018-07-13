package midas.coinmarket.controller.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import midas.coinmarket.R;
import midas.coinmarket.model.CoinObject;
import midas.coinmarket.model.DatabaseHelper;
import midas.coinmarket.utils.BaseActivity;
import midas.coinmarket.view.adapter.BookmarkAdapter;
import midas.coinmarket.view.adapter.MainAdapter;

public class BookmarkActivity extends BaseActivity {
    @BindView(R.id.rcy_book)
    RecyclerView mRcyBook;
    @BindView(R.id.tv_no_data)
    TextView mTvNoData;

    private DatabaseHelper mHelper;
    private ArrayList<CoinObject> mListCoin = new ArrayList<>();
    private BookmarkAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bookmark;
    }

    @Override
    public void initFunction() {
        mHelper = new DatabaseHelper(BookmarkActivity.this);
        mListCoin = mHelper.getAllBookmark();
        mRcyBook.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BookmarkAdapter(mListCoin, false, false);
        mAdapter.setListner(new BookmarkAdapter.onActionBookmark() {
            @Override
            public void onEdit(CoinObject coin, int position) {
                Toast.makeText(BookmarkActivity.this, "Not done", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDelete(CoinObject coin, int position) {
                boolean remove = mHelper.removeItemBookmark(String.valueOf(coin.getId()));
                if (remove) {
                    mListCoin.remove(position);
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(BookmarkActivity.this, "Delete success !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BookmarkActivity.this, "Cannot delete now !", Toast.LENGTH_SHORT).show();
                }
                mTvNoData.setVisibility(mListCoin.size() > 0 ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onPinToTop(CoinObject coin, int position) {
                if (mHelper.pinToTop(String.valueOf(coin.getId()))) {
                    CoinObject coinTop = mListCoin.get(position);
                    mListCoin.remove(position);
                    mListCoin.add(0, coinTop);
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(BookmarkActivity.this, "Pin success !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BookmarkActivity.this, "Cannot pin now !", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mRcyBook.setAdapter(mAdapter);
        mTvNoData.setVisibility(mListCoin.size() > 0 ? View.GONE : View.VISIBLE);
    }

    @OnClick({R.id.imv_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.imv_back:
                finish();
                break;
        }
    }
}
