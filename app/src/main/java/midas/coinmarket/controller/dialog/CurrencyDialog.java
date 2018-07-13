package midas.coinmarket.controller.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import midas.coinmarket.R;

public class CurrencyDialog extends Dialog {
    private ListView mListCurrency;
    private ImageView mImvClose;
    private onActionChoiceCurrency mAction;

    public CurrencyDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_currency);
        mListCurrency = findViewById(R.id.lv_currency);
        mImvClose = findViewById(R.id.imv_close);
        mImvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        final ArrayAdapter<CharSequence> mAdapter = ArrayAdapter.createFromResource(getContext(), R.array.value_currency, android.R.layout.simple_list_item_1);
        mListCurrency.setAdapter(mAdapter);
        mListCurrency.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currency = (String) parent.getItemAtPosition(position);
                if (mAction != null)
                    mAction.onChoice(currency);
                dismiss();
            }
        });
    }

    public void setListener(onActionChoiceCurrency action) {
        mAction = action;
    }

    public interface onActionChoiceCurrency {
        void onChoice(String currency);
    }
}
