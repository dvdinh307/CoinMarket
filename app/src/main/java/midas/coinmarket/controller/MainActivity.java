package midas.coinmarket.controller;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.paginate.Paginate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import midas.coinmarket.R;
import midas.coinmarket.model.CoinObject;
import midas.coinmarket.utils.AppConstants;
import midas.coinmarket.utils.BaseActivity;
import midas.coinmarket.utils.RequestDataUtils;
import midas.coinmarket.view.CustomLoadingListItemCreator;
import midas.coinmarket.view.adapter.MainAdapter;

public class MainActivity extends BaseActivity implements Paginate.Callbacks {
    private final int NUMBER_ITEM = 10;
    @BindView(R.id.rcy_main)
    RecyclerView mRcyMain;

    private int page = 1;
    private boolean isLoading;
    private MainAdapter mAdapter;
    private ArrayList<CoinObject> mListCoin = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initFunction() {
        mAdapter = new MainAdapter(mListCoin, false, false);
        mRcyMain.setLayoutManager(new LinearLayoutManager(this));
        mRcyMain.setAdapter(mAdapter);
        Paginate.with(mRcyMain, this)
                .setLoadingTriggerThreshold(0)
                .setLoadingListItemCreator(new CustomLoadingListItemCreator(MainActivity.this))
                .addLoadingListItem(true)
                .build();


    }

    @Override
    public void onLoadMore() {
        getData("EUR", "id");
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return page == 0;
    }

    public void getData(final String currency, String sort) {
        isLoading = true;
        final HashMap<String, String> params = new HashMap<>();
        params.put(AppConstants.KEY_PARAMS.LIMIT.toString(), String.valueOf(NUMBER_ITEM));
        params.put(AppConstants.KEY_PARAMS.SORT.toString(), sort);
        params.put(AppConstants.KEY_PARAMS.START.toString(), String.valueOf(page * NUMBER_ITEM));
        params.put(AppConstants.KEY_PARAMS.STRUCTURE.toString(), "array");
        if (currency.length() > 0)
            params.put(AppConstants.KEY_PARAMS.CONVERT.toString(), currency);
        RequestDataUtils.requestData(Request.Method.GET, AppConstants.PATH_URL.LIST_TICKER.toString(), params, new RequestDataUtils.onResult() {
            @Override
            public void onSuccess(JSONObject object) {
                if (page == 1)
                    mListCoin.clear();
                if (object.length() > 0) {
                    // data.
                    try {
                        JSONArray datas = object.getJSONArray(AppConstants.KEY_PARAMS.DATA.toString());
                        if (datas.length() > 0) {
                            for (int i = 0; i < datas.length(); i++) {
                                mListCoin.add(CoinObject.parserData(datas.getJSONObject(i), currency));
                            }
                        }
                        isLoading = false;
                        page++;
                        mAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    isLoading = false;
                    page = 0;
                    mAdapter.notifyDataSetChanged();
                }
                Log.e("Values", "Values : " + object.toString());
            }

            @Override
            public void onFail(int error) {
                isLoading = false;
                page = 0;
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
