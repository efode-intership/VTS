package vn.efode.vts;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ProfileActivity extends AppCompatActivity {
    private Button btnCancel;
    private Button btnOk;
    private ImageButton btnChangePassword;
    private EditText edtPassword;
    private EditText edtNewPass;
    private EditText edtConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnChangePassword = (ImageButton)findViewById(R.id.button_profile_changepass);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();

                return true;
        }
        return super.onOptionsItemSelected(item);
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
}
