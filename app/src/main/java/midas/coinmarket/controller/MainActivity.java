package midas.coinmarket.controller;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.paginate.Paginate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import midas.coinmarket.R;
import midas.coinmarket.controller.dialog.ChoiceSortTypeDialog;
import midas.coinmarket.model.CoinObject;
import midas.coinmarket.model.CryptocurrencyObject;
import midas.coinmarket.utils.AppConstants;
import midas.coinmarket.utils.AppPreference;
import midas.coinmarket.utils.BaseActivity;
import midas.coinmarket.utils.RecyclerItemClickListener;
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
    private String mSort = AppConstants.SORT.RANK;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initFunction() {
        mSort = AppPreference.getInstance(MainActivity.this).getString(AppConstants.KEY_PREFERENCE.SORT, AppConstants.SORT.RANK);
        mSearchView = findViewById(R.id.search);
        mSearchView.setQueryHint(getString(R.string.msg_hint_input_search));
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

        mRcyMain.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this, mRcyMain, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                CoinObject coin = mListCoin.get(position);
                if (coin != null)
                    startActivity(new Intent(MainActivity.this, SpecificCurrencyActivity.class).putExtra(AppConstants.INTENT.DATA, String.valueOf(coin.getId())));
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        getListSuggest();
    }


    @Override
    public void onLoadMore() {
        getData("EUR", mSort);
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return page == 0;
    }


    @OnClick({R.id.imv_menu})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.imv_menu:
                PopupMenu popup = new PopupMenu(this, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.popup_menu, popup.getMenu());
                try {
                    Field[] fields = popup.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if ("mPopup".equals(field.getName())) {
                            field.setAccessible(true);
                            Object menuPopupHelper = field.get(popup);
                            Class<?> classPopupHelper = Class
                                    .forName(menuPopupHelper.getClass().getName());
                            Method setForceIcons = classPopupHelper
                                    .getMethod("setForceShowIcon", boolean.class);
                            setForceIcons.invoke(menuPopupHelper, true);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_sort:
                                ChoiceSortTypeDialog dialog = new ChoiceSortTypeDialog(MainActivity.this);
                                dialog.setListener(new ChoiceSortTypeDialog.onActionChoice() {
                                    @Override
                                    public void onValueChoice(String value) {
                                        page = 1;
                                        mSort = value;
                                        getData("EUR", value);
                                    }
                                });
                                dialog.show();
//                                có chức năng thay đổi kiểu sắp xếp <Id, rank, volume_24h, percent_change_24h>
                                break;
                            case R.id.item_switch:
                                Toast.makeText(MainActivity.this, "Switch", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.item_book_mark:
                                break;
                            case R.id.item_history:
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
                break;
        }
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
