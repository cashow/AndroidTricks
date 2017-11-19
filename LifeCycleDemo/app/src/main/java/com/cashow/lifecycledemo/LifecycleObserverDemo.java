package com.cashow.lifecycledemo;

import android.arch.lifecycle.DefaultLifecycleObserver;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;

public class LifecycleObserverDemo implements DefaultLifecycleObserver {
    private Lifecycle lifecycle;

    public void setLifecycle(Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
        lifecycle.addObserver(this);
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        MLog.d();
        checkState();
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        MLog.d();
        checkState();
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        MLog.d();
        checkState();
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        MLog.d();
        checkState();
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        MLog.d();
        checkState();
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        MLog.d();
        checkState();
    }

    private void checkState() {
        Lifecycle.State state = lifecycle.getCurrentState();
        MLog.d(String.format("lifecycle isAtLeast : (%s : %s), (%s : %s), (%s : %s), (%s : %s), (%s : %s)",
                "RESUMED", state.isAtLeast(Lifecycle.State.RESUMED),
                "STARTED", state.isAtLeast(Lifecycle.State.STARTED),
                "CREATED", state.isAtLeast(Lifecycle.State.CREATED),
                "INITIALIZED", state.isAtLeast(Lifecycle.State.INITIALIZED),
                "DESTROYED", state.isAtLeast(Lifecycle.State.DESTROYED)));
    }
}
