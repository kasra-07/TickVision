package ir.mahchegroup.tickvision.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Objects;

import ir.mahchegroup.tickvision.Animations;
import ir.mahchegroup.tickvision.CheckConnection;
import ir.mahchegroup.tickvision.R;
import ir.mahchegroup.tickvision.Shared;
import ir.mahchegroup.tickvision.UserItems;

public class DisconnectActivity extends AppCompatActivity {
    private String activityName;
    private boolean isUserSignup, isLoginStay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disconnect);

        init();

        if (activityName != null && activityName.equals("SplashActivity")) {
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
    }

    public void btnOnClick(View view) {
        boolean isConnected = CheckConnection.check(this);
        if (isConnected) {
            setIntent(activityName);
        }
    }

    private void setIntent(String str) {
        String stringClass = "ir.mahchegroup.tickvision.activities." + str;
        Class<?> c = null;
        try {
            c = Class.forName(stringClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(DisconnectActivity.this, c);
        Animations.AnimActivity(this, intent);
    }

    private void init() {
        Shared shared = new Shared(this);
        isUserSignup = shared.getShared().getBoolean(UserItems.IS_USER_SIGNUP, false);
        isLoginStay = shared.getShared().getBoolean(UserItems.IS_LOGIN_STAY, false);

        activityName = Objects.requireNonNull(getIntent().getExtras()).getString(UserItems.ACTIVITY_NAME);
    }
}