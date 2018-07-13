package midas.coinmarket.controller.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import midas.coinmarket.R;
import midas.coinmarket.utils.AppConstants;
import midas.coinmarket.utils.AppPreference;

public class ChoiceSortTypeDialog extends Dialog {
    @BindView(R.id.rg_sort)
    RadioGroup mRgSort;
    @BindView(R.id.rb_id)
    RadioButton mRbId;
    @BindView(R.id.rb_rank)
    RadioButton mRbRank;
    @BindView(R.id.rb_volume_24h)
    RadioButton mRbVolume24h;
    @BindView(R.id.rb_percent_change_24h)
    RadioButton mRbPercentChange24h;

    private String mSort = AppConstants.SORT.RANK;
    private String mChoice = "";
    private onActionChoice mAction;

    public ChoiceSortTypeDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choice_sort_type);
        ButterKnife.bind(this);
        mSort = AppPreference.getInstance(getContext()).getString(AppConstants.KEY_PREFERENCE.SORT, AppConstants.SORT.RANK);
        if (mSort.equalsIgnoreCase(AppConstants.SORT.ID)) {
            mRbId.setChecked(true);
            mChoice = AppConstants.SORT.ID;
        } else if (mSort.equalsIgnoreCase(AppConstants.SORT.RANK)) {
            mRbRank.setChecked(true);
            mChoice = AppConstants.SORT.RANK;
        } else if (mSort.equalsIgnoreCase(AppConstants.SORT.VOLUME_24H)) {
            mRbVolume24h.setChecked(true);
            mChoice = AppConstants.SORT.VOLUME_24H;
        } else if (mSort.equalsIgnoreCase(AppConstants.SORT.PERCENT_CHANGE_24H)) {
            mRbPercentChange24h.setChecked(true);
            mChoice = AppConstants.SORT.PERCENT_CHANGE_24H;
        }
    }

    @OnClick({R.id.btn_submit})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                if (mSort.equalsIgnoreCase(mChoice)) {
                    dismiss();
                } else {
                    AppPreference.getInstance(getContext()).putString(AppConstants.KEY_PREFERENCE.SORT, mChoice);
                    if (mAction != null)
                        mAction.onValueChoice(mChoice);
                    dismiss();
                }
                break;
        }
    }

    public void setListener(onActionChoice action) {
        mAction = action;
    }

    public interface onActionChoice {
        void onValueChoice(String value);
    }

}
