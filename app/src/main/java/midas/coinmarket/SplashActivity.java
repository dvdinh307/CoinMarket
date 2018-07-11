package midas.coinmarket;


import android.content.Intent;
import android.os.Handler;

import midas.coinmarket.controller.LoginActivity;
import midas.coinmarket.utils.BaseActivity;

public class SplashActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initFunction() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, 2000);
    }
}
