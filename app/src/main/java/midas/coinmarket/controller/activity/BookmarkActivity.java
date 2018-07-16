package midas.coinmarket.controller.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import midas.coinmarket.R;
import midas.coinmarket.controller.dialog.LoadingDialog;
import midas.coinmarket.model.BookMarkOnlineObject;
import midas.coinmarket.model.CoinObject;
import midas.coinmarket.model.DatabaseHelper;
import midas.coinmarket.utils.AppConstants;
import midas.coinmarket.utils.BaseActivity;
import midas.coinmarket.view.adapter.BookmarkAdapter;

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
                    getmDatabaseOnline().child(AppConstants.DB_VALUES.TBL_BOOK_MARK + "/" + getUser().getId() + "/" + coin.getId()).removeValue();
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
                String time = new Date().toString();
                if (mHelper.pinToTop(String.valueOf(coin.getId()), time)) {
                    getmDatabaseOnline().child(AppConstants.DB_VALUES.TBL_BOOK_MARK + "/" + getUser().getId() + "/" + coin.getId()).child("time").setValue(time);
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
        getListDataOnline();
    }

    private void getListDataOffline() {
        mListCoin.addAll(mHelper.getAllBookmark());
        mAdapter.notifyDataSetChanged();
        mTvNoData.setVisibility(mListCoin.size() > 0 ? View.GONE : View.VISIBLE);
        LoadingDialog.getDialog(BookmarkActivity.this).dismiss();
    }

    private void getListDataOnline() {
        LoadingDialog.getDialog(BookmarkActivity.this).show();
        getmDatabaseOnline().child(AppConstants.DB_VALUES.TBL_BOOK_MARK + "/" + getUser().getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mListCoin.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BookMarkOnlineObject object = snapshot.getValue(BookMarkOnlineObject.class);
                    if (null != object) {
                        String data = object.getContent();
                        try {
                            CoinObject coin = CoinObject.parserData(new JSONObject(data), "");
                            mHelper.insertBookMark(coin, data, object.getTime());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                getListDataOffline();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                LoadingDialog.getDialog(BookmarkActivity.this).dismiss();
            }
        });
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
