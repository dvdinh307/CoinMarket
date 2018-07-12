package midas.coinmarket.controller;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import midas.coinmarket.model.CryptocurrencyObject;
import midas.coinmarket.utils.AppConstants;
import midas.coinmarket.utils.BaseActivity;
import midas.coinmarket.utils.RequestDataUtils;
import midas.coinmarket.view.CustomLoadingListItemCreator;
import midas.coinmarket.view.adapter.CryptocurrentcyAdapter;
import midas.coinmarket.view.adapter.MainAdapter;

public class MainActivity extends BaseActivity implements Paginate.Callbacks, SearchView.OnQueryTextListener {
    private final int NUMBER_ITEM = 10;
    @BindView(R.id.rcy_main)
    RecyclerView mRcyMain;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;
    @BindView(R.id.list_suggest)
    ListView mLvSuggest;
    SearchView mSearchView;

    private int page = 1;
    private boolean isLoading;
    private MainAdapter mAdapter;
    private ArrayList<CoinObject> mListCoin = new ArrayList<>();
    private ArrayList<CryptocurrencyObject> mListSuggest = new ArrayList<>();
    private CryptocurrentcyAdapter mSearchAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initFunction() {
        // init actionbar.
        mSearchView = findViewById(R.id.search);
        mSearchView.setQueryHint("Input name coin");
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLlSearch.setVisibility(View.VISIBLE);
            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mLlSearch.setVisibility(View.GONE);
                return false;
            }
        });

        mLvSuggest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CryptocurrencyObject cryobject = mListSuggest.get(position);
                startActivity(new Intent(MainActivity.this, SpecificCurrencyActivity.class).putExtra(AppConstants.INTENT.DATA, String.valueOf(cryobject.getId())));
            }
        });
        // End
        mAdapter = new MainAdapter(mListCoin, false, false);
        mRcyMain.setLayoutManager(new LinearLayoutManager(this));
        mRcyMain.setAdapter(mAdapter);
        Paginate.with(mRcyMain, this)
                .setLoadingTriggerThreshold(0)
                .setLoadingListItemCreator(new CustomLoadingListItemCreator(MainActivity.this))
                .addLoadingListItem(true)
                .build();
        getListSuggest();
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

    protected void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getSupportActionBar().getThemedContext(),
                R.style.DialogTheme);
        builder.setPositiveButton(android.R.string.ok, null);
        String title = getString(R.string.app_name) + " version: ";
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            title += " " + packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            //Handle exception
        }
        builder.setTitle(title);

        builder.setIcon(R.mipmap.ic_launcher);
        SpannableString aboutMessage = new SpannableString(Html.fromHtml(getString(R.string
                .msg_about)));
        builder.setMessage(aboutMessage);

        TextView messageText = builder.show().findViewById(android.R.id.message);
        messageText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * Get Data from server.
     *
     * @param currency
     * @param sort
     */
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
            }

            @Override
            public void onFail(int error) {
                isLoading = false;
                page = 0;
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getListSuggest() {
        final HashMap<String, String> params = new HashMap<>();
        RequestDataUtils.requestData(Request.Method.GET, AppConstants.PATH_URL.LIST_CRYTOCURRENCY.toString(), params, new RequestDataUtils.onResult() {
            @Override
            public void onSuccess(JSONObject object) {
                if (object.length() > 0) {
                    // data.
                    try {
                        JSONArray datas = object.getJSONArray(AppConstants.KEY_PARAMS.DATA.toString());
                        if (datas.length() > 0) {
                            for (int i = 0; i < datas.length(); i++) {
                                mListSuggest.add(CryptocurrencyObject.parserData(datas.getJSONObject(i)));
                            }
                        }
                        // Search.
                        mSearchAdapter = new CryptocurrentcyAdapter(MainActivity.this, mListSuggest);
                        mLvSuggest.setAdapter(mSearchAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFail(int error) {

            }
        });
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.e("Values", "Values1 :" + query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.e("Values", "Values2 :" + newText);
        mSearchAdapter.filter(newText);
        return false;
    }
}
