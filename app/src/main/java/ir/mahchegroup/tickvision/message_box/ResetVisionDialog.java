package ir.mahchegroup.tickvision.message_box;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

import ir.mahchegroup.tickvision.R;

public class ResetVisionDialog {
    private final Context context;
    private final OnResetVisionDialogCallBack onResetVisionDialogCallBack;
    private Dialog dialog;

    public ResetVisionDialog(Context context) {
        this.context = context;
        onResetVisionDialogCallBack = (OnResetVisionDialogCallBack) context;
    }

    @SuppressLint("InflateParams")
    public void show() {
        dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.reset_vision_dialog_layout, null);
        Button btnBack = view.findViewById(R.id.btn_back);
        Button btnReset = view.findViewById(R.id.btn_reset);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.dialog_anim;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        btnReset.setOnClickListener(v -> onResetVisionDialogCallBack.onResetVisionDialogResetListener());
        btnBack.setOnClickListener(v -> dialog.dismiss());

        dialog.setContentView(view);

        dialog.create();
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public interface OnResetVisionDialogCallBack {
        void onResetVisionDialogResetListener();
    }
}
