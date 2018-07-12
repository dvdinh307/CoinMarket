package midas.coinmarket.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import midas.coinmarket.R;
import midas.coinmarket.controller.dialog.LoadingDialog;
import midas.coinmarket.model.CoinObject;
import midas.coinmarket.utils.AppConstants;
import midas.coinmarket.utils.BaseActivity;
import midas.coinmarket.utils.RequestDataUtils;

public class SpecificCurrencyActivity extends BaseActivity {
    @BindView(R.id.tv_name)
    TextView mTvName;
    private String mId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_specific_currency;
    }

    @Override
    public void initFunction() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            mId = bundle.getString(AppConstants.INTENT.DATA, "");
        if (mId.length() > 0)
            getData(mId, "EUR");
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
                        CoinObject coinObject = CoinObject.parserData(data, otherCurrency);
                        loadDataToView(coinObject);
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
//    symbol: "BTC",
//    website_slug: "bitcoin",
//    rank: 1,
//    circulating_supply: 17144887,
//    total_supply: 17144887,
//    max_supply: 21000000,
    private void loadDataToView(CoinObject coin) {
        mTvName.setText(coin.getName());
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
