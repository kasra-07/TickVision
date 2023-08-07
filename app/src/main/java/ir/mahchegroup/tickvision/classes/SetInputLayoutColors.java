package ir.mahchegroup.tickvision.classes;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import ir.mahchegroup.tickvision.R;

public class SetInputLayoutColors {

    public static void setColor(Context context, TextInputLayout l, TextInputEditText e, String error, int flag) {
        switch (flag) {
            case 0: {
                l.setErrorEnabled(false);
                l.setCounterTextColor(ColorStateList.valueOf(context.getColor(R.color.gray)));
                l.setStartIconTintList(ColorStateList.valueOf(context.getColor(R.color.gray)));
                l.setDefaultHintTextColor(ColorStateList.valueOf(context.getColor(R.color.gray)));
                if (l.getEndIconDrawable() != null) {
                    l.setEndIconTintList(ColorStateList.valueOf(context.getColor(R.color.gray)));
                }
                e.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.gray)));
                break;
            }

            case 1: {
                l.setErrorEnabled(false);
                l.setCounterTextColor(ColorStateList.valueOf(context.getColor(R.color.primary_color)));
                l.setStartIconTintList(ColorStateList.valueOf(context.getColor(R.color.primary_color)));
                l.setDefaultHintTextColor(ColorStateList.valueOf(context.getColor(R.color.primary_color)));
                if (l.getEndIconDrawable() != null) {
                    l.setEndIconTintList(ColorStateList.valueOf(context.getColor(R.color.primary_color)));
                }
                e.setTextColor(context.getColor(R.color.primary_color));
                e.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.primary_color)));
                break;
            }

            case 2: {
                l.setErrorEnabled(true);
                l.setError(error);
                l.setCounterTextColor(ColorStateList.valueOf(context.getColor(R.color.red)));
                l.setStartIconTintList(ColorStateList.valueOf(context.getColor(R.color.red)));
                l.setDefaultHintTextColor(ColorStateList.valueOf(context.getColor(R.color.red)));
                if (l.getEndIconDrawable() != null) {
                    l.setEndIconTintList(ColorStateList.valueOf(context.getColor(R.color.red)));
                }
                e.setTextColor(context.getColor(R.color.red));
                e.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.red)));
                break;
            }
        }
    }

}
