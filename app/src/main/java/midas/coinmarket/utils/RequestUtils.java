package midas.coinmarket.utils;

public class RequestUtils {

    public static RequestInterfaceService getBaseService() {
        return RetrofitClient.getClient().create(RequestInterfaceService.class);
    }
}
