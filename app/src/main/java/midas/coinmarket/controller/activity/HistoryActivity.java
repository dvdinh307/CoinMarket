package midas.coinmarket.controller.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import midas.coinmarket.R;
import midas.coinmarket.controller.SpecificCurrencyActivity;
import midas.coinmarket.model.CryptocurrencyObject;
import midas.coinmarket.model.DatabaseHelper;
import midas.coinmarket.utils.AppConstants;
import midas.coinmarket.utils.BaseActivity;
import midas.coinmarket.view.adapter.HistoryAdapter;

public class HistoryActivity extends BaseActivity {
    @BindView(R.id.lst_history)
    ListView mListHistory;
    @BindView(R.id.tv_no_data)
    TextView mTvNoData;
    private DatabaseHelper mHelper;
    private HistoryAdapter mAdapter;
    private ArrayList<CryptocurrencyObject> mList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_history;
    }

    @Override
    public void initFunction() {
        mList = new ArrayList<>();
        mHelper = new DatabaseHelper(HistoryActivity.this);
        mList = mHelper.getAllHistory();
        mAdapter = new HistoryAdapter(HistoryActivity.this, mList);
        mListHistory.setAdapter(mAdapter);
        mListHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CryptocurrencyObject cryobject = mList.get(position);
                startActivity(new Intent(HistoryActivity.this, SpecificCurrencyActivity.class).putExtra(AppConstants.INTENT.DATA, String.valueOf(cryobject.getId())));
            }
        });
        mTvNoData.setVisibility(mList.size() > 0 ? View.GONE : View.VISIBLE);
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
