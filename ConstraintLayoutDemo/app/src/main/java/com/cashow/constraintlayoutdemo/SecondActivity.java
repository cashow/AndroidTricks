package com.cashow.constraintlayoutdemo;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SecondActivity extends AppCompatActivity {
    @BindView(R.id.left)
    View left;
    @BindView(R.id.middle)
    View middle;
    @BindView(R.id.right)
    View right;
    @BindView(R.id.root)
    ConstraintLayout root;

    private View selectedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.left)
    void onLeftClick() {
        if (selectedView != left) {
            updateConstraints(R.layout.activity_second_left);
            selectedView = left;
        } else {
            toDefault();
        }
    }

    @OnClick(R.id.middle)
    void onMiddleClick() {
        if (selectedView != middle) {
            updateConstraints(R.layout.activity_second_middle);
            selectedView = middle;
        } else {
            toDefault();
        }
    }

    @OnClick(R.id.right)
    void onRightClick() {
        if (selectedView != right) {
            updateConstraints(R.layout.activity_second_right);
            selectedView = right;
        } else {
            toDefault();
        }
    }

    @OnClick(R.id.root)
    void onRootClick() {
        toDefault();
    }

    private void toDefault() {
        if (selectedView != null) {
            updateConstraints(R.layout.activity_second);
            selectedView = null;
        }
    }

    private void updateConstraints(@LayoutRes int id) {
        ConstraintSet newConstraintSet = new ConstraintSet();
        newConstraintSet.clone(this, id);
        newConstraintSet.applyTo(root);

        ChangeBounds transition = new ChangeBounds();
        transition.setInterpolator(new OvershootInterpolator());
        TransitionManager.beginDelayedTransition(root, transition);
    }
}
