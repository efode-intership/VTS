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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import vn.efode.vts.model.User;
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
    private String validateUrl = ServiceHandler.DOMAIN + "/api/v1/user/validate";
    private String changPasswordUrl = ServiceHandler.DOMAIN + "/api/v1/user/changePassword";
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
        serviceHandler.makeServiceCall(validateUrl, Request.Method.POST,
                params, new ServerCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        Log.d("Result",result.toString());
                        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                        try {
                            Boolean error = gson.fromJson(result.getString("error"), Boolean.class);
                            if (!error) {
                                user = gson.fromJson(result.getString("content"), User.class);

                                if(user.getName()!= null)
                                edtName.setText(user.getName());
                                if(user.getSex()!= null)
                                edtSex.setText(user.getSex());
                                if(user.getPhone()!= null)
                                edtPhone.setText(user.getPhone());
                                if(user.getBirthday()!= null)
                                edtBirth.setText(user.getBirthday());
                                if(user.getAddress()!= null)
                                edtAddress.setText(user.getAddress());
                                if(user.getImage()!= null){
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
        edtPassword = (EditText) dialog.findViewById(R.id.edittext_dialog_password);
        edtNewPass = (EditText) dialog.findViewById(R.id.edittext_dialog_newpass);
        edtConfirm = (EditText) dialog.findViewById(R.id.edittext_dialog_confirm);

        btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(edtNewPass.getText().toString().equals(edtConfirm.getText().toString()) && !edtNewPass.getText().toString().equals("")){
                    HashMap<String,String> params = new HashMap<String,String>();
                    params.put("email","tuan@gmail.com");
                    params.put("currentPassword",edtPassword.getText().toString());
                    params.put("newPassword",edtNewPass.getText().toString());

                    ServiceHandler serviceHandler = new ServiceHandler();
                    serviceHandler.makeServiceCall(changPasswordUrl, Request.Method.POST,
                            params, new ServerCallback() {
                                @Override
                                public void onSuccess(JSONObject result) {
                                    Log.d("Result",result.toString());
                                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                                    try {
                                        Boolean error = gson.fromJson(result.getString("error"), Boolean.class);
                                        if (!error) {
                                            Toast.makeText(ProfileActivity.this, "Đổi thành công", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();

                                        }
                                        else {
                                            Toast.makeText(ProfileActivity.this, "Mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                                            dialog.setTitle("Mật khẩu không chính xác");
                                            edtPassword.setText("");
                                            edtNewPass.setText("");
                                            edtConfirm.setText("");
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(VolleyError error){
                                    Log.d("Loiii",error.getMessage());
                                    Toast.makeText(ProfileActivity.this, "Không thành công", Toast.LENGTH_SHORT).show();
                                    dialog.setTitle("Đổi không thành công");
                                }

                            });

                }else {
                    //Toast.makeText(ProfileActivity.this, "Không hợp lệ, vui lòng nhập lại", Toast.LENGTH_SHORT).show();
                    dialog.setTitle("Không hợp lệ, vui lòng nhập lại");
                    edtPassword.setText("");
                    edtNewPass.setText("");
                    edtConfirm.setText("");
                    Log.d("sadsa","sadsa");
                }

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
