package midas.coinmarket.controller.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.RadioGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import midas.coinmarket.R;

public class ChoiceSortTypeDialog extends Dialog {
    @BindView(R.id.rg_sort)
    RadioGroup mRgSort;

    public ChoiceSortTypeDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choice_sort_type);
        ButterKnife.bind(this);

    }
}
