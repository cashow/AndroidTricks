package com.cashow.coordinatorlayoutdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_activity_1)
    void onActivity1Click() {
        startActivity(new Intent(this, FirstActivity.class));
    }

    @OnClick(R.id.button_activity_2_1)
    void onActivity21Click() {
        startActivity(new Intent(this, Second1Activity.class));
    }

    @OnClick(R.id.button_activity_2_2)
    void onActivity22Click() {
        startActivity(new Intent(this, Second2Activity.class));
    }

    @OnClick(R.id.button_activity_2_3)
    void onActivity23Click() {
        startActivity(new Intent(this, Second3Activity.class));
    }

    @OnClick(R.id.button_activity_2_4)
    void onActivity24Click() {
        startActivity(new Intent(this, Second4Activity.class));
    }

    @OnClick(R.id.button_activity_3)
    void onActivity3Click() {
        startActivity(new Intent(this, ThirdActivity.class));
    }

    @OnClick(R.id.button_activity_4)
    void onActivity4Click() {
        startActivity(new Intent(this, ForthActivity.class));
    }

    @OnClick(R.id.button_activity_5)
    void onActivity5Click() {
        startActivity(new Intent(this, FifthActivity.class));
    }

    @OnClick(R.id.button_activity_6)
    void onActivity6Click() {
        startActivity(new Intent(this, SixthActivity.class));
    }
}
