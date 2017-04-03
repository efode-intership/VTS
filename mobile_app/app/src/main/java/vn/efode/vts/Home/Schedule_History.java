package vn.efode.vts.Home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

import vn.efode.vts.R;

public class Schedule_History extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_history);

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
