package com.cashow.livedatademo;

import android.arch.lifecycle.LiveData;

public class StockLiveData extends LiveData<Long> {
    private StockManager mStockManager;

    private static StockLiveData sInstance;

    private SimplePriceListener mListener = price -> setValue(price);

    public static StockLiveData get() {
        if (sInstance == null) {
            sInstance = new StockLiveData();
        }
        return sInstance;
    }

    private StockLiveData() {
        mStockManager = new StockManager();
    }

    @Override
    protected void onActive() {
        mStockManager.requestPriceUpdates(mListener);
    }

    @Override
    protected void onInactive() {
        mStockManager.removeUpdates();
    }
}
