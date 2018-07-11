package midas.coinmarket.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import midas.coinmarket.utils.AppConstants;

public class CryptocurrencyObject implements Parcelable {
    int id;
    String name;
    String symbol;
    String website_slug;

    public CryptocurrencyObject() {
    }

    protected CryptocurrencyObject(Parcel in) {
        id = in.readInt();
        name = in.readString();
        symbol = in.readString();
        website_slug = in.readString();
    }

    public static final Creator<CryptocurrencyObject> CREATOR = new Creator<CryptocurrencyObject>() {
        @Override
        public CryptocurrencyObject createFromParcel(Parcel in) {
            return new CryptocurrencyObject(in);
        }

        @Override
        public CryptocurrencyObject[] newArray(int size) {
            return new CryptocurrencyObject[size];
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

    public static CryptocurrencyObject parserData(JSONObject object) {
        CryptocurrencyObject result = new CryptocurrencyObject();
        result.setId(object.optInt(AppConstants.KEY_PARAMS.ID.toString()));
        result.setName(object.optString(AppConstants.KEY_PARAMS.NAME.toString()));
        result.setSymbol(object.optString(AppConstants.KEY_PARAMS.SYMBOL.toString()));
        result.setWebsiteSlug(object.optString(AppConstants.KEY_PARAMS.WEBSITE_SLUG.toString()));
        return result;
    }
}
