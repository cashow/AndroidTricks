package com.cashow.articleeditor.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseActivity extends AppCompatActivity {

    public Context context;
    public BaseActivity mActivity;
    private ProgressHUD mProgressHUD;

    public CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = this;
        context = getApplicationContext();
    }

    public void addSubscription(Subscription subscription) {
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity = this;
        context = getApplicationContext();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideMyDialog();
        mCompositeSubscription.unsubscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        hideMyDialog();
        mProgressHUD = null;
    }

    public void showMyDialog(boolean isCancelable) {
        showMyDialog("", isCancelable);
    }

    public void showMyDialog(String loadingString, boolean isCancelable) {
        if (isFinishing()) {
            return;
        }
        if (mProgressHUD == null || !mProgressHUD.isShowing()) {
            mProgressHUD = ProgressHUD.show(mActivity, loadingString, true, isCancelable, null);
        } else {
            mProgressHUD.setMessage(loadingString);
        }
    }

    public void hideMyDialog() {
        if (isFinishing()) {
            return;
        }
        if (mProgressHUD != null && mProgressHUD.isShowing())
            mProgressHUD.dismiss();
    }
}
