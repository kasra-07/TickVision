package ir.mahchegroup.tickvision;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import ir.mahchegroup.tickvision.classes.Animations;
import ir.mahchegroup.tickvision.classes.CheckConnection;
import ir.mahchegroup.tickvision.classes.Shared;
import ir.mahchegroup.tickvision.classes.StartBtn;
import ir.mahchegroup.tickvision.classes.UserItems;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private ConstraintLayout root;
    private ImageView btnClose;
    private boolean isUserSignup;
    private boolean isLoginStay;
    private StartBtn startBtn;
    private String activityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();

        new Handler().postDelayed(() -> Animations.AnimSplash(this, root, true), 400);

        btnClose.setOnClickListener(v -> {
            Animations.AnimSplash(this, root, false);
            new Handler().postDelayed(this::finish, 1000);
        });


        startBtn.setOnClickStartListener(v -> {
            boolean isConnected = CheckConnection.check(this);
            if (!isConnected) {
                activityName = "DisconnectActivity";
            } else {
                if (isLoginStay) {
                    activityName = "HomeActivity";
                } else {
                    if (isUserSignup) {
                        activityName = "LoginActivity";
                    } else {
                        activityName = "SignupActivity";
                    }
                }
            }
            setIntent(activityName);
        });
    }

    private void setIntent(String str) {
        String stringClass = "ir.mahchegroup.tickvision." + str;
        Class<?> c = null;
        try {
            c = Class.forName(stringClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(SplashActivity.this, c);
        intent.putExtra(UserItems.ACTIVITY_NAME, "SplashActivity");
        Animations.AnimActivity(SplashActivity.this, intent);
    }

    private void init() {
        root = findViewById(R.id.splash_root);
        btnClose = findViewById(R.id.splash_btn_close);
        startBtn = new StartBtn(this);
        startBtn = findViewById(R.id.start_btn);
        Shared shared = new Shared(this);
        isUserSignup = shared.getShared().getBoolean(UserItems.IS_USER_SIGNUP, false);
        isLoginStay = shared.getShared().getBoolean(UserItems.IS_LOGIN_STAY, false);
    }
}