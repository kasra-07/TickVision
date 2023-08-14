package ir.mahchegroup.tickvision;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Objects;

import ir.mahchegroup.tickvision.classes.Animations;
import ir.mahchegroup.tickvision.classes.KeyboardManager;
import ir.mahchegroup.tickvision.classes.SetInputLayoutColors;
import ir.mahchegroup.tickvision.classes.Shared;
import ir.mahchegroup.tickvision.classes.UserItems;
import ir.mahchegroup.tickvision.message_box.LoadingDialog;
import ir.mahchegroup.tickvision.message_box.ToastMessage;
import ir.mahchegroup.tickvision.network.NetworkReceiver;
import ir.mahchegroup.tickvision.network.UserLogin;

public class LoginActivity extends AppCompatActivity implements UserLogin.OnUserLoginCallBack {
    private TextInputLayout lUserMail, lPass;
    private TextInputEditText eUserMail, ePass;
    private Shared shared;
    private TextView loginLink, forgetLink;
    private RelativeLayout root;
    private NetworkReceiver receiver;
    private CheckBox chRemember;
    private boolean isRememberChecked, isChecked;
    private String userMail, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        if (isRememberChecked) {
            chRemember.setTextColor(getColor(R.color.primary_color));
            chRemember.setChecked(true);
            eUserMail.setText(shared.getShared().getString(UserItems.USER_MAIL, ""));
            ePass.setText(shared.getShared().getString(UserItems.PASS, ""));
            eUserMail.setTextColor(getColor(R.color.green));
            ePass.setTextColor(getColor(R.color.green));
        } else {
            chRemember.setTextColor(getColor(R.color.gray));
            chRemember.setChecked(false);
            eUserMail.setText("");
            ePass.setText("");
        }

        root.setOnClickListener(v -> {
            KeyboardManager.hideKeyboardOnActivity(this, this);
            setInputLayoutGray();
        });

        edtTextChange();

        edtFocusChange();

        loginLink.setOnClickListener(v -> {
            root.performClick();
            shared.getEditor().putBoolean(UserItems.IS_USER_SIGNUP, false).apply();
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                Animations.AnimActivity(this, intent);
            }, 200);
        });

        chRemember.setOnCheckedChangeListener((buttonView, b) -> {
            root.performClick();
            if (b) {
                chRemember.setTextColor(getColor(R.color.primary_color));
                userMail = Objects.requireNonNull(eUserMail.getText()).toString().trim();
                pass = Objects.requireNonNull(ePass.getText()).toString().trim();
            } else {
                chRemember.setTextColor(getColor(R.color.gray));
                userMail = "";
                pass = "";
            }
            isChecked = b;
        });

        forgetLink.setOnClickListener(v -> ToastMessage.show(this, getString(R.string.coming_soon_text), false, false));
    }

    private void edtFocusChange() {
        eUserMail.setOnFocusChangeListener((v, b) -> {
            if (b) {
                SetInputLayoutColors.setColor(this, lUserMail, eUserMail, null, 1);
            } else {
                SetInputLayoutColors.setColor(this, lUserMail, eUserMail, null, 0);
                eUserMail.setTextColor(getColor(R.color.green));
            }
        });
        ePass.setOnFocusChangeListener((v, b) -> {
            if (b) {
                SetInputLayoutColors.setColor(this, lPass, ePass, null, 1);
            } else {
                SetInputLayoutColors.setColor(this, lPass, ePass, null, 0);
                ePass.setTextColor(getColor(R.color.green));
            }
        });
    }

    private void edtTextChange() {
        eUserMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    SetInputLayoutColors.setColor(LoginActivity.this, lUserMail, eUserMail, getString(R.string.empty_field_error), 2);
                } else {
                    SetInputLayoutColors.setColor(LoginActivity.this, lUserMail, eUserMail, null, 1);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ePass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    SetInputLayoutColors.setColor(LoginActivity.this, lPass, ePass, getString(R.string.empty_field_error), 2);
                } else {
                    SetInputLayoutColors.setColor(LoginActivity.this, lPass, ePass, null, 1);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            receiver = new NetworkReceiver();
            registerReceiver(receiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    private void setInputLayoutGray() {
        eUserMail.clearFocus();
        SetInputLayoutColors.setColor(this, lUserMail, eUserMail, null, 0);
        eUserMail.setTextColor(getColor(R.color.green));
        ePass.clearFocus();
        SetInputLayoutColors.setColor(this, lPass, ePass, null, 0);
        ePass.setTextColor(getColor(R.color.green));
    }

    public void btnOnClick(View view) {
        root.performClick();

        LoadingDialog.show(this, getString(R.string.getting_info_text));

        userMail = Objects.requireNonNull(eUserMail.getText()).toString().trim();
        pass = Objects.requireNonNull(ePass.getText()).toString().trim();

        if (userMail.isEmpty() || pass.isEmpty()) {
            if (userMail.isEmpty()) {
                SetInputLayoutColors.setColor(this, lUserMail, eUserMail, getString(R.string.empty_field_error), 2);
            } else {
                SetInputLayoutColors.setColor(this, lUserMail, eUserMail, null, 0);
            }

            if (pass.isEmpty()) {
                SetInputLayoutColors.setColor(this, lPass, ePass, getString(R.string.empty_field_error), 2);
            } else {
                SetInputLayoutColors.setColor(this, lPass, ePass, null, 0);
            }

        } else {
            setOnLogin();
        }
    }

    private void setOnLogin() {
        UserLogin userLogin = new UserLogin(this);
        userLogin.login(userMail, pass);
    }

    @Override
    public void onUserLoginListener(List<String> loginList) {
        if (loginList.get(0).equals("empty")) {
            new Handler().postDelayed(() -> {
                if (LoadingDialog.isShow()) {
                    LoadingDialog.dismiss();
                    ToastMessage.show(this, getString(R.string.login_error), false, false);
                }
            }, 700);
        } else if (loginList.get(0).equals("pass_error")) {
            new Handler().postDelayed(() -> {
                if (LoadingDialog.isShow()) {
                    LoadingDialog.dismiss();
                    ToastMessage.show(this, getString(R.string.login_pass_error), false, false);
                }
            }, 700);
        } else {
            shared.getEditor().putString(UserItems.USERNAME, loginList.get(1));
            shared.getEditor().putString(UserItems.MAIL, loginList.get(2));
            shared.getEditor().putString(UserItems.USER_TBL, loginList.get(3));
            if (isChecked) {
                shared.getEditor().putBoolean(UserItems.IS_REMEMBER_CHECKED, true);
                shared.getEditor().putString(UserItems.USER_MAIL, userMail);
                shared.getEditor().putString(UserItems.PASS, pass);
            } else {
                shared.getEditor().putBoolean(UserItems.IS_REMEMBER_CHECKED, false);
                shared.getEditor().remove(UserItems.USER_MAIL);
                shared.getEditor().remove(UserItems.PASS);
            }
            shared.getEditor().apply();

            new Handler().postDelayed(() -> {
                LoadingDialog.dismiss();
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    Animations.AnimActivity(this, intent);
                }, 400);
            }, 1500);
        }
    }

    private void init() {
        root = findViewById(R.id.login_root);
        lUserMail = findViewById(R.id.user_mail_layout);
        lPass = findViewById(R.id.pass_layout);
        eUserMail = findViewById(R.id.edt_user_mail);
        ePass = findViewById(R.id.edt_pass);
        loginLink = findViewById(R.id.login_link);
        forgetLink = findViewById(R.id.forget_link);
        chRemember = findViewById(R.id.ch_remember);
        shared = new Shared(this);
        isRememberChecked = shared.getShared().getBoolean(UserItems.IS_REMEMBER_CHECKED, false);
        isChecked = shared.getShared().getBoolean(UserItems.IS_REMEMBER_CHECKED, false);
        lUserMail.setDefaultHintTextColor(ColorStateList.valueOf(getColor(R.color.gray)));
        lPass.setDefaultHintTextColor(ColorStateList.valueOf(getColor(R.color.gray)));
    }
}