package vn.efode.vts.service;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

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
import java.util.List;

import vn.efode.vts.MainActivity;
import vn.efode.vts.application.ApplicationController;
import vn.efode.vts.model.ScheduleActive;
import vn.efode.vts.utils.LocationCallback;
import vn.efode.vts.utils.PathJSONParser;
import vn.efode.vts.utils.RealmDatabase;
import vn.efode.vts.utils.ServerCallback;
import vn.efode.vts.utils.ServiceHandler;

import static vn.efode.vts.MainActivity.mGoogleMap;
import static vn.efode.vts.service.DeviceTokenService.DEVICE_TOKEN;

/**
 * Created by Tuan on 10/04/2017.
 */

public class TrackGPS extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    public static GoogleApiClient mGoogleApiClient;
    private static Context mContext;
    private static LocationRequest mLocationRequest;
    private static String API_KEY_MATRIX = "AIzaSyCGXiVPlm9M72lupfolIXkxzSTPNIvRr8g";
    private static String REQUEST_PERMISSION_INTENT = "vn.efode.vts.requestpermission";
    public static Location mLocation = null;
    public static String TAG_ERROR = "log_error";
    boolean zoomOneTime = true;//Just zoom 1
    private static int CONTROLL_ON = 1;
    private static int CONTROLL_OFF = -1;

    final private static int REQ_PERMISSION = 20;// Value permission locaiton COARSE
    final private static int REQ_LOCATION = 10;// Value permission locaiton

    public TrackGPS() {
    }

    public TrackGPS(Context context){
//        mContext = context;

//        buildGoogleApiClient();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("service_aaaaaaa","create");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("service_aaaaaaa","startcommand");
        mContext = getApplicationContext();
        Log.d("service_aaaaaaa","startcommand1");
        if(mGoogleApiClient == null){
            buildGoogleApiClient();
            Log.d("service_aaaaaaa","startcommand2");
        } else {
            if(!mGoogleApiClient.isConnected()) mGoogleApiClient.connect();
        }
        return START_STICKY;

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("service_aaaaaaa","connected");
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

        if(zoomOneTime && mGoogleMap != null){//Just zoom 1 time
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),15));
            zoomOneTime =false;
        }
        Log.d("service_aaaaaaa","locationchanged");
        mLocation = location;
        if(ApplicationController.getActiveSchudule() != null){
            Log.d("service_aaaaaaa","senddata");
//            handleNewLocation(location);
            sendLocationDataToServer(location);//Send data to server
        }

    }

    /**
     * Check internet is enable
     * @return true - enable
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(mContext.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private boolean isOpenMainActivity() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfo = manager.getRunningTasks(1);
        ComponentName componentInfo = runningTaskInfo.get(0).topActivity;

        String currentPackageName = componentInfo.getClassName();
        if(currentPackageName.equals("vn.efode.vts.MainActivity")) {
            //Do whatever here
            return true;
        }
        return false;
    }

    /**
     * Check permission to using location
     * @return true - can
     */
    public boolean checkLocationPermission() {

        if (ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale( (MainActivity) mContext,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)) {


                    //Prompt the user once explanation has been shown
                    ActivityCompat.requestPermissions((MainActivity) mContext,
                            new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                            REQ_PERMISSION);

                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions((Activity) mContext,
                            new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                            REQ_PERMISSION);
                }
                return false;

        } else {
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
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
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
            if(checkPermission())
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            else {
                if(isOpenMainActivity()) {
                    Intent i = new Intent();
                    i.setAction(REQUEST_PERMISSION_INTENT);
                    sendBroadcast(i);
                }
            }
        }
    }

    /**
     * send Location data to server
     * @param origin previous location 2s
     */
    private void sendLocationDataToServer(final Location origin){
//        if(RealmDatabase.getListData().size() > 0 && !ApplicationController.sharedPreferences.getBoolean("isUploading",false)) {
//            ApplicationController.sharedPreferences.edit().putBoolean("isUploading",true);
//            RealmDatabase.uploadOfflineDatatoServer(0);// Upload data khi device co du lieu
//            Log.d("bugg","0");
//        }
        Log.d("service_aaaaaaa","1");
        getCurrentLocation(new LocationCallback() {
            @Override
            public void onSuccess() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("service_aaaaaaa","2");
                        //Do something after 2s
                        if(ApplicationController.getActiveSchudule() != null){
                            try {
                                final int speed = getSpeed(origin,mLocation);
                                HashMap<String,String> params = new HashMap<String, String>();
                                Log.d("service_aaaaaaa1",String.valueOf(ApplicationController.getActiveSchudule().getScheduleId()));
                                params.put("scheduleId", String.valueOf(ApplicationController.getActiveSchudule().getScheduleId()));
                                params.put("locationLat", String.valueOf(mLocation.getLatitude()));
                                params.put("locationLong", String.valueOf(mLocation.getLongitude()));
                                params.put("speed", String.valueOf(speed));
                                params.put("deviceId", ApplicationController.sharedPreferences.getString(DEVICE_TOKEN, null));
                                ServiceHandler.makeServiceCall(ServiceHandler.DOMAIN + "/api/v1/scheduleActive/insert",
                                        Request.Method.POST, params, new ServerCallback() {
                                            @Override
                                            public void onSuccess(JSONObject result) {
                                                Log.d("service_aaaaaaa","3");
                                                try {
                                                    Log.d("INSERT", result.getString("content"));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
//                                            Toast.makeText(mContext,"Insert Schedule Active",Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onError(VolleyError error) {
//                                           Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_SHORT).show();
                                                RealmDatabase.storageOnDiviceRealm(
                                                        new ScheduleActive(
                                                                String.valueOf(ApplicationController.getActiveSchudule().getScheduleId()),
                                                                ApplicationController.sharedPreferences.getString(DEVICE_TOKEN,null),
                                                                mLocation.getLatitude(), mLocation.getLongitude(),speed)
                                                );
                                                Log.d("INSERT", "error");
                                            }
                                        });

                            } catch (Exception e){
                                Log.d(TAG_ERROR,String.valueOf(e.getMessage()));
                                Log.d("service_aaaaaaa",String.valueOf(e.getMessage()));
                            }

                        }

                    }
                }, 2000);
            }

            @Override
            public void onError() {
                Log.e(TAG_ERROR,"error get location");
                Log.d("bugg","5");
            }
        });


    }

    /**
     * get Speed with 2 location
     * @param origin previous location
     * @param dest current location
     * @return speed(double)
     */
    private int getSpeed(Location origin, Location dest){
        final int[] speed = {0};
        String url = getURLDistance(origin,dest);
        if(url != null)
            ServiceHandler.makeServiceCall(url,
                    Request.Method.GET, null, new ServerCallback() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            Double distance = PathJSONParser.pareDistance(result);

                            int temp = (int) (distance * 60 * 60);//km/h
                            speed[0] = Integer.valueOf(temp);
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
        String url = null;
        try {
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

            url = "https://maps.googleapis.com/maps/api/distancematrix/" + output + parameters;
            Log.d("AAAAAAAAAAAAAa",url);
        } catch (Exception e){
            Log.e("error_location",String.valueOf(e.getMessage()));
        }

        return url;
    }


}
