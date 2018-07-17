package midas.coinmarket.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResultParserObject {
    @SerializedName("data")
    @Expose
    private ArrayList<CurrencyObject> data = new ArrayList<>();
    @SerializedName("metadata")
    @Expose
    private MetaObject metadata;

    public ArrayList<CurrencyObject> getData() {
        return data;
    }

    public void setData(ArrayList<CurrencyObject> data) {
        this.data = data;
    }

    public MetaObject getMetadata() {
        return metadata;
    }

    public void setMetadata(MetaObject metadata) {
        this.metadata = metadata;
    }
}
