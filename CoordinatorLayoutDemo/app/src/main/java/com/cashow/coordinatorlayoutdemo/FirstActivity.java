package com.cashow.coordinatorlayoutdemo;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FirstActivity extends AppCompatActivity {
    @BindView(R.id.button_floating)
    View buttonFloating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_floating)
    void onFloatingButtonClick() {
        Snackbar.make(buttonFloating, "Snackbar", Snackbar.LENGTH_SHORT).show();
    }
}
