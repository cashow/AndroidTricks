package com.cashow.mlog;

import java.io.Serializable;

public class TestObject implements Serializable {
    public int int_a;
    public int int_b;
    public String string_a;
    public String string_b;

    public TestObject(int int_a, int int_b, String string_a, String string_b) {
        this.int_a = int_a;
        this.int_b = int_b;
        this.string_a = string_a;
        this.string_b = string_b;
    }
}
