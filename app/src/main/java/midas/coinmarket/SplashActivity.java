package midas.coinmarket;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Handler;

import midas.coinmarket.controller.LoginActivity;
import midas.coinmarket.controller.MainActivity;
import midas.coinmarket.utils.AccountUtils;
import midas.coinmarket.utils.BaseActivity;

public class SplashActivity extends BaseActivity {
    private Account mAccount;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initFunction() {
        mAccount = AccountUtils.getAccountUser(SplashActivity.this, AccountManager.get(this));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, mAccount != null ? MainActivity.class : LoginActivity.class));
                finish();
            }
        }, 2000);
    }
}
