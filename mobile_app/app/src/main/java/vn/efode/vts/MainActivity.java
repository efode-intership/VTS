package vn.efode.vts;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import vn.efode.vts.service.ReadTask;
import vn.efode.vts.service.TrackGPS;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    public static GoogleMap map = null;
    private TrackGPS gps;
    private static int REQUEST_LOCATION = 10;
    private static String API_KEY_DIRECTION = "AIzaSyAJCQ6Wf-aQbUbF5wLRMs4XtgCS-vph6IE";
    private static String API_KEY_MATRIX =  "AIzaSyCGXiVPlm9M72lupfolIXkxzSTPNIvRr8g";
    private ListView listView;
    private EditText edtDescription;
    private Button btnOk;
    private Button btnCancel;
    double longitude;
    double latitude;

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
                Snackbar.make(view, "Call", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fab_warning = (FloatingActionButton) findViewById(R.id.fab_warning);
        fab_warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWarning();
            }
        });

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
//            @Override
//            public void onError(VolleyError error){
//                Log.d("Result",error.getMessage());
//            }
//        });

        addControls();
        addEvents();
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
            Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_home) {

        } else if (id == R.id.nav_schedule) {
            Intent intent1 = new Intent(MainActivity.this, ScheduleHistoryActivity.class);
            startActivity(intent1);

        } else if (id == R.id.nav_signout) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        gps = new TrackGPS(MainActivity.this);

        if(!gps.canGetLocation()) {

            gps.showSettingsAlert();

        }
        longitude = gps.getLongitude();
        latitude = gps .getLatitude();

        try {
            LatLng currentMaker = new LatLng(latitude,longitude);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentMaker, 15));

            map.addMarker(new MarkerOptions().title("Current Location").position(currentMaker));

            map.addPolyline(new PolylineOptions()
                    .add( currentMaker, new LatLng(10.882323, 106.782625))
                    .width(5)
                    .color(Color.RED));
        } catch (Exception e){
            Log.d("MAPEXCEPTION", e.getMessage());
        }
        Toast.makeText(getApplicationContext(),"Longitude:"+Double.toString(longitude)+"\nLatitude:"+Double.toString(latitude),Toast.LENGTH_SHORT).show();


        //path of two point
        String url = getMapsApiDirectionsUrl(new LatLng(latitude,longitude),  new LatLng(10.882323, 106.782625));

        Log.d("onMapClick", url.toString());
        ReadTask downloadTask = new ReadTask();
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);

        //To calculate distance between points
//        float[] results = new float[1];
//        Location.distanceBetween(latitude, longitude,
//                10.882323, 106.782625,
//                results);
//        Log.d("onMapClick",String.valueOf(results));

    }

    /**
     * get Url to request the Google Directions API
     * @param origin start point location
     * @param dest destination point location
     * @return
     */
    private String getMapsApiDirectionsUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        //key
        String keyDirection = "key="+API_KEY_DIRECTION;

        // Building the parameters to the web service
//        String parameters = str_origin+"&"+str_dest+"&"+sensor;
        String parameters = str_origin+"&"+str_dest+"&"+keyDirection;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if(grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We can now safely use the API we requested access to
                Log.d("LOCATIONNETWORK","result");

            } else {
                // Permission was denied or request was cancelled
            }
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        gps.stopUsingGPS();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            switch (requestCode) {
                case 1:

            }
        }
    }

    public void showWarning(){
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_warning);
        dialog.setTitle("Thêm cảnh báo");

        // set the custom dialog components - text, image and button

        listView = (ListView) dialog.findViewById(R.id.listview_dialog_warning);

        edtDescription = (EditText) dialog.findViewById(R.id.edittext_dialog_description);
        //edtDescription.setLayoutParams(new LinearLayout.LayoutParams(200,50));
        btnOk = (Button) dialog.findViewById(R.id.button_dialog_ok);
        btnCancel = (Button) dialog.findViewById(R.id.button_dialog_cancel);

        btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

            }
        });


        dialog.show();
    }
}
