package vn.efode.vts.service;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import vn.efode.vts.application.ApplicationController;

/**
 * Created by Tri on 4/17/17.
 */

public class DeviceTokenService extends FirebaseInstanceIdService {

    /**
     * Device token key's name.
     */
    public static final String DEVICE_TOKEN = "device_token";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("onTokenRefresh", "Refreshed token: " + refreshedToken);

        // save token into sharedPreferences
        ApplicationController.sharedPreferences.edit().putString(DEVICE_TOKEN,refreshedToken).commit();
    }
}
