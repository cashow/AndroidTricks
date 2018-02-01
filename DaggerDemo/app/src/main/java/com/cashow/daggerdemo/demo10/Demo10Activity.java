package com.cashow.daggerdemo.demo10;

import android.os.Bundle;

import com.cashow.daggerdemo.common.BaseActivity;
import com.cashow.daggerdemo.common.model.TestObject2;
import com.cashow.daggerdemo.common.model.TestObject5;

import javax.inject.Inject;

public class Demo10Activity extends BaseActivity {
    @Inject
    TestObject2 testObject2;
    @Inject
    TestObject5 testObject5;

    private Demo10Component demo10Component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init1();
//        init2();
        init3();
    }

    /**
     * 这里的 Demo10Component 暴露出了 TestObject2，Demo10Module 提供了 TestObject2 和 TestObject5
     */
    private void init1() {
        demo10Component = DaggerDemo10Component.create();
//        demo10Component.inject(this);
    }

    /**
     * 这里的 Demo10DependencyComponent 通过 dependencies 指定了对 Demo10Component 的依赖，
     * 这时如果要注入 TestObject5 会报错，因为通过 dependencies 只能获取到 Component 暴露出来的对象
     */
    private void init2() {
//        Demo10DependencyComponent demo10DependencyComponent = DaggerDemo10DependencyComponent.builder().demo10Component(demo10Component).build();
//        demo10DependencyComponent.inject(this);
    }

    /**
     * 这里的 Demo10SubComponent 使用了 @Subcomponent 注解，
     * 并在 Demo10Component 里添加了 Demo10SubComponent demo10SubComponent()
     * 这里的 Demo10SubComponent 可以获取到父类，也就是 Demo10Component 里所有的对象
     */
    private void init3() {
        Demo10SubComponent demo10SubComponent = demo10Component.demo10SubComponent();
        demo10SubComponent.inject(this);

        textView.setText(testObject2.toString() + "\n" + testObject5.toString());
    }
}
