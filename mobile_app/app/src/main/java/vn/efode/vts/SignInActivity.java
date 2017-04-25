package vn.efode.vts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import vn.efode.vts.application.ApplicationController;
import vn.efode.vts.model.User;
import vn.efode.vts.service.DeviceTokenService;
import vn.efode.vts.forgot_password.EnterPhoneActivity;
import vn.efode.vts.utils.ServerCallback;
import vn.efode.vts.utils.ServiceHandler;

public class SignInActivity extends AppCompatActivity {

    Button btnLogin;
    EditText txtUserName;
    EditText txtPassWord;
    private TextView txtForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtUserName = (EditText) findViewById(R.id.txtUserName);
        txtPassWord = (EditText) findViewById(R.id.txtPassWord);
        txtForgotPassword = (TextView)findViewById(R.id.textview_signin_forgotpassword);

        User user = ApplicationController.getCurrentUser();
        // mapping device token with user
        String deviceToken = ApplicationController.sharedPreferences.getString(DeviceTokenService.DEVICE_TOKEN, null);
        if( user != null) {
            if (deviceToken != null ) {
                String saveTokenUrl = "/api/v1/token/save";
                HashMap<String, String> saveTokenParams = new HashMap<String, String>();
                saveTokenParams.put("deviceId", deviceToken);
                saveTokenParams.put("userId", String.valueOf(user.getId()));
                ServiceHandler.makeServiceCall(ServiceHandler.DOMAIN +saveTokenUrl, Request.Method.POST, saveTokenParams, null);
            }
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        txtUserName.setText("tuan@gmail.com");
        txtPassWord.setText("123123");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtUserName.getText().toString().equals("") || txtPassWord.getText().toString().equals(""))
                {
                    Toast.makeText(SignInActivity.this, "Không được để trống UserName hoặc PassWord!!!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    HashMap<String, String> params = new HashMap<String, String>();

                    params.put("email", txtUserName.getText().toString());
                    params.put("password", txtPassWord.getText().toString());

                    ServiceHandler serviceHandler = new ServiceHandler();
                    serviceHandler.makeServiceCall(ServiceHandler.DOMAIN + "/api/v1/user/validate", Request.Method.POST, params, new ServerCallback() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            Log.d("Result", result.toString());

                            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                            try {
                                Boolean error = gson.fromJson(result.getString("error"), Boolean.class);
                                String userJson = result.getString("content");
                                // save user into session
                                ApplicationController.sharedPreferences.edit().putString(ApplicationController.USER_SESSION,userJson).commit();
                                User user = ApplicationController.getCurrentUser();
                                if (!error) {
                                    // mapping device token with user
                                    String deviceToken = ApplicationController.sharedPreferences.getString(DeviceTokenService.DEVICE_TOKEN, null);
                                    if (deviceToken != null && user != null) {
                                        String saveTokenUrl = "/api/v1/token/save";
                                        HashMap<String, String> saveTokenParams = new HashMap<String, String>();
                                        saveTokenParams.put("deviceId", deviceToken);
                                        saveTokenParams.put("userId", String.valueOf(user.getId()));
                                        ServiceHandler.makeServiceCall(ServiceHandler.DOMAIN +saveTokenUrl, Request.Method.POST, saveTokenParams, null);
                                    }

                                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                    startActivity(intent);

                                }
                                else {
                                    Toast.makeText(SignInActivity.this, "Vui lòng kiểm tra lại UserName hoặc PassWord!!!", Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(VolleyError error) {
                            Log.d("Result", error.getMessage());
                        }

                    });
                }


            }

        });
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, EnterPhoneActivity.class);
                startActivity(intent);

            }
        });
    }
}
