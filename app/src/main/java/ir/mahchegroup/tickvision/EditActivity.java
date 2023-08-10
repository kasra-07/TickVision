package ir.mahchegroup.tickvision;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import ir.mahchegroup.tickvision.classes.Animations;
import ir.mahchegroup.tickvision.classes.Shared;
import ir.mahchegroup.tickvision.classes.UserItems;
import ir.mahchegroup.tickvision.database.ModelVision;
import ir.mahchegroup.tickvision.database.VisionDao;
import ir.mahchegroup.tickvision.database.VisionDatabase;
import ir.mahchegroup.tickvision.network.NetworkReceiver;
import ir.mahchegroup.tickvision.network.RemoveVision;

public class EditActivity extends AppCompatActivity implements RemoveVision.OnRemoveVisionCallBack {
    private TextView tvTitle;
    private TextInputLayout lTitle, lAmount, lDay;
    private TextInputEditText eTitle, eAmount, eDay;
    private ImageView btnDel, btnAllDel, btnEdit;
    private ModelVision model;
    private VisionDao dao;
    private Toolbar toolbar;
    private Shared shared;
    private static String userTbl, title;
    private boolean isEquals;
    private int result = 0;
    private NetworkReceiver receiver;

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
        setContentView(R.layout.activity_edit);

        init();

        btnDel.setOnClickListener(v -> RemoveVisionDialog.show(this));
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
        super.onBackPressed();
        Intent intent = new Intent(EditActivity.this, HomeActivity.class);
        Animations.AnimActivity(this, intent);
    }

    @Override
    public void onRemoveVisionListener() {
        result = dao.removeVision(model);

        if (result == 1) {
            if ((isEquals && dao.getCountVision() == 0) || (dao.getCountVision() == 0)) {
                shared.getEditor().putBoolean(UserItems.IS_FIRST_TIME, true);
                shared.getEditor().putBoolean(UserItems.IS_USER_ADD_VISION, false);
                shared.getEditor().putBoolean(UserItems.IS_USER_SELECT_VISION, false);

            } else if (isEquals) {
                shared.getEditor().putBoolean(UserItems.IS_FIRST_TIME, false);
                shared.getEditor().putBoolean(UserItems.IS_USER_ADD_VISION, true);
                shared.getEditor().putBoolean(UserItems.IS_USER_SELECT_VISION, false);

            }else {
                shared.getEditor().putBoolean(UserItems.IS_FIRST_TIME, false);
                shared.getEditor().putBoolean(UserItems.IS_USER_ADD_VISION, true);
                shared.getEditor().putBoolean(UserItems.IS_USER_SELECT_VISION, true);
            }
            shared.getEditor().apply();
            RemoveVisionDialog.dismiss();
            new Handler().postDelayed(this::onBackPressed, 300);
        }
    }

    private static class RemoveVisionDialog {
        private static Dialog dialog;

        @SuppressLint("InflateParams")
        public static void show(Context context) {
            dialog = new Dialog(context);
            View view = LayoutInflater.from(context).inflate(R.layout.remove_vision_dialog_layout, null);
            Button btnCancel = view.findViewById(R.id.btn_cancel);
            Button btnRemove = view.findViewById(R.id.btn_remove);
            dialog.setCancelable(true);
            Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.dialog_anim;

            btnCancel.setOnClickListener(v -> dialog.dismiss());

            btnRemove.setOnClickListener(v -> {
                RemoveVision removeVision = new RemoveVision(context);
                removeVision.remove(userTbl, title);
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

            dialog.setContentView(view);
            dialog.show();
        }

        public static void dismiss() {
            dialog.dismiss();
        }
    }

    private void init() {
        toolbar = findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        tvTitle = findViewById(R.id.tv_title_edit);
        lTitle = findViewById(R.id.title_layout);
        lAmount = findViewById(R.id.amount_layout);
        lDay = findViewById(R.id.day_layout);
        eTitle = findViewById(R.id.edt_title);
        eAmount = findViewById(R.id.edt_amount);
        eDay = findViewById(R.id.edt_day);
        btnDel = findViewById(R.id.btn_del_vision);
        btnAllDel = findViewById(R.id.btn_del_all_vision);
        btnEdit = findViewById(R.id.btn_edit_vision);

        title = getIntent().getStringExtra(UserItems.TITLE);
        isEquals = getIntent().getBooleanExtra(UserItems.IS_EQUALS_SELECTED_VISION, false);

        dao = VisionDatabase.getVisionDatabase(this).visionDao();
        model = dao.getVision(title);

        tvTitle.setText(model.getTitle());
        eTitle.setText(model.getTitle());
        eAmount.setText(model.getAmount());
        eDay.setText(model.getDay_vision());

        shared = new Shared(this);
        userTbl = shared.getShared().getString(UserItems.USER_TBL, "");
        isEquals = shared.getShared().getBoolean(UserItems.IS_EQUALS_SELECTED_VISION, false);
    }
}