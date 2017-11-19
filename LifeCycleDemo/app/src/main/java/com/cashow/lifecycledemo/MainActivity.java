package com.cashow.lifecycledemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private LifecycleObserverDemo lifecycleObserverDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lifecycleObserverDemo = new LifecycleObserverDemo();
        lifecycleObserverDemo.setLifecycle(getLifecycle());

        findViewById(R.id.textview).setOnClickListener(v -> startActivity(new Intent(this, SecondActivity.class)));
    }
}
