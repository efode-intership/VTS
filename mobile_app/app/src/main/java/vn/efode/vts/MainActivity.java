package vn.efode.vts;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import vn.efode.vts.adapter.WarningAdapter;
import vn.efode.vts.application.ApplicationController;
import vn.efode.vts.model.OtherVehiclesInformation;
import vn.efode.vts.model.Schedule;
import vn.efode.vts.model.Warning;
import vn.efode.vts.model.WarningTypes;
import vn.efode.vts.service.TrackGPS;
import vn.efode.vts.utils.LocationCallback;
import vn.efode.vts.utils.PathJSONParser;
import vn.efode.vts.utils.ServerCallback;
import vn.efode.vts.utils.ServiceHandler;

import static vn.efode.vts.R.id.map;
import static vn.efode.vts.service.DeviceTokenService.DEVICE_TOKEN;
import static vn.efode.vts.service.TrackGPS.mLocation;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, RoutingListener {

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;


    public static GoogleMap mGoogleMap = null;
    LocationRequest mLocationRequest;
//    GoogleApiClient mGoogleApiClient;

    public static Polyline polyline = null;//Instance

    private static TrackGPS trackgps;

    private static String scheduleJson;


    FloatingActionButton fabCancel;//Button Controll Schedule
    FloatingActionButton fabComplete;//Button Controll Schedule

    private static int CONTROLL_ON = 1;
    private static int CONTROLL_OFF = -1;

    private static AlertDialog dialogRequestStartSchedule;// Dialog request user start journey or not
    private static AlertDialog dialogConfirm;//Dialog confirm cancel/complete journey
    private static final int[] COLORS = new int[]{R.color.colorPrimaryDark,R.color.colorAccent,R.color.colorPrimary,R.color.colorBtent,R.color.primary_dark_material_light};

    private static Location previousLocation = null;

    PendingResult<LocationSettingsResult> result;
    final private static int REQ_PERMISSION = 20;// Value permission locaiton
    final private static int REQ_PERMISSION_CALL = 100;// Value permission call phone
    final private static int REQ_LOCATION = 10;
    private static String API_KEY_DIRECTION = "AIzaSyAJCQ6Wf-aQbUbF5wLRMs4XtgCS-vph6IE";
    private static String API_KEY_MATRIX = "AIzaSyCGXiVPlm9M72lupfolIXkxzSTPNIvRr8g";
    private static Schedule scheduleLatest = null;//Lich trinh gan nhat cua user
    public static Schedule scheduleActive = null; //Schedule dang chay cua user

    private boolean controllDraw = true;
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
    private ArrayList<Warning> arrWarningPoint;
    private Timer timerShowWarning;
    private TimerTask timerTaskShowWarning;
    private Timer timerShowOtherVehicles;
    private TimerTask timerTaskShowOtherVehicles;
    private Timer timerSessionSchedule;
    private TimerTask timerTaskSessionSchedule;

    private Button btnConfirmCallServer;
    private Button btnCancelCallServer;
    private TextView txtDriverName;
    private TextView txtDriverPhone;
    private Button btnCancelCallOthervehicles;
    private Button btnConfirmCallOtherVehicles;
    private ArrayList<OtherVehiclesInformation> listOrtherVehicles;
    private Dialog dialogCallOtherVehicles;
    private Dialog dialogCallServer;

    private int ID_KETXE = 1;
    private int ID_PIKACHU = 2;
    private int ID_HONGDUONG = 3;

    NavigationView navigationView;

    /**
     * Selected vehicle marker.
     */
    OtherVehiclesInformation selectedVehicleMarker;

    /**
     * Vehicle marker map.
     */
    static final HashMap<String, Marker> vehicleMarkerMap = new HashMap<String, Marker>();

    static final HashMap<String, OtherVehiclesInformation> vehicleMarkerInfo = new HashMap<String, OtherVehiclesInformation>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        vehicleMarkerMap.clear();
        if (mGoogleMap != null) {
            mGoogleMap.clear();
        }
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
        super.onCreate(savedInstanceState);
        setupVehicleDialog();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MapsInitializer.initialize(this);

        /**
         * setOnClick button call server
         */
        FloatingActionButton fab_call = (FloatingActionButton) findViewById(R.id.fab_call);
        fab_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCallServer = new Dialog(MainActivity.this);
                dialogCallServer.setContentView(R.layout.dialog_confirm_call_server);
                dialogCallServer.setTitle("Call Server");

                btnConfirmCallServer = (Button) dialogCallServer.findViewById(R.id.btnConfirmCallServer);
                btnCancelCallServer = (Button) dialogCallServer.findViewById(R.id.btnCancelCallServer);

                btnConfirmCallServer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callServer();
                        dialogCallServer.dismiss();
                    }
                });

                btnCancelCallServer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogCallServer.dismiss();
                    }
                });

                dialogCallServer.show();

            }
        });

        FloatingActionButton fab_warning = (FloatingActionButton) findViewById(R.id.fab_warning);
        fab_warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWarning();
            }
        });

        fabComplete = (FloatingActionButton) findViewById(R.id.fab_complete);
        fabCancel = (FloatingActionButton) findViewById(R.id.fab_cancel);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        Log.d("onCreate", "AAAAAAAAAAAAAA");


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

//
//        String userId = "6";
//        if (checkLocationPermission())
//            getScheduleLatest(String.valueOf(ApplicationController.getCurrentUser().getId()));//Lấy shedule gần nhất của user dựa theo userid va show dialog

    }

    /**
     * check permission to call phone
     * @return
     */
    private boolean checkPermissionCallPhone() {
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, REQ_PERMISSION_CALL);
            return false;
        } else return true;
    }

    /**
     * call server
     */
    private void callServer() {
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
        String phone = getString(R.string.phone);
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri uri = Uri.parse("tel:" + phone);
        intent.setData(uri);
        if (checkPermissionCallPhone())
            startActivity(intent);

    }

    private void addEvents() {

    }


    private void addControls() {

    }

    @Override
    public void onBackPressed() {
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
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
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
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
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
        mGoogleMap = googleMap;
        if(checkLocationPermission() )
            mGoogleMap.setMyLocationEnabled(true);


        trackgps = new TrackGPS(MainActivity.this);

        startTimerVehicles();
//        if(trackgps.canGetLocation){
//            showDialogStartJourney();
//        }

//        if(checkLocationPermission()){
//
//            buildGoogleApiClient();//setting GoogleAPIclient
//        }
        startShowWarningTimer();
    }

    /**
     * Check permission to using location for setMyLocationEnable (Point blue in google map)
     * @return true - can
     */
    public boolean checkLocationPermission() {
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            return false;
        } else {
            return true;
        }
    }


//    protected synchronized void buildGoogleApiClient() {
//        Toast.makeText(this, "buildGoogleApiClient", Toast.LENGTH_SHORT).show();
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//        mGoogleApiClient.connect();
//    }

    /**
     * Draw road between 2 location in google map
     */
    private void drawroadBetween2Location(LatLng latLng1, LatLng latLng2) {

//        ArrayList<LatLng> temp = new ArrayList<LatLng>();
//        temp.add(new LatLng(10.8719808, 106.790409));
//        String url = getMapsApiDirectionsUrl(latLng1, latLng2, temp);
//
//        Log.d("onMapClick", url.toString());
//        ReadTask downloadTask = new ReadTask();
//        // Start downloading json data from Google Directions API
//        downloadTask.execute(url);
//        Log.d("Log12/4", "12");
        Routing routing = new Routing.Builder()
                .travelMode(Routing.TravelMode.DRIVING)
                .withListener(this)
                .waypoints(latLng1, latLng2)
                .build();
        routing.execute();
        controllDraw = false;

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
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
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
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        //Waypoints
        String str_waypoints = "";
        if(waypoints != null){
            str_waypoints = "waypoints=";
            boolean firts = false;
            for (LatLng latlng : waypoints) {
                if (!firts) {
                    str_waypoints += "via:" + latlng.latitude + "," + latlng.longitude;
                    firts = true;
                } else {
                    str_waypoints += "|via:" + latlng.latitude + "," + latlng.longitude;
                }
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
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
        Log.d("Permission", "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_PERMISSION: {
                Log.d("BUGAAAAA1", String.valueOf(grantResults.length));
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    if (ActivityCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mGoogleMap.setMyLocationEnabled(true);
                        trackgps.controllonLocationChanged(CONTROLL_ON);
                        Log.d("BUGAAAAA1", "REsult");
                        if(isLocationEnabled())
                            getScheduleLatest(String.valueOf(ApplicationController.getCurrentUser().getId()));//Lấy shedule gần nhất của user dựa theo userid va show dialog
//                        buildGoogleApiClient();

                    }

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission FINE denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
            case REQ_PERMISSION_CALL: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                    if (ActivityCompat.checkSelfPermission(this,
                            android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        callServer();
                    }
                }
                return;
            }
//            case REQ_LOCATION: {
//                Log.d("BUGAAAAA2", String.valueOf(grantResults.length));
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // permission was granted, yay!
//                    if (ActivityCompat.checkSelfPermission(this,
//                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                        mGoogleMap.setMyLocationEnabled(true);
////                        buildGoogleApiClient();
//                    }
//                    else {
//                        Toast.makeText(this, "permission COARSE denied", Toast.LENGTH_LONG).show();
//                    }
//                    return;
//                }
//            }
        }

    }


    @Override
    protected void onDestroy() {
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
        super.onDestroy();
        //Unregister receiver on destroy
        if (mReceiver != null)
            unregisterReceiver(mReceiver);
        if (dialogCallOtherVehicles != null) {
            dialogCallOtherVehicles.dismiss();
        }
        stopShowWarningTimerTask();
        stopShowOtherVehicleTimerTask();
        if (dialogRequestStartSchedule != null) {
            dialogRequestStartSchedule.dismiss();
        }
        if (dialogConfirm != null) {
            dialogConfirm.dismiss();
        }
        if (dialogCallServer != null)
        {
            dialogCallServer.dismiss();
        }
    }

    @Override
    protected void onResume() {
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
        super.onResume();
        Log.d("onResume","AAAAAAAAAAAAAA");
        if(scheduleActive != null){
            Log.d("AAAAAAAAAAAAAA", String.valueOf(scheduleActive.getScheduleId()));
        }
        //Register broadcast reciver Location/Network state
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.location.PROVIDERS_CHANGED");
        registerReceiver(mReceiver, filter);
    }


//    @Override
//    public void onConnected(Bundle bundle) {
//        Toast.makeText(this, "onConnected", Toast.LENGTH_SHORT).show();
////        checkLocationPermission();
//        Location mLastLocation = getCurrentLocation();
//        Log.d("onConnected",String.valueOf(mLastLocation));
//        if (mLastLocation != null) {
////            place marker at current position
////            mGoogleMap.clear();
//            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
////            MarkerOptions markerOptions = new MarkerOptions();
////            markerOptions.position(latLng);
////            markerOptions.title("Current Position");
////            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
////            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
////            currLocationMarker = mGoogleMap.addMarker(markerOptions);
//            showDialogStartJourney();
//        }
//
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(50000); //50 seconds
//        mLocationRequest.setFastestInterval(30000); //30 seconds
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        mLocationRequest.setSmallestDisplacement(5); //5 meter
//        controllonLocationChanged(CONTROLL_ON);//Enable event onLocationChanged
//
////        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
////        showWarningPoint();
//    }

    /**
     * Handler service location is enable/disable
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            if(scheduleActive != null) {
                if(isNetworkAvailable()){
                    fabCancel.setVisibility(View.VISIBLE);
                    fabComplete.setVisibility(View.VISIBLE);
                } else {
                    fabCancel.setVisibility(View.INVISIBLE);
                    fabComplete.setVisibility(View.INVISIBLE);
                }

            }
            if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                Log.d("log_gps_network","enable");
                if(isNetworkAvailable()) getScheduleLatest(String.valueOf(ApplicationController.getCurrentUser().getId()));//Lấy shedule gần nhất của user dựa theo userid va show dialog
                if(checkLocationPermission())
                    mGoogleMap.setMyLocationEnabled(true);
                //Do your stuff on GPS status change
//                Toast.makeText(MainActivity.this,"GPS enable!",Toast.LENGTH_LONG).show();
            }
            else  {
                Log.d("log_gps_network","disable");
                if(checkLocationPermission())
                    mGoogleMap.setMyLocationEnabled(false);
//                Toast.makeText(MainActivity.this,"Location service disable!",Toast.LENGTH_LONG).show();
            }
        }
    };

//    @Override
//    public void onConnectionSuspended(int i) {
//        Toast.makeText(this,"onConnectionSuspended",Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();
//    }
//
//
//
//    @Override
//    public void onLocationChanged(Location location) {
//        startTimer();
//        //place marker at current position
//        //mGoogleMap.clear();
////        demo++;
////        if(demo == 2) {//Changed fabbutton cancel journey to complete journey
////            controllFabSchedule = true;
////            fabControllSchedule.setImageDrawable(getResources().getDrawable(R.drawable.completed));
////        }
//        if (currLocationMarker != null) {
//            currLocationMarker.remove();
//        }
//        latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        if(scheduleActive != null)
//            sendLocationDataToServer(location);//Send data to server
////        MarkerOptions markerOptions = new MarkerOptions();
////        markerOptions.position(latLng);
////        markerOptions.title("Current Position");
////        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
////        currLocationMarker = mGoogleMap.addMarker(markerOptions);
////
//        Toast.makeText(this,"Location Changed",Toast.LENGTH_SHORT).show();
//
////        //zoom to current position:
//        if(zoomOneTime){//Just zoom 1 time
//            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));
//            zoomOneTime =false;
//        }
//
//
//        //If you only need one location, unregister the listener
//        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//    }


    /**
     * Timer send request get warning point and show to the map
     */
    public void startShowWarningTimer() {
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
        //set a new Timer
        timerShowWarning = new Timer();

        //initialize the TimerTask's job
        showWarningPoint();

        //schedule the timer, after the first 5000ms the TimerTask will run every 30000ms
        timerShowWarning.schedule(timerTaskShowWarning, 5000, 30000); //
    }

    /**
     * Timer Other Vehicles
     */
    public void startTimerVehicles() {
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
        //set a new Timer
        timerShowOtherVehicles = new Timer();

        //initialize the TimerTask's job
        showOrtherVehicles();

        //schedule the timer, after the first 5000ms the TimerTask will run every 30000ms
        timerShowOtherVehicles.schedule(timerTaskShowOtherVehicles, 5000, 30000);
    }

    /**
     * Stop the ShowWarning timer, if it's not already null
     *
     */
    public void stopShowWarningTimerTask() {
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
        if (timerShowWarning != null) {
            timerShowWarning.cancel();
            timerShowWarning = null;
        }
    }

    /**
     * Stop the Show other vehicle timer, if it's not already null
     *
     */
    public void stopShowOtherVehicleTimerTask() {
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
        if (timerShowOtherVehicles != null) {
            timerShowOtherVehicles.cancel();
            timerShowOtherVehicles = null;
        }
    }


    /**
     * show list warning when driver click button warning
     */
    public void showWarning() {
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
        // custom dialog
        final Dialog dialogShowWarning = new Dialog(this);
        dialogShowWarning.setContentView(R.layout.dialog_add_warning);
        dialogShowWarning.setTitle("Thêm cảnh báo");
        listView = (ListView) dialogShowWarning.findViewById(R.id.listview_dialog_warning);
        // set the custom dialog components - text, image and button
        ServiceHandler serviceHandler = new ServiceHandler();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("", "");

        serviceHandler.makeServiceCall(showWarningUrl, Request.Method.GET, params, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                try {
                    Boolean error = gson.fromJson(result.getString("error"), Boolean.class);
                    if (!error) {
                        arrWarning = new ArrayList<WarningTypes>();
                        Type listType = new TypeToken<List<WarningTypes>>() {
                        }.getType();
                        arrWarning = gson.fromJson(result.getString("content"), listType);


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
                    } else {

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                Log.d("Resultsxxx", error.getMessage());
            }
        });


        dialogShowWarning.show();
    }


    /**
     * add warning(locationLat,Long,Type,Description) to the database
     */
    public void addWarning(){
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());

        final Dialog dialogConfirmWarning = new Dialog(this);
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


                if (trackgps.mGoogleApiClient.isConnected()) {
                    //Request warning
                    LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
                    if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                        HashMap<String, String> paramsCreateWarning = new HashMap<String, String>();
                        paramsCreateWarning.put("userId", String.valueOf(ApplicationController.getCurrentUser().getId()));
                        paramsCreateWarning.put("warningTypeId", String.valueOf(warningTypes.getWarningTypeId()));
                        paramsCreateWarning.put("locationLat", String.valueOf(mLocation.getLatitude()));
                        paramsCreateWarning.put("locationLong", String.valueOf(mLocation.getLongitude()));
                        paramsCreateWarning.put("description", String.valueOf(edtDescription.getText()));
                        ServiceHandler serviceHandler = new ServiceHandler();
                        serviceHandler.makeServiceCall(addWarningUrl, Request.Method.POST,
                                paramsCreateWarning, new ServerCallback() {
                                    @Override
                                    public void onSuccess(JSONObject result) {
                                        Log.d("Result", result.toString());
                                        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                                        try {
                                            Boolean error = gson.fromJson(result.getString("error"), Boolean.class);
                                            if (!error) {

                                                Toast.makeText(MainActivity.this, "Thông báo " + warningTypes.getType() + " thành công", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(MainActivity.this, "Không thành công", Toast.LENGTH_SHORT).show();
                                            }


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(VolleyError error) {
                                        Log.d("Result", error.getMessage());
                                        Toast.makeText(MainActivity.this, "Không thành công", Toast.LENGTH_SHORT).show();
                                    }

                                });

                    } else {
                        Toast.makeText(MainActivity.this, "Chưa bật GPS", Toast.LENGTH_SHORT).show();
                    }
                } else Toast.makeText(MainActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
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
     * Show warning point on Map
     */
    public void showWarningPoint(){
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());

        timerTaskShowWarning = new TimerTask() {
            public void run() {
                if (trackgps.mGoogleApiClient.isConnected()) {
                    trackgps.getCurrentLocation(new LocationCallback() {
                        @Override
                        public void onSuccess() {
                            Log.d("STttttt", "main1");
                            Log.d("Location_lat_long", String.valueOf(mLocation.getLatitude()));
                            Log.d("Location_lat_long", String.valueOf(mLocation.getLongitude()));
                            Log.d("start_warning_point", "start_warning_point");
                            ServiceHandler serviceHandler = new ServiceHandler();
                            HashMap<String, String> params = new HashMap<String, String>();
                            params.put("locationLat", String.valueOf(mLocation.getLatitude()));
                            params.put("locationLong", String.valueOf(mLocation.getLongitude()));
                            params.put("distance", "5");

                            serviceHandler.makeServiceCall(ServiceHandler.DOMAIN + "/api/v1/warning/search/{locationLat}/{locationLong}/{distance}", Request.Method.GET, params, new ServerCallback() {
                                @Override
                                public void onSuccess(JSONObject result) {

//                        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                                    try {
                                        Boolean error = gson.fromJson(result.getString("error"), Boolean.class);
                                        if (!error) {
                                            Log.d("not_error", "not_error");
                                            //Warning showPoint = new Warning();
                                            arrWarningPoint = new ArrayList<Warning>();
                                            Type listType = new TypeToken<List<Warning>>() {
                                            }.getType();
                                            arrWarningPoint = gson.fromJson(result.getString("content"), listType);
                                            for (int i = 0; i < arrWarningPoint.size(); i++) {
                                                Log.d("long_lat_location", arrWarningPoint.get(i).getLocationLat().toString());
                                                Log.d("long_lat_location", arrWarningPoint.get(i).getLocationLong().toString());
                                                LatLng warningPosition = new LatLng(arrWarningPoint.get(i).getLocationLat(), arrWarningPoint.get(i).getLocationLong());
                                                BitmapDrawable bitmapdraw;
                                                Bitmap b;


                                                if (arrWarningPoint.get(i).getWarningTypeId() == ID_KETXE) {
                                                    bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.ketxemarker);
                                                    b = bitmapdraw.getBitmap();
                                                } else if (arrWarningPoint.get(i).getWarningTypeId() == ID_PIKACHU) {
                                                    bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.pikachumarker);
                                                    b = bitmapdraw.getBitmap();
                                                } else if (arrWarningPoint.get(i).getWarningTypeId() == ID_HONGDUONG) {
                                                    bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.hongduongmarker);
                                                    b = bitmapdraw.getBitmap();
                                                } else {
                                                    bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.hongduongmarker);
                                                    b = bitmapdraw.getBitmap();
                                                }
                                                Bitmap warningMarker = Bitmap.createScaledBitmap(b, 100, 130, false);
                                                Marker warning = mGoogleMap.addMarker(new MarkerOptions()
                                                        .position(warningPosition)
                                                        .icon(BitmapDescriptorFactory.fromBitmap(warningMarker))
                                                        .title(arrWarningPoint.get(i).getDescription())
                                                        .visible(true)
                                                );

                                            }

                                        } else {
                                            Log.d("loixxxxxxx", "loixxx");
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(VolleyError error) {
                                    Log.d("Resultsxxx", error.getMessage());
                                }
                            });
                        }

                        @Override
                        public void onError() {
//                            Toast.makeText(MainActivity.this, "Can't get Location", Toast.LENGTH_SHORT).show();
                            Log.d("error_location","can't get location");
                        }
                    });

                }


            }
        };

    }

    /**
     * Function to call API start journey
     * @param scheduleId schedule ID
     * @param deviceId Token ID device
     */
    public void startJourney(final String scheduleId, String deviceId){
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
        HashMap<String, String> params = new HashMap<String,String>();
        Log.d("startJourney",scheduleId);
        Log.d("startJourney",deviceId);
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
                        ApplicationController.sharedPreferences.edit().putString(ApplicationController.SCHEDULE_SESSION,scheduleJson).commit();
                        LatLng locationDestination = new LatLng(Double.parseDouble(scheduleActive.getLocationLatEnd()),
                                Double.parseDouble(scheduleActive.getLocationLongEnd()));
                        makeMaker(locationDestination,scheduleActive.getEndPointAddress());
                        trackgps.getCurrentLocation(new LocationCallback() {
                            @Override
                            public void onSuccess() {
                                startTimerforSheculeSession();

                                fabCancel.setVisibility(View.VISIBLE);
                                fabComplete.setVisibility(View.VISIBLE);
                                addOnClickForButton();

//                                ctrollFabButtonSchedule(CONTROLL_ON);
                            }

                            @Override
                            public void onError() {

                            }
                        });

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
        scheduleLatest = null;
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());

        HashMap<String, String> params = new HashMap<String,String>();
        params.put("userId",userId);
        Log.d("AAAAAAAA",userId);
        ServiceHandler.makeServiceCall(ServiceHandler.DOMAIN + "/api/v1/schedule/incoming/user/{userId}", Request.Method.GET, params, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                Log.d("Result_volley",result.toString());
                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                try {
                    Boolean error = gson.fromJson(result.getString("error"), Boolean.class);
                    if (!error) {
                        scheduleJson = result.getString("content");
                        Log.d("Latest Schedule",scheduleJson);
                        scheduleLatest = gson.fromJson(scheduleJson, Schedule.class);//Gan schedule gan nhat
                        showDialogStartJourney();
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
    public void showDialogStartJourney(){
        scheduleActive = null;
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
        if(scheduleLatest != null ){
            int statusSchedule = scheduleLatest.getScheduleStatusTypeId();
            if(statusSchedule == 1){
                dialogRequestStartSchedule = new AlertDialog.Builder(this).create();
                dialogRequestStartSchedule.setTitle("Bắt đầu hành trình?");
                dialogRequestStartSchedule.setMessage("Địa chỉ: " + scheduleLatest.getEndPointAddress()
                + "\nThời gian: " + scheduleLatest.getEndPointAddress());
                dialogRequestStartSchedule.setButton(Dialog.BUTTON_POSITIVE,"Đồng ý",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scheduleActive = scheduleLatest;//set schedule active = scheduleLatest
                        Log.d("showDialogStartJourney", String.valueOf(scheduleActive.getScheduleId()));
                        Log.d("showDialogStartJourney", String.valueOf(scheduleActive.getEndPointAddress()));
                        startJourney(String.valueOf(scheduleActive.getScheduleId()),ApplicationController.sharedPreferences.getString(DEVICE_TOKEN,null));
                    }
                });

                dialogRequestStartSchedule.setButton(Dialog.BUTTON_NEGATIVE,"Không, cảm ơn!",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });
                dialogRequestStartSchedule.setIcon(android.R.drawable.ic_dialog_alert);
                dialogRequestStartSchedule.show();

//                new AlertDialog.Builder(MainActivity.this)
//                        .setTitle("You have journey go to "+ scheduleLatest.getEndPointAddress() + " with "
//                                + scheduleLatest.getIntendStartTime() + " [id:"+scheduleLatest.getScheduleId()+ "]")
//                        .setMessage("Do you want start journey now?")
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                scheduleActive = scheduleLatest;//set schedule active = scheduleLatest
//                                Log.d("showDialogStartJourney", String.valueOf(scheduleActive.getScheduleId()));
//                                Log.d("showDialogStartJourney", String.valueOf(scheduleActive.getEndPointAddress()));
//                                startJourney(String.valueOf(scheduleActive.getScheduleId()),ApplicationController.sharedPreferences.getString(DEVICE_TOKEN,null));
//                            }
//                        })
//                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // do nothing
//                            }
//                        })
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .show();
            }
            else if(statusSchedule == 3){
                scheduleActive = scheduleLatest;
                startTimerforSheculeSession();
                fabCancel.setVisibility(View.VISIBLE);
                fabComplete.setVisibility(View.VISIBLE);
                addOnClickForButton();
                Log.d("log_gps","active");
//                Toast.makeText(MainActivity.this,"Schedule active id: "+ scheduleActive.getScheduleId(),Toast.LENGTH_SHORT).show();
            }

        }
        else {
            dialogRequestStartSchedule = new AlertDialog.Builder(this).create();
            dialogRequestStartSchedule.setTitle("Quẩy thôi!");
            dialogRequestStartSchedule.setMessage("Bạn không có bất kỳ hành trình nào");
            dialogRequestStartSchedule.show();
            fabCancel.setVisibility(View.INVISIBLE);
            fabComplete.setVisibility(View.INVISIBLE);
//            new AlertDialog.Builder(MainActivity.this)
//                    .setTitle("Have fun tonight")
//                    .setMessage("You don't have any schedule!")
//                    .show();

        }

    }

    /**
     * Register / Unregister the listener location
     * @param value
     */
//    private void controllonLocationChanged(int value){changed
//        if(value == CONTROLL_OFF)// unregister the listener
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//        if(value == CONTROLL_ON) {//register the listener to listen location change
//            checkLocationPermission();
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//        }
//    }

    /**
     * Invisible/Visible fab button Controll schedule
     * @param value on/off
     */
//    private void ctrollFabButtonSchedule(int value){
//        if(value == CONTROLL_ON){
////            controllFabSchedule = true;
//            fabControllSchedule.setVisibility(View.VISIBLE);
//            if(!controllFabSchedule)
//                fabControllSchedule.setImageDrawable(getResources().getDrawable(R.drawable.cancel));
//            else fabControllSchedule.setImageDrawable(getResources().getDrawable(R.drawable.completed));
//            fabControllSchedule.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(controllFabSchedule) {//Completed Journey
//                        controllFabSchedule = false;
//                        completedJourney(String.valueOf(scheduleLatest.getScheduleId()), ApplicationController.sharedPreferences.getString(DEVICE_TOKEN, null));
//                        fabControllSchedule.setImageDrawable(getResources().getDrawable(R.drawable.cancel));
//
//                    } else {//Cancel journey
//                        controllFabSchedule = true;
//                        cancelJourney(String.valueOf(scheduleLatest.getScheduleId()), ApplicationController.sharedPreferences.getString(DEVICE_TOKEN, null));
//                        fabControllSchedule.setImageDrawable(getResources().getDrawable(R.drawable.completed));
//
//                    }
//                    scheduleActive = null;//don't active any schedule
//                    fabControllSchedule.setVisibility(View.INVISIBLE);
//
//                }
//            });
//        }
//
//    }
    /**
     * Function to call API completed journey
     */
    private void completedJourney(final String scheduleId, String deviceId){
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
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
//                        mGoogleMap.clear();
                        removePolyline();//Remove direction in google map
                        if (timerSessionSchedule != null) {//remove timer
                            timerSessionSchedule.cancel();
                            timerSessionSchedule = null;
                        }
                        controllDraw = true;
//                        controllonLocationChanged(CONTROLL_OFF);
                        scheduleActive = null;
                        fabCancel.setVisibility(View.INVISIBLE);
                        fabComplete.setVisibility(View.INVISIBLE);
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
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
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
//                        mGoogleMap.clear();
                        removePolyline();//Remove direction in google map
                        if (timerSessionSchedule != null) {//remove timer
                            timerSessionSchedule.cancel();
                            timerSessionSchedule = null;
                        }
                        controllDraw = true;
                        scheduleActive = null;
                        trackgps.controllonLocationChanged(CONTROLL_OFF);
                        fabCancel.setVisibility(View.INVISIBLE);
                        fabComplete.setVisibility(View.INVISIBLE);
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

    /**
     * Remove road with source Location + dest Location in google map
     */
    private void removePolyline() {
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
        if (polyline != null)
            polyline.remove();
        polyline = null;
    }

//    /**
//     * get Current Location
//     * @return location
//     */
//    private Location getCurrentLocation(){
//        Location mLastLocation = null;
//        checkLocationPermission();
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                mGoogleApiClient);
////        Log.d("CurrentLocation",mLastLocation.getLatitude() + " | " + mLastLocation.getLongitude());
//        return mLastLocation;
//    }

    /**
     * get URL to call API distance
     * @param origin source location
     * @param dest destination location
     * @return string url
     */
    private String getURLDistance(Location origin, Location dest){
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
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


    /**
     * send Location data to server
     * @param origin previous location 2s
     */
//    private void sendLocationDataToServer(final Location origin){
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //Do something after 2s
//                Location currentLocation = getCurrentLocation();
//                Double speed = getSpeed(origin,currentLocation);
//                HashMap<String,String> params = new HashMap<String, String>();
//                params.put("scheduleId", String.valueOf(scheduleActive.getScheduleId()));
//                params.put("locationLat", String.valueOf(currentLocation.getLatitude()));
//                params.put("locationLong", String.valueOf(currentLocation.getLongitude()));
//                params.put("speed", String.valueOf(speed));
//                params.put("deviceId", ApplicationController.sharedPreferences.getString(DEVICE_TOKEN, null));
//                ServiceHandler.makeServiceCall(ServiceHandler.DOMAIN + "/api/v1/scheduleActive/insert",
//                        Request.Method.POST, params, new ServerCallback() {
//                            @Override
//                            public void onSuccess(JSONObject result) {
//                                Toast.makeText(MainActivity.this,"Insert Schedule Active",Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onError(VolleyError error) {
//                                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
//                            }
//                        });
//            }
//        }, 2000);
//
//    }

    /**
     * get Speed with 2 location
     * @param origin previous location
     * @param dest current location
     * @return speed(double)
     */
    private Double getSpeed(Location origin, Location dest){
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
        final Double[] speed = {0.0};
        if(previousLocation != null){
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
        }
        return speed[0];
    }

    /**
     * Show Other Vehicles Infomation
     */

    private void showOrtherVehicles() {
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
        timerTaskShowOtherVehicles = new TimerTask() {
            @Override
            public void run() {

                if (trackgps.mGoogleApiClient.isConnected())
                {
                    Log.d("start_show_other_vehicles", "start_show_other_vehicles");
                    trackgps.getCurrentLocation(new LocationCallback() {
                        @Override
                        public void onSuccess() {
                            HashMap<String,String> params = new HashMap<String, String>();

                            params.put("locationLat", String.valueOf(mLocation.getLatitude()));
                            params.put("locationLong", String.valueOf(mLocation.getLongitude()));
                            params.put("distance", "1000");

                            ServiceHandler.makeServiceCall(ServiceHandler.DOMAIN + "/api/v1/vehicle/nearby/{locationLat}/{locationLong}/{distance}", Request.Method.GET, params, new ServerCallback() {
                                @Override
                                public void onSuccess(JSONObject result) {
                                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                                    try {
                                        Boolean error = gson.fromJson(result.getString("error"), Boolean.class);
                                        if (!error)
                                        {

                                            listOrtherVehicles = new ArrayList<OtherVehiclesInformation>();
                                            Type listType = new TypeToken<List<OtherVehiclesInformation>>() {}.getType();
                                            listOrtherVehicles = gson.fromJson(result.getString("content"), listType );
                                            Log.d("array",String.valueOf(listOrtherVehicles.size()));
                                            for (String marker : vehicleMarkerMap.keySet()) {
                                                vehicleMarkerMap.get(marker).remove();
                                            }
                                            vehicleMarkerInfo.clear();
                                            vehicleMarkerMap.clear();
                                            Log.d("start_show_other_vehicles", "vehicleMarkerMap.clear()");
                                            for (final OtherVehiclesInformation object : listOrtherVehicles) {
//                            makeMaker(new LatLng(Double.parseDouble(object.getLocationLat()),
//                                    Double.parseDouble(object.getLocationLong())),"Driver name: " + object.getDriverName()+ "\n" + "Driver phone: " + object.getDriverPhone());
                                                LatLng otherVehiclePosition = new LatLng(Double.parseDouble(object.getLocationLat()), Double.parseDouble(object.getLocationLong()));

                                                BitmapDrawable bitmapDrawable;
                                                Bitmap bitmap;
                                                bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.vehicles);
                                                bitmap = bitmapDrawable.getBitmap();
                                                Bitmap vehicleMarker = Bitmap.createScaledBitmap(bitmap, 60, 90, false);

                                                Marker show = mGoogleMap.addMarker(new MarkerOptions()
                                                        .position(otherVehiclePosition)
                                                        .title(String.valueOf(object.getScheduleId()))
//                                                        .title(object.getDriverName())
//                                                        .snippet(object.getDriverPhone())
                                                        .icon(BitmapDescriptorFactory.fromBitmap(vehicleMarker))
                                                        .visible(true));
                                                vehicleMarkerMap.put(String.valueOf(object.getScheduleId()), show);
                                                vehicleMarkerInfo.put(String.valueOf(object.getScheduleId()), object);
                                            }
                                            Log.d("start_show_other_vehicles", "Added");
                                            mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                                @Override
                                                public boolean onMarkerClick(final Marker marker) {
                                                    selectedVehicleMarker = vehicleMarkerInfo.get(marker.getTitle());
                                                    Log.d("selectedVehicleMarker", String.valueOf(marker.getId()));
                                                    Log.d("selectedVehicleMarker", String.valueOf(selectedVehicleMarker));
                                                    if (selectedVehicleMarker != null) {
                                                        txtDriverName.setText("Driver Name: " + selectedVehicleMarker.getDriverName());
                                                        txtDriverPhone.setText("Driver Phone: " + selectedVehicleMarker.getDriverPhone());
                                                        Log.d("selectedVehicleMarker", "Not Null");
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                if (!MainActivity.this.isFinishing()) {
                                                                    dialogCallOtherVehicles.show();
                                                                }
                                                            }
                                                        });
//                                                        if(!MainActivity.this.isFinishing())
//                                                        {
//                                                            Log.d("selectedVehicleMarker", "not finished");
//                                                            try {
//                                                                dialogCallOtherVehicles.show();
//                                                            } catch (WindowManager.BadTokenException e) {
//                                                                Log.d("MainActiviy", e.getMessage());
//                                                            }
//
//                                                        }


                                                        return true;
                                                    }
                                                    return false;
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
                                public void onError(VolleyError error) {

                                }
                            });
                        }

                        @Override
                        public void onError() {
//                            Toast.makeText(MainActivity.this,"Can't get location",Toast.LENGTH_SHORT).show();
                            Log.d("error_location","can't get locaiton");
                        }
                    });


                }

            }
        };

    }

    /**
     * Setup show vehicle info dialog and call vehicles.
     */
    private void setupVehicleDialog() {
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
        dialogCallOtherVehicles = new Dialog(MainActivity.this);
        dialogCallOtherVehicles.setContentView(R.layout.dialog_confirm_call_other_vehicles);
        dialogCallOtherVehicles.setTitle("Call Other Vehicles");

        btnConfirmCallOtherVehicles = (Button) dialogCallOtherVehicles.findViewById(R.id.btnConfirmCallOtherVehicles);
        btnCancelCallOthervehicles = (Button) dialogCallOtherVehicles.findViewById(R.id.btnCancelCallOthervehicles);
        txtDriverName = (TextView) dialogCallOtherVehicles.findViewById(R.id.txtDriverName);
        txtDriverPhone = (TextView) dialogCallOtherVehicles.findViewById(R.id.txtDriverPhone);

        btnConfirmCallOtherVehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phoneVehicles = selectedVehicleMarker.getDriverPhone();
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri uri = Uri.parse("tel:" + phoneVehicles);
                intent.setData(uri);
                if (checkPermissionCallPhone())
                    startActivity(intent);
                dialogCallOtherVehicles.dismiss();
            }
        });

        btnCancelCallOthervehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCallOtherVehicles.dismiss();
            }
        });
    }

    @Override
    protected void onPause() {
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
        super.onPause();
        if (dialogRequestStartSchedule != null) {
            dialogRequestStartSchedule.dismiss();
        }
        if(dialogConfirm != null){
            dialogConfirm.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (checkLocationPermission() && isLocationEnabled() && isNetworkAvailable())
            getScheduleLatest(String.valueOf(ApplicationController.getCurrentUser().getId()));//Lấy shedule gần nhất của user dựa theo userid va show dialog
//        if (checkLocationPermission() && scheduleActive == null)
//            getScheduleLatest(String.valueOf(ApplicationController.getCurrentUser().getId()));//Lấy shedule gần nhất của user dựa theo userid va show dialog


//        Schedule scheduleSession = ApplicationController.getCurrentSchudle();
//        if (checkLocationPermission())
//            if(scheduleSession == null)
//                getScheduleLatest(String.valueOf(ApplicationController.getCurrentUser().getId()));//Lấy shedule gần nhất của user dựa theo userid va show dialog
//            else {
//                scheduleActive = scheduleSession;
//                Toast.makeText(MainActivity.this, "You are starting schedule id:" + scheduleActive.getScheduleId(), Toast.LENGTH_SHORT).show();
//                startTimerforSheculeSession();
//                fabCancel.setVisibility(View.VISIBLE);
//                fabComplete.setVisibility(View.VISIBLE);
//                addOnClickForButton();
//            }
//        if(scheduleActive == null)
////            showDialogStartJourney();
//            getScheduleLatest(String.valueOf(ApplicationController.getCurrentUser().getId()));//Lấy shedule gần nhất của user dựa theo userid va show dialog

    }
    protected void onStop(){
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
        super.onStop();
    }


    /**
     * Add event onClick for button cancel / complete journey
     */
    private void addOnClickForButton(){
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
        fabCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogConfirm = new AlertDialog.Builder(MainActivity.this).create();
                dialogConfirm.setTitle("Cancel Journey!");
                dialogConfirm.setMessage("Do you want cancel journey now?");
                dialogConfirm.setButton(Dialog.BUTTON_POSITIVE,"Yes",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelJourney(String.valueOf(scheduleActive.getScheduleId()), ApplicationController.sharedPreferences.getString(DEVICE_TOKEN, null));
                        removeonClickForButton();
                    }
                });

                dialogConfirm.setButton(Dialog.BUTTON_NEGATIVE,"NO,Thanks!",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });
                dialogConfirm.setIcon(android.R.drawable.ic_dialog_alert);
                dialogConfirm.show();


            }
        });
        fabComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogConfirm = new AlertDialog.Builder(MainActivity.this).create();
                dialogConfirm.setTitle("Complete Journey!");
                dialogConfirm.setMessage("Please, Confirm complete journey.");
                dialogConfirm.setButton(Dialog.BUTTON_POSITIVE,"Yes",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        completedJourney(String.valueOf(scheduleActive.getScheduleId()), ApplicationController.sharedPreferences.getString(DEVICE_TOKEN, null));
                        removeonClickForButton();
                    }
                });

                dialogConfirm.setButton(Dialog.BUTTON_NEGATIVE,"NO,Thanks!",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });
                dialogConfirm.setIcon(android.R.drawable.ic_dialog_alert);
                dialogConfirm.show();
            }
        });
    }

    /**
     * Remove onClick for button cancel / complete journey
     */
    private void removeonClickForButton(){
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
        fabCancel.setOnClickListener(null);
        fabComplete.setOnClickListener(null);
        ApplicationController.sharedPreferences.edit().remove(ApplicationController.SCHEDULE_SESSION).commit();

    }


    private void timerSaveScheduleSession(){
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
        timerTaskSessionSchedule = new TimerTask() {
            @Override
            public void run() {
                if (trackgps.mGoogleApiClient.isConnected()) {
                    trackgps.getCurrentLocation(new LocationCallback() {
                        @Override
                        public void onSuccess() {
                            if(controllDraw && scheduleActive != null)
                                drawroadBetween2Location(new LatLng(mLocation.getLatitude(),
                                                mLocation.getLongitude()),
                                        new LatLng(Double.parseDouble(scheduleActive.getLocationLatEnd()),
                                                Double.parseDouble(scheduleActive.getLocationLongEnd())));
                            if (timerSessionSchedule != null) {//remove timer
                                timerSessionSchedule.cancel();
                                timerSessionSchedule = null;
                            }
                        }

                        @Override
                        public void onError() {

                        }
                    });
                }
            }
        };
    }

    private void startTimerforSheculeSession(){
        Log.d("MainActivity", new Object(){}.getClass().getEnclosingMethod().getName());
        //set a new Timer
        timerSessionSchedule = new Timer();

        //initialize the TimerTask's job
        timerSaveScheduleSession();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timerSessionSchedule.schedule(timerTaskSessionSchedule, 5000, 30000);
    }

    /**
     * Check Location service is enable/disable
     * @return true if provider GPS or NETWORK is enable
     */
    private boolean isLocationEnabled(){
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            return true;
        else {
            Toast.makeText(MainActivity.this,"Bật Vị trí, Please!!!",Toast.LENGTH_LONG).show();
            return false;
        }
    }

    /**
     * Check internet is enable
     * @return true - enable
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> arrayList, int shortestRouteIndex) {
        try {
            if(polyline != null) polyline.remove();
            PolylineOptions polyoptions = new PolylineOptions();
            polyoptions.color(Color.BLUE);
            polyoptions.width(10);
            for (int i = 0; i <arrayList.size(); i++) {

                //In case of more than 5 alternative routes
                int colorIndex = i % COLORS.length;

                PolylineOptions polyOptions = new PolylineOptions();
                polyOptions.color(getResources().getColor(COLORS[colorIndex]));
                polyOptions.width(10 + i * 3);
                polyOptions.addAll(arrayList.get(i).getPoints());
                polyline = mGoogleMap.addPolyline(polyOptions);

                Toast.makeText(getApplicationContext(),"Route "+ (i+1) +": distance - "+ arrayList.get(i).getDistanceValue()+": duration - "+ arrayList.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){

        }
    }

    @Override
    public void onRoutingCancelled() {

    }
}
