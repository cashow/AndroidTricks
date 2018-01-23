package com.cashow.hencoderdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.cashow.hencoderdemo.chapter_1_1.Chapter_1_1_Activity;
import com.cashow.hencoderdemo.chapter_1_2.Chapter_1_2_Activity;
import com.cashow.hencoderdemo.chapter_1_3.Chapter_1_3_Activity;
import com.cashow.hencoderdemo.chapter_1_4.Chapter_1_4_Activity;
import com.cashow.hencoderdemo.chapter_1_5.Chapter_1_5_Activity;
import com.cashow.hencoderdemo.chapter_1_6.Chapter_1_6_Activity;
import com.cashow.hencoderdemo.chapter_1_7.Chapter_1_7_Activity;
import com.cashow.hencoderdemo.chapter_1_8.Chapter_1_8_Activity;
import com.cashow.hencoderdemo.chapter_1_practice.Chapter_1_Practice_Activity;
import com.cashow.hencoderdemo.chapter_2_1.Chapter_2_1_Activity;
import com.cashow.hencoderdemo.chapter_2_2.Chapter_2_2_Activity;
import com.cashow.hencoderdemo.chapter_2_3.Chapter_2_3_Activity;
import com.cashow.hencoderdemo.chapter_2_practice.Chapter_2_Practice_Activity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        onChapter1PracticeClick(null);
    }

    @OnClick(R.id.button_chapter_1_1)
    void onChapter11Click(Button button) {
        startChapterActivity(Chapter_1_1_Activity.class, button);
    }

    @OnClick(R.id.button_chapter_1_2)
    void onChapter12Click(Button button) {
        startChapterActivity(Chapter_1_2_Activity.class, button);
    }

    @OnClick(R.id.button_chapter_1_3)
    void onChapter13Click(Button button) {
        startChapterActivity(Chapter_1_3_Activity.class, button);
    }

    @OnClick(R.id.button_chapter_1_4)
    void onChapter14Click(Button button) {
        startChapterActivity(Chapter_1_4_Activity.class, button);
    }

    @OnClick(R.id.button_chapter_1_5)
    void onChapter15Click(Button button) {
        startChapterActivity(Chapter_1_5_Activity.class, button);
    }

    @OnClick(R.id.button_chapter_1_6)
    void onChapter16Click(Button button) {
        startChapterActivity(Chapter_1_6_Activity.class, button);
    }

    @OnClick(R.id.button_chapter_1_7)
    void onChapter17Click(Button button) {
        startChapterActivity(Chapter_1_7_Activity.class, button);
    }

    @OnClick(R.id.button_chapter_1_8)
    void onChapter18Click(Button button) {
        startChapterActivity(Chapter_1_8_Activity.class, button);
    }

    @OnClick(R.id.button_chapter_2_1)
    void onChapter21Click(Button button) {
        startChapterActivity(Chapter_2_1_Activity.class, button);
    }

    @OnClick(R.id.button_chapter_2_2)
    void onChapter22Click(Button button) {
        startChapterActivity(Chapter_2_2_Activity.class, button);
    }

    @OnClick(R.id.button_chapter_2_3)
    void onChapter23Click(Button button) {
        startChapterActivity(Chapter_2_3_Activity.class, button);
    }

    @OnClick(R.id.button_chapter_2_practice)
    void onChapter2PracticeClick(Button button) {
        startChapterActivity(Chapter_2_Practice_Activity.class, button);
    }

    @OnClick(R.id.button_chapter_1_practice)
    void onChapter1PracticeClick(Button button) {
        startChapterActivity(Chapter_1_Practice_Activity.class, button);
    }

    private void startChapterActivity(Class clz, Button button) {
        Intent intent = new Intent(this, clz);
        if (button != null) {
            intent.putExtra("title", button.getText().toString());
        }
        startActivity(intent);
    }
}
