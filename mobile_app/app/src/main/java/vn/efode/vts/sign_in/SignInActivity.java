package vn.efode.vts.sign_in;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import vn.efode.vts.MainActivity;
import vn.efode.vts.R;
import vn.efode.vts.service.ServiceHandler;
import vn.efode.vts.utils.ServerCallback;

public class SignInActivity extends AppCompatActivity {

    Button btnLogin;
    EditText txtUserName;
    EditText txtPassWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtUserName = (EditText) findViewById(R.id.txtUserName);
        txtPassWord = (EditText) findViewById(R.id.txtPassWord);

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
                    serviceHandler.makeServiceCall("http://192.168.0.102/web_app/public/api/v1/user/validate", Request.Method.POST, params, new ServerCallback() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            Log.d("Result", result.toString());

                            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                            try {
                                Boolean error = gson.fromJson(result.getString("error"), Boolean.class);
                                if (!error) {
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

    }
}
