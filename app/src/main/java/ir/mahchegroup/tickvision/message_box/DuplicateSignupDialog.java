package ir.mahchegroup.tickvision.message_box;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

import ir.mahchegroup.tickvision.R;
import ir.mahchegroup.tickvision.SettingActivity;
import ir.mahchegroup.tickvision.classes.Animations;
import ir.mahchegroup.tickvision.classes.Shared;
import ir.mahchegroup.tickvision.classes.UserItems;

public class DuplicateSignupDialog {
    private final Shared shared;

    public DuplicateSignupDialog(Shared shared) {
        this.shared = shared;
    }

    @SuppressLint("InflateParams")
    public void show(Context context, Activity activity) {
        Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.delete_acount_dialog_layout, null);
        Button btnCancel = view.findViewById(R.id.btn_back);
        Button btnSetting = view.findViewById(R.id.btn_setting);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.dialog_anim;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        shared.getEditor().putBoolean(UserItems.IS_FROM_SIGNUP, true).apply();

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnSetting.setOnClickListener(v -> {
            dialog.dismiss();
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(activity, SettingActivity.class);
                Animations.AnimActivity(context, intent);
            }, 300);
        });

        dialog.setContentView(view);
        dialog.show();
    }
}
