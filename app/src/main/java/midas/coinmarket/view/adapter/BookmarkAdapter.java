package midas.coinmarket.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import midas.coinmarket.R;
import midas.coinmarket.controller.MainActivity;
import midas.coinmarket.controller.activity.BookmarkActivity;
import midas.coinmarket.controller.activity.HistoryActivity;
import midas.coinmarket.controller.dialog.ChoiceSortTypeDialog;
import midas.coinmarket.controller.dialog.CurrencyDialog;
import midas.coinmarket.model.CoinObject;
import midas.coinmarket.utils.AppConstants;
import midas.coinmarket.utils.AppPreference;
import midas.coinmarket.utils.HFRecyclerView;

public class BookmarkAdapter extends HFRecyclerView<CoinObject> {
    private Context mContext;
    private onActionBookmark mAction;

    public BookmarkAdapter(List<CoinObject> data, boolean withHeader, boolean withFooter) {
        super(data, withHeader, withFooter);
    }

    @Override
    protected RecyclerView.ViewHolder getItemView(LayoutInflater inflater, ViewGroup parent) {
        mContext = parent.getContext();
        return new ItemViewHolder(inflater.inflate(R.layout.item_book_mark, parent, false));
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
        @BindView(R.id.imv_menu)
        ImageView mImvMenu;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(final CoinObject coin, final int position) {
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
            mImvMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position == 0) {
                        PopupMenu popup = new PopupMenu(mContext, view);
                        MenuInflater inflater = popup.getMenuInflater();
                        inflater.inflate(R.menu.popup_action_normal, popup.getMenu());
                        try {
                            Field[] fields = popup.getClass().getDeclaredFields();
                            for (Field field : fields) {
                                if ("mPopup".equals(field.getName())) {
                                    field.setAccessible(true);
                                    Object menuPopupHelper = field.get(popup);
                                    Class<?> classPopupHelper = Class
                                            .forName(menuPopupHelper.getClass().getName());
                                    Method setForceIcons = classPopupHelper
                                            .getMethod("setForceShowIcon", boolean.class);
                                    setForceIcons.invoke(menuPopupHelper, true);
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.item_edit:
                                        if (mAction != null)
                                            mAction.onEdit(coin, position);
                                        break;
                                    case R.id.item_delete:
                                        if (mAction != null)
                                            mAction.onDelete(coin, position);
                                        break;
                                }
                                return false;
                            }
                        });
                        popup.show();
                    } else {
                        PopupMenu popup = new PopupMenu(mContext, view);
                        MenuInflater inflater = popup.getMenuInflater();
                        inflater.inflate(R.menu.popup_action, popup.getMenu());
                        try {
                            Field[] fields = popup.getClass().getDeclaredFields();
                            for (Field field : fields) {
                                if ("mPopup".equals(field.getName())) {
                                    field.setAccessible(true);
                                    Object menuPopupHelper = field.get(popup);
                                    Class<?> classPopupHelper = Class
                                            .forName(menuPopupHelper.getClass().getName());
                                    Method setForceIcons = classPopupHelper
                                            .getMethod("setForceShowIcon", boolean.class);
                                    setForceIcons.invoke(menuPopupHelper, true);
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.item_edit:
                                        if (mAction != null)
                                            mAction.onEdit(coin, position);
                                        break;
                                    case R.id.item_delete:
                                        if (mAction != null)
                                            mAction.onDelete(coin, position);
                                        break;
                                    case R.id.item_pin_top:
                                        if (mAction != null)
                                            mAction.onPinToTop(coin, position);
                                        break;
                                }
                                return false;
                            }
                        });
                        popup.show();
                    }

                }
            });
        }
    }

    public void setListner(onActionBookmark action) {
        mAction = action;
    }

    public interface onActionBookmark {
        void onEdit(CoinObject coin, int position);

        void onDelete(CoinObject coin, int position);

        void onPinToTop(CoinObject coin, int position);

    }
}
