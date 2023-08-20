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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import ir.mahchegroup.tickvision.classes.Animations;
import ir.mahchegroup.tickvision.classes.KeyboardManager;
import ir.mahchegroup.tickvision.classes.SetInputLayoutColors;
import ir.mahchegroup.tickvision.classes.Shared;
import ir.mahchegroup.tickvision.classes.UserItems;
import ir.mahchegroup.tickvision.database.ModelVision;
import ir.mahchegroup.tickvision.database.VisionDao;
import ir.mahchegroup.tickvision.database.VisionDatabase;
import ir.mahchegroup.tickvision.date.ChangeDate;
import ir.mahchegroup.tickvision.message_box.ToastMessage;
import ir.mahchegroup.tickvision.network.ClearAllVisions;
import ir.mahchegroup.tickvision.network.EditVision;
import ir.mahchegroup.tickvision.network.NetworkReceiver;
import ir.mahchegroup.tickvision.network.RemoveVision;

public class EditActivity extends AppCompatActivity implements RemoveVision.OnRemoveVisionCallBack, ClearAllVisions.OnClearAllVisionsCallBack, EditVision.OnEditVisionCallBack {
    private RelativeLayout root;
    private TextInputLayout lTitle, lAmount, lDay;
    private TextInputEditText eTitle, eAmount, eDay;
    private ImageView btnDel, btnAllDel, btnEdit;
    private ModelVision model;
    private VisionDao dao;
    private Shared shared;
    private String userTbl, title;
    private boolean isEquals;
    private int mode, dayAmount;
    private NetworkReceiver receiver;
    private String titleVision, amountVision, dayVision, dateVision;
    private ThisDialog thisDialog;

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

        root.setOnClickListener(v -> {
            KeyboardManager.hideKeyboardOnActivity(this, this);
            setInputLayoutGray();
        });

        thisDialog = new ThisDialog(this);

        btnDel.setOnClickListener(v -> {
            mode = 0;
            thisDialog.show();
        });

        btnAllDel.setOnClickListener(v -> {
            mode = 1;
            thisDialog.show();
        });

        btnEdit.setOnClickListener(v -> {
            if (eTitle.length() == 0 || eAmount.length() == 0 || eDay.length() == 0) {
                if (eTitle.length() == 0) {
                    SetInputLayoutColors.setColor(this, lTitle, eTitle, getString(R.string.empty_field_error), 2);
                } else {
                    SetInputLayoutColors.setColor(this, lTitle, eTitle, null, 0);
                    eTitle.setTextColor(getColor(R.color.green));
                }

                if (eAmount.length() == 0) {
                    SetInputLayoutColors.setColor(this, lAmount, eAmount, getString(R.string.empty_field_error), 2);
                } else {
                    SetInputLayoutColors.setColor(this, lAmount, eAmount, null, 0);
                    eAmount.setTextColor(getColor(R.color.green));
                }

                if (eDay.length() == 0) {
                    SetInputLayoutColors.setColor(this, lDay, eDay, getString(R.string.empty_field_error), 2);
                } else {
                    SetInputLayoutColors.setColor(this, lDay, eDay, null, 0);
                    eDay.setTextColor(getColor(R.color.green));
                }
            } else {
                mode = 2;
                thisDialog.show();
            }
        });

        setOnFocus();

        setOnTextChange();
    }

    private void setInputLayoutGray() {
        SetInputLayoutColors.setColor(this, lTitle, eTitle, null, 0);
        SetInputLayoutColors.setColor(this, lAmount, eAmount, null, 0);
        SetInputLayoutColors.setColor(this, lDay, eDay, null, 0);
        setTextInputTextColorGreen();
    }

    private void setOnTextChange() {
        eTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    SetInputLayoutColors.setColor(EditActivity.this, lTitle, eTitle, null, 1);
                } else {
                    SetInputLayoutColors.setColor(EditActivity.this, lTitle, eTitle, getString(R.string.empty_field_error), 2);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        eAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    SetInputLayoutColors.setColor(EditActivity.this, lAmount, eAmount, null, 1);
                } else {
                    SetInputLayoutColors.setColor(EditActivity.this, lAmount, eAmount, getString(R.string.empty_field_error), 2);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        eDay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    SetInputLayoutColors.setColor(EditActivity.this, lDay, eDay, null, 1);
                } else {
                    SetInputLayoutColors.setColor(EditActivity.this, lDay, eDay, getString(R.string.empty_field_error), 2);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setOnFocus() {
        eTitle.setOnFocusChangeListener((v, b) -> {
            if (b) {
                SetInputLayoutColors.setColor(this, lTitle, eTitle, null, 1);
                SetInputLayoutColors.setColor(this, lAmount, eAmount, null, 0);
                SetInputLayoutColors.setColor(this, lDay, eDay, null, 0);
            } else {
                SetInputLayoutColors.setColor(this, lTitle, eTitle, null, 0);
                setTextInputTextColorGreen();
            }
        });
        eAmount.setOnFocusChangeListener((v, b) -> {
            if (b) {
                SetInputLayoutColors.setColor(this, lAmount, eAmount, null, 1);
                SetInputLayoutColors.setColor(this, lTitle, eTitle, null, 0);
                SetInputLayoutColors.setColor(this, lDay, eDay, null, 0);
            } else {
                SetInputLayoutColors.setColor(this, lAmount, eAmount, null, 0);
                setTextInputTextColorGreen();
            }
        });
        eDay.setOnFocusChangeListener((v, b) -> {
            if (b) {
                SetInputLayoutColors.setColor(this, lDay, eDay, null, 1);
                SetInputLayoutColors.setColor(this, lAmount, eAmount, null, 0);
                SetInputLayoutColors.setColor(this, lTitle, eTitle, null, 0);
            } else {
                SetInputLayoutColors.setColor(this, lDay, eDay, null, 0);
                setTextInputTextColorGreen();
            }
        });
    }

    private void setTextInputTextColorGreen() {
        eTitle.setTextColor(getColor(R.color.green));
        eAmount.setTextColor(getColor(R.color.green));
        eDay.setTextColor(getColor(R.color.green));
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
        int result = dao.removeVision(model);

        if (result == 1) {
            shared.getEditor().putBoolean(UserItems.IS_LOGIN_STAY, false);

            if ((isEquals && dao.getCountVision() == 0) || (dao.getCountVision() == 0)) {
                shared.getEditor().putBoolean(UserItems.IS_FIRST_TIME, true);
                shared.getEditor().putBoolean(UserItems.IS_USER_ADD_VISION, false);
                shared.getEditor().putBoolean(UserItems.IS_USER_SELECT_VISION, false);

            } else if (isEquals) {
                shared.getEditor().putBoolean(UserItems.IS_FIRST_TIME, false);
                shared.getEditor().putBoolean(UserItems.IS_USER_ADD_VISION, true);
                shared.getEditor().putBoolean(UserItems.IS_USER_SELECT_VISION, false);

            } else {
                shared.getEditor().putBoolean(UserItems.IS_FIRST_TIME, false);
                shared.getEditor().putBoolean(UserItems.IS_USER_ADD_VISION, true);
                shared.getEditor().putBoolean(UserItems.IS_USER_SELECT_VISION, true);
            }
            shared.getEditor().apply();
            thisDialog.dismiss();
            new Handler().postDelayed(this::onBackPressed, 300);
        }
    }

    @Override
    public void onClearAllVisionsListener(String isClearAllVisions) {
        if (isClearAllVisions.equals("success")) {
            dao.clearAllVisions();

            shared.getEditor().putBoolean(UserItems.IS_FIRST_TIME, true);
            shared.getEditor().putBoolean(UserItems.IS_USER_ADD_VISION, false);
            shared.getEditor().putBoolean(UserItems.IS_USER_SELECT_VISION, false);
            shared.getEditor().putBoolean(UserItems.IS_LOGIN_STAY, false);
            shared.getEditor().apply();

            thisDialog.dismiss();

            ToastMessage.show(this, getString(R.string.clear_all_vision_success_text), true, true);

            new Handler().postDelayed(this::onBackPressed, 300);
        }
    }

    @Override
    public void onEditVisionListener(String isEditVision) {
        if (isEditVision.equals("success")) {

            if (isEquals) {
                shared.getEditor().putBoolean(UserItems.IS_USER_SELECT_VISION, false).apply();
            }

            ModelVision editModel = new ModelVision();

            editModel.setId(model.getId());
            editModel.setTitle(titleVision);
            editModel.setDate_vision(dateVision);
            editModel.setAmount(amountVision);
            editModel.setDay_vision(dayVision);
            editModel.setDay_amount(String.valueOf(dayAmount));
            editModel.setDay_pass("0");
            editModel.setDay_rest(dayVision);
            editModel.setIncome_amount("0");
            editModel.setRest_amount(amountVision);
            editModel.setIncome("0");
            editModel.setPayment("0");
            editModel.setProfit("0");
            editModel.setRest(String.valueOf(dayAmount));
            editModel.setMilli_sec("0");
            editModel.setIs_tick("0");

            dao.editVision(editModel);

            ToastMessage.show(this, getString(R.string.edit_vision_success_text), true, true);

            onBackPressed();
        } else {
            ToastMessage.show(this, getString(R.string.edit_vision_success_error), true, true);
        }
    }

    private class ThisDialog {
        private TextView tvTitle, tvText;
        private Dialog dialog;
        private final Context context;

        public ThisDialog(Context context) {
            this.context = context;
        }

        @SuppressLint("InflateParams")
        public void show() {
            dialog = new Dialog(context);
            View view = LayoutInflater.from(context).inflate(R.layout.remove_vision_dialog_layout, null);
            Button btnCancel = view.findViewById(R.id.btn_cancel);
            Button btnOk = view.findViewById(R.id.btn_remove);
            tvTitle = view.findViewById(R.id.tv_title_dialog);
            tvText = view.findViewById(R.id.tv_text_remove_vision_dialog);

            switch (mode) {
                case 0: {
                    setMode(context.getString(R.string.remove_vision_title), context.getString(R.string.remove_vision_text));
                    btnOk.setText(context.getString(R.string.delete_text));
                    break;
                }

                case 1: {
                    setMode(context.getString(R.string.clear_all_visions_title), context.getString(R.string.clear_all_visions_text));
                    btnOk.setText(context.getString(R.string.delete_text));
                    break;
                }

                case 2: {
                    setMode(context.getString(R.string.edit_vision_title), context.getString(R.string.edit_vision_text));
                    btnOk.setText(context.getString(R.string.edit_text));
                    break;
                }
            }

            dialog.setCancelable(true);
            Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.dialog_anim;

            btnCancel.setOnClickListener(v -> dialog.dismiss());

            btnOk.setOnClickListener(v -> {
                if (mode == 0) {
                    RemoveVision removeVision = new RemoveVision(context);
                    removeVision.remove(userTbl, title);
                } else if (mode == 1) {
                    ClearAllVisions clearAllVisions = new ClearAllVisions(context);
                    clearAllVisions.clear(userTbl);
                } else {
                    titleVision = Objects.requireNonNull(eTitle.getText()).toString().trim();
                    amountVision = Objects.requireNonNull(eAmount.getText()).toString().trim();
                    dayVision = Objects.requireNonNull(eDay.getText()).toString().trim();
                    dateVision = ChangeDate.getCurrentDay() + "/" + ChangeDate.getCurrentMonth() + "/" + ChangeDate.getCurrentYear();

                    dayAmount = Integer.parseInt(amountVision) / Integer.parseInt(dayVision);

                    EditVision editVision = new EditVision(context);
                    editVision.edit(userTbl, title, titleVision, dateVision, amountVision, dayVision, String.valueOf(dayAmount));
                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

            dialog.setContentView(view);
            dialog.show();
        }

        public void dismiss() {
            dialog.dismiss();
        }

        private void setMode(String title, String text) {
            tvTitle.setText(title);
            tvText.setText(text);
        }
    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        root = findViewById(R.id.edit_root);
        TextView tvTitle = findViewById(R.id.tv_title_edit);
        lTitle = findViewById(R.id.title_layout);
        lAmount = findViewById(R.id.amount_layout);
        lDay = findViewById(R.id.day_layout);
        eTitle = findViewById(R.id.edt_title);
        eAmount = findViewById(R.id.edt_amount);
        eDay = findViewById(R.id.edt_day);
        btnDel = findViewById(R.id.btn_del_vision);
        btnAllDel = findViewById(R.id.btn_del_all_vision);
        btnEdit = findViewById(R.id.btn_edit_vision);
        shared = new Shared(this);

        title = shared.getShared().getString(UserItems.TITLE_SELECTED_VISION, "");
        isEquals = getIntent().getBooleanExtra(UserItems.IS_EQUALS_SELECTED_VISION, false);

        dao = VisionDatabase.getVisionDatabase(this).visionDao();
        model = dao.getVision(title);

        tvTitle.setText(model.getTitle());
        eTitle.setText(model.getTitle());
        eAmount.setText(model.getAmount());
        eDay.setText(model.getDay_vision());

        userTbl = shared.getShared().getString(UserItems.USER_TBL, "");
        isEquals = shared.getShared().getBoolean(UserItems.IS_EQUALS_SELECTED_VISION, false);
    }
}