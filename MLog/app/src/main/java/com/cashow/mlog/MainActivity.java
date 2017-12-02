package com.cashow.mlog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private TestObject testObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MLog.d("aaa");

        MLog.d("newtag", "bbbbb");

        MLog.d("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");

        testObject = new TestObject(1, 2, "3", "4");
        MLog.d("testObject", testObject);

        testMethod();

        MLog.d("testException", new RuntimeException("test runtime exception"));
    }

    @Override
    protected void onResume() {
        MLog.d();
        super.onResume();
    }

    @Override
    protected void onPause() {
        MLog.d();
        super.onPause();
    }

    private void testMethod() {
        MLog.t();
        for (int i = 0; i < 500000000; i++) {
        }
        MLog.t();
    }
}
