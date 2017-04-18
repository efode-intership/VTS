package vn.efode.vts.utils;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import vn.efode.vts.application.ApplicationController;

/**
 * Created by Tuan on 04/04/2017.
 */


/**
 * Class to call API
 */
public class ServiceHandler {
    private String result = null;
    private static String VOLLEY_TAG = "VOLLEY";

    public final static String DOMAIN = "http://192.168.0.130/web_app/public";

    public ServiceHandler() {
    }

    /**
     *
     * @param url url.
     * @param method GET OR POST.
     * @param params Map key, value params.
     * @param callback Interface callback functions.
     */
    public static void makeServiceCall(String url, int method, HashMap<String,String> params, final ServerCallback callback){
        JsonObjectRequest req = null;
        StringRequest sr = null;
        if(method == Request.Method.POST){
            req = new JsonObjectRequest(url, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.d(VOLLEY_TAG, response.toString(4));
                                if(callback != null)
                                {
                                    callback.onSuccess(response); // call back function here
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(VOLLEY_TAG, error.getMessage());
                    if (callback != null) {
                        callback.onError(error);
                    }
                }
            });
        }
        else {

            // replace param value into url
            for (String key : params.keySet()) {
                url = url.replace("{"+key+"}", params.get(key));
            }
            sr = new StringRequest(Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            try {
                                Log.d(VOLLEY_TAG, response);
                                callback.onSuccess(new JSONObject(response));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(VOLLEY_TAG, error.getMessage());
                    if (callback != null) {
                        callback.onError(error);
                    }
                }
            });
        }
        if(req == null)
            ApplicationController.getInstance().addToRequestQueue(sr); // add the request object to the queue to be executed
        else
            ApplicationController.getInstance().addToRequestQueue(req); // add the request object to the queue to be executed
    }


}
