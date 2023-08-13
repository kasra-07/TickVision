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
    private static Dialog dialog;
    @SuppressLint("InflateParams")
    public static void show(Context context, String txt) {
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

    public static void dismiss() {
        dialog.dismiss();
    }

    public static boolean isShow() {
        return dialog.isShowing();
    }
}
