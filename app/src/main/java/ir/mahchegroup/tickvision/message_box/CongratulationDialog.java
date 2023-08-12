package ir.mahchegroup.tickvision.message_box;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import java.util.Objects;

import ir.mahchegroup.tickvision.R;

public class CongratulationDialog {

    @SuppressLint("InflateParams")
    public static void show(Context context) {
        Dialog dialog  = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.congratulation_layout, null);
        Button btnOk = view.findViewById(R.id.btn_ok);

        btnOk.setOnClickListener(v -> dialog.dismiss());

        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.cong_anim;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(view);
        dialog.show();
    }
}
