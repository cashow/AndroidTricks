package com.cashow.livedatademo;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView textview;
    private Button buttonChangeName;
    private Button buttonSecond;

    private NameViewModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        mModel = ViewModelProviders.of(this).get(NameViewModel.class);
        mModel.getCurrentName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String newName) {
                textview.setText(newName);
            }
        });
    }

    private void initView() {
        textview = findViewById(R.id.textview);
        buttonChangeName = findViewById(R.id.button_change_name);
        buttonSecond = findViewById(R.id.button_second);

        buttonChangeName.setOnClickListener(v -> {
            String newName = "name_" + System.currentTimeMillis() % 1000;
            mModel.getCurrentName().setValue(newName);
        });

        buttonSecond.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SecondActivity.class)));
    }
}
