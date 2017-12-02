package com.cashow.livedatademo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {
    private TextView textview;
    private Button buttonSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textview = findViewById(R.id.textview);
        buttonSecond = findViewById(R.id.button_second);

        StockLiveData.get().observe(this, integer -> textview.setText(String.valueOf(integer)));

        buttonSecond.setOnClickListener(v -> startActivity(new Intent(SecondActivity.this, SecondActivity.class)));
    }
}
