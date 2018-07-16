package midas.coinmarket;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        printHashKey(SplashActivity.this);
        mAccount = AccountUtils.getAccountUser(SplashActivity.this, AccountManager.get(this));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, mAccount != null ? MainActivity.class : LoginActivity.class));
                finish();
            }
        }, 2000);

    }

    public void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("KEY", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("KEY", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("KEY", "printHashKey()", e);
        }
    }
}
