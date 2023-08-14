package ir.mahchegroup.tickvision.message_box;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Objects;

import ir.mahchegroup.tickvision.R;
import ir.mahchegroup.tickvision.classes.KeyboardManager;

public class AddVisionDialog {
    private final Context context;
    private Dialog dialog;
    private final OnAddVisionDialogCallBack onAddVisionDialogCallBack;
    private int level = 0;
    private String title, amount, day;

    public AddVisionDialog(Context context) {
        this.context = context;
        onAddVisionDialogCallBack = (OnAddVisionDialogCallBack) context;
    }

    @SuppressLint("InflateParams")
    public void show() {
        dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.add_vision_dialog_layout, null);
        EditText edtTitle = view.findViewById(R.id.edt_title_vision);
        EditText edtAmount = view.findViewById(R.id.edt_amount_vision);
        EditText edtDay = view.findViewById(R.id.edt_day_vision);
        Button btnSave = view.findViewById(R.id.btn_save);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.dialog_anim;
        dialog.setContentView(view);

        new Handler().postDelayed(() -> {
            edtTitle.requestFocus();
            KeyboardManager.showKeyboard(dialog.getContext(), edtTitle);
        }, 400);

        btnCancel.setOnClickListener(view1 -> {
            level = 0;
            KeyboardManager.hideKeyboardOnDialog(dialog, context);
            new Handler().postDelayed(onAddVisionDialogCallBack::onAddVisionDialogCancelListener, 200);
        });

        btnSave.setOnClickListener(view1 -> {
            if (level == 0) {
                title = edtTitle.getText().toString().trim();

                if (title.isEmpty()) {
                    ToastMessage.show(dialog.getContext(), context.getString(R.string.add_vision_title_error), false, false);
                } else {
                    level++;
                    edtAmount.setVisibility(View.VISIBLE);
                    edtAmount.requestFocus();
                }

            } else if (level == 1) {
                amount = edtAmount.getText().toString().trim();

                if (amount.isEmpty()) {
                    ToastMessage.show(dialog.getContext(), context.getString(R.string.add_vision_amount_error), false, false);
                } else {
                    level++;
                    btnSave.setText(context.getString(R.string.save_vision_text));
                    edtDay.setVisibility(View.VISIBLE);
                    edtDay.requestFocus();
                }
            } else {
                day = edtDay.getText().toString().trim();

                if (day.isEmpty()) {
                    ToastMessage.show(dialog.getContext(), context.getString(R.string.add_vision_day_error), false, false);
                } else {
                    title = edtTitle.getText().toString().trim();
                    amount = edtAmount.getText().toString().trim();
                    day = edtDay.getText().toString().trim();

                    if (!title.isEmpty() && !amount.isEmpty() && !day.isEmpty()) {
                        KeyboardManager.hideKeyboardOnDialog(dialog, context);
                        new Handler().postDelayed(() -> {
                            onAddVisionDialogCallBack.onAddVisionDialogSaveListener(title, amount, day);
                            level = 0;
                        }, 200);
                    } else {
                        ToastMessage.show(dialog.getContext(), context.getString(R.string.add_vision_all_field_error), false, false);
                    }
                }
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        dialog.create();
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public interface OnAddVisionDialogCallBack {
        void onAddVisionDialogSaveListener(String title, String amount, String day);

        void onAddVisionDialogCancelListener();
    }
}
