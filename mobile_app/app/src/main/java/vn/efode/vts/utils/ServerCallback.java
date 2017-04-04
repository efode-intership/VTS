package vn.efode.vts.utils;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by Tuan on 04/04/2017.
 */

public interface ServerCallback {
    void onSuccess(JSONObject result);
    void onError(VolleyError error);
}
