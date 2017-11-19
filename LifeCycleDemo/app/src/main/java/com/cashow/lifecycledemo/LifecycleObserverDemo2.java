package com.cashow.lifecycledemo;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

public class LifecycleObserverDemo2 implements LifecycleObserver {
    private Lifecycle lifecycle;

    public void setLifecycle(Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
        lifecycle.addObserver(this);
    }

    // java 7需要使用注解的方式实现lifecycle的回调
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void create() {
        MLog.d();
        checkState();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void start() {
        MLog.d();
        checkState();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void resume() {
        MLog.d();
        checkState();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void pause() {
        MLog.d();
        checkState();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void stop() {
        MLog.d();
        checkState();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void destroy() {
        MLog.d();
        checkState();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    void any() {
        MLog.d();
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
