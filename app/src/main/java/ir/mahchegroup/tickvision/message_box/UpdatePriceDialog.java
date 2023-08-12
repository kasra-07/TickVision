package ir.mahchegroup.tickvision.message_box;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Objects;

import ir.mahchegroup.tickvision.R;

public class UpdatePriceDialog {
    private Dialog dialog;
    private final Context context;
    private final OnUpdatePriceDialogCallBack onUpdatePriceDialogCallBack;

    public UpdatePriceDialog(Context context) {
        this.context = context;
        onUpdatePriceDialogCallBack = (OnUpdatePriceDialogCallBack) context;
    }

    @SuppressLint("InflateParams")
    public void show(boolean isIncome) {
        dialog = new Dialog(context);
        View view;
        if (isIncome) {
            view = LayoutInflater.from(context).inflate(R.layout.income_dialog_layout, null);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.payment_dialog_layout, null);
        }
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.dialog_anim;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnSave = view.findViewById(R.id.btn_save);
        ImageView btnVoice = view.findViewById(R.id.btn_voice);
        EditText edt = view.findViewById(R.id.edt_price);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnVoice.setOnClickListener(v -> ToastMessage.show(context, context.getString(R.string.coming_soon), false, true));

        btnSave.setOnClickListener(v -> {
            if (edt.length() == 0) {
                ToastMessage.show(context, context.getString(R.string.empty_save_price_field_error), false, true);
            } else {
                int price = Integer.parseInt(edt.getText().toString().trim());
                onUpdatePriceDialogCallBack.onUpdatePriceDialogListener(price);
            }
        });

        dialog.setContentView(view);
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public interface OnUpdatePriceDialogCallBack {
        void onUpdatePriceDialogListener(int price);
    }
}
