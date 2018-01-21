package com.cashow.hencoderdemo.chapter_2_1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.cashow.hencoderdemo.R;

public class SquareImageParentView extends FrameLayout {
    private Context context;
    private ViewGroup.LayoutParams layoutParams;

    public SquareImageParentView(Context context) {
        super(context);
        init(context);
    }

    public SquareImageParentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SquareImageParentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        layoutParams = new ViewGroup.LayoutParams(200, 200);
        setLayoutParams(layoutParams);
        setBackgroundResource(R.drawable.grey_stoke);

        SquareImageView squareImageView = new SquareImageView(context);
        squareImageView.setImageResource(R.drawable.ic_launcher);
        addView(squareImageView);

        post(() -> startAnim());
    }

    private void startAnim() {
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofInt(this, "width", 200, 400);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofInt(this, "height", 200, 400);
        ObjectAnimator objectAnimator3 = ObjectAnimator.ofInt(this, "width", 400, 200);
        ObjectAnimator objectAnimator4 = ObjectAnimator.ofInt(this, "height", 400, 200);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1500);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                postDelayed(() -> startAnim(), 1000);
            }
        });
        animatorSet.playSequentially(objectAnimator1, objectAnimator2, objectAnimator3, objectAnimator4);
        animatorSet.start();
    }

    public void setWidth(int width) {
        getLayoutParams().width = width;
        requestLayout();
    }

    public void setHeight(int height) {
        getLayoutParams().height = height;
        requestLayout();
    }
}
