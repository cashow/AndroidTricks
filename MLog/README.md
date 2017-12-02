# MLog

自定义的 log 工具

-------

#### 普通的 log

```java
MLog.d("aaaa");
// MLog: aaaa

MLog.d("newtag", "bbbbb");
// newtag: bbbbb
```

#### 超长的 log 分段打印

```java
MLog.d("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");

// MLog: 12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
// MLog: 12345678901234567890123456789012345678901234567890123456789012345678901234567890
```

#### 打印 json 对象

```java
TestObject testObject = new TestObject(1, 2, "3", "4");
MLog.d("testObject", testObject);

// MLog: 【testObject】
// MLog: {"string_b":"4","string_a":"3","int_b":2,"int_a":1}
```

#### 打印异常的 StackTrace

```java
MLog.d("testException", new RuntimeException("test runtime exception"));
// MLog: 【testException】: java.lang.RuntimeException: test runtime exception
//    at com.cashow.mlog.MainActivity.onCreate(MainActivity.java:25)
//    at android.app.Activity.performCreate(Activity.java:5084)
// 	at andro
// MLog: id.app.Instrumentation.callActivityOnCreate(Instrumentation.java:1079)
//    at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2045)
// 	at android.app.ActivityThread.handleLaunchActivit
// MLog: y(ActivityThread.java:2106)
//    at android.app.ActivityThread.access$600(ActivityThread.java:137)
//    at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1202)
// 	at android.os.Handler.dispatchM
// MLog: essage(Handler.java:99)
//    at android.os.Looper.loop(Looper.java:137)
//    at android.app.ActivityThread.main(ActivityThread.java:4819)
//    at java.lang.reflect.Method.invokeNative(Native Method)
// 	at java.lang
// MLog: .reflect.Method.invoke(Method.java:511)
//    at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:793)
//    at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:560)
// 	at dalvik.
```

#### 打印当前代码的类名、方法名和行数

```java
protected void onResume() {
    MLog.d();
    super.onResume();
}
// MLog: 【MainActivity.java】 onResume() line 30
```

#### 打印当前代码的类名、方法名和行数以及与上一次调用 MLog.t() 的时间间隔

```java
private void testMethod() {
    MLog.t();
    for (int i = 0; i < 500000000; i++) {
    }
    MLog.t();
}
// MLog: 【MainActivity.java】 testMethod() line 41 : 0.000000s
// MLog: 【MainActivity.java】 testMethod() line 44 : 4.982000s
```
