package vn.efode.vts.service;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import vn.efode.vts.MainActivity;
import vn.efode.vts.application.ApplicationController;
import vn.efode.vts.utils.LocationCallback;
import vn.efode.vts.utils.PathJSONParser;
import vn.efode.vts.utils.ServerCallback;
import vn.efode.vts.utils.ServiceHandler;

import static vn.efode.vts.MainActivity.mGoogleMap;
import static vn.efode.vts.MainActivity.scheduleActive;
import static vn.efode.vts.service.DeviceTokenService.DEVICE_TOKEN;

/**
 * Created by Tuan on 10/04/2017.
 */

public class TrackGPS extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    public static GoogleApiClient mGoogleApiClient;
    private Context mContext;
    private static LocationRequest mLocationRequest;
    private static String API_KEY_MATRIX = "AIzaSyCGXiVPlm9M72lupfolIXkxzSTPNIvRr8g";
    public static Location mLocation = null;
    public static boolean canGetLocation = false;
    boolean zoomOneTime = true;//Just zoom 1
    private static int CONTROLL_ON = 1;
    private static int CONTROLL_OFF = -1;

    final private static int REQ_PERMISSION = 20;// Value permission locaiton FINE
    final private static int REQ_LOCATION = 10;// Value permission locaiton COARSE

    public TrackGPS(Context context){
        mContext = context;
        buildGoogleApiClient();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("STttttt", "track");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
//        mLocationRequest.setFastestInterval(30000); //30 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        getCurrentLocation(new LocationCallback() {
            @Override
            public void onSuccess() {
                Log.d("BUGAAAA", "1");
            }

            @Override
            public void onError() {
                Log.d("BUGAAAA", "2");
            }
        });

//        mLocationRequest.setSmallestDisplacement(5); //5 meter

        controllonLocationChanged(CONTROLL_ON);//Enable event onLocationChanged
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onLocationChanged(Location location) {
        if(zoomOneTime){//Just zoom 1 time
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),15));
            zoomOneTime =false;
        }
        Log.d("ONLOCATIONCHANGE","AAAAAAAAAAAAA");

        if(ApplicationController.getActiveSchudule() != null)
            sendLocationDataToServer(location);//Send data to server
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Check permission to using location
     * @return true - can
     */
    public boolean checkLocationPermission() {

        if (ActivityCompat.checkSelfPermission(mContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((MainActivity) mContext,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {


                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions((MainActivity) mContext,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQ_PERMISSION);

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions((Activity) mContext,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQ_PERMISSION);
            }
            canGetLocation = false;
            return false;
        } else {
            //mGoogleMap.setMyLocationEnabled(true);
            canGetLocation = true;
            return true;
        }
    }

    public synchronized void buildGoogleApiClient() {
//        Toast.makeText(mContext, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /**
     * Check permission to using location for setMyLocationEnable (Point blue in google map)
     * @return true - can
     */
    public boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(mContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            return false;
        } else {
            return true;
        }
    }

    /**
     * get Current Location
     * @param callback callback when user can get location
     * @return location
     */
    public void getCurrentLocation(LocationCallback callback){
        if(checkPermission()){
            mLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if(mLocation != null)
                    callback.onSuccess();
            //        Log.d("CurrentLocation",mLastLocation.getLatitude() + " | " + mLastLocation.getLongitude());
            else callback.onError();

        }
    }

    /**
     * Register / Unregister the listener location changed
     * @param value
     */
    public void controllonLocationChanged(int value){
        if(value == CONTROLL_OFF)// unregister the listener
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        if(value == CONTROLL_ON) {//register the listener to listen location change
            if(checkLocationPermission())
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    /**
     * send Location data to server
     * @param origin previous location 2s
     */
    private void sendLocationDataToServer(final Location origin){
        getCurrentLocation(new LocationCallback() {
            @Override
            public void onSuccess() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 2s
                        if(scheduleActive != null){
                            Double speed = getSpeed(origin,mLocation);
                            HashMap<String,String> params = new HashMap<String, String>();
                            params.put("scheduleId", String.valueOf(scheduleActive.getScheduleId()));
                            params.put("locationLat", String.valueOf(mLocation.getLatitude()));
                            params.put("locationLong", String.valueOf(mLocation.getLongitude()));
                            params.put("speed", String.valueOf(speed));
                            params.put("deviceId", ApplicationController.sharedPreferences.getString(DEVICE_TOKEN, null));
                            ServiceHandler.makeServiceCall(ServiceHandler.DOMAIN + "/api/v1/scheduleActive/insert",
                                    Request.Method.POST, params, new ServerCallback() {
                                        @Override
                                        public void onSuccess(JSONObject result) {
                                            try {
                                                Log.d("INSERT", result.getString("content"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            Toast.makeText(mContext,"Insert Schedule Active",Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onError(VolleyError error) {
                                            Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                    }
                }, 2000);
            }

            @Override
            public void onError() {

            }
        });


    }

    /**
     * get Speed with 2 location
     * @param origin previous location
     * @param dest current location
     * @return speed(double)
     */
    private Double getSpeed(Location origin, Location dest){
        final Double[] speed = {0.0};
            ServiceHandler.makeServiceCall(getURLDistance(origin,dest),
                    Request.Method.GET, null, new ServerCallback() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            Double distance = PathJSONParser.pareDistance(result);

                            speed[0] = distance * 60 * 60;
                        }

                        @Override
                        public void onError(VolleyError error) {

                        }
                    });
        return speed[0];
    }

    /**
     * get URL to call API distance
     * @param origin source location
     * @param dest destination location
     * @return string url
     */
    private String getURLDistance(Location origin, Location dest){
        String output = "json";

        // Origin of route
        String str_origin = "origin=" + origin.getLatitude() + "," + origin.getLongitude();

        // Destination of route
        String str_dest = "destination=" + dest.getLatitude() + "," + dest.getLongitude();

        //key
        String keyDistance = "key=" + API_KEY_MATRIX;

        // Building the parameters to the web service
//        String parameters = str_origin+"&"+str_dest+"&"+sensor;
        String parameters = "?units=imperial&"+ str_origin + "&" + str_dest + "&" + keyDistance;

        String url = "https://maps.googleapis.com/maps/api/distancematrix/" + output + parameters;
        Log.d("AAAAAAAAAAAAAa",url);
        return url;
    }

}
