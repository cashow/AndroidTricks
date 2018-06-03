package com.cashow.aopdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cashow.aspectjannoation.DebugLog;
import com.cashow.aspectjannoation.DebugTrace;

public class MainActivity extends AppCompatActivity {

    @DebugLog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        test();
    }

    @DebugTrace
    private void test() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
