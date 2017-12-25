package com.cashow.databindingdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class NavigatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigator);

        findViewById(R.id.text_first).setOnClickListener(v -> toActivity(FirstActivity.class));
        findViewById(R.id.text_second).setOnClickListener(v -> toActivity(SecondActivity.class));
        findViewById(R.id.text_third).setOnClickListener(v -> toActivity(ThirdActivity.class));
        findViewById(R.id.text_forth).setOnClickListener(v -> toActivity(ForthActivity.class));
    }

    private void toActivity(Class clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }
}
