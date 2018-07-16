package midas.coinmarket;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import midas.coinmarket.controller.LoginActivity;
import midas.coinmarket.controller.MainActivity;
import midas.coinmarket.controller.dialog.LoadingDialog;
import midas.coinmarket.model.UserObject;
import midas.coinmarket.utils.AccountUtils;
import midas.coinmarket.utils.AppConstants;
import midas.coinmarket.utils.BaseActivity;

public class SplashActivity extends BaseActivity {
    private Account mAccount;
    private UserObject mUser = null;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initFunction() {
        printHashKey(SplashActivity.this);
        mAccount = AccountUtils.getAccountUser(SplashActivity.this, AccountManager.get(this));
        if (null != mAccount) {
            String name = mAccount.name;
            String password = mAccount.type;
            checkCanInsertDb(name);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }, 2000);
        }

    }

    public void checkCanInsertDb(final String name) {
        LoadingDialog.getDialog(SplashActivity.this).show();
        getmDatabaseOnline().child(AppConstants.DB_VALUES.TBL_USERS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<UserObject> listUser = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    listUser.add(snapshot.getValue(UserObject.class));
                }
                for (UserObject object : listUser) {
                    if (object.getName().equals(name))
                        mUser = object;
                }
                LoadingDialog.getDialog(SplashActivity.this).dismiss();
                if (null != mUser) {
                    AccountUtils.setUser(mUser);
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            finish();
                        }
                    }, 2000);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                LoadingDialog.getDialog(SplashActivity.this).dismiss();
                Toast.makeText(SplashActivity.this, "Cannot log app now", Toast.LENGTH_SHORT).show();
            }
        });
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
