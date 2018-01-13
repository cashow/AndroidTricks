package com.cashow.hencoderdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cashow.hencoderdemo.chapter_1_1.Chapter_1_1_Activity;
import com.cashow.hencoderdemo.chapter_1_2.Chapter_1_2_Activity;
import com.cashow.hencoderdemo.chapter_1_3.Chapter_1_3_Activity;
import com.cashow.hencoderdemo.chapter_1_4.Chapter_1_4_Activity;
import com.cashow.hencoderdemo.chapter_1_5.Chapter_1_5_Activity;
import com.cashow.hencoderdemo.chapter_1_6.Chapter_1_6_Activity;
import com.cashow.hencoderdemo.chapter_1_7.Chapter_1_7_Activity;
import com.cashow.hencoderdemo.chapter_1_8.Chapter_1_8_Activity;

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
        startActivity(new Intent(this, Chapter_1_1_Activity.class));
    }

    @OnClick(R.id.button_chapter_2)
    void onChapter2Click() {
        startActivity(new Intent(this, Chapter_1_2_Activity.class));
    }

    @OnClick(R.id.button_chapter_3)
    void onChapter3Click() {
        startActivity(new Intent(this, Chapter_1_3_Activity.class));
    }

    @OnClick(R.id.button_chapter_4)
    void onChapter4Click() {
        startActivity(new Intent(this, Chapter_1_4_Activity.class));
    }

    @OnClick(R.id.button_chapter_5)
    void onChapter5Click() {
        startActivity(new Intent(this, Chapter_1_5_Activity.class));
    }

    @OnClick(R.id.button_chapter_6)
    void onChapter6Click() {
        startActivity(new Intent(this, Chapter_1_6_Activity.class));
    }

    @OnClick(R.id.button_chapter_7)
    void onChapter7Click() {
        startActivity(new Intent(this, Chapter_1_7_Activity.class));
    }

    @OnClick(R.id.button_chapter_8)
    void onChapter8Click() {
        startActivity(new Intent(this, Chapter_1_8_Activity.class));
    }
}
