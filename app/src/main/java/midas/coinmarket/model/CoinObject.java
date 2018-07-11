package midas.coinmarket.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import midas.coinmarket.utils.AppConstants;

public class CoinObject implements Parcelable {
    private int id;
    private String name;
    private String symbol;
    private String website_slug;
    private int rank;
    private int circulating_supply;
    private int total_supply;
    private int max_supply;
    //quote.
    private int last_updated;
    private QuoteObject usd;
    private QuoteObject other;

    public CoinObject() {
    }

    protected CoinObject(Parcel in) {
        id = in.readInt();
        name = in.readString();
        symbol = in.readString();
        website_slug = in.readString();
        rank = in.readInt();
        circulating_supply = in.readInt();
        total_supply = in.readInt();
        max_supply = in.readInt();
        last_updated = in.readInt();
        usd = in.readParcelable(QuoteObject.class.getClassLoader());
        other = in.readParcelable(QuoteObject.class.getClassLoader());
    }

    public static final Creator<CoinObject> CREATOR = new Creator<CoinObject>() {
        @Override
        public CoinObject createFromParcel(Parcel in) {
            return new CoinObject(in);
        }

        @Override
        public CoinObject[] newArray(int size) {
            return new CoinObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(symbol);
        dest.writeString(website_slug);
        dest.writeInt(rank);
        dest.writeInt(circulating_supply);
        dest.writeInt(total_supply);
        dest.writeInt(max_supply);
        dest.writeInt(last_updated);
        dest.writeParcelable(usd, flags);
        dest.writeParcelable(other, flags);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getWebsiteSlug() {
        return website_slug;
    }

    public void setWebsiteSlug(String website_slug) {
        this.website_slug = website_slug;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getCirculatingSupply() {
        return circulating_supply;
    }

    public void setCirculatingSupply(int circulating_supply) {
        this.circulating_supply = circulating_supply;
    }

    public int getTotalSupply() {
        return total_supply;
    }

    public void setTotalSupply(int total_supply) {
        this.total_supply = total_supply;
    }

    public int getMaxSupply() {
        return max_supply;
    }

    public void setMaxSupply(int max_supply) {
        this.max_supply = max_supply;
    }

    public int getLastUpdated() {
        return last_updated;
    }

    public void setLastUpdated(int last_updated) {
        this.last_updated = last_updated;
    }

    public QuoteObject getUsd() {
        return usd;
    }

    public void setUsd(QuoteObject usd) {
        this.usd = usd;
    }

    public QuoteObject getOther() {
        return other;
    }

    public void setOther(QuoteObject other) {
        this.other = other;
    }

    public static CoinObject parserData(JSONObject object, String name) {
        CoinObject result = new CoinObject();
        result.setId(object.optInt(AppConstants.KEY_PARAMS.ID.toString()));
        result.setName(object.optString(AppConstants.KEY_PARAMS.NAME.toString()));
        result.setSymbol(object.optString(AppConstants.KEY_PARAMS.SYMBOL.toString()));
        result.setWebsiteSlug(object.optString(AppConstants.KEY_PARAMS.WEBSITE_SLUG.toString()));
        result.setRank(object.optInt(AppConstants.KEY_PARAMS.RANK.toString()));
        result.setCirculatingSupply(object.optInt(AppConstants.KEY_PARAMS.CIRCULATING_SUPPLY.toString()));
        result.setTotalSupply(object.optInt(AppConstants.KEY_PARAMS.TOTAL_SUPPLY.toString()));
        result.setMaxSupply(object.optInt(AppConstants.KEY_PARAMS.MAX_SUPPLY.toString()));
        // Get quotes.
        try {
            JSONObject quote = object.getJSONObject(AppConstants.KEY_PARAMS.QUOTES.toString());
            if (quote.length() > 0) {
                // USD.
                JSONObject usd = quote.getJSONObject(AppConstants.KEY_PARAMS.USD.toString());
                if (usd.length() > 0) {
                    result.setUsd(QuoteObject.parserData(usd, AppConstants.KEY_PARAMS.USD.toString()));
                }
                // Other quote.
                JSONObject other = quote.getJSONObject(name);
                if (object.length() > 0) {
                    result.setOther(QuoteObject.parserData(other, name));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
