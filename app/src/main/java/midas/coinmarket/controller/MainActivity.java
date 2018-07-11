package midas.coinmarket.controller;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.paginate.Paginate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import midas.coinmarket.R;
import midas.coinmarket.model.CoinObject;
import midas.coinmarket.utils.AppConstants;
import midas.coinmarket.utils.BaseActivity;
import midas.coinmarket.utils.RequestDataUtils;
import midas.coinmarket.view.CustomLoadingListItemCreator;
import midas.coinmarket.view.SuggestionProvider;
import midas.coinmarket.view.adapter.MainAdapter;

public class MainActivity extends BaseActivity implements Paginate.Callbacks {
    private final int NUMBER_ITEM = 10;
    @BindView(R.id.rcy_main)
    RecyclerView mRcyMain;
    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    private int page = 1;
    private boolean isLoading;
    private MainAdapter mAdapter;
    private ArrayList<CoinObject> mListCoin = new ArrayList<>();
    private MenuItem searchItem;
    private SearchRecentSuggestions suggestions;
    private SearchView searchView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initFunction() {
        // init actionbar.
        setSupportActionBar(mToolBar);
        suggestions = new SearchRecentSuggestions(this, SuggestionProvider.AUTHORITY,
                SuggestionProvider.MODE);
        for (int i = 0; i < 10; i++) {
            suggestions.saveRecentQuery("A :" + i, "is a nice cheese");
        }
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = new SearchView(Objects.requireNonNull(getSupportActionBar()).getThemedContext());
        searchView.setSearchableInfo(Objects.requireNonNull(searchManager).getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(false);
        searchView.setIconifiedByDefault(true);
        searchView.setIconified(false);
        searchView.setMaxWidth(1000);

        SearchView.SearchAutoComplete searchAutoComplete = searchView
                .findViewById(android.support.v7.appcompat.R.id.search_src_text);

        // Collapse the search menu when the user hits the back key
        searchAutoComplete.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus)
                    showSearch(false);
            }
        });

        try {
            // This sets the cursor
            // resource ID to 0 or @null
            // which will make it visible
            // on white background
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchAutoComplete, 0);

        } catch (Exception e) {
        }

        // End
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showSearch(false);
        Bundle extras = intent.getExtras();
        String userQuery = String.valueOf(extras.get(SearchManager.USER_QUERY));
        String query = String.valueOf(extras.get(SearchManager.QUERY));
        Toast.makeText(this, "query: " + query + " user_query: " + userQuery, Toast.LENGTH_SHORT)
                .show();
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


    /**
     * Called when the hardware search button is pressed
     */
    @Override
    public boolean onSearchRequested() {
        showSearch(true);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                showAboutDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        searchItem = menu.add(android.R.string.search_go);
        searchItem.setIcon(R.mipmap.ic_search);
        searchItem.setActionView(searchView);
        searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        menu.add(0, R.id.menu_about, 0, R.string.lbl_about);
        return super.onCreateOptionsMenu(menu);
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

    protected void showSearch(boolean visible) {
        if (visible)
            searchItem.expandActionView();
        else
            searchItem.collapseActionView();
    }
}
