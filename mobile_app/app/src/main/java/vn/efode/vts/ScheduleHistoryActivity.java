package vn.efode.vts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
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

import vn.efode.vts.adapter.ScheduleAdapter;
import vn.efode.vts.application.ApplicationController;
import vn.efode.vts.model.Schedule;
import vn.efode.vts.utils.ServerCallback;
import vn.efode.vts.utils.ServiceHandler;

import static vn.efode.vts.service.TrackGPS.TAG_ERROR;


public class ScheduleHistoryActivity extends AppCompatActivity {

    private ListView lvSchedule, lvHistory;
    private ArrayList<Schedule> listSchedule;
    public static List<Schedule> notStartedList  = new ArrayList<Schedule>();; //List Schedule not start
    public static List<Schedule> historyList = new ArrayList<Schedule>(); //List Schedule complete or cancel
    private HashMap<String,String> schedulelist = new HashMap<String, String>();
    private ScheduleAdapter scheduleAdapter;
    private boolean countScheduleActive = true;
    private TextView txtNotSchedule;
    private int countScheduleHistory = 0;
    private TextView txtCountHistory;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvSchedule = (ListView) findViewById(R.id.listview_schedulehistory_schedule);
        lvHistory = (ListView) findViewById(R.id.listview_schedulehistory_history);
        txtNotSchedule = (TextView) findViewById(R.id.textview_schedulehistory_notschedule);
        txtCountHistory = (TextView) findViewById(R.id.textview_schedulehistory_counthistory);

        addControls();
        getDataFromServer();


    }



    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(this,MainActivity.class);
                this.startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * get data from server
     */
    private void getDataFromServer(){

        ServiceHandler serviceHandler = new ServiceHandler();
        schedulelist.put("userId", String.valueOf(ApplicationController.getCurrentUser().getId()));
        ServiceHandler.makeServiceCall(ServiceHandler.DOMAIN + "/api/v1/schedule/user/{userId}", Request.Method.GET, schedulelist, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                Log.d("Result_volley",result.toString());
                final Schedule schedule = new Schedule();
                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                try {
                    Boolean error = gson.fromJson(result.getString("error"), Boolean.class);
                    if (!error)
                    {

                        listSchedule = new ArrayList<Schedule>();
                        notStartedList = new ArrayList<Schedule>();
                        historyList = new ArrayList<Schedule>();
                        Type listType = new TypeToken<List<Schedule>>() {}.getType();
                        listSchedule = gson.fromJson(result.getString("content"), listType );
                        Log.d("array",String.valueOf(listSchedule.size()));
//                        List<Schedule> notStartedList = new ArrayList<Schedule>();
//                        List<Schedule> history = new ArrayList<Schedule>();
                        for (int i = 0; i < listSchedule.size(); i++)
                        {
                            if (listSchedule.get(i).getScheduleStatusTypeId() == 1)
                            {
                                notStartedList.add(listSchedule.get(i));
                                countScheduleActive = false;
                            }
                            scheduleAdapter = new ScheduleAdapter(
                                    ScheduleHistoryActivity.this,
                                    R.layout.schedule_list_layout,
                                    notStartedList);

                            lvSchedule.setAdapter(scheduleAdapter);


                            if (listSchedule.get(i).getScheduleStatusTypeId() == 2 || listSchedule.get(i).getScheduleStatusTypeId() == 4)
                            {
                                Log.d("Value_count",String.valueOf(countScheduleHistory) + " | " + String.valueOf(i));

                                historyList.add(listSchedule.get(i));
                                countScheduleHistory = historyList.size();
                            }
                            scheduleAdapter = new ScheduleAdapter(
                                    ScheduleHistoryActivity.this,
                                    R.layout.schedule_list_layout,
                                    historyList);
//                            scheduleAdapter.notifyDataSetChanged();
                            lvHistory.setAdapter(scheduleAdapter);

                        }
                        Log.d("Value_count",String.valueOf(countScheduleHistory));

                        if(countScheduleActive == true){
                            txtNotSchedule.setVisibility(View.VISIBLE);
                            //Toast.makeText(ScheduleHistoryActivity.this, "không có hành trình nào", Toast.LENGTH_LONG).show();
                            txtNotSchedule.setText("Không có lịch trình nào");

                        }
                        if(countScheduleHistory ==0){
                            txtCountHistory.setText("Bạn chưa có cuốc nào");
                        }else txtCountHistory.setText("Bạn đã chạy xong " + String.valueOf(countScheduleHistory) + " cuốc");

                        lvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Schedule history = historyList.get(i);
                                Intent intentHistory = new Intent(ScheduleHistoryActivity.this, ScheduleDetailsActivity.class);
                                Bundle bundleHistory = new Bundle();
                                bundleHistory.putSerializable("Position", i);
                                bundleHistory.putSerializable("ListSchedule", history);
                                intentHistory.putExtra("ScheduleDetails", bundleHistory);
                                startActivity(intentHistory);
                            }
                        });

                        lvSchedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Schedule schedule = notStartedList.get(i);
                                Intent intentSchedule = new Intent(ScheduleHistoryActivity.this, ScheduleDetailsActivity.class);
                                Bundle bundleSchedule = new Bundle();
                                bundleSchedule.putSerializable("Position", i);
                                bundleSchedule.putSerializable("ListSchedule", schedule);
                                intentSchedule.putExtra("ScheduleDetails", bundleSchedule);
                                startActivity(intentSchedule);
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
                Log.e(TAG_ERROR,String.valueOf(error.getMessage()));

            }
        });

    }


    /**
     * Set Tabhost
     */
    private void addControls() {
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSchedule = tabHost.newTabSpec("tabSchedule");
        tabSchedule.setIndicator("Lịch trình");
        //tabSchedule.setIndicator("Lịch trình", ScheduleHistoryActivity.this.getResources().getDrawable(R.drawable.calendar));
        tabSchedule.setContent(R.id.tabSchedule);
        tabHost.addTab(tabSchedule);

        TabHost.TabSpec tabHistory = tabHost.newTabSpec("tabHistory");
        tabHistory.setIndicator("Lịch sử");
        //tabHistory.setIndicator("Lịch sử", ScheduleHistoryActivity.this.getResources().getDrawable(R.drawable.trucking));
        tabHistory.setContent(R.id.tabHistory);
        tabHost.addTab(tabHistory);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataFromServer();
    }

}
