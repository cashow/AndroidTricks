package com.cashow.daggerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.cashow.daggerdemo.demo1.Demo1Activity;
import com.cashow.daggerdemo.demo2.Demo2Activity;
import com.cashow.daggerdemo.demo3.Demo3Activity;
import com.cashow.daggerdemo.demo4.Demo4Activity;
import com.cashow.daggerdemo.demo5.Demo5Activity;
import com.cashow.daggerdemo.demo6.Demo6Activity;
import com.cashow.daggerdemo.demo7.Demo7Activity;
import com.cashow.daggerdemo.demo8.Demo8Activity;
import com.cashow.daggerdemo.demo9.Demo9Activity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_1)
    void onButton1Click(Button button) {
        startChapterActivity(Demo1Activity.class, button);
    }

    @OnClick(R.id.button_2)
    void onButton2Click(Button button) {
        startChapterActivity(Demo2Activity.class, button);
    }

    @OnClick(R.id.button_3)
    void onButton3Click(Button button) {
        startChapterActivity(Demo3Activity.class, button);
    }

    @OnClick(R.id.button_4)
    void onButton4Click(Button button) {
        startChapterActivity(Demo4Activity.class, button);
    }

    @OnClick(R.id.button_5)
    void onButton5Click(Button button) {
        startChapterActivity(Demo5Activity.class, button);
    }

    @OnClick(R.id.button_6)
    void onButton6Click(Button button) {
        startChapterActivity(Demo6Activity.class, button);
    }

    @OnClick(R.id.button_7)
    void onButton7Click(Button button) {
        startChapterActivity(Demo7Activity.class, button);
    }

    @OnClick(R.id.button_8)
    void onButton8Click(Button button) {
        startChapterActivity(Demo8Activity.class, button);
    }

    @OnClick(R.id.button_9)
    void onButton9Click(Button button) {
        startChapterActivity(Demo9Activity.class, button);
    }

    private void startChapterActivity(Class clz, Button button) {
        Intent intent = new Intent(this, clz);
        if (button != null) {
            intent.putExtra("title", button.getText().toString());
        }
        startActivity(intent);
    }
}
