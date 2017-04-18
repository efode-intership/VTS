package vn.efode.vts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;

import vn.efode.vts.model.Schedule;

public class ScheduleDetailsActivity extends AppCompatActivity {

    TextView txtScheduleId, txtDriverId, txtVehicleId, txtStartPointAddress, txtEndPointAddress, txtIntendStartTime, txtIntendEndTime, txtScheduleStatusTypeId, txtLocationLatStart, txtLocationLongStart, txtLocationLatEnd, txtLocationLongEnd, txtDeviceId;
    Intent intent;
    ArrayList<Schedule> schedulesDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtScheduleId = (TextView) findViewById(R.id.txtScheduleId);
        txtDriverId = (TextView) findViewById(R.id.txtDriverId);
        txtVehicleId = (TextView) findViewById(R.id.txtVehicleId);
        txtStartPointAddress = (TextView) findViewById(R.id.txtStartPointAddress);
        txtEndPointAddress = (TextView) findViewById(R.id.txtEndPointAddress);
        txtIntendStartTime = (TextView) findViewById(R.id.txtIntendStartTime);
        txtIntendEndTime = (TextView) findViewById(R.id.txtIntendEndTime);
        txtScheduleStatusTypeId = (TextView) findViewById(R.id.txtScheduleStatusTypeId);
        txtLocationLatStart = (TextView) findViewById(R.id.txtLocationLatStart);
        txtLocationLongStart = (TextView) findViewById(R.id.txtLocationLongStart);
        txtLocationLatEnd = (TextView) findViewById(R.id.txtLocationLatEnd);
        txtLocationLongEnd = (TextView) findViewById(R.id.txtLocationLongEnd);
        txtDeviceId = (TextView) findViewById(R.id.txtDeviceId);

        intent = getIntent();
        Bundle bundle = intent.getBundleExtra("ScheduleDetails");
        Schedule schedule = new Schedule();
        schedule = (Schedule) bundle.get("ListSchedule");

        txtScheduleId.setText("Schedule ID: " + schedule.getScheduleId());
        txtIntendStartTime.setText("Intend Start Time: " + schedule.getIntendStartTime());
        txtIntendEndTime.setText("Intend End Time: " + schedule.getIntendEndTime());
        txtDriverId.setText("Driver Id: " + schedule.getDriverId());
        txtVehicleId.setText("Vehicle Id: " + schedule.getVehicleId());
        txtStartPointAddress.setText("Start Point Address: " + schedule.getStartPointAddress());
        txtEndPointAddress.setText("End Point Address: " + schedule.getEndPointAddress());
        txtScheduleStatusTypeId.setText("Schedule Status Type Id: " + schedule.getScheduleStatusTypeId());
        txtLocationLatStart.setText("Location Lat Start: " + schedule.getLocationLatStart());
        txtLocationLongStart.setText("Location Long Start: " + schedule.getLocationLongStart());
        txtLocationLatEnd.setText("Location Lat End: " + schedule.getLocationLatEnd());
        txtLocationLongEnd.setText("Location Long End: " + schedule.getLocationLongEnd());
        txtDeviceId.setText("Device Id: " + schedule.getDeviceId());

    }
}
