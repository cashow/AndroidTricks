package com.cashow.daggerdemo.common;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import com.cashow.daggerdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    @BindView(R.id.textView)
    protected TextView textView;
    @BindView(R.id.button_release)
    protected Button buttonRelease;
    @BindView(R.id.button_restore)
    protected Button buttonRestore;

    protected Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        context = getApplicationContext();

        ButterKnife.bind(this);

        String title = getIntent().getStringExtra("title");
        if (TextUtils.isEmpty(title)) {
            setTitle(getClass().getSimpleName());
        } else {
            setTitle(title);
        }
    }
}
