package midas.coinmarket.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import midas.coinmarket.R;
import midas.coinmarket.model.CryptocurrencyObject;

public class HistoryAdapter extends ArrayAdapter<CryptocurrencyObject> {
    private ArrayList<CryptocurrencyObject> mListCry;
    private List<CryptocurrencyObject> mListSearch;

    public HistoryAdapter(@NonNull Context context, @NonNull ArrayList<CryptocurrencyObject> objects) {
        super(context, 0, objects);
        mListCry = objects;
        mListSearch = new ArrayList<>();
        mListSearch.addAll(objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        CryptocurrencyObject cryptocurrencyObject = mListCry.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_history, null);
            holder.mTvName = convertView.findViewById(R.id.tv_name);
            holder.mTvTime = convertView.findViewById(R.id.tv_time);
            Objects.requireNonNull(convertView).setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTvName.setText(cryptocurrencyObject.getName());
        holder.mTvTime.setText(cryptocurrencyObject.getTime());
        return convertView;
    }

    public class ViewHolder {
        TextView mTvName, mTvTime;
    }

}
