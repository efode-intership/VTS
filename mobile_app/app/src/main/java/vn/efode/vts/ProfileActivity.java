package vn.efode.vts;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import vn.efode.vts.Model.User;
import vn.efode.vts.service.ServiceHandler;
import vn.efode.vts.utils.ServerCallback;

public class ProfileActivity extends AppCompatActivity {
    private Button btnCancel;
    private Button btnOk;
    private ImageButton btnChangePassword;
    private ImageView imgProfile;
    private EditText edtName;
    private EditText edtSex;
    private EditText edtPhone;
    private EditText edtBirth;
    private EditText edtAddress;
    private EditText edtPassword;
    private EditText edtNewPass;
    private EditText edtConfirm;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        imgProfile = (ImageView)findViewById(R.id.imageview_profile_avatar);
        edtName = (EditText)findViewById(R.id.edittext_profile_name);
        edtSex = (EditText)findViewById(R.id.edittext_profile_sex);
        edtPhone = (EditText)findViewById(R.id.edittext_profile_phonenumber);
        edtBirth = (EditText)findViewById(R.id.edittext_profile_birth);
        edtAddress = (EditText)findViewById(R.id.edittext_profile_address);
        //edtName.setText("Lê Phú Đạt");
        btnChangePassword = (ImageButton)findViewById(R.id.button_profile_changepass);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();

            }
        });

        HashMap<String,String> params = new HashMap<String,String>();
        params.put("email","tuan@gmail.com");
        params.put("password","123123");

        ServiceHandler serviceHandler = new ServiceHandler();
        serviceHandler.makeServiceCall("http://192.168.1.16/web_app/public/api/v1/user/validate", Request.Method.POST,
                params, new ServerCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        Log.d("Result",result.toString());
                        User user = new User();
                        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                        try {
                            Boolean error = gson.fromJson(result.getString("error"), Boolean.class);
                            if (!error) {
                                user = gson.fromJson(result.getString("content"), User.class);

                                edtName.setText(user.getName());
                                edtSex.setText(user.getSex());
                                edtPhone.setText(user.getPhone());
                                edtBirth.setText(user.getBirthday());
                                edtAddress.setText(user.getAddress());
                                if(user.getImage() != null){
                                    Log.d("sads",user.getImage());
                                    String urlImage = user.getImage();
                                    Picasso.with(ProfileActivity.this).load(urlImage).resize(400,400).into(imgProfile);
                                }


                            }
                            else {

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(VolleyError error){
                        Log.d("Result",error.getMessage());
                    }

                });


    }


    public void changePassword(){

        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_changepassword);
        dialog.setTitle("Đổi mật khẩu");

        // set the custom dialog components - text, image and button


        btnOk = (Button) dialog.findViewById(R.id.button_dialog_ok);
        btnCancel = (Button) dialog.findViewById(R.id.button_dialog_cancel);

        btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }



    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(this,MainActivity.class);
                this.startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
