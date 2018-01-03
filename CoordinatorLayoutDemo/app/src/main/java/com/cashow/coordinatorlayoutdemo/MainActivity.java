package com.cashow.coordinatorlayoutdemo;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cashow.coordinatorlayoutdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.button_floating)
    View buttonFloating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_floating)
    void onFloatingButtonClick() {
        Snackbar.make(buttonFloating, "Snackbar", Snackbar.LENGTH_SHORT).show();
    }
}
