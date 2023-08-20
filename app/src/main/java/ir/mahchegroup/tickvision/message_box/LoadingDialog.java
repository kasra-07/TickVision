package ir.mahchegroup.tickvision.message_box;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

import ir.mahchegroup.tickvision.R;

public class LoadingDialog {
    private final Context context;
    private Dialog dialog;

    public LoadingDialog(Context context) {
        this.context = context;
    }

    @SuppressLint("InflateParams")
    public void show(String txt) {
        dialog = new Dialog(context);
        View view = Objects.requireNonNull(LayoutInflater.from(context)).inflate(R.layout.loading_dialog_layout, null);
        TextView tv = view.findViewById(R.id.tv_loading);
        tv.setText(txt);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.dialog_anim;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(false);
        dialog.setContentView(view);
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public boolean isShow() {
        return dialog.isShowing();
    }
}
