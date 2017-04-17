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
import vn.efode.vts.model.WarningTypes;

/**
 * Created by Nam on 12/04/2017.
 */

public class WarningAdapter extends ArrayAdapter<WarningTypes> {

    private TextView txtId;
    private TextView txtType;
    private TextView txtDescription;

    Activity context; //Màn hình sử dụng layout này (giao diện này)
    int resource; // Layout cho từng dòng muốn hiển thị
    List<WarningTypes> objects; // Danh sách nguồn dữ liệu muốn hiển thị lên giao diện
    public WarningAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<WarningTypes> objects) {
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
        txtId = (TextView) row.findViewById(R.id.textview_warningtypeslist_id);
        txtType = (TextView) row.findViewById(R.id.textview_warningtypeslist_type);
        txtDescription = (TextView) row.findViewById(R.id.textview_warningtypeslist_description);

        final WarningTypes warningTypes = this.objects.get(position);
        txtId.setText(String.valueOf(warningTypes.getWarningTypeId()));
        txtType.setText(warningTypes.getType());
        txtDescription.setText(warningTypes.getDescription());

        return row;
    }




}
