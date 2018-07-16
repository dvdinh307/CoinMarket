package midas.coinmarket.utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;

import butterknife.ButterKnife;
import midas.coinmarket.AppApplication;
import midas.coinmarket.model.UserObject;

public abstract class BaseActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private UserObject mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutId() == 0)
            throw new NullPointerException("You need init id layout first");
        setContentView(getLayoutId());
        mDatabase = AppApplication.getFireBaseDb().getReference();
        mUser = AccountUtils.getUser();
        ButterKnife.bind(this);
        initFunction();
    }

    /**
     * Get User Information.
     *
     * @return
     */
    public UserObject getUser() {
        if (null != mUser)
            return mUser;
        return AccountUtils.getUser();
    }

    public DatabaseReference getmDatabaseOnline() {
        return mDatabase;
    }

    public abstract int getLayoutId();

    public abstract void initFunction();
}
