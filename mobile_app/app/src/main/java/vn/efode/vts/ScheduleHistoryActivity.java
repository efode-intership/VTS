package vn.efode.vts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;

public class ScheduleHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addControls();
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
    }
}
