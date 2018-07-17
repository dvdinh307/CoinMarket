package midas.coinmarket.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class USDObject {
    @SerializedName("total_market_cap")
    @Expose
    private String total_market_cap;
    @SerializedName("total_volume_24h")
    @Expose
    private String total_volume_24h;

    public String getTotal_market_cap() {
        return total_market_cap;
    }

    public void setTotal_market_cap(String total_market_cap) {
        this.total_market_cap = total_market_cap;
    }

    public String getTotal_volume_24h() {
        return total_volume_24h;
    }

    public void setTotal_volume_24h(String total_volume_24h) {
        this.total_volume_24h = total_volume_24h;
    }
}
