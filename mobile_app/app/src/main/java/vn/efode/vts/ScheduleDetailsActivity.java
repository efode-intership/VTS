package vn.efode.vts;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import vn.efode.vts.application.ApplicationController;
import vn.efode.vts.model.Schedule;
import vn.efode.vts.utils.ServerCallback;
import vn.efode.vts.utils.ServiceHandler;

import static vn.efode.vts.MainActivity.scheduleActive;
import static vn.efode.vts.service.DeviceTokenService.DEVICE_TOKEN;

public class ScheduleDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    TextView txtScheduleId, txtDriverId, txtVehicleId, txtStartPointAddress, txtEndPointAddress, txtIntendStartTime, txtIntendEndTime, txtScheduleStatusTypeId;
    Intent intentSchedule;
    Button btnStartSchedule;
    Schedule schedule;
    ImageView imgScheduleStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addControlls();
        addEvents();

        intentSchedule = getIntent();
        Bundle bundleSchedule = intentSchedule.getBundleExtra("ScheduleDetails");
        schedule = new Schedule();
        schedule = (Schedule) bundleSchedule.get("ListSchedule");

        setContentSchedule();

    }

    /**
     * set content details about schedule
     */

    private void setContentSchedule() {
        txtScheduleId.setText("Schedule ID: " + schedule.getScheduleId());
        txtIntendStartTime.setText("Intend Start Time: " + schedule.getIntendStartTime());
        txtIntendEndTime.setText("Intend End Time: " + schedule.getIntendEndTime());
        txtDriverId.setText("Driver Id: " + schedule.getDriverId());
        txtVehicleId.setText("Vehicle Id: " + schedule.getVehicleId());
        txtStartPointAddress.setText("Start Point Address: " + schedule.getStartPointAddress());
        txtEndPointAddress.setText("End Point Address: " + schedule.getEndPointAddress());

        if(schedule.getScheduleStatusTypeId() == 1) {
            btnStartSchedule.setVisibility(View.VISIBLE);
            txtScheduleStatusTypeId.setText("Schedule Status: " + schedule.getScheduleStatusName() + "!!!");
            imgScheduleStatus.setImageResource(R.drawable.notstart);
        }
        else btnStartSchedule.setVisibility(View.INVISIBLE);

        if (schedule.getScheduleStatusTypeId() == 2)
        {
            txtScheduleStatusTypeId.setText("Schedule Status: " + schedule.getScheduleStatusName() + "!!!");
            imgScheduleStatus.setImageResource(R.drawable.complete);
        }
        else
        if (schedule.getScheduleStatusTypeId() == 3)
        {
            txtScheduleStatusTypeId.setText("Schedule Status: " + schedule.getScheduleStatusName() + "!!!");
            imgScheduleStatus.setImageResource(R.drawable.inprogress);
        }
        else
        if (schedule.getScheduleStatusTypeId() == 4)
        {
            txtScheduleStatusTypeId.setText("Schedule Status: " + schedule.getScheduleStatusName() + "!!!");
            imgScheduleStatus.setImageResource(R.drawable.cancelschedule);
        }
    }

    private void addEvents() {
        btnStartSchedule.setOnClickListener(this);
    }

    private void addControlls() {
        btnStartSchedule = (Button) findViewById(R.id.btn_start_schedule);
        txtScheduleId = (TextView) findViewById(R.id.txtScheduleId);
        txtDriverId = (TextView) findViewById(R.id.txtDriverId);
        txtVehicleId = (TextView) findViewById(R.id.txtVehicleId);
        txtStartPointAddress = (TextView) findViewById(R.id.txtStartPointAddress);
        txtEndPointAddress = (TextView) findViewById(R.id.txtEndPointAddress);
        txtIntendStartTime = (TextView) findViewById(R.id.txtIntendStartTime);
        txtIntendEndTime = (TextView) findViewById(R.id.txtIntendEndTime);
        txtScheduleStatusTypeId = (TextView) findViewById(R.id.txtScheduleStatusTypeId);
        imgScheduleStatus = (ImageView) findViewById(R.id.imgScheduleStatus);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_start_schedule:
                showDialogAccept();
                break;
        }
    }

    /**
     * Show dialog accept or not
     */
    private void showDialogAccept() {
        if(scheduleActive == null)
            new AlertDialog.Builder(ScheduleDetailsActivity.this)
                    .setTitle("Do you want start this schedule?")
                    .setMessage("Scheduleid:" + schedule.getScheduleId() + "\nAddress: "+schedule.getEndPointAddress())
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startJourneyAndswtichActivity(String.valueOf(schedule.getScheduleId()),  ApplicationController.sharedPreferences.getString(DEVICE_TOKEN, null));
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        else
            new AlertDialog.Builder(ScheduleDetailsActivity.this)
                .setTitle("You are in a schedule active!")
                .setMessage("Scheduleid:" + scheduleActive.getScheduleId() + "\nAddress: "+ scheduleActive.getEndPointAddress())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Start activity and put data to another Activity
     */
    private void swtichActivity(){
        Intent intent = new Intent(ScheduleDetailsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void startJourneyAndswtichActivity(final String scheduleId, String deviceId){
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
                        swtichActivity();

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
