package midas.coinmarket.utils;

/**
 * Created by Hss on 8/19/2015.
 */
public class AppConstants {

    public static boolean isTestMode = true;
    public static int REQUEST_SUCCESS = 200;

    public static String URL_BASE = "https://api.coinmarketcap.com/v2/";
    public static String CURRENCY_DEFAULT = "USD";

    public static class STATUS_REQUEST {
        public static int REQUEST_ERROR = 0;
    }

    public static class INTENT {
        public static String DATA = "DATA";
    }

    public static class SORT {
        public static String ID = "id";
        public static String RANK = "rank";
        public static String VOLUME_24H = "volume_24h";
        public static String PERCENT_CHANGE_24H = "percent_change_24h ";
    }

    public static class KEY_PREFERENCE {
        public static String SORT = "APP_SORT";
        public static String CURRENCY = "APP_CURRENCY";
    }

    /**
     * Define columns.
     */
    public enum COLUMNS {
        ID("id"),
        ID_COIN("id_coin"),
        ID_BOOKMARK("id_book_mark"),
        TITLE("title"),
        CONTENT_BOOK_MARK("content_book_mark"),
        TIME("time"),;
        private String mName;

        COLUMNS(String name) {
            mName = name;
        }

        @Override
        public String toString() {
            return mName;
        }
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
        START("start"),
        SORT("sort"),
        STRUCTURE("structure"),
        CONVERT("convert"),
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
        CURRENCY_DETAIL("ticker/"),
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
