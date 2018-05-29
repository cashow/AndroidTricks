package com.cashow.aopdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cashow.aspectjannoation.DebugLog;

public class MainActivity extends AppCompatActivity {

    @DebugLog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
