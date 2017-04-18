package vn.efode.vts.forgot_password;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import vn.efode.vts.R;
import vn.efode.vts.utils.ServiceHandler;
import vn.efode.vts.utils.ServerCallback;

public class EnterPhoneActivity extends AppCompatActivity {
    private EditText edtEmail;
    private Button btnDone;
    private TextView txtDescription;
    private String fogotPasswordUrl = ServiceHandler.DOMAIN + "/api/v1/user/forgotPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_phone);
        edtEmail = (EditText)findViewById(R.id.edittext_entermail_email);
        btnDone = (Button)findViewById(R.id.button_entermail_done);
        txtDescription = (TextView)findViewById(R.id.textview_entermail_description);



        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(EnterPhoneActivity.this, VerificationActivity.class);

                HashMap<String,String> params = new HashMap<String,String>();
                params.put("email",edtEmail.getText().toString());

                ServiceHandler serviceHandler = new ServiceHandler();
                serviceHandler.makeServiceCall(fogotPasswordUrl, Request.Method.POST,
                        params, new ServerCallback() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                Log.d("Result",result.toString());
                                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                                try {
                                    Boolean error = gson.fromJson(result.getString("error"), Boolean.class);
                                    if (!error) {
                                        startActivity(intent);

                                    }
                                    else {
                                        txtDescription.setText("Nhập sai email, vui lòng nhập lại");
                                        txtDescription.setTextColor(Color.parseColor("#c0392b"));
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
        });


    }
}
