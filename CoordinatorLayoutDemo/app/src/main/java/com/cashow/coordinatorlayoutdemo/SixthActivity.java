package com.cashow.coordinatorlayoutdemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SixthActivity extends AppCompatActivity {

    @BindView(R.id.button_dependency)
    View buttonDependency;

    private int lastX;
    private int lastY;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sixth);

        ButterKnife.bind(this);

        buttonDependency.setOnTouchListener((v, event) -> {
            int x = (int) event.getRawX();
            int y = (int) event.getRawY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE: {
                    CoordinatorLayout.MarginLayoutParams layoutParams = (CoordinatorLayout.MarginLayoutParams) buttonDependency.getLayoutParams();
                    int left = layoutParams.leftMargin + x - lastX;
                    int top = layoutParams.topMargin + y - lastY;

                    layoutParams.leftMargin = left;
                    layoutParams.topMargin = top;
                    buttonDependency.setLayoutParams(layoutParams);
                    buttonDependency.requestLayout();
                    break;
                }
            }
            lastX = x;
            lastY = y;
            return true;
        });
    }
}
