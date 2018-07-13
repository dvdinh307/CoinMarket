package midas.coinmarket.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import midas.coinmarket.R;
import midas.coinmarket.controller.dialog.LoadingDialog;
import midas.coinmarket.model.CoinObject;
import midas.coinmarket.model.DatabaseHelper;
import midas.coinmarket.model.QuoteObject;
import midas.coinmarket.utils.AppConstants;
import midas.coinmarket.utils.AppPreference;
import midas.coinmarket.utils.BaseActivity;
import midas.coinmarket.utils.RequestDataUtils;

public class SpecificCurrencyActivity extends BaseActivity {
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_symbol)
    TextView mTvSymbol;
    @BindView(R.id.tv_website)
    TextView mTvWebsite;
    @BindView(R.id.tv_rank)
    TextView mTvRank;
    @BindView(R.id.tv_circulating_supply)
    TextView mTvCirculating;
    @BindView(R.id.tv_total_supply)
    TextView mTvTotalSupply;
    @BindView(R.id.tv_max_supply)
    TextView mTvMaxSupply;
    @BindView(R.id.tbr_other)
    TableRow mTbrOther;
    // Usd
    @BindView(R.id.tv_usd_cur)
    TextView mTvUsdCur;
    @BindView(R.id.tv_usd_price)
    TextView mTvUsdPrice;
    @BindView(R.id.tv_usd_vol)
    TextView mTvUsdVol;
    @BindView(R.id.tv_usd_mar)
    TextView mTvUsdMar;
    @BindView(R.id.tv_usd_per_1h)
    TextView mTvPer1h;
    @BindView(R.id.tv_usd_per_24h)
    TextView mTvPer24h;
    @BindView(R.id.tv_usd_per_7d)
    TextView mTvUsdPer7d;
    // Other
    @BindView(R.id.tv_other_cur)
    TextView mTvOtherCur;
    @BindView(R.id.tv_other_price)
    TextView mTvOtherPrice;
    @BindView(R.id.tv_other_vol)
    TextView mTvOtherVol;
    @BindView(R.id.tv_other_mar)
    TextView mTvOtherMar;
    @BindView(R.id.tv_other_per_1h)
    TextView mTvOtherPer1h;
    @BindView(R.id.tv_other_per_24h)
    TextView mTvOtherPer24h;
    @BindView(R.id.tv_other_per_7d)
    TextView mTvOtherPer7d;

    private String mCurrency = "";
    private String mId;
    private DatabaseHelper mHelper;
    private String mContentCoin = "";
    private CoinObject mCoinObject;

    @Override
    public int getLayoutId() {
        return R.layout.activity_specific_currency;
    }

    @Override
    public void initFunction() {
        mHelper = new DatabaseHelper(SpecificCurrencyActivity.this);
        mCurrency = AppPreference.getInstance(SpecificCurrencyActivity.this).getString(AppConstants.KEY_PREFERENCE.CURRENCY, AppConstants.CURRENCY_DEFAULT);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            mId = bundle.getString(AppConstants.INTENT.DATA, "");
        if (mId.length() > 0)
            getData(mId, mCurrency);
    }

    private void getData(String id, final String otherCurrency) {
        LoadingDialog.getDialog(SpecificCurrencyActivity.this).show();
        final HashMap<String, String> params = new HashMap<>();
        params.put(AppConstants.KEY_PARAMS.CONVERT.toString(), otherCurrency);
        RequestDataUtils.requestData(Request.Method.GET, AppConstants.PATH_URL.CURRENCY_DETAIL.toString() + "/" + id + "/", params, new RequestDataUtils.onResult() {
            @Override
            public void onSuccess(JSONObject object) {
                try {
                    JSONObject data = object.getJSONObject(AppConstants.KEY_PARAMS.DATA.toString());
                    if (data.length() > 0) {
                        mContentCoin = data.toString();
                        mCoinObject = CoinObject.parserData(data, otherCurrency);
                        loadDataToView(mCoinObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                LoadingDialog.getDialog(SpecificCurrencyActivity.this).dismiss();
            }

            @Override
            public void onFail(int error) {
                LoadingDialog.getDialog(SpecificCurrencyActivity.this).dismiss();
            }
        });
    }

    //    max_supply: 21000000,
    private void loadDataToView(CoinObject coin) {
        mTvName.setText(coin.getName());
        mTvSymbol.setText(String.format("Symbol : %s", coin.getSymbol()));
        mTvWebsite.setText(String.format("Website slug : %s", coin.getWebsiteSlug()));
        mTvRank.setText(String.format("Rank : %s", coin.getRank()));
        mTvCirculating.setText(String.format("Circulating supply : %s", coin.getSymbol()));
        mTvTotalSupply.setText(String.format("Total supply : %s", coin.getSymbol()));
        mTvMaxSupply.setText(String.format("Max supply : %s", coin.getSymbol()));
        // USD.
        QuoteObject usd = coin.getUsd();
        mTvUsdCur.setText(usd.getName());
        mTvUsdPrice.setText(String.valueOf(usd.getPrice()));
        mTvUsdVol.setText(String.valueOf(usd.getVolume24h()));
        mTvUsdMar.setText(String.valueOf(usd.getMarketCap()));
        mTvPer1h.setText(String.valueOf(usd.getPercentChange_1h()));
        mTvPer24h.setText(String.valueOf(usd.getPercentChange24h()));
        mTvUsdPer7d.setText(String.valueOf(usd.getPercentChange7d()));
        // Other.
        QuoteObject other = coin.getOther();
        if (other != null) {
            mTvOtherCur.setText(other.getName());
            mTvOtherPrice.setText(String.valueOf(other.getPrice()));
            mTvOtherVol.setText(String.valueOf(other.getVolume24h()));
            mTvOtherMar.setText(String.valueOf(other.getMarketCap()));
            mTvOtherPer1h.setText(String.valueOf(other.getPercentChange_1h()));
            mTvOtherPer24h.setText(String.valueOf(other.getPercentChange24h()));
            mTvOtherPer7d.setText(String.valueOf(other.getPercentChange7d()));
        } else {
            mTbrOther.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.imv_back, R.id.imv_favorite})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.imv_back:
                finish();
                break;
            case R.id.imv_favorite:
                // save to bookmark.
                if (mContentCoin.length() > 0)
                    mHelper.insertBookMark(mCoinObject, mContentCoin);
                Toast.makeText(SpecificCurrencyActivity.this, "Save success", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
