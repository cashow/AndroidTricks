package com.cashow.hencoderdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cashow.hencoderdemo.chapter_1.Chapter1Activity;
import com.cashow.hencoderdemo.chapter_2.Chapter2Activity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        onChapter2Click();
    }

    @OnClick(R.id.button_chapter_1)
    void onChapter1Click() {
        startActivity(new Intent(this, Chapter1Activity.class));
    }

    @OnClick(R.id.button_chapter_2)
    void onChapter2Click() {
        startActivity(new Intent(this, Chapter2Activity.class));
    }
}
