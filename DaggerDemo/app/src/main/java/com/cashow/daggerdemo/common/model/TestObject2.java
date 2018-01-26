package com.cashow.daggerdemo.common.model;

import com.cashow.daggerdemo.common.MLog;

public class TestObject2 {
    private String name;
    private int value;

    public TestObject2(String name, int value) {
        MLog.d();
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("name = %s\nvalue = %d", name, value);
    }
}
