package com.cashow.daggerdemo.common.model;

import javax.inject.Inject;

public class TestObject3 {
    public TestObject testObject;
    public TestObject2 testObject2;

    @Inject
    public TestObject3(TestObject testObject, TestObject2 testObject2) {
        this.testObject = testObject;
        this.testObject2 = testObject2;
    }
}
