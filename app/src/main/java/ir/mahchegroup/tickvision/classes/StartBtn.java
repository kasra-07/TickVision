package ir.mahchegroup.tickvision.classes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;

import ir.mahchegroup.tickvision.R;

public class StartBtn extends RelativeLayout {
    private View view;
    private Button btn;

    public StartBtn(Context context) {
        super(context);
        init(context);
    }

    public StartBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StartBtn(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public StartBtn(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.start_btn_layout, this, true);
        view = root.findViewById(R.id.view_start_btn);
        btn = root.findViewById(R.id.button_start_btn);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1.2f, 1f, 1.2f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
            AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
            scaleAnimation.setDuration(600);
            scaleAnimation.setRepeatCount(Animation.INFINITE);
            scaleAnimation.setRepeatMode(Animation.RESTART);
            alphaAnimation.setDuration(600);
            alphaAnimation.setRepeatCount(Animation.INFINITE);
            alphaAnimation.setRepeatMode(Animation.RESTART);
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(scaleAnimation);
            animationSet.addAnimation(alphaAnimation);
            view.setAnimation(animationSet);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = getResources().getDimensionPixelSize(R.dimen.start_btn_size);
        super.onMeasure(MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY));
    }

    public void setOnClickStartListener(OnClickListener onClickStartListener) {
        btn.setOnClickListener(onClickStartListener);
    }
}
