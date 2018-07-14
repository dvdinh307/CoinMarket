package midas.coinmarket.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import midas.coinmarket.R;
import midas.coinmarket.controller.MainActivity;
import midas.coinmarket.model.UserObject;
import midas.coinmarket.utils.AccountUtils;
import midas.coinmarket.utils.AppConstants;
import midas.coinmarket.utils.BaseActivity;

public class ConfirmLoginActivity extends BaseActivity {
    @BindView(R.id.edt_name)
    EditText mEdtName;
    @BindView(R.id.edt_email)
    EditText mEdtEmail;
    private UserObject mUser;

    @Override
    public int getLayoutId() {
        return R.layout.activity_confirm_login;
    }

    @Override
    public void initFunction() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mUser = bundle.getParcelable(AppConstants.INTENT.USER);
            if (mUser != null)
                loadDataToView(mUser);
        }
    }

    private void loadDataToView(UserObject user) {
        if (user.getEmail() != null)
            mEdtEmail.setText(user.getEmail());
    }

    @OnClick({R.id.btn_submit})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                String name = mEdtName.getText().toString();
                String email = mEdtEmail.getText().toString();
                if (checkValidate(name, email)) {
                    AccountUtils.saveAccountInformation(ConfirmLoginActivity.this, email, name);
                    startActivity(new Intent(ConfirmLoginActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
                break;
        }
    }

    private boolean checkValidate(String name, String email) {
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(ConfirmLoginActivity.this, R.string.msg_error_name, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(ConfirmLoginActivity.this, R.string.msg_error_email, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(ConfirmLoginActivity.this, R.string.msg_email_not_validate, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
