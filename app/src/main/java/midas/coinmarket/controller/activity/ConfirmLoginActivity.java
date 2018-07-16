package midas.coinmarket.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class ConfirmLoginActivity extends BaseActivity {
    @BindView(R.id.edt_name)
    EditText mEdtName;
    @BindView(R.id.edt_email)
    EditText mEdtEmail;
    @BindView(R.id.edt_password)
    EditText mEdtPassword;
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

    @OnClick({R.id.btn_submit, R.id.imv_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                String name = mEdtName.getText().toString();
                String email = mEdtEmail.getText().toString();
                String password = mEdtPassword.getText().toString();
                if (checkValidate(name, email, password)) {
                    mUser.setEmail(email);
                    mUser.setPassword(password);
                    mUser.setName(name);
                    checkCanInsertDb(mUser);
                }
                break;
            case R.id.imv_back:
                finish();
                break;
        }
    }

    public void checkCanInsertDb(final UserObject user) {
        LoadingDialog.getDialog(ConfirmLoginActivity.this).show();
        getmDatabaseOnline().child(AppConstants.DB_VALUES.TBL_USERS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<UserObject> listUser = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    listUser.add(snapshot.getValue(UserObject.class));
                }
                boolean isCanInsert = true;
                for (UserObject object : listUser) {
                    if (object.getName().equals(user.getName()))
                        isCanInsert = false;
                }
                if (isCanInsert) {
                    insertUserInfomation();
                } else {
                    Toast.makeText(ConfirmLoginActivity.this, "This user name has exist.", Toast.LENGTH_SHORT).show();
                }
                LoadingDialog.getDialog(ConfirmLoginActivity.this).dismiss();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                LoadingDialog.getDialog(ConfirmLoginActivity.this).dismiss();
                Toast.makeText(ConfirmLoginActivity.this, "Cannot insert now", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void insertUserInfomation() {
        // Save User to DB Online.
        // Using user name / password to login.
        getmDatabaseOnline().child(AppConstants.DB_VALUES.TBL_USERS).child(mUser.getName()).setValue(mUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ConfirmLoginActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                AccountUtils.saveAccountInformation(ConfirmLoginActivity.this, mUser.getName(), mUser.getPassword());
                AccountUtils.setUser(mUser);
                startActivity(new Intent(ConfirmLoginActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ConfirmLoginActivity.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkValidate(String name, String email, String password) {
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
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(ConfirmLoginActivity.this, R.string.msg_error_password, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
