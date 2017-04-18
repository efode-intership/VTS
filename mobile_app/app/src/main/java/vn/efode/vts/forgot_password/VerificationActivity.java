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
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import vn.efode.vts.R;
import vn.efode.vts.application.ApplicationController;
import vn.efode.vts.utils.ServiceHandler;
import vn.efode.vts.utils.ServerCallback;

public class VerificationActivity extends AppCompatActivity {
    private EditText edtNumber;
    private Button btnDone;
    private TextView txtDescription;
    private String checkResetCodeUrl = ServiceHandler.DOMAIN + "/api/v1/user/checkResetCode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        edtNumber = (EditText)findViewById(R.id.edittext_verifi_number);
        btnDone = (Button)findViewById(R.id.button_verifi_done);
        txtDescription = (TextView)findViewById(R.id.textview_verifi_description);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Intent intent = new Intent(VerificationActivity.this, ChangePasswordActivity.class);

                HashMap<String,String> params = new HashMap<String,String>();
                params.put("email", ApplicationController.getCurrentUser().getEmail());
                params.put("confirmCode", edtNumber.getText().toString());

                ServiceHandler serviceHandler = new ServiceHandler();
                serviceHandler.makeServiceCall(checkResetCodeUrl, Request.Method.POST,
                        params, new ServerCallback() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                Log.d("Result",result.toString());
                                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                                try {
                                    Boolean error = gson.fromJson(result.getString("error"), Boolean.class);
                                    if (!error) {
                                        Type type = new TypeToken<Map<String, String>>(){}.getType();
                                        HashMap<String,String> outputMap = gson.fromJson(result.getString("content"), type);
                                        String passwordResetToken = outputMap.get("reset_password_token");

                                        Log.d("asds",String.valueOf(result.getString("content")));
                                        Log.d("asds", String.valueOf(passwordResetToken));
                                        intent.putExtra("RESET_PASSWORD_TOKEN", String.valueOf(passwordResetToken));
                                        startActivity(intent);
                                    }
                                    else {
                                        txtDescription.setText("Nhập sai mã xác nhận, vui lòng nhập lại");
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
