package com.cashow.canceldisposable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.cashow.canceldisposableprocessor.DisposableList;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@DisposableList
public class MainActivity extends AppCompatActivity {
    Disposable disposable1;
    Disposable disposable2;
    public Disposable disposable3;

    private TextView text1;
    private TextView text2;

    public static final int NUMBER_1 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text1 = findViewById(R.id.text_1);
        text2 = findViewById(R.id.text_2);

        disposable1 = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(a -> text1.setText("time_1 : " + a));

        disposable2 = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(a -> text2.setText("time_2 : " + a));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainActivityDisposableList.cancelAll(this);
    }
}
