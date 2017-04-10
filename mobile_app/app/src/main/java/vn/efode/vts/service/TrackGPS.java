package vn.efode.vts.service;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import vn.efode.vts.MainActivity;

/**
 * Created by Tuan on 10/04/2017.
 */

public class TrackGPS extends Service implements LocationListener {

    private final Context mContext;


    boolean checkGPS = false;


    boolean checkNetwork = false;

    boolean canGetLocation = false;

    Location loc;
    double latitude;
    double longitude;


    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;


    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    protected LocationManager locationManager;

    public TrackGPS(Context mContext) {
        this.mContext = mContext;
        getLocation();
    }

    private Location getLocation() {

        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            checkGPS = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            checkNetwork = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!checkGPS && !checkNetwork) {
                Toast.makeText(mContext, "No Service Provider Available", Toast.LENGTH_SHORT).show();
            } else {
                this.canGetLocation = true;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions((MainActivity) mContext,new String[]{
                                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                                        android.Manifest.permission.INTERNET
                                }, 10);
                            }
                            if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions((MainActivity) mContext, new String[]{
                                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                        android.Manifest.permission.INTERNET
                                }, 10);
                            }
                        }
                // First get location from Network Provider
                if (checkNetwork) {
                    Toast.makeText(mContext, "Network", Toast.LENGTH_SHORT).show();

                    try {
                        Log.d("LOCATIONNETWORK", "Network1");
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("LOCATIONNETWORK", "Network2");
                        if (locationManager != null) {
                            Log.d("LOCATIONNETWORK", "Network3");
                            loc = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            Log.d("LOCATIONNETWORK", "Network4");

                        }

                        if (loc != null) {
                            latitude = loc.getLatitude();
                            longitude = loc.getLongitude();

                            Log.d("LOCATIONNETWORK",latitude + " | " + longitude);

                        }
                    }
                    catch(SecurityException e){
                        Log.d("LOCATIONNETWORK", e.getMessage());
                    }
                }
                else {
                    // if GPS Enabled get lat/long using GPS Services
                    if (checkGPS) {
                        Toast.makeText(mContext,"GPS",Toast.LENGTH_SHORT).show();
                        if (loc == null) {
                            try {
                                locationManager.requestLocationUpdates(
                                        LocationManager.GPS_PROVIDER,
                                        MIN_TIME_BW_UPDATES,
                                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                                Log.d("GPS Enabled", "GPS Enabled");
                                if (locationManager != null) {
                                    loc = locationManager
                                            .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                    if (loc != null) {
                                        latitude = loc.getLatitude();
                                        longitude = loc.getLongitude();
                                    }
                                }
                            } catch (SecurityException e) {

                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return loc;
    }

    public double getLongitude() {
        if (loc != null) {
            longitude = loc.getLongitude();
        }
        return longitude;
    }

    public double getLatitude() {
        if (loc != null) {
            latitude = loc.getLatitude();
        }
        return latitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);


        alertDialog.setTitle("GPS Not Enabled");

        alertDialog.setMessage("Do you wants to turn On GPS");


        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                ((AppCompatActivity)mContext).startActivityForResult(intent,1);
            }
        });


        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        alertDialog.show();
    }


    public void stopUsingGPS() {
        if (locationManager != null) {

            locationManager.removeUpdates(TrackGPS.this);
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
