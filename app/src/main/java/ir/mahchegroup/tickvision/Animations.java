package ir.mahchegroup.tickvision;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class Animations {

    public static void AnimSplash(Context context, View view, boolean isIn) {
        Animation animation;
        if (isIn) {
            animation = AnimationUtils.loadAnimation(context, R.anim.splash_fade_in);
            view.setVisibility(View.VISIBLE);
        } else {
            animation = AnimationUtils.loadAnimation(context, R.anim.splash_fade_out);
            view.setVisibility(View.INVISIBLE);
        }
        view.startAnimation(animation);
    }

    public static void AnimActivity(Context context, Intent intent) {
        ActivityOptions options = ActivityOptions.makeCustomAnimation(context.getApplicationContext(), R.anim.activity_fade_in, R.anim.activity_fade_out);
        context.startActivity(intent, options.toBundle());
        ((Activity)context).finish();
    }
}