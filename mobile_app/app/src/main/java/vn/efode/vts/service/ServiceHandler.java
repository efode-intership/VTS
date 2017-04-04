package vn.efode.vts.service;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import vn.efode.vts.application.ApplicationController;
import vn.efode.vts.utils.ServerCallback;

/**
 * Created by Tuan on 04/04/2017.
 */


/**
 * Class to call API
 */
public class ServiceHandler {
    private String result = null;
    public final static int GET = 1;
    public final static int POST = 2;

    public ServiceHandler() {
    }

    public void makeServiceCall(String url, int method, HashMap<String,String> params, final ServerCallback callback){
        JsonObjectRequest req = null;
        StringRequest sr = null;
        if(method == POST){
            req = new JsonObjectRequest(url, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                VolleyLog.v("Response:%n %s", response.toString(4));
                                callback.onSuccess(response); // call back function here

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                    callback.onError(error);
                }
            });


        }
        else {
            String uri = String.format(url,
                    params);

            sr = new StringRequest(Request.Method.GET,
                    uri,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            try {
                                callback.onSuccess(new JSONObject(response));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                    callback.onError(error);
                }
            });
        }
        if(req == null)
            ApplicationController.getInstance().addToRequestQueue(sr); // add the request object to the queue to be executed
        else
            ApplicationController.getInstance().addToRequestQueue(req); // add the request object to the queue to be executed
    }


}
