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
import java.util.Locale;
import java.util.Objects;

import midas.coinmarket.R;
import midas.coinmarket.model.CryptocurrencyObject;

public class CryptocurrentcyAdapter extends ArrayAdapter<CryptocurrencyObject> {
    private ArrayList<CryptocurrencyObject> mListCry;
    private List<CryptocurrencyObject> mListSearch;

    public CryptocurrentcyAdapter(@NonNull Context context, @NonNull ArrayList<CryptocurrencyObject> objects) {
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
            convertView = View.inflate(parent.getContext(), R.layout.item_cryptocurrency, null);
            holder.mTvName = convertView.findViewById(R.id.tv_name);
            Objects.requireNonNull(convertView).setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTvName.setText(cryptocurrencyObject.getName());
        return convertView;
    }


    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mListCry.clear();
        if (charText.length() == 0) {
            mListCry.addAll(mListSearch);
        } else {
            for (CryptocurrencyObject wp : mListSearch) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mListCry.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder {
        TextView mTvName;
    }

}
