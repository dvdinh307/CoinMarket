package midas.coinmarket.controller;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.paginate.Paginate;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import midas.coinmarket.R;
import midas.coinmarket.utils.AppConstants;
import midas.coinmarket.utils.BaseActivity;
import midas.coinmarket.utils.RequestDataUtils;
import midas.coinmarket.view.CustomLoadingListItemCreator;

public class MainActivity extends BaseActivity implements Paginate.Callbacks {
    @BindView(R.id.rcy_main)
    RecyclerView mRcyMain;

    private int page = 1;
    private boolean isLoading;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initFunction() {
        Paginate.with(mRcyMain, this)
                .setLoadingTriggerThreshold(0)
                .setLoadingListItemCreator(new CustomLoadingListItemCreator(MainActivity.this))
                .addLoadingListItem(true)
                .build();


    }

    @Override
    public void onLoadMore() {
        getData();
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return page == 0;
    }

    public void getData() {
        HashMap<String, String> params = new HashMap<>();
        params.put(AppConstants.KEY_PARAMS.LIMIT.toString(), "10");
        params.put(AppConstants.KEY_PARAMS.SORT.toString(), "id");
        RequestDataUtils.requestData(Request.Method.GET, AppConstants.PATH_URL.LIST_TICKER.toString(), params, new RequestDataUtils.onResult() {
            @Override
            public void onSuccess(JSONObject object) {
                Log.e("Values", "Values : " + object.toString());
            }

            @Override
            public void onFail(int error) {

            }
        });
    }
}
