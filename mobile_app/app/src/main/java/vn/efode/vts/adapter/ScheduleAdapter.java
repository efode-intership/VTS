package vn.efode.vts.adapter;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import vn.efode.vts.R;
import vn.efode.vts.model.Schedule;

/**
 * Created by CongVu on 4/4/2017.
 */

public class ScheduleAdapter extends ArrayAdapter<Schedule>{

    Activity context; //Màn hình sử dụng layout này (giao diện này)
    int resource; // Layout cho từng dòng muốn hiển thị
    List<Schedule> objects; // Danh sách nguồn dữ liệu muốn hiển thị lên giao diện

    public ScheduleAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Schedule> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource, null);
        TextView txtScheduleId = (TextView) row.findViewById(R.id.txtScheduleId);
        TextView txtDriverId = (TextView) row.findViewById(R.id.txtDriverId);
        TextView txtVehicleId = (TextView) row.findViewById(R.id.txtVehicleId);
        TextView txtStartPointAddress = (TextView) row.findViewById(R.id.txtStartPointAddress);
        TextView txtEndPointAddress = (TextView) row.findViewById(R.id.txtEndPointAddress);
        TextView txtIntendStartTime = (TextView) row.findViewById(R.id.txtIntendStartTime);
        TextView txtIntendEndTime = (TextView) row.findViewById(R.id.txtIntendEndTime);
        TextView txtDetails = (TextView) row.findViewById(R.id.txtDetails);

        final Schedule schedule = this.objects.get(position);
        txtScheduleId.setText("Schedule Id: " + schedule.getScheduleId());
        txtDriverId.setText("Driver Id: " + schedule.getDriverId());
        txtVehicleId.setText("Vehicle Id: " + schedule.getVehicleId());
        txtStartPointAddress.setText("Start Point Address: " + schedule.getStartPointAddress());
        txtEndPointAddress.setText("End Point Address: " + schedule.getEndPointAddress());
        txtIntendStartTime.setText("Intend Start Time: " + schedule.getIntendStartTime());
        txtIntendEndTime.setText("Intend End Time: " + schedule.getIntendEndTime());


        return row;
    }


    }
