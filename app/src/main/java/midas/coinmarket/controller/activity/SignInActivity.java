package midas.coinmarket.controller.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import midas.coinmarket.R;
import midas.coinmarket.controller.MainActivity;
import midas.coinmarket.controller.dialog.LoadingDialog;
import midas.coinmarket.model.UserObject;
import midas.coinmarket.utils.AccountUtils;
import midas.coinmarket.utils.AppConstants;
import midas.coinmarket.utils.BaseActivity;

public class SignInActivity extends BaseActivity {
    @BindView(R.id.edt_name)
    EditText mEdtName;
    @BindView(R.id.edt_password)
    EditText mEdtPassword;
    private UserObject mUser;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sign_in;
    }

    @Override
    public void initFunction() {

    }

    @OnClick({R.id.btn_submit, R.id.imv_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                String name = mEdtName.getText().toString();
                String password = mEdtPassword.getText().toString();
                if (checkValidate(name, password)) {
                    checkCanInsertDb(name, password);
                }
                break;
            case R.id.imv_back:
                finish();
                break;
        }
    }

    public void checkCanInsertDb(final String name, final String password) {
        LoadingDialog.getDialog(SignInActivity.this).show();
        getmDatabaseOnline().child(AppConstants.DB_VALUES.TBL_USERS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<UserObject> listUser = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    listUser.add(snapshot.getValue(UserObject.class));
                }
                for (UserObject object : listUser) {
                    if (object.getName().equals(name) && object.getPassword().equals(password))
                        mUser = object;
                }
                if (null != mUser) {
                    AccountUtils.saveAccountInformation(SignInActivity.this, mUser.getName(), mUser.getPassword());
                    AccountUtils.setUser(mUser);
                    startActivity(new Intent(SignInActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                } else {
                    Toast.makeText(SignInActivity.this, "Please check your account !", Toast.LENGTH_SHORT).show();
                }
                LoadingDialog.getDialog(SignInActivity.this).dismiss();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                LoadingDialog.getDialog(SignInActivity.this).dismiss();
                Toast.makeText(SignInActivity.this, "Cannot insert now", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkValidate(String name, String password) {
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(SignInActivity.this, R.string.msg_error_name, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(SignInActivity.this, R.string.msg_error_password, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
