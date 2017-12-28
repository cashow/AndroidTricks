package com.cashow.constraintlayoutdemo;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.view.animation.OvershootInterpolator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ThirdActivity extends AppCompatActivity {
    @BindView(R.id.root)
    ConstraintLayout root;

    private boolean isContentAnimated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.root)
    void onRootClick() {
        if (isContentAnimated) {
            updateConstraints(R.layout.activity_third);
        } else {
            updateConstraints(R.layout.activity_third_in);
        }
        isContentAnimated = !isContentAnimated;
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
