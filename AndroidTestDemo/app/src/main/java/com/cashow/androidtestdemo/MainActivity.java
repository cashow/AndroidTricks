package com.cashow.androidtestdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.button_say_hello)
    Button buttonSayHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_say_hello)
    void onSayHelloClick() {
        textView.setText("Hello, " + editText.getText().toString() + "!");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }
}
