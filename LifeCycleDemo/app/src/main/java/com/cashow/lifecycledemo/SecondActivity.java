package com.cashow.lifecycledemo;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity implements LifecycleOwner {
    private LifecycleObserverDemo2 lifecycleObserverDemo;
    private LifecycleRegistry mLifecycleRegistry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);

        lifecycleObserverDemo = new LifecycleObserverDemo2();
        lifecycleObserverDemo.setLifecycle(getLifecycle());
    }

    @Override
    public void onStart() {
        super.onStart();
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }
}
