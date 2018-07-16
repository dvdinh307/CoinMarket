package midas.coinmarket.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.OnClick;
import midas.coinmarket.AppApplication;
import midas.coinmarket.R;
import midas.coinmarket.controller.activity.ConfirmLoginActivity;
import midas.coinmarket.controller.activity.SignInActivity;
import midas.coinmarket.controller.dialog.LoadingDialog;
import midas.coinmarket.model.UserObject;
import midas.coinmarket.utils.AccountUtils;
import midas.coinmarket.utils.AppConstants;
import midas.coinmarket.utils.BaseActivity;

public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 123;
    private CallbackManager callbackManager;
    private DatabaseReference mDatabase;
    private UserObject mUser = null;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initFunction() {
        mDatabase = AppApplication.getFireBaseDb().getReference();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions
                .DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // Facebook.
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        getDataAccount();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginActivity.this, "Cannot reigster with facebook", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getDataAccount() {
        GraphRequest req = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest
                .GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (null != object && object.length() > 0) {
                    String email = object.optString("email");
                    UserObject userObject = new UserObject();
                    userObject.setId(object.optString("id"));
                    userObject.setEmail(email);
                    checkExistUserInfomation(userObject);
                } else {
                    Toast.makeText(LoginActivity.this, "Cannot reigster with facebook", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "email");
        req.setParameters(parameters);
        req.executeAsync();
    }

    @OnClick({R.id.btn_facebook, R.id.btn_google, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_facebook:
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList
                        ("public_profile", "email"));
                break;
            case R.id.btn_google:
                signIn();
                break;
            case R.id.btn_login:
                startActivity(new Intent(LoginActivity.this, SignInActivity.class));
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
                        checkExistUserInfomation(userObject);
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

    private void checkExistUserInfomation(final UserObject user) {
        LoadingDialog.getDialog(LoginActivity.this).show();
        mDatabase.child(AppConstants.DB_VALUES.TBL_USERS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<UserObject> listUser = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    listUser.add(snapshot.getValue(UserObject.class));
                }
                for (UserObject object : listUser) {
                    if (object.getId().equals(user.getId())) {
                        mUser = object;
                    }
                }
                if (null != mUser) {
                    AccountUtils.saveAccountInformation(LoginActivity.this, mUser.getEmail(), mUser.getName());
                    startActivity(new Intent(LoginActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                } else {
                    startActivity(new Intent(LoginActivity.this, ConfirmLoginActivity.class).putExtra(AppConstants.INTENT.USER, user));
                }
                LoadingDialog.getDialog(LoginActivity.this).dismiss();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                LoadingDialog.getDialog(LoginActivity.this).dismiss();
                Toast.makeText(LoginActivity.this, "Cannot insert now", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
