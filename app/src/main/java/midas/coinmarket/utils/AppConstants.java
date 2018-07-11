package midas.coinmarket.utils;

/**
 * Created by Hss on 8/19/2015.
 */
public class AppConstants {

    public static boolean isTestMode = true;
    public static int REQUEST_SUCCESS = 200;

    public static class STATUS_REQUEST {
        public static int REQUEST_ERROR = 0;
    }

    public enum KEY_PARAMS {
        DEVICE_ID("device_id"),
        TOKEN("token"),
        NAME("name"),
        DATA("data"),
        STATUS("status"),
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
}
