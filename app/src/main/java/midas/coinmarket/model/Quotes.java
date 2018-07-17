package midas.coinmarket.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Quotes {
    @SerializedName("USD")
    @Expose
    private USDObject uSD;

    public USDObject getUSD() {
        return uSD;
    }

    public void setUSD(USDObject uSD) {
        this.uSD = uSD;
    }

}
