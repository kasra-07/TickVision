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

public class ClearAllVisionsDialog {
    private final Context context;
    private final OnClearAllVisionsDialogCallBack onClearAllVisionsDialogCallBack;
    private Dialog dialog;

    public ClearAllVisionsDialog(Context context) {
        this.context = context;
        onClearAllVisionsDialogCallBack = (OnClearAllVisionsDialogCallBack) context;
    }

    @SuppressLint("InflateParams")
    public void show() {
        dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.clear_all_visions_dialog_layout, null);
        Button btnBack = view.findViewById(R.id.btn_back);
        Button btnRemove = view.findViewById(R.id.btn_remove_all);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.dialog_anim;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        btnRemove.setOnClickListener(view1 -> onClearAllVisionsDialogCallBack.onClearAllVisionsDialogRemoveListener());
        btnBack.setOnClickListener(view1 -> onClearAllVisionsDialogCallBack.onClearAllVisionsDialogBackListener());

        dialog.setContentView(view);

        dialog.create();
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public interface OnClearAllVisionsDialogCallBack {
        void onClearAllVisionsDialogRemoveListener();
        void onClearAllVisionsDialogBackListener();
    }
}
