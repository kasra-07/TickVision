package ir.mahchegroup.tickvision.message_box;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.Objects;

import ir.mahchegroup.tickvision.R;
import ir.mahchegroup.tickvision.classes.Shared;
import ir.mahchegroup.tickvision.classes.UserItems;

public class ExitDialog {
    private final Context context;
    private final OnExitDialogCallBack onExitDialogCallBack;
    private Shared shared;

    public ExitDialog(Context context) {
        this.context = context;
        onExitDialogCallBack = (OnExitDialogCallBack) context;
        shared = new Shared(context);
    }

    @SuppressLint("InflateParams")
    public void show() {
        Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.exit_dialog_layout, null);
        Button btnBack = view.findViewById(R.id.btn_back);
        Button btnExit = view.findViewById(R.id.btn_exit);
        CheckBox chShowAgain = view.findViewById(R.id.ch_show_again);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.dialog_anim;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(true);

        chShowAgain.setChecked(!shared.getShared().getBoolean(UserItems.IS_SHOW_EXIT_DIALOG, true));

        btnBack.setOnClickListener(v -> dialog.dismiss());

        btnExit.setOnClickListener(v -> onExitDialogCallBack.onExitDialogListener());

        chShowAgain.setOnCheckedChangeListener((buttonView, isChecked) -> {
            shared.getEditor().putBoolean(UserItems.IS_SHOW_EXIT_DIALOG, !isChecked).apply();
            chShowAgain.setTextColor(isChecked ? context.getColor(R.color.primary_color) : context.getColor(R.color.gray));
            chShowAgain.setButtonTintList(ColorStateList.valueOf(isChecked ? context.getColor(R.color.primary_color) : context.getColor(R.color.gray)));
        });

        dialog.setContentView(view);
        dialog.show();
    }

    public interface OnExitDialogCallBack {
        void onExitDialogListener();
    }
}
