package midas.coinmarket.utils;

import midas.coinmarket.model.GlobalObject;
import midas.coinmarket.model.ResultParserObject;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RequestInterfaceService {
    @GET("listings")
    Call<ResultParserObject> listCurrency();

    @GET("global/")
    Call<GlobalObject> dataGlobal();

}
