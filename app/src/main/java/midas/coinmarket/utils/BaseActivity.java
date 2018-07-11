package midas.coinmarket.utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutId() == 0)
            throw new NullPointerException("You need init id layout first");
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initFunction();
    }

    public abstract int getLayoutId();

    public abstract void initFunction();
}
