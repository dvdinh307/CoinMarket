package midas.coinmarket.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class USDObject {
    @SerializedName("total_market_cap")
    @Expose
    private String totalMarketCap;
    @SerializedName("total_volume_24h")
    @Expose
    private String totalVolume24h;

    public String getTotalMarketCap() {
        return totalMarketCap;
    }

    public void setTotalMarketCap(String totalMarketCap) {
        this.totalMarketCap = totalMarketCap;
    }

    public String getTotalVolume24h() {
        return totalVolume24h;
    }

    public void setTotalVolume24h(String totalVolume24h) {
        this.totalVolume24h = totalVolume24h;
    }
}
