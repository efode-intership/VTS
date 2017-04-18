package vn.efode.vts;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import vn.efode.vts.adapter.WarningAdapter;
import vn.efode.vts.application.ApplicationController;
import vn.efode.vts.model.Schedule;
import vn.efode.vts.model.WarningTypes;
import vn.efode.vts.utils.ReadTask;
import vn.efode.vts.utils.ServerCallback;
import vn.efode.vts.utils.ServiceHandler;

import static vn.efode.vts.service.DeviceTokenService.DEVICE_TOKEN;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;


    public static GoogleMap mGoogleMap = null;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    LatLng latLng;//Current location
    Marker currLocationMarker;//Current maker

    int demo = 0;//Demo controll fab button cancel/completed journey

    boolean temp = true;//Just zoom 1 time

    boolean controllFabSchedule = false;//Boolean to controll fabButton Cancel/Completed Journey

    FloatingActionButton fabControllSchedule;//Button Controll Schedule

    private static int CONTROLL_ON = 1;
    private static int CONTROLL_OFF = -1;

    PendingResult<LocationSettingsResult> result;
    final private static int REQ_PERMISSION = 20;// Value permission locaiton
    final private static int REQ_PERMISSION_CALL = 100;// Value permission call phone
    private static int REQUEST_LOCATION = 10;
    final private static int REQ_PERMISSION = 20;
    private static String API_KEY_DIRECTION = "AIzaSyAJCQ6Wf-aQbUbF5wLRMs4XtgCS-vph6IE";
    private static String API_KEY_MATRIX = "AIzaSyCGXiVPlm9M72lupfolIXkxzSTPNIvRr8g";
    private static Schedule scheduleLatest = null;//Lich trinh gan nhat cua user
    private ListView listView;
    private EditText edtDescription;
    private Button btnOk;
    private Button btnCancel;
    private TextView txtConfirmWarning;
    double longitude;
    double latitude;
    private String showWarningUrl = ServiceHandler.DOMAIN + "/api/v1/warningTypes";
    private String addWarningUrl = ServiceHandler.DOMAIN + "/api/v1/warning/create";
    private WarningTypes warningTypes;
    private ArrayList<WarningTypes> arrWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MapsInitializer.initialize(this);

        FloatingActionButton fab_call = (FloatingActionButton) findViewById(R.id.fab_call);
        fab_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               callServer();
            }
        });

        FloatingActionButton fab_warning = (FloatingActionButton) findViewById(R.id.fab_warning);
        fab_warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWarning();
            }
        });

        fabControllSchedule = (FloatingActionButton) findViewById(R.id.fab_controll_schedule);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Log.d("MAPPPP", "onCreate");


//        HashMap<String,String> params = new HashMap<String,String>();
//        params.put("email","tuan@gmail.com");
//        params.put("password","123123");
//
//        ServiceHandler serviceHandler = new ServiceHandler();
//        serviceHandler.makeServiceCall("http://192.168.1.16/web_app/public/api/v1/user/validate", Request.Method.POST,
//                params, new ServerCallback() {
//            @Override
//            public void onSuccess(JSONObject result) {
//                Log.d("Result",result.toString());
//            }
//
//            @Override
//            public void onError(VolleyError error){
//                Log.d("Result",error.getMessage());
//            }
//
//        });
//
//        HashMap<String,String> params2 = new HashMap<String,String>();
//        params2.put("userId", "6");
//        String url = "http://192.168.1.16/web_app/public/api/v1/user/{userId}";
//
//        serviceHandler.makeServiceCall(url, Request.Method.GET, params2, new ServerCallback() {
//            @Override
//            public void onSuccess(JSONObject result) {
//                Log.d("Result", result.toString());
//            }
//            @Overrid
//            public void onError(VolleyError error){
//                Log.d("Result",error.getMessage());
//            }
//        });

        addControls();
        addEvents();


        String userId = "6";
        Log.d("ScheduleAAA","START");
        getScheduleLatest(userId);//Lấy shedule gần nhất của user dựa theo userid
        Log.d("ScheduleAAA","END");

        Log.d("Devide ID AAAA", ApplicationController.sharedPreferences.getString(DEVICE_TOKEN,null));
    }

    /**
     * check permission to call phone
     * @return
     */
    private boolean checkPermissionCallPhone(){
        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.CALL_PHONE},REQ_PERMISSION_CALL);
            return false;
        }
        else return true;
    }

    private void callServer() {
        String phone = getString(R.string.phone);
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri uri = Uri.parse("tel:" + phone);
        intent.setData(uri);
        if(checkPermissionCallPhone())
            startActivity(intent);

    }

    private void addEvents() {

    }


    private void addControls() {


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_home) {

        } else if (id == R.id.nav_schedule) {
            Intent intent = new Intent(MainActivity.this, ScheduleHistoryActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_signout) {
            ApplicationController.sharedPreferences.edit().remove(ApplicationController.USER_SESSION).commit();
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        if(checkLocationPermission()){
            mGoogleMap.setMyLocationEnabled(true);
            buildGoogleApiClient();//setting GoogleAPIclient
        }





    }

    /**
     * Check permission to using location
     * @return true - can
     */
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {


                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQ_PERMISSION);

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQ_PERMISSION);
            }
            return false;
        } else {
            return true;
        }
    }


    protected synchronized void buildGoogleApiClient() {
        Toast.makeText(this, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /**
     * Draw road between 2 location in google map
     */
    private void drawroadBetween2Location(LatLng latLng1, LatLng latLng2) {
        ArrayList<LatLng> temp = new ArrayList<LatLng>();
        temp.add(new LatLng(10.8719808, 106.790409));
        String url = getMapsApiDirectionsUrl(latLng1, latLng2, temp);

        Log.d("onMapClick", url.toString());
        ReadTask downloadTask = new ReadTask();
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
        Log.d("Log12/4", "12");

        //To calculate distance between points
//        float[] results = new float[1];
//        Location.distanceBetween(latitude, longitude,
//                10.882323, 106.782625,
//                results);
//        Log.d("onMapClick",String.valueOf(results));
    }

    /**
     * Make a Maker in google map
     * @param location location maker
     * @param title title maker
     */
    private void makeMaker(LatLng location, String title) {
        Log.d("Log12/4", "9");
        LatLng maker = new LatLng(location.latitude, location.longitude);
        mGoogleMap.addMarker(new MarkerOptions().title(title).position(maker));
        Log.d("Log12/4", "10");
    }

    /**
     * get Url to request the Google Directions API
     * @param origin start point location
     * @param dest destination point location
     * @return
     */
    private String getMapsApiDirectionsUrl(LatLng origin, LatLng dest, ArrayList<LatLng> waypoints) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        //Waypoints
        String str_waypoints = "waypoints=";
        boolean firts = false;
        for (LatLng latlng : waypoints) {
            if (!firts) {
                str_waypoints += "via:" + latlng.latitude + "," + latlng.longitude;
                firts = true;
            } else {
                str_waypoints += "|via:" + latlng.latitude + "," + latlng.longitude;
            }
        }

        //key
        String keyDirection = "key=" + API_KEY_DIRECTION;

        // Building the parameters to the web service
//        String parameters = str_origin+"&"+str_dest+"&"+sensor;
        String parameters = str_origin + "&" + str_dest + "&" + str_waypoints + "&" + keyDirection;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("Permission", "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch ( requestCode ) {
            case REQ_PERMISSION: {
                if ( grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    // permission was granted, yay!
                    if (ActivityCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mGoogleMap.setMyLocationEnabled(true);
                        buildGoogleApiClient();

                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
            case REQ_PERMISSION_CALL: {
                if ( grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    // permission was granted, yay!
                    if (ActivityCompat.checkSelfPermission(this,
                            android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        callServer();
                    }
                }
                return;
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Unregister receiver on destroy
        if (gpsReceiver != null)
            unregisterReceiver(gpsReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(gpsReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));//Register broadcast r
    }

    /**
     * check permission for app
     */
    private void checkPermisstion(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.INTERNET
                }, 10);
            }
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.INTERNET
                }, 10);
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "onConnected", Toast.LENGTH_SHORT).show();
        checkLocationPermission();
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        Log.d("onConnected",String.valueOf(mLastLocation));
        if (mLastLocation != null) {
//            place marker at current position
            mGoogleMap.clear();
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            currLocationMarker = mGoogleMap.addMarker(markerOptions);
            showDialogStartJourney();
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(50000); //50 seconds
        mLocationRequest.setFastestInterval(30000); //30 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setSmallestDisplacement(5); //5 meter

//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    private BroadcastReceiver gpsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                //Do your stuff on GPS status change
                Toast.makeText(MainActivity.this,"GPS enable!",Toast.LENGTH_LONG).show();
            }
            else  Toast.makeText(MainActivity.this,"GPS disable!",Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"onConnectionSuspended",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onLocationChanged(Location location) {

        //place marker at current position
        //mGoogleMap.clear();
        demo++;
        if(demo == 2) {//Changed fabbutton cancel journey to complete journey
            controllFabSchedule = true;
            fabControllSchedule.setImageDrawable(getResources().getDrawable(R.drawable.completed));
        }
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if(temp){
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
//            drawroadBetween2Location(latLng,new LatLng(10.8719808, 106.790409));
            temp = false;
        }
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Current Position");
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//        currLocationMarker = mGoogleMap.addMarker(markerOptions);
//
        Toast.makeText(this,"Location Changed",Toast.LENGTH_SHORT).show();
//
//        //zoom to current position:
//        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));

        //If you only need one location, unregister the listener
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


    public void showWarning() {
        // custom dialog
        final Dialog dialogShowWarning = new Dialog(this);
        dialogShowWarning.setContentView(R.layout.dialog_add_warning);
        dialogShowWarning.setTitle("Thêm cảnh báo");
        listView = (ListView) dialogShowWarning.findViewById(R.id.listview_dialog_warning);
        // set the custom dialog components - text, image and button
        ServiceHandler serviceHandler = new ServiceHandler();
        HashMap<String,String> params = new HashMap<String,String>();
        params.put("","");

        serviceHandler.makeServiceCall(showWarningUrl, Request.Method.GET, params, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                try {
                    Boolean error = gson.fromJson(result.getString("error"), Boolean.class);
                    if (!error) {
                        arrWarning = new ArrayList<WarningTypes>();
                        Type listType = new TypeToken<List<WarningTypes>>() {}.getType();
                        arrWarning = gson.fromJson(result.getString("content"), listType );


                        final WarningAdapter warningAdapter = new WarningAdapter(
                                MainActivity.this,
                                R.layout.warning_types_list,
                                arrWarning);
                        listView.setAdapter(warningAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                warningTypes = (WarningTypes) adapterView.getItemAtPosition(i);
                                addWarning();
                                dialogShowWarning.dismiss();
                            }
                        });
                    }
                    else {

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(VolleyError error){
                Log.d("Resultsxxx",error.getMessage());
            }
        });


        dialogShowWarning.show();
    }

    public void addWarning(){

        final Dialog dialogConfirmWarning = new Dialog(MainActivity.this);
        dialogConfirmWarning.setContentView(R.layout.dialog_confirm_warning);
        dialogConfirmWarning.setTitle("Xác nhận");


        btnOk = (Button) dialogConfirmWarning.findViewById(R.id.button_confirmwarning_done);
        btnCancel = (Button) dialogConfirmWarning.findViewById(R.id.button_confirmwarning_cancel);
        txtConfirmWarning = (TextView) dialogConfirmWarning.findViewById(R.id.textview_confirmwarning_confirm);
        edtDescription = (EditText) dialogConfirmWarning.findViewById(R.id.edittext_confirmwarning_description);
        txtConfirmWarning.setText("Xác nhận cảnh báo " + warningTypes.getType() + "?");
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Request warning
                HashMap<String,String> paramsCreateWarning = new HashMap<String,String>();
                paramsCreateWarning.put("userId", String.valueOf(ApplicationController.getCurrentUser().getId()));
                paramsCreateWarning.put("warningTypeId",String.valueOf(warningTypes.getWarningTypeId()));
                Log.d("location_warning", latLng.toString());
                Log.d("location_warning", String.valueOf(latLng.latitude));
                Log.d("location_warning", String.valueOf(latLng.longitude));
                paramsCreateWarning.put("locationLat",String.valueOf(latLng.latitude));
                paramsCreateWarning.put("locationLong",String.valueOf(latLng.longitude));
                paramsCreateWarning.put("description",String.valueOf(edtDescription.getText()));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String startTime = sdf.format(new Date());
                paramsCreateWarning.put("startTime",startTime);

                Date date = null;
                try {
                    date = sdf.parse(startTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar calendar = Calendar.getInstance();

                calendar.setTime(date);
                calendar.add(Calendar.MINUTE, warningTypes.getDefaultTime());
                String endTime = sdf.format(calendar.getTime());
                Log.d("timesss",startTime);
                Log.d("timesss",endTime);
                Log.d("timesss",String.valueOf(warningTypes.getDefaultTime()));

                paramsCreateWarning.put("endTime",endTime);
                ServiceHandler serviceHandler = new ServiceHandler();
                serviceHandler.makeServiceCall(addWarningUrl, Request.Method.POST,
                        paramsCreateWarning, new ServerCallback() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                Log.d("Result",result.toString());
                                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                                try {
                                    Boolean error = gson.fromJson(result.getString("error"), Boolean.class);
                                    if (!error) {

                                        Toast.makeText(MainActivity.this,"Thông báo " + warningTypes.getType() + " thành công",Toast.LENGTH_SHORT).show();
                                        Log.d("positiss",String.valueOf(warningTypes.getWarningTypeId()));
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this,"Không thành công",Toast.LENGTH_SHORT).show();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(VolleyError error){
                                Log.d("Result",error.getMessage());
                                Toast.makeText(MainActivity.this,"Không thành công",Toast.LENGTH_SHORT).show();
                            }

                        });




                dialogConfirmWarning.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogConfirmWarning.dismiss();
            }
        });

        dialogConfirmWarning.show();
    }
    /**
     * Function to call API start journey
     * @param scheduleId schedule ID
     * @param deviceId Token ID device
     */
    private void startJourney(final String scheduleId, String deviceId){
        controllonLocationChanged(CONTROLL_ON);//Enable event onLocationChanged
        HashMap<String, String> params = new HashMap<String,String>();
        params.put("scheduleId",scheduleId);
        params.put("deviceId",deviceId);

        ServiceHandler.makeServiceCall(ServiceHandler.DOMAIN + "/api/v1/schedule/start", Request.Method.POST, params, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                Log.d("Result_volley",result.toString());
                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                try {
                    Boolean error = gson.fromJson(result.getString("error"), Boolean.class);
                    if (!error) {
                        LatLng locationDestination = new LatLng(Double.parseDouble(scheduleLatest.getLocationLatEnd()),
                                Double.parseDouble(scheduleLatest.getLocationLongEnd()));
                        makeMaker(locationDestination,scheduleLatest.getEndPointAddress());
                        drawroadBetween2Location(latLng,new LatLng(Double.parseDouble(scheduleLatest.getLocationLatEnd()),
                                Double.parseDouble(scheduleLatest.getLocationLongEnd())));
                        ctrollFabButtonSchedule(CONTROLL_ON);
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });

    }

    /**
     * Lấy shedule gần nhất của user dựa theo userid
     * @param userId userId
     */
    private void getScheduleLatest(String userId){

        HashMap<String, String> params = new HashMap<String,String>();
        params.put("userId",userId);

        ServiceHandler.makeServiceCall(ServiceHandler.DOMAIN + "/api/v1/schedule/incoming/user/{userId}", Request.Method.GET, params, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                Log.d("Result_volley",result.toString());
                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                try {
                    Boolean error = gson.fromJson(result.getString("error"), Boolean.class);
                    if (!error) {

                        scheduleLatest = gson.fromJson(result.getString("content"), Schedule.class);//Gan schedule gan nhat
                        if(scheduleLatest != null)
                            Log.d("AAAAAAAA", String.valueOf(scheduleLatest.getScheduleId()));
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });

    }

    /**
     * Show dialog request user start journey
     * Yes -> call API start journey
     * No -> call API cancel journey
     */
    private void showDialogStartJourney(){
        if(scheduleLatest != null ){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("You have journey go to "+ scheduleLatest.getEndPointAddress() + " with "
                            + scheduleLatest.getIntendStartTime() + " [id:"+scheduleLatest.getScheduleId()+ "]")
                    .setMessage("Do you want start journey now?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startJourney(String.valueOf(scheduleLatest.getScheduleId()),ApplicationController.sharedPreferences.getString(DEVICE_TOKEN,null));
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Have fun tonight")
                    .setMessage("You don't have any schedule!")
                    .show();

        }

    }

    /**
     * Register / Unregister the listener location changed
     * @param value
     */
    private void controllonLocationChanged(int value){
        if(value == CONTROLL_OFF)// unregister the listener
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        if(value == CONTROLL_ON) {//register the listener to listen location change
            checkLocationPermission();
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    /**
     * Invisible/Visible fab button Controll schedule
     * @param value on/off
     */
    private void ctrollFabButtonSchedule(int value){
        if(value == CONTROLL_ON){
//            controllFabSchedule = true;
            fabControllSchedule.setVisibility(View.VISIBLE);
            if(!controllFabSchedule)
                fabControllSchedule.setImageDrawable(getResources().getDrawable(R.drawable.cancel));
            else fabControllSchedule.setImageDrawable(getResources().getDrawable(R.drawable.completed));
            fabControllSchedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(controllFabSchedule) {//Completed Journey
                        controllFabSchedule = false;
                        completedJourney(String.valueOf(scheduleLatest.getScheduleId()), ApplicationController.sharedPreferences.getString(DEVICE_TOKEN, null));
                        fabControllSchedule.setImageDrawable(getResources().getDrawable(R.drawable.cancel));

                    } else {//Cancel journey
                        controllFabSchedule = true;
                        cancelJourney(String.valueOf(scheduleLatest.getScheduleId()), ApplicationController.sharedPreferences.getString(DEVICE_TOKEN, null));
                        fabControllSchedule.setImageDrawable(getResources().getDrawable(R.drawable.completed));

                    }
                    fabControllSchedule.setVisibility(View.INVISIBLE);

                }
            });
        }

    }
    /**
     * Function to call API completed journey
     */
    private void completedJourney(final String scheduleId, String deviceId){
        HashMap<String, String> params = new HashMap<String,String>();
        params.put("scheduleId",scheduleId);
        params.put("deviceId",deviceId);

        ServiceHandler.makeServiceCall(ServiceHandler.DOMAIN + "/api/v1/schedule/complete", Request.Method.POST, params, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                Log.d("Result_volley",result.toString());
                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                try {
                    Boolean error = gson.fromJson(result.getString("error"), Boolean.class);
                    if (!error) {
                        mGoogleMap.clear();
                        controllonLocationChanged(CONTROLL_OFF);
                        Toast.makeText(MainActivity.this, "Completed journey_id:" + scheduleId,Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    /**
     * Function to call API cancel journey
     */
    private void cancelJourney(final String scheduleId, String deviceId){
        HashMap<String, String> params = new HashMap<String,String>();
        params.put("scheduleId",scheduleId);
        params.put("deviceId",deviceId);

        ServiceHandler.makeServiceCall(ServiceHandler.DOMAIN + "/api/v1/schedule/cancel", Request.Method.POST, params, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                Log.d("Result_volley",result.toString());
                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                try {
                    Boolean error = gson.fromJson(result.getString("error"), Boolean.class);
                    if (!error) {
                        mGoogleMap.clear();
                        controllonLocationChanged(CONTROLL_OFF);
                        Toast.makeText(MainActivity.this, "Cancel journey_id:" + scheduleId,Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }
}
