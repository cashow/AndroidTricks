package com.cashow.photobrush.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

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

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void addSubscription(Subscription subscription) {
        mCompositeSubscription.add(subscription);
    }

    public void setFullScreen() {
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window myWindow = this.getWindow();
        myWindow.setFlags(flag, flag);
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

    public void showMyDialog() {
        if (isFinishing()) {
            return;
        }
        if (mProgressHUD == null || !mProgressHUD.isShowing()) {
            mProgressHUD = ProgressHUD.show(mActivity, "", true, true, null);
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
