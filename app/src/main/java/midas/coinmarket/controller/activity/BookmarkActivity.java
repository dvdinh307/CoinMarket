package midas.coinmarket.controller.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import midas.coinmarket.R;
import midas.coinmarket.model.CoinObject;
import midas.coinmarket.model.DatabaseHelper;
import midas.coinmarket.utils.BaseActivity;
import midas.coinmarket.view.adapter.MainAdapter;

public class BookmarkActivity extends BaseActivity {
    @BindView(R.id.rcy_book)
    RecyclerView mRcyBook;

    private DatabaseHelper mHelper;
    private ArrayList<CoinObject> mListCoin = new ArrayList<>();
    private MainAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bookmark;
    }

    @Override
    public void initFunction() {
        mHelper = new DatabaseHelper(BookmarkActivity.this);
        mListCoin = mHelper.getAllBookmark();
        mRcyBook.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MainAdapter(mListCoin, false, false);
        mRcyBook.setAdapter(mAdapter);
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
