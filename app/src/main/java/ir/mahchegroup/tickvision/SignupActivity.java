package ir.mahchegroup.tickvision;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import ir.mahchegroup.tickvision.R;

public class SignupActivity extends AppCompatActivity {
    private RelativeLayout root;
    private TextInputLayout lUsername, lMail, lPass, lCPass;
    private TextInputEditText eUsername, eMail, ePass, eCPass;
    private TextView link;
    private Button btn;
    private boolean isUsernameError, isMailError, isPassError;
    private String username, mail, pass, cPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        init();


    }

    private void init() {
        root = findViewById(R.id.signup_root);
        lUsername = findViewById(R.id.username_layout);
        lMail = findViewById(R.id.mail_layout);
        lPass = findViewById(R.id.pass_layout);
        lCPass = findViewById(R.id.c_pass_layout);
        eUsername = findViewById(R.id.edt_username);
        eMail = findViewById(R.id.edt_mail);
        ePass = findViewById(R.id.edt_pass);
        eCPass = findViewById(R.id.edt_c_pass);
        btn = findViewById(R.id.btn_signup);
        link = findViewById(R.id.signup_link);
    }
}