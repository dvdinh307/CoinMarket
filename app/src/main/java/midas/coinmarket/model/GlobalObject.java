package midas.coinmarket.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GlobalObject {
    @SerializedName("data")
    @Expose
    private DataObject data;
    @SerializedName("USD")
    @Expose
    private USDObject usd;

    public DataObject getData() {
        return data;
    }

    public void setData(DataObject data) {
        this.data = data;
    }

    public USDObject getUsd() {
        return usd;
    }

    public void setUsd(USDObject usd) {
        this.usd = usd;
    }
}
