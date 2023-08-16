package ir.mahchegroup.tickvision;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ir.mahchegroup.tickvision.classes.Shared;
import ir.mahchegroup.tickvision.classes.UserItems;

public class SettingActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView tvToolbar;
    private ImageView imgToolbar;
    private SwitchCompat loginStay, showExitDialog;
    private Button btnRemove;
    private Shared shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        init();

        loginStay.setOnCheckedChangeListener((buttonView, isChecked) -> shared.getEditor().putBoolean(UserItems.IS_LOGIN_STAY, isChecked).apply());

        showExitDialog.setOnCheckedChangeListener((buttonView, isChecked) -> shared.getEditor().putBoolean(UserItems.IS_SHOW_EXIT_DIALOG, isChecked));

        btnRemove.setOnClickListener(v -> {

        });
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        tvToolbar = findViewById(R.id.tv_toolbar);
        imgToolbar = findViewById(R.id.img_toolbar);
        loginStay = findViewById(R.id.s_login_stay);
        showExitDialog = findViewById(R.id.s_show_exit_dialog);
        btnRemove = findViewById(R.id.btn_delete_account);
        shared = new Shared(this);

        loginStay.setChecked(shared.getShared().getBoolean(UserItems.IS_LOGIN_STAY, false));

        showExitDialog.setChecked(shared.getShared().getBoolean(UserItems.IS_SHOW_EXIT_DIALOG, true));
    }
}