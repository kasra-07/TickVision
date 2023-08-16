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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import ir.mahchegroup.tickvision.classes.Animations;
import ir.mahchegroup.tickvision.classes.CheckEmailType;
import ir.mahchegroup.tickvision.classes.CheckPassType;
import ir.mahchegroup.tickvision.classes.CheckUsernameType;
import ir.mahchegroup.tickvision.classes.KeyboardManager;
import ir.mahchegroup.tickvision.classes.SetInputLayoutColors;
import ir.mahchegroup.tickvision.classes.Shared;
import ir.mahchegroup.tickvision.classes.UserItems;
import ir.mahchegroup.tickvision.message_box.DuplicateSignupDialog;
import ir.mahchegroup.tickvision.message_box.LoadingDialog;
import ir.mahchegroup.tickvision.message_box.ToastMessage;
import ir.mahchegroup.tickvision.network.NetworkReceiver;
import ir.mahchegroup.tickvision.network.UserSignup;

public class SignupActivity extends AppCompatActivity implements UserSignup.UserSignupCallBack {
    private RelativeLayout root;
    private TextInputLayout lUsername, lMail, lPass, lCPass;
    private TextInputEditText eUsername, eMail, ePass, eCPass;
    private TextView link;
    private boolean isUsernameError, isMailError, isPassError;
    private String username, mail, pass, cPass;
    private Shared shared;
    private NetworkReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        init();

        root.setOnClickListener(v -> {
            KeyboardManager.hideKeyboardOnActivity(this, this);
            setInputLayoutGray();
        });

        edtTextChange();

        edtFocusChange();

        link.setOnClickListener(v -> {
            root.performClick();
            shared.getEditor().putBoolean(UserItems.IS_USER_SIGNUP, true).apply();
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                Animations.AnimActivity(this, intent);
            }, 200);
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

    private void edtFocusChange() {
        eUsername.setOnFocusChangeListener((v, b) -> {
            if (b) {
                edtUnFocus(lMail, lPass, lCPass, eMail, ePass, eCPass);
                if (isUsernameError) {
                    SetInputLayoutColors.setColor(this, lUsername, eUsername, getString(R.string.username_type_error), 2);
                } else {
                    SetInputLayoutColors.setColor(this, lUsername, eUsername, null, 1);
                }

            } else {
                SetInputLayoutColors.setColor(this, lUsername, eUsername, null, 0);
                if (isUsernameError) {
                    eUsername.setTextColor(getColor(R.color.red));
                } else {
                    eUsername.setTextColor(getColor(R.color.green));
                }
            }
        });
        eMail.setOnFocusChangeListener((v, b) -> {
            edtUnFocus(lUsername, lPass, lCPass, eUsername, ePass, eCPass);
            if (b) {
                if (isMailError) {
                    SetInputLayoutColors.setColor(this, lMail, eMail, getString(R.string.mail_type_error), 2);
                    eMail.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.red)));
                } else {
                    SetInputLayoutColors.setColor(this, lMail, eMail, null, 1);
                    eMail.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.primary_color)));
                }

            } else {
                SetInputLayoutColors.setColor(this, lMail, eMail, null, 0);
                eMail.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.gray)));
                if (isMailError) {
                    eMail.setTextColor(getColor(R.color.red));
                } else {
                    eMail.setTextColor(getColor(R.color.green));
                }
            }
        });
        ePass.setOnFocusChangeListener((v, b) -> {
            if (b) {
                if (isPassError || (ePass.length() == 0 && eCPass.length() > 0) || (ePass.length() > 0 && eCPass.length() == 0)) {
                    SetInputLayoutColors.setColor(this, lPass, ePass, getString(R.string.pass_type_error), 2);
                    eCPass.setTextColor(getColor(R.color.red));
                } else {
                    SetInputLayoutColors.setColor(this, lPass, ePass, null, 1);
                    eCPass.setTextColor(getColor(R.color.green));
                }

            } else {
                SetInputLayoutColors.setColor(this, lPass, ePass, null, 0);
                if (isPassError) {
                    ePass.setTextColor(getColor(R.color.red));
                    eCPass.setTextColor(getColor(R.color.red));
                } else if (ePass.length() == 0) {
                    eCPass.setTextColor(getColor(R.color.red));
                } else {
                    ePass.setTextColor(getColor(R.color.green));
                    eCPass.setTextColor(getColor(R.color.green));
                }
                ePass.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.gray)));
            }
            edtUnFocus(lMail, lUsername, lCPass, eMail, eUsername, eCPass);
        });
        eCPass.setOnFocusChangeListener((v, b) -> {
            if (b) {
                edtUnFocus(lMail, lPass, lUsername, eMail, ePass, eUsername);
                if (isPassError || (eCPass.length() == 0 && ePass.length() > 0) || (eCPass.length() > 0 && ePass.length() == 0)) {
                    SetInputLayoutColors.setColor(this, lCPass, eCPass, getString(R.string.pass_type_error), 2);
                    ePass.setTextColor(getColor(R.color.red));
                } else {
                    SetInputLayoutColors.setColor(this, lCPass, eCPass, null, 1);
                    ePass.setTextColor(getColor(R.color.green));
                }
            } else {
                SetInputLayoutColors.setColor(this, lCPass, eCPass, null, 0);
                eCPass.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.gray)));
                if (isPassError) {
                    eCPass.setTextColor(getColor(R.color.red));
                    ePass.setTextColor(getColor(R.color.red));
                } else if (eCPass.length() == 0) {
                    ePass.setTextColor(getColor(R.color.red));
                } else {
                    eCPass.setTextColor(getColor(R.color.green));
                    ePass.setTextColor(getColor(R.color.green));
                }
            }
        });
    }

    private void setInputLayoutGray() {
        SetInputLayoutColors.setColor(this, lUsername, eUsername, null, 0);
        SetInputLayoutColors.setColor(this, lMail, eMail, null, 0);
        SetInputLayoutColors.setColor(this, lPass, ePass, null, 0);
        SetInputLayoutColors.setColor(this, lCPass, eCPass, null, 0);

        if (isUsernameError) {
            eUsername.setTextColor(getColor(R.color.red));
        } else {
            eUsername.setTextColor(getColor(R.color.green));
        }

        if (isMailError) {
            eMail.setTextColor(getColor(R.color.red));
        } else {
            eMail.setTextColor(getColor(R.color.green));
        }

        if (isPassError) {
            ePass.setTextColor(getColor(R.color.red));
            eCPass.setTextColor(getColor(R.color.red));
        } else if (ePass.length() == 0) {
            eCPass.setTextColor(getColor(R.color.red));
        } else if (eCPass.length() == 0) {
            ePass.setTextColor(getColor(R.color.red));
        } else {
            ePass.setTextColor(getColor(R.color.green));
            eCPass.setTextColor(getColor(R.color.green));
        }
    }

    private void edtTextChange() {
        eUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isUsernameError = CheckUsernameType.check(s.toString());

                if (isUsernameError) {
                    SetInputLayoutColors.setColor(SignupActivity.this, lUsername, eUsername, getString(R.string.username_type_error), 2);
                } else if (start == 0 && before == 1) {
                    SetInputLayoutColors.setColor(SignupActivity.this, lUsername, eUsername, getString(R.string.empty_field_error), 2);
                } else {
                    SetInputLayoutColors.setColor(SignupActivity.this, lUsername, eUsername, null, 1);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        eMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isMailError = CheckEmailType.check(s.toString());

                if (isMailError) {
                    SetInputLayoutColors.setColor(SignupActivity.this, lMail, eMail, getString(R.string.mail_type_error), 2);
                } else if (start == 0 && before == 1) {
                    SetInputLayoutColors.setColor(SignupActivity.this, lMail, eMail, getString(R.string.empty_field_error), 2);
                } else {
                    SetInputLayoutColors.setColor(SignupActivity.this, lMail, eMail, null, 1);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                eMail.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.primary_color)));
            }
        });
        ePass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (eCPass.length() > 0 && ePass.length() > 0) {
                    pass = s.toString().trim();
                    cPass = Objects.requireNonNull(eCPass.getText()).toString().trim();
                    isPassError = CheckPassType.check(Integer.parseInt(pass), Integer.parseInt(cPass));

                    if (isPassError) {
                        SetInputLayoutColors.setColor(SignupActivity.this, lPass, ePass, getString(R.string.pass_type_error), 2);
                        eCPass.setTextColor(getColor(R.color.red));
                    } else {
                        SetInputLayoutColors.setColor(SignupActivity.this, lPass, ePass, null, 1);
                        eCPass.setTextColor(getColor(R.color.green));
                    }

                } else if (start == 0 && before == 1) {
                    isPassError = false;
                    SetInputLayoutColors.setColor(SignupActivity.this, lPass, ePass, getString(R.string.empty_field_error), 2);
                    eCPass.setTextColor(getColor(R.color.red));
                } else if (eCPass.length() == 0) {
                    SetInputLayoutColors.setColor(SignupActivity.this, lPass, ePass, getString(R.string.pass_type_error), 2);
                } else {
                    isPassError = false;
                    SetInputLayoutColors.setColor(SignupActivity.this, lPass, ePass, null, 1);
                    eCPass.setTextColor(getColor(R.color.green));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        eCPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (ePass.length() > 0 && eCPass.length() > 0) {
                    cPass = s.toString().trim();
                    pass = Objects.requireNonNull(ePass.getText()).toString().trim();
                    isPassError = CheckPassType.check(Integer.parseInt(pass), Integer.parseInt(cPass));

                    if (isPassError) {
                        SetInputLayoutColors.setColor(SignupActivity.this, lCPass, eCPass, getString(R.string.pass_type_error), 2);
                        ePass.setTextColor(getColor(R.color.red));
                    } else {
                        SetInputLayoutColors.setColor(SignupActivity.this, lCPass, eCPass, null, 1);
                        ePass.setTextColor(getColor(R.color.green));
                    }

                } else if (start == 0 && before == 1) {
                    isPassError = false;
                    SetInputLayoutColors.setColor(SignupActivity.this, lCPass, eCPass, getString(R.string.empty_field_error), 2);
                    ePass.setTextColor(getColor(R.color.red));
                } else if (ePass.length() == 0) {
                    SetInputLayoutColors.setColor(SignupActivity.this, lCPass, eCPass, getString(R.string.pass_type_error), 2);
                } else {
                    isPassError = false;
                    SetInputLayoutColors.setColor(SignupActivity.this, lCPass, eCPass, null, 1);
                    eCPass.setTextColor(getColor(R.color.green));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void btnOnClick(View view) {
        root.performClick();

        if (!shared.getShared().getString(UserItems.USERNAME, "").isEmpty() || !shared.getShared().getString(UserItems.USER_MAIL, "").isEmpty()) {
            DuplicateSignupDialog duplicateSignupDialog = new DuplicateSignupDialog(shared);
            duplicateSignupDialog.show(this, this);
        } else {
            username = Objects.requireNonNull(eUsername.getText()).toString().trim();
            mail = Objects.requireNonNull(eMail.getText()).toString().trim();
            pass = Objects.requireNonNull(ePass.getText()).toString().trim();
            cPass = Objects.requireNonNull(eCPass.getText()).toString().trim();

            if (username.isEmpty() || mail.isEmpty() || pass.isEmpty() || cPass.isEmpty() || isUsernameError || isMailError || isPassError) {
                if (username.isEmpty()) {
                    SetInputLayoutColors.setColor(this, lUsername, eUsername, getString(R.string.empty_field_error), 2);
                } else {
                    if (isUsernameError) {
                        SetInputLayoutColors.setColor(this, lUsername, eUsername, getString(R.string.username_type_error), 2);
                    } else {
                        SetInputLayoutColors.setColor(this, lUsername, eUsername, null, 0);
                        eUsername.setTextColor(getColor(R.color.green));
                    }
                }

                if (mail.isEmpty()) {
                    SetInputLayoutColors.setColor(this, lMail, eMail, getString(R.string.empty_field_error), 2);
                } else {
                    if (isMailError) {
                        SetInputLayoutColors.setColor(this, lMail, eMail, getString(R.string.mail_type_error), 2);
                    } else {
                        SetInputLayoutColors.setColor(this, lMail, eMail, null, 0);
                        eMail.setTextColor(getColor(R.color.green));
                    }
                }

                if (pass.isEmpty()) {
                    SetInputLayoutColors.setColor(this, lPass, ePass, getString(R.string.empty_field_error), 2);
                    eCPass.setTextColor(getColor(R.color.red));
                } else {
                    if (isPassError) {
                        SetInputLayoutColors.setColor(this, lPass, ePass, getString(R.string.pass_type_error), 2);
                        eCPass.setTextColor(getColor(R.color.red));
                    } else {
                        SetInputLayoutColors.setColor(this, lPass, ePass, null, 0);
                        ePass.setTextColor(getColor(R.color.green));
                        eCPass.setTextColor(getColor(R.color.green));
                    }
                }

                if (cPass.isEmpty()) {
                    SetInputLayoutColors.setColor(this, lCPass, eCPass, getString(R.string.empty_field_error), 2);
                    ePass.setTextColor(getColor(R.color.red));
                } else {
                    if (isPassError) {
                        SetInputLayoutColors.setColor(this, lCPass, eCPass, getString(R.string.pass_type_error), 2);
                        ePass.setTextColor(getColor(R.color.red));
                    } else {
                        SetInputLayoutColors.setColor(this, lCPass, eCPass, null, 0);
                        eCPass.setTextColor(getColor(R.color.green));
                        ePass.setTextColor(getColor(R.color.green));
                    }
                }

                eUsername.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.gray)));
                eMail.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.gray)));
                ePass.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.gray)));
                eCPass.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.gray)));
            } else {
                setOnSignup();
            }
        }
    }

    private void setOnSignup() {
        LoadingDialog.show(this, getString(R.string.sending_info_text));
        UserSignup userSignup = new UserSignup(this);
        userSignup.signup(username, mail, pass);
    }

    @Override
    public void onUserSignupListener(String isSignup) {
        if (isSignup.equals("error")) {
            ToastMessage.show(this, getString(R.string.signup_error), false, false);
        } else if (isSignup.equals("duplicate")) {
            ToastMessage.show(this, getString(R.string.duplicate_error), false, false);
        } else {
            new Handler().postDelayed(() -> {
                shared.getEditor().putBoolean(UserItems.IS_USER_SIGNUP, true);
                shared.getEditor().apply();
                LoadingDialog.dismiss();
                new Handler().postDelayed(() -> {
                    ToastMessage.show(this, getString(R.string.signup_success), true, true);
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    Animations.AnimActivity(this, intent);
                }, 400);
            }, 1700);
        }
    }

    private void edtUnFocus(TextInputLayout l1, TextInputLayout l2, TextInputLayout l3, TextInputEditText e1, TextInputEditText e2, TextInputEditText e3) {
        SetInputLayoutColors.setColor(this, l1, e1, null, 0);
        e1.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.gray)));
        SetInputLayoutColors.setColor(this, l2, e2, null, 0);
        e2.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.gray)));
        SetInputLayoutColors.setColor(this, l3, e3, null, 0);
        e3.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.gray)));
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
        link = findViewById(R.id.signup_link);
        shared = new Shared(this);
        lUsername.setDefaultHintTextColor(ColorStateList.valueOf(getColor(R.color.gray)));
        lMail.setDefaultHintTextColor(ColorStateList.valueOf(getColor(R.color.gray)));
        lPass.setDefaultHintTextColor(ColorStateList.valueOf(getColor(R.color.gray)));
        lCPass.setDefaultHintTextColor(ColorStateList.valueOf(getColor(R.color.gray)));
    }
}