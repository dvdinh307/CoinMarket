package midas.coinmarket.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import midas.coinmarket.R;
import midas.coinmarket.model.CoinObject;
import midas.coinmarket.utils.HFRecyclerView;

public class MainAdapter extends HFRecyclerView<CoinObject> {

    public MainAdapter(List<CoinObject> data, boolean withHeader, boolean withFooter) {
        super(data, withHeader, withFooter);
    }

    @Override
    protected RecyclerView.ViewHolder getItemView(LayoutInflater inflater, ViewGroup parent) {
        return new ItemViewHolder(inflater.inflate(R.layout.item_main, parent, false));
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderView(LayoutInflater inflater, ViewGroup parent) {
        return null;
    }

    @Override
    protected RecyclerView.ViewHolder getFooterView(LayoutInflater inflater, ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.bindData(getItem(position), position);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_symbol)
        TextView mTvSymbol;
        @BindView(R.id.tv_website)
        TextView mTvWebsite;
        @BindView(R.id.tv_rank)
        TextView mTvRank;
        @BindView(R.id.tv_circulating_supply)
        TextView mTvCirculating;
        @BindView(R.id.tv_total_supply)
        TextView mTvTotal;
        @BindView(R.id.tv_max_supply)
        TextView mTvMax;
        @BindView(R.id.tv_quote)
        TextView mTvQuote;
        @BindView(R.id.tv_price)
        TextView mTvPrice;
        @BindView(R.id.tv_value_24h)
        TextView mTvValue24h;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(CoinObject coin, int position) {
            mTvName.setText(String.format("Name : %s", coin.getName()));
            mTvSymbol.setText(String.format("Symbol :%s", coin.getSymbol()));
            mTvWebsite.setText(String.format("Website slug : %s", coin.getWebsiteSlug()));
            mTvRank.setText(String.format("Rank : %s", String.valueOf(coin.getRank())));
            mTvCirculating.setText(String.format("Circulating Supply :%s", String.valueOf(coin.getCirculatingSupply())));
            mTvTotal.setText(String.format("Total Supply :%s", String.valueOf(coin.getTotalSupply())));
            mTvMax.setText(String.format("Max Supply :%s", String.valueOf(coin.getMaxSupply())));
            if (coin.getOther() != null) {
                // Change.
                mTvQuote.setText(String.format("Quote(%s)", coin.getOther().getName()));
                mTvPrice.setText(String.format("Price : %s", coin.getOther().getPrice()));
                mTvValue24h.setText(String.format("Price : %s", coin.getOther().getVolume24h()));
            } else {
                // Default is USD
                mTvQuote.setText(String.format("Quote(%s)", "USD"));
                mTvPrice.setText(String.format("Price : %s", coin.getUsd().getPrice()));
                mTvValue24h.setText(String.format("Price : %s", coin.getUsd().getVolume24h()));
            }
        }
    }
}
