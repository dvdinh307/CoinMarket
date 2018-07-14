package midas.coinmarket.controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import butterknife.OnClick;
import midas.coinmarket.R;
import midas.coinmarket.controller.activity.ConfirmLoginActivity;
import midas.coinmarket.model.UserObject;
import midas.coinmarket.utils.AppConstants;
import midas.coinmarket.utils.BaseActivity;

public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 123;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initFunction() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions
                .DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @OnClick({R.id.btn_facebook, R.id.btn_google})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_facebook:
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                break;

            case R.id.btn_google:
                signIn();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount acct = result.getSignInAccount();
                if (acct != null) {
                    String id = acct.getId();
                    String email = acct.getEmail();
                    if (null != email && email.length() > 0) {
                        UserObject userObject = new UserObject();
                        userObject.setEmail(email);
                        userObject.setId(id);
                        startActivity(new Intent(LoginActivity.this, ConfirmLoginActivity.class).putExtra(AppConstants.INTENT.USER, userObject));
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Cannot register now.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * Sign Google api.
     */
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
