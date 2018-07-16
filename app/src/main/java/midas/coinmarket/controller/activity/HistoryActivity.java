package midas.coinmarket.controller.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import midas.coinmarket.R;
import midas.coinmarket.controller.SpecificCurrencyActivity;
import midas.coinmarket.controller.dialog.LoadingDialog;
import midas.coinmarket.model.CryptocurrencyObject;
import midas.coinmarket.model.DatabaseHelper;
import midas.coinmarket.utils.AppConstants;
import midas.coinmarket.utils.BaseActivity;
import midas.coinmarket.view.adapter.HistoryAdapter;

public class HistoryActivity extends BaseActivity {
    @BindView(R.id.lst_history)
    ListView mListHistory;
    @BindView(R.id.tv_no_data)
    TextView mTvNoData;
    private DatabaseHelper mHelper;
    private HistoryAdapter mAdapter;
    private ArrayList<CryptocurrencyObject> mList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_history;
    }

    @Override
    public void initFunction() {
        mList = new ArrayList<>();
        mHelper = new DatabaseHelper(HistoryActivity.this);
        mList = mHelper.getAllHistory();
        mAdapter = new HistoryAdapter(HistoryActivity.this, mList);
        mListHistory.setAdapter(mAdapter);
        mListHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CryptocurrencyObject cryobject = mList.get(position);
                startActivity(new Intent(HistoryActivity.this, SpecificCurrencyActivity.class).putExtra(AppConstants.INTENT.DATA, String.valueOf(cryobject.getId())));
            }
        });
        mTvNoData.setVisibility(mList.size() > 0 ? View.GONE : View.VISIBLE);
        getOnlineData();
    }

    private void getOnlineData() {
        LoadingDialog.getDialog(HistoryActivity.this).show();
        getmDatabaseOnline().child(AppConstants.DB_VALUES.TBL_HISTORY + "/" + getUser().getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CryptocurrencyObject object = snapshot.getValue(CryptocurrencyObject.class);
                    addDataToList(object);
                }
                mAdapter.notifyDataSetChanged();
                mTvNoData.setVisibility(mList.size() > 0 ? View.GONE : View.VISIBLE);
                LoadingDialog.getDialog(HistoryActivity.this).dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                LoadingDialog.getDialog(HistoryActivity.this).dismiss();
            }
        });
    }

    private void addDataToList(CryptocurrencyObject object) {
        boolean isCanAdd = true;
        for (int i = 0; i < mList.size(); i++) {
            if (CryptocurrencyObject.compareObject(mList.get(i), object)) {
                isCanAdd = false;
                break;
            }
        }
        if (isCanAdd)
            mList.add(object);
    }


    @OnClick({R.id.imv_back, R.id.imv_clear})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.imv_back:
                finish();
                break;
            case R.id.imv_clear:
                AlertDialog alertDialog = new AlertDialog.Builder(HistoryActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Do you want to clear history ?");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mHelper.clearHistory();
                                Toast.makeText(HistoryActivity.this, "Clear success !", Toast.LENGTH_SHORT).show();
                                mList.clear();
                                mAdapter.notifyDataSetChanged();
                                mTvNoData.setVisibility(mList.size() > 0 ? View.GONE : View.VISIBLE);
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
                break;
        }
    }
}
