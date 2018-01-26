package com.cashow.daggerdemo.demo8;

import android.os.Bundle;
import android.view.View;

import com.cashow.daggerdemo.R;
import com.cashow.daggerdemo.common.BaseActivity;
import com.cashow.daggerdemo.common.model.TestObject4;

import javax.inject.Inject;

import butterknife.OnClick;
import dagger.releasablereferences.ForReleasableReferences;
import dagger.releasablereferences.ReleasableReferenceManager;

public class Demo8Activity extends BaseActivity {
    @Inject
    @ForReleasableReferences(ReleaseScope.class)
    ReleasableReferenceManager releasableReferenceManager;

    Demo8Component demo8Component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buttonRelease.setVisibility(View.VISIBLE);
        buttonRestore.setVisibility(View.VISIBLE);

        init();
    }

    /**
     * Demo8Module 里将 provideTestObject4() 标记成了 @ReleaseScope
     * 在进入页面时，demo8Component 会创建一个 TestObject4 实例，并留有这个实例的引用
     * 这时如果强制 gc，demo8Component 里的 testObject4 不会被回收掉。
     * 调用 releasableReferenceManager.releaseStrongReferences() 后再强制 gc，demo8Component 里的 testObject4 就能被回收了。
     */
    private void init() {
        demo8Component = DaggerDemo8Component.create();
        TestObject4 testObject4 = demo8Component.testObject4();
        demo8Component.inject(this);
    }

    @OnClick(R.id.button_release)
    void onReleaseClick() {
        releasableReferenceManager.releaseStrongReferences();
    }

    @OnClick(R.id.button_restore)
    void onRestoreClick() {
        releasableReferenceManager.restoreStrongReferences();
        TestObject4 testObject4 = demo8Component.testObject4();
    }
}
