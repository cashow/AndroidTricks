package com.cashow.canceldisposable.lalala;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.cashow.canceldisposable.R;
import com.cashow.canceldisposableprocessor.DisposableList;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@DisposableList
public class SecondActivity extends AppCompatActivity {
    Disposable disposable3;
    Disposable disposable4;

    TextView text3;
    TextView text4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text3 = findViewById(R.id.text_1);
        text4 = findViewById(R.id.text_2);

        disposable3 = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(a -> text3.setText("time_1 : " + a));

        disposable4 = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(a -> text4.setText("time_2 : " + a));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SecondActivityDisposableList.cancelAll(this);
    }
}
