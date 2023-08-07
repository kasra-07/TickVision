package ir.mahchegroup.tickvision.message_box;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ir.mahchegroup.tickvision.R;

public class ToastMessage {

    @SuppressLint({"InflateParams"})
    public static void show(Context context, String text, boolean isOk, boolean isShort) {
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.toast_layout, null, false);
        TextView tv = view.findViewById(R.id.tv_toast);
        ImageView img = view.findViewById(R.id.img_toast);
        if (isShort) {
            toast.setDuration(Toast.LENGTH_SHORT);
        } else {
            toast.setDuration(Toast.LENGTH_LONG);
        }
        toast.setView(view);
        tv.setText(text);
        if (!isOk) {
            tv.setBackgroundResource(R.drawable.error_toast_background);
            img.setImageResource(R.drawable.error);
        } else {
            tv.setBackgroundResource(R.drawable.success_toast_background);
            img.setImageResource(R.drawable.success);
        }
        toast.show();
    }
}
