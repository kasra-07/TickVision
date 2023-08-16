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

public class DeleteAccountDialog {
    private final Context context;
    private final OnDeleteAccountDialogCallBack onDeleteAccountDialogCallBack;
    private Dialog dialog;

    public DeleteAccountDialog(Context context) {
        this.context = context;
        onDeleteAccountDialogCallBack = (OnDeleteAccountDialogCallBack) context;
    }

    @SuppressLint("InflateParams")
    public void show() {
        dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.ok_delete_account_dialog_layout, null);
        Button btnBack = view.findViewById(R.id.btn_back);
        Button btnDelete = view.findViewById(R.id.btn_delete);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.dialog_anim;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        btnDelete.setOnClickListener(v -> {
            dialog.dismiss();
            onDeleteAccountDialogCallBack.onDeleteAccountDialogListener();
        });
        btnBack.setOnClickListener(v -> dialog.dismiss());

        dialog.setContentView(view);

        dialog.create();
        dialog.show();
    }

    public interface OnDeleteAccountDialogCallBack {
        void onDeleteAccountDialogListener();
    }
}
