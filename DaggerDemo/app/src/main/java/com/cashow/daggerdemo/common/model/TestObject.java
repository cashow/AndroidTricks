package com.cashow.daggerdemo.common.model;

import com.cashow.daggerdemo.common.MLog;

import javax.inject.Inject;

public class TestObject {
    private String name;
    private int value;

    /**
     * 给构造函数添加 `@Inject` 注解后，当你需要这个类的新的实例时，Dagger 会获取到构造函数里需要的值并调用构造函数生成一个新的实例。
     */
    @Inject
    public TestObject() {
        MLog.d();
        this.name = "name_1";
        this.value = 123;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("name = %s\nvalue = %d", name, value);
    }
}
