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

public class UpdateAllVisionsDialog {
    private final OnUpdateAllVisionsDialogCallBack onUpdateAllVisionsDialogCallBack;
    private final Context context;
    private Dialog dialog;

    public UpdateAllVisionsDialog(Context context) {
        this.context = context;
        onUpdateAllVisionsDialogCallBack = (OnUpdateAllVisionsDialogCallBack) context;

    }

    @SuppressLint("InflateParams")
    public void show() {
        dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.update_all_visions_dialog_layout, null);
        Button btnUpdate = view.findViewById(R.id.btn_update);
        Button btnRemove = view.findViewById(R.id.btn_remove);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.dialog_anim;

        btnUpdate.setOnClickListener(view1 -> onUpdateAllVisionsDialogCallBack.onUpdateAllVisionsDialogUpdateListener());

        btnRemove.setOnClickListener(view1 -> onUpdateAllVisionsDialogCallBack.onUpdateAllVisionsDialogRemoveListener());

        dialog.setContentView(view);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        dialog.create();
        dialog.show();
    }


    public void dismiss() {
        dialog.dismiss();
    }

    public interface OnUpdateAllVisionsDialogCallBack {
        void onUpdateAllVisionsDialogUpdateListener();
        void onUpdateAllVisionsDialogRemoveListener();
    }
}
