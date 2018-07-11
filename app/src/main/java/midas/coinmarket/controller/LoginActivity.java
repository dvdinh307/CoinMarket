package midas.coinmarket.controller;

import android.view.View;

import butterknife.OnClick;
import midas.coinmarket.R;
import midas.coinmarket.utils.BaseActivity;

public class LoginActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initFunction() {

    }

    @OnClick({R.id.btn_facebook, R.id.btn_google})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_facebook:
                break;

            case R.id.btn_google:
                break;
        }
    }
}
