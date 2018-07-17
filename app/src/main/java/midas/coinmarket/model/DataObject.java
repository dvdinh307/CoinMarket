package midas.coinmarket.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataObject {
    @SerializedName("active_cryptocurrencies")
    @Expose
    private String active_cryptocurrencies;
    @SerializedName("active_markets")
    @Expose
    private String active_markets;
    @SerializedName("bitcoin_percentage_of_market_cap")
    @Expose
    private String bitcoin_percentage_of_market_cap;
    @SerializedName("last_updated")
    @Expose
    private String last_updated;

    public String getActive_cryptocurrencies() {
        return active_cryptocurrencies;
    }

    public void setActive_cryptocurrencies(String active_cryptocurrencies) {
        this.active_cryptocurrencies = active_cryptocurrencies;
    }

    public String getActive_markets() {
        return active_markets;
    }

    public void setActive_markets(String active_markets) {
        this.active_markets = active_markets;
    }

    public String getBitcoin_percentage_of_market_cap() {
        return bitcoin_percentage_of_market_cap;
    }

    public void setBitcoin_percentage_of_market_cap(String bitcoin_percentage_of_market_cap) {
        this.bitcoin_percentage_of_market_cap = bitcoin_percentage_of_market_cap;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }
}
