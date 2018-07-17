package midas.coinmarket.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MetaObject {
    @SerializedName("timestamp")
    @Expose
    private Integer timestamp;
    @SerializedName("num_cryptocurrencies")
    @Expose
    private Integer numCryptocurrencies;
    @SerializedName("error")
    @Expose
    private Object error;

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getNumCryptocurrencies() {
        return numCryptocurrencies;
    }

    public void setNumCryptocurrencies(Integer numCryptocurrencies) {
        this.numCryptocurrencies = numCryptocurrencies;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }
}
