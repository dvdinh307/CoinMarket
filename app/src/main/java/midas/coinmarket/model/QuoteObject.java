package midas.coinmarket.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import midas.coinmarket.utils.AppConstants;

public class QuoteObject implements Parcelable {

    private double price;
    private double volume_24h;
    private double market_cap;
    private double percent_change_1h;
    private double percent_change_24h;
    private double percent_change_7d;
    private String name;

    public QuoteObject() {
    }

    protected QuoteObject(Parcel in) {
        price = in.readInt();
        volume_24h = in.readInt();
        market_cap = in.readInt();
        percent_change_1h = in.readDouble();
        percent_change_24h = in.readDouble();
        percent_change_7d = in.readDouble();
        name = in.readString();
    }

    public static final Creator<QuoteObject> CREATOR = new Creator<QuoteObject>() {
        @Override
        public QuoteObject createFromParcel(Parcel in) {
            return new QuoteObject(in);
        }

        @Override
        public QuoteObject[] newArray(int size) {
            return new QuoteObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(price);
        dest.writeDouble(volume_24h);
        dest.writeDouble(market_cap);
        dest.writeDouble(percent_change_1h);
        dest.writeDouble(percent_change_24h);
        dest.writeDouble(percent_change_7d);
        dest.writeString(name);
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getVolume24h() {
        return volume_24h;
    }

    public void setVolume24h(double volume_24h) {
        this.volume_24h = volume_24h;
    }

    public double getMarketCap() {
        return market_cap;
    }

    public void setMarketCap(double market_cap) {
        this.market_cap = market_cap;
    }

    public double getPercentChange_1h() {
        return percent_change_1h;
    }

    public void setPercentChange1h(double percent_change_1h) {
        this.percent_change_1h = percent_change_1h;
    }

    public double getPercentChange24h() {
        return percent_change_24h;
    }

    public void setPercentChange24h(double percent_change_24h) {
        this.percent_change_24h = percent_change_24h;
    }

    public double getPercentChange7d() {
        return percent_change_7d;
    }

    public void setPercentChange7d(double percent_change_7d) {
        this.percent_change_7d = percent_change_7d;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static QuoteObject parserData(JSONObject object, String name) {
        QuoteObject result = new QuoteObject();
        result.setPrice(object.optDouble(AppConstants.KEY_PARAMS.PRICE.toString(), 0));
        result.setVolume24h(object.optDouble(AppConstants.KEY_PARAMS.VOLUME_24h.toString(), 0));
        result.setMarketCap(object.optDouble(AppConstants.KEY_PARAMS.MARKET_CAP.toString(), 0));
        result.setPercentChange1h(object.optDouble(AppConstants.KEY_PARAMS.PERCENT_CHANGE_1H.toString(), 0));
        result.setPercentChange24h(object.optDouble(AppConstants.KEY_PARAMS.PERCENT_CHANGE_24H.toString(), 0));
        result.setPercentChange7d(object.optDouble(AppConstants.KEY_PARAMS.PERCENT_CHANGE_7D.toString(), 0));
        result.setName(name);
        return result;
    }


}
