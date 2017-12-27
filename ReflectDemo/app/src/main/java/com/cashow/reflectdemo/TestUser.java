package com.cashow.reflectdemo;

public class TestUser extends TestParentUser implements TestListener {
    private int age;
    private String name;
    public String desc = "userdesc";
    public String address;

    public static final int MY_FINAL_INT = 233;

    public TestUser() {
    }

    public TestUser(int age, String name) {
        this.age = age;
        this.name = name;
    }

    private TestUser(int age) {
        this.age = age;
    }

    public void testMethod() {
    }

    private void testPrivateMethod() {
    }

    private String testPrivateUserDesc(String suffix) {
        return name + suffix;
    }

    @Override
    public void onClickUser(TestUser testUser) {
    }
}
