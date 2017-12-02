package com.cashow.livedatademo;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

class StockManager {
    private Disposable disposable;

    public void requestPriceUpdates(final SimplePriceListener mListener) {
        disposable = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(str -> System.currentTimeMillis() % 10000)
                .subscribe(value -> mListener.onPriceChanged(value));
    }

    public void removeUpdates() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
