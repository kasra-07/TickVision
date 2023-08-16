package ir.mahchegroup.tickvision;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import ir.mahchegroup.tickvision.classes.Animations;
import ir.mahchegroup.tickvision.classes.Shared;
import ir.mahchegroup.tickvision.classes.UserItems;
import ir.mahchegroup.tickvision.database.VisionDao;
import ir.mahchegroup.tickvision.database.VisionDatabase;
import ir.mahchegroup.tickvision.message_box.DeleteAccountDialog;
import ir.mahchegroup.tickvision.message_box.LoadingDialog;
import ir.mahchegroup.tickvision.message_box.ToastMessage;
import ir.mahchegroup.tickvision.network.DeleteAccount;
import ir.mahchegroup.tickvision.network.NetworkReceiver;

public class SettingActivity extends AppCompatActivity implements DeleteAccount.OnDeleteAccountCallBack, DeleteAccountDialog.OnDeleteAccountDialogCallBack {
    private SwitchCompat loginStay, showExitDialog;
    private Button btnRemove;
    private Shared shared;
    private Intent intent;
    private NetworkReceiver receiver;
    private String userTbl, userMail;
    private VisionDao dao;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = android.R.id.home;

        if (item.getItemId() == id) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        init();

        loginStay.setOnCheckedChangeListener((buttonView, isChecked) -> shared.getEditor().putBoolean(UserItems.IS_LOGIN_STAY, isChecked).apply());

        showExitDialog.setOnCheckedChangeListener((buttonView, isChecked) -> shared.getEditor().putBoolean(UserItems.IS_SHOW_EXIT_DIALOG, isChecked).apply());

        btnRemove.setOnClickListener(v -> {
            DeleteAccountDialog deleteAccountDialog = new DeleteAccountDialog(this);
            deleteAccountDialog.show();
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

    @Override
    public void onBackPressed() {
        if (shared.getShared().getBoolean(UserItems.IS_FROM_SIGNUP, false)) {
            intent = new Intent(SettingActivity.this, SignupActivity.class);

        } else {
            intent = new Intent(SettingActivity.this, HomeActivity.class);
        }
        Animations.AnimActivity(this, intent);
    }

    @Override
    public void onDeleteAccountDialogListener() {
        LoadingDialog.show(this, getString(R.string.please_vait_text));
        DeleteAccount deleteAccount = new DeleteAccount(this);
        deleteAccount.delete(userMail, userTbl);
    }

    @Override
    public void onDeleteAccountListener(String isDelete) {
        if (isDelete.equals("success")) {
            new Handler().postDelayed(() -> {
                if (LoadingDialog.isShow()) {
                    LoadingDialog.dismiss();
                }
                dao.clearAllVisions();
                shared.getEditor().clear().apply();

                new Handler().postDelayed(() -> {
                    intent = new Intent(SettingActivity.this, SignupActivity.class);
                    Animations.AnimActivity(this, intent);
                    ToastMessage.show(this, getString(R.string.delete_account_success_text), true, false);
                }, 300);

            }, 900);
        } else {
            if (LoadingDialog.isShow()) {
                LoadingDialog.dismiss();
            }
            ToastMessage.show(this, getString(R.string.delete_account_error), false, false);
        }
    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView tvToolbar = findViewById(R.id.tv_toolbar);
        tvToolbar.setText(getString(R.string.setting_text));
        ImageView imgToolbar = findViewById(R.id.img_toolbar);
        imgToolbar.setVisibility(View.GONE);
        loginStay = findViewById(R.id.s_login_stay);
        showExitDialog = findViewById(R.id.s_show_exit_dialog);
        btnRemove = findViewById(R.id.btn_delete_account);
        shared = new Shared(this);

        userTbl = shared.getShared().getString(UserItems.USER_TBL, "");
        userMail = shared.getShared().getString(UserItems.USER_MAIL, "");

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        loginStay.setChecked(shared.getShared().getBoolean(UserItems.IS_LOGIN_STAY, false));

        showExitDialog.setChecked(shared.getShared().getBoolean(UserItems.IS_SHOW_EXIT_DIALOG, true));

        dao = VisionDatabase.getVisionDatabase(this).visionDao();
    }
}