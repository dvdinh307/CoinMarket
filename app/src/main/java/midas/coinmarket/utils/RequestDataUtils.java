package midas.coinmarket.utils;

import android.app.Activity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import midas.coinmarket.AppApplication;

public class RequestDataUtils {

    private static final int MY_SOCKET_TIMEOUT_MS = 30000;

    /**
     * Using request data from server
     *
     * @param params
     * @param action
     */
    public static void requestData(int method, String url, final Map<String, String> params, final onResult action) {
        StringRequest request = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.length() > 0) {
                    try {
                        JSONObject objectResponse = new JSONObject(response);
                        if (action != null)
                            action.onSuccess(objectResponse);
                    } catch (JSONException e) {
                        if (action != null)
                            action.onFail(AppConstants.STATUS_REQUEST.REQUEST_ERROR);
                        e.printStackTrace();
                    }
                } else {
                    if (action != null)
                        action.onFail(AppConstants.STATUS_REQUEST.REQUEST_ERROR);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (action != null)
                    action.onFail(AppConstants.STATUS_REQUEST.REQUEST_ERROR);
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppApplication.getInstance().addToRequestQueue(request);
    }


    public interface onResult {
        void onSuccess(JSONObject object);

        void onFail(int error);
    }
}
