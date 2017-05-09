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

import static vn.efode.vts.MainActivity.TAG_ERROR;
import static vn.efode.vts.MainActivity.scheduleActive;
import static vn.efode.vts.ScheduleHistoryActivity.notStartedList;
import static vn.efode.vts.service.DeviceTokenService.DEVICE_TOKEN;

public class ScheduleDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView txtStartPointAddress;
    private TextView txtEndPointAddress;
    private TextView txtIntendStartTime;
    private TextView txtIntendEndTime;
    private TextView txtScheduleStatusTypeId;
    private TextView txtDescription;
    public Intent intentSchedule;
    private Button btnStartSchedule;
    private Schedule schedule;
    private ImageView imgScheduleStatus;
    private int position;
    private int POSITION_FIRST = 0;
    public static final String CUSTOM_INTENT = "jason.wei.custom.intent.action.TEST";


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
        position = (int) bundleSchedule.get("Position");

        setContentSchedule();

    }

    /**
     * set content details about schedule
     */

    private void setContentSchedule() {
        txtIntendStartTime.setText(schedule.getIntendStartTime());
        txtIntendEndTime.setText(schedule.getIntendEndTime());
        txtStartPointAddress.setText(schedule.getStartPointAddress());
        txtEndPointAddress.setText(schedule.getEndPointAddress());
        txtDescription.setText(schedule.getDescription());
        if(schedule.getScheduleStatusTypeId() == 1) {
            btnStartSchedule.setVisibility(View.VISIBLE);
            txtScheduleStatusTypeId.setText("Trạng thái: " + schedule.getScheduleStatusName() + "!!!");
            imgScheduleStatus.setImageResource(R.drawable.notstart);
        }
        else btnStartSchedule.setVisibility(View.INVISIBLE);

        if (schedule.getScheduleStatusTypeId() == 2)
        {
            txtScheduleStatusTypeId.setText("Trạng thái: " + schedule.getScheduleStatusName() + "!!!");
            imgScheduleStatus.setImageResource(R.drawable.complete);
        }
        else
        if (schedule.getScheduleStatusTypeId() == 3)
        {
            txtScheduleStatusTypeId.setText("Trạng thái: " + schedule.getScheduleStatusName() + "!!!");
            imgScheduleStatus.setImageResource(R.drawable.inprogress);
        }
        else
        if (schedule.getScheduleStatusTypeId() == 4)
        {
            txtScheduleStatusTypeId.setText("Trạng thái: " + schedule.getScheduleStatusName() + "!!!");
            imgScheduleStatus.setImageResource(R.drawable.cancelschedule);
        }
    }

    private void addEvents() {
        btnStartSchedule.setOnClickListener(this);
    }

    private void addControlls() {
        btnStartSchedule = (Button) findViewById(R.id.btn_start_schedule);
        txtStartPointAddress = (TextView) findViewById(R.id.textview_scheduledetails_startpointaddress);
        txtEndPointAddress = (TextView) findViewById(R.id.textview_scheduledetails_endpointaddress);
        txtIntendStartTime = (TextView) findViewById(R.id.textview_scheduledetails_intendstarttime);
        txtIntendEndTime = (TextView) findViewById(R.id.textview_scheduledetails_intendendtime);
        txtScheduleStatusTypeId = (TextView) findViewById(R.id.textview_scheduledetails_schedulestatustypeid);
        imgScheduleStatus = (ImageView) findViewById(R.id.image_scheduledetails_schedulestatus);
        txtDescription = (TextView)findViewById(R.id.textview_scheduledetails_description);
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
        if(position != POSITION_FIRST)
        {
            if(scheduleActive == null)
                new AlertDialog.Builder(ScheduleDetailsActivity.this)
                        .setTitle("Đây không phải là lịch trình gần nhất của bạn!!!")
                        .setMessage("Bạn vẫn muốn bắt đầu hành trình?")
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
                        .setTitle("Không thể bắt đầu hành trình!")
                        .setMessage("Bạn đang chạy lịch trình " + scheduleActive.getScheduleId() + "\nVui lòng kết thúc lịch trình đang chạy trước khi bắt đầu hành trình mới!!!")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
        }
        else
        {
            if(scheduleActive == null)
                new AlertDialog.Builder(ScheduleDetailsActivity.this)
                        .setTitle("Bạn muốn bắt đầu lịch trình này?")
//                        .setMessage("Scheduleid:" + schedule.getScheduleId() + "\nAddress: "+schedule.getEndPointAddress())
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
                        .setTitle("Không thể bắt đầu hành trình!")
                        .setMessage("Bạn đang chạy lịch trình " + scheduleActive.getScheduleId() + "\nVui lòng kết thúc lịch trình đang chạy trước khi bắt đầu hành trình mới!!!")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
        }

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
                        Schedule removedSchedule = new Schedule();
                        removedSchedule.setScheduleId(Integer.parseInt(scheduleId));
                        notStartedList.remove(removedSchedule);

                    }
                }catch (JSONException e) {
                    Log.e(TAG_ERROR,String.valueOf(e.getMessage()));
                }
            }

            @Override
            public void onError(VolleyError error) {
                Log.e(TAG_ERROR,String.valueOf(error.getMessage()));
            }
        });
    }



}
