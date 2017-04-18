package vn.efode.vts.adapter;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import vn.efode.vts.R;
import vn.efode.vts.model.WarningTypes;

/**
 * Created by Nam on 12/04/2017.
 */

public class WarningAdapter extends ArrayAdapter<WarningTypes> {

    private TextView txtType;
    private ImageView imgType;

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
        txtType = (TextView) row.findViewById(R.id.textview_warningtypeslist_type);
        imgType = (ImageView) row.findViewById(R.id.imageview_warningtypeslist_image);

        final WarningTypes warningTypes = this.objects.get(position);

        txtType.setText(warningTypes.getType());
        //imgType.setImageURI(Uri.parse(warningTypes.getImage()));
        //Picasso.with(context).load(warningTypes.getImage()).resize(100, 100).into(imgType);
        if(warningTypes.getWarningTypeId() == 1) {
            Picasso.with(context).load(R.drawable.ketxe).resize(100, 100).into(imgType);
        }else if(warningTypes.getWarningTypeId() == 2) {
            Picasso.with(context).load(R.drawable.pokemon).resize(100, 100).into(imgType);
        }else if(warningTypes.getWarningTypeId() == 3) {
            Picasso.with(context).load(R.drawable.hongduong).resize(100, 100).into(imgType);
        }else  {
            Picasso.with(context).load(R.drawable.hongxe).resize(100, 100).into(imgType);
        }

        return row;
    }




}
