package ir.mahchegroup.tickvision;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import java.util.List;

import ir.mahchegroup.tickvision.classes.Animations;
import ir.mahchegroup.tickvision.classes.Shared;
import ir.mahchegroup.tickvision.classes.UserItems;
import ir.mahchegroup.tickvision.database.ModelVision;
import ir.mahchegroup.tickvision.network.ResetAllVisions;

public class UpdateErrorActivity extends AppCompatActivity implements ResetAllVisions.OnResetAllVisionsCallBack {
    private String userTbl, diff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_error);

        Shared shared = new Shared(this);

        userTbl = shared.getShared().getString(UserItems.USER_TBL, "");
        diff = shared.getShared().getString(UserItems.DIFF, "1");
    }

    public void click(View view) {
        ImageView img = findViewById(R.id.img_reset);
        RotateAnimation rot = new RotateAnimation(0f, -360f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        rot.setDuration(800);
        rot.setRepeatCount(Animation.INFINITE);
        rot.setRepeatMode(Animation.RESTART);
        rot.setStartOffset(300);
        img.startAnimation(rot);
        view.setEnabled(false);

        ResetAllVisions resetAllVisions = new ResetAllVisions(this);
        resetAllVisions.reset(userTbl, diff);
    }

    @Override
    public void onResetAllVisionsListener(List<ModelVision> resetAllList) {

    }
}