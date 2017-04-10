package vn.efode.vts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

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

import vn.efode.vts.model.Schedule;
import vn.efode.vts.service.ServiceHandler;
import vn.efode.vts.utils.ServerCallback;


public class ScheduleHistoryActivity extends AppCompatActivity {

    ListView lvSchedule;
    ArrayList<Schedule> listSchedule;
    HashMap<String,String> schedulelist = new HashMap<String, String>();
    ArrayAdapter<Schedule> adapterSchedule;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addControls();
        ServiceHandler serviceHandler = new ServiceHandler();
        schedulelist.put("userId", "6");
        serviceHandler.makeServiceCall("http://192.168.1.16/web_app/public/api/v1/schedule/user/{userId}", Request.Method.GET, schedulelist, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                Log.d("Result_volley",result.toString());
                Schedule schedule = new Schedule();
                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                try {
                    Boolean error = gson.fromJson(result.getString("error"), Boolean.class);
                    if (!error)
                    {
                        ArrayList<Schedule> scheduleList = new ArrayList<Schedule>();
//                        schedule = gson.fromJson(result.getString("content"), Schedule.class);
                        Type listType = new TypeToken<List<Schedule>>() {}.getType();
                        scheduleList = gson.fromJson(result.getString("content"), listType );
                        Log.d("array",String.valueOf(scheduleList.size()));
                    }
                    else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
                Log.d("Result",error.getMessage());

            }
        });
    }

    private void addControls() {
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSchedule = tabHost.newTabSpec("tabSchedule");
        tabSchedule.setIndicator("Schedule");
        tabSchedule.setContent(R.id.tabSchedule);
        tabHost.addTab(tabSchedule);

        TabHost.TabSpec tabHistory = tabHost.newTabSpec("tabHistory");
        tabHistory.setIndicator("History");
        tabHistory.setContent(R.id.tabHistory);
        tabHost.addTab(tabHistory);

        lvSchedule = (ListView) findViewById(R.id.lvSchedule);
        listSchedule = new ArrayList<>();
        adapterSchedule = new ArrayAdapter<Schedule>(
                ScheduleHistoryActivity.this,
                android.R.layout.simple_list_item_1,
                (List<Schedule>) schedulelist);
        lvSchedule.setAdapter(adapterSchedule);
    }

}
