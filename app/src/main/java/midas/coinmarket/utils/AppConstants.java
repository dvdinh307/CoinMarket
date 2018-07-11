package midas.coinmarket.utils;

/**
 * Created by Hss on 8/19/2015.
 */
public class AppConstants {

    public static boolean isTestMode = true;
    public static int REQUEST_SUCCESS = 200;

    public static String URL_BASE = "https://api.coinmarketcap.com/v2/";

    public static class STATUS_REQUEST {
        public static int REQUEST_ERROR = 0;
    }

    public enum KEY_PARAMS {
        DEVICE_ID("device_id"),
        TOKEN("token"),
        NAME("name"),
        DATA("data"),
        STATUS("status"),
        ID("id"),
        PRICE("price"),
        VOLUME_24h("volume_24h"),
        MARKET_CAP("market_cap"),
        PERCENT_CHANGE_1H("percent_change_1h"),
        PERCENT_CHANGE_24H("percent_change_24h"),
        PERCENT_CHANGE_7D("percent_change_7d"),
        SYMBOL("symbol"),
        WEBSITE_SLUG("website_slug"),
        RANK("rank"),
        CIRCULATING_SUPPLY("circulating_supply"),
        TOTAL_SUPPLY("total_supply"),
        MAX_SUPPLY("max_supply"),
        QUOTES("quotes"),
        USD("USD"),
        LIMIT("limit"),
        SORT("sort"),
        MESSAGE("message");

        private String mName;

        KEY_PARAMS(String name) {
            mName = name;
        }

        @Override
        public String toString() {
            return mName;
        }
    }

    /**
     * Define link request url.
     */
    public enum PATH_URL {
        //active cryptocurrency
        LIST_CRYTOCURRENCY("listings"),
        LIST_TICKER("ticker/"),
        MESSAGE("message");

        private String mName;

        PATH_URL(String name) {
            mName = name;
        }

        @Override
        public String toString() {
            return URL_BASE + mName;
        }
    }
}
