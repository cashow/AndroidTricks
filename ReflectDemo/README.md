# ReflectDemo

反射的学习笔记

---

### 相关链接

<http://www.cnblogs.com/crazypebble/archive/2011/04/13/2014582.html>

---

### 简介

反射允许运行中的 Java 程序对自身进行检查，并能直接操作程序的内部属性或方法。反射机制允许程序在正在执行的过程中，利用反射 API 取得任何已知名称的类的内部信息，包括：package、 type parameters、 superclass、 implemented interfaces、 inner classes、 outer classes、 fields、 constructors、 methods、 modifiers 等，并可以在执行的过程中，动态生成 Instances、变更 fields 内容或唤起 methods。

我们可以利用反射机制在 Java 程序中，动态的去调用一些 protected 甚至是 private 的方法或类，这样可以很大程度上满足我们的一些比较特殊需求。

---

### Class 类

Class（首字母是大写的，不要和 class 搞混）本身就是一个类，Class 是该类的名称。

Class 类是整个 Java 反射机制的源头，Class 类本身表示 Java 对象的类型，我们可通过一个 Object 对象的 getClass() 方法取得一个对象的类型，此函数返回的就是一个 Class 类。获取 Class 对象的方法有很多种：

```java
// 通过 getClass() 获取对象的 Class
String str = "abc";
Class c1 = str.getClass();

// 通过 getSuperclass() 获取 Class 的父类
TestUser b = new TestUser();
Class c1 = b.getClass();
Class c2 = c1.getSuperclass();

// 通过 Class.forName() 获取 Class
Class c1 = Class.forName("java.lang.String");

// 通过 .class 获取 Class
Class c1 = String.class;
Class c2 = TestUser.class;
Class c3 = int.class;
Class c4 = int[].class;

// 通过基本类型包装类的 TYPE 获取 Class
Class c1 = Boolean.TYPE;
Class c2 = Void.TYPE;
```

---

### 获取类的构造方法

Class 类有四个 public 方法可以获取类的构造函数：

```java
// 返回所有 public 构造函数
Constructor<?>[] getConstructors()
// 根据构造函数的参数，返回一个具体的 public 的构造函数
Constructor<T> getConstructor(Class<?>... parameterTypes)
// 返回所有的构造函数（不分 public 和非 public）
Constructor<?>[] getDeclaredConstructors()
// 根据构造函数的参数，返回一个具体的构造函数（不分 public 和非 public）
Constructor<T> getDeclaredConstructor(Class<?>... parameterTypes)
```

获取构造函数的 demo：

```Java
public class TestUser {
    private int age;
    private String name;

    public TestUser() {
    }

    public TestUser(int age, String name) {
        this.age = age;
        this.name = name;
    }

    private TestUser(int age) {
        this.age = age;
    }
}

private void getReflectionConstructors() {
    // 获取 TestUser 的 Class
    Class mClass = TestUser.class;
    // 获取 TestUser 的 className
    String className = mClass.getName();

    try {
        // 获取 TestUser.class 的所有构造函数
        Constructor[] declaredConstructors = mClass.getDeclaredConstructors();
        for (Constructor constructor : declaredConstructors) {
            // 获取构造函数的修饰域（public, private, protected）
            int modifier = constructor.getModifiers();
            String modifierName = Modifier.toString(modifier);

            StringBuilder constructorStr = new StringBuilder();
            constructorStr.append(modifierName).append(" ").append(className).append("(");

            // 获取构造函数里所有参数的 Class
            Class[] parameterTypes = constructor.getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i ++) {
                Class parameterType = parameterTypes[i];
                constructorStr.append(parameterType.getName());
                if (i != parameterTypes.length - 1) {
                    constructorStr.append(", ");
                }
            }
            constructorStr.append(")");

            MLog.d(constructorStr.toString());
        }
    } catch (Exception e) {
        MLog.d("getReflectionConstructors", e);
    }
}
```

调用 `getReflectionConstructors()` 后打印出来的日志：

```
public com.cashow.reflectdemo.TestUser()
private com.cashow.reflectdemo.TestUser(int)
public com.cashow.reflectdemo.TestUser(int, java.lang.String)
```

---

### 获取类的成员方法

Class 类有四个 public 方法可以获取类的成员方法：

```java
// 返回这个类和父类里所有的 public 方法
Method[] getMethods()
// 根据方法的参数，返回这个类和父类里的一个具体的 public 的方法
Method getMethod(String name, Class<?>... parameterTypes)
// 返回这个类所有的方法（不分 public 和非 public），但不返回父类的方法
Method[] getDeclaredMethods()
// 根据方法的参数，返回这个类一个具体的方法（不分 public 和非 public），但不返回父类的方法
Method getDeclaredMethod(String name, Class<?>... parameterTypes)
```

获取成员方法的 demo：

```java
public class TestUser extends TestParentUser implements TestListener {
    private int age;
    private String name;

    public void testMethod() {
    }

    private void testPrivateMethod() {
    }

    @Override
    public void onClickUser(TestUser testUser) {
    }
}

public interface TestParentListener {
    void onClickView(View view);
}

public class TestParentUser implements TestParentListener {
    public void parentMethod() {
    }

    @Override
    public void onClickView(View view) {
    }
}

public interface TestListener {
    void onClickUser(TestUser testUser);
}

private void getReflectionMethod() {
    // 获取 TestUser 的 Class
    Class mClass = TestUser.class;

    try {
        Method[] methods = mClass.getMethods();
        for (Method method : methods) {
            // 方法的修饰域（public, private, protected）
            int modifier = method.getModifiers();
            String modifierName = Modifier.toString(modifier);

            StringBuilder constructorStr = new StringBuilder();
            constructorStr.append(modifierName).append(" ").append(method.getName()).append("(");

            // 方法里所有参数的 Class
            Class[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i ++) {
                Class parameterType = parameterTypes[i];
                constructorStr.append(parameterType.getName());
                if (i != parameterTypes.length - 1) {
                    constructorStr.append(", ");
                }
            }
            constructorStr.append(")");

            MLog.d(constructorStr.toString());
        }
    } catch (Exception e) {
        MLog.d("getReflectionMethod", e);
    }
}
```

调用 `getReflectionMethod()` 后打印出来的日志：

```
public equals(java.lang.Object)
public final getClass()
public hashCode()
public final native notify()
public final native notifyAll()
public onClickUser(com.cashow.reflectdemo.TestUser)
public onClickView(android.view.View)
public parentMethod()
public testMethod()
public toString()
public final native wait()
public final wait(long)
public final native wait(long, int)
```

如果把 `getReflectionMethod()` 里的 `mClass.getMethods()` 改成 `mClass.getDeclaredMethods()`，打印出来的日志：

```
public onClickUser(com.cashow.reflectdemo.TestUser)
public testMethod()
private testPrivateMethod()
```

---

### 获取类的成员变量

Class 类有四个 public 方法可以获取类的成员变量：

```java
// 获取所有的 public 成员变量
Field[] getFields()
// 根据变量名获取 public 成员变量
Field getField(String name)
// 获取所有的成员变量
Field getDeclaredField(String name)
// 根据变量名获取成员变量
Field[] getDeclaredFields()
```

获取成员变量的 demo：

```java
public class TestUser {
    private int age;
    private String name;
    public String desc = "userdesc";
    public String address;
}

private void getReflectionField() {
    TestUser testUser = new TestUser(14, "userName");
    // 获取 TestUser 的 Class
    Class mClass = testUser.getClass();

    MLog.d();
    try {
        Field[] fields = mClass.getFields();
        for (Field field : fields) {
            // 可以通过 Field.getType() 方法取得字段类型（String, int 等等)
            Class fieldClass = field.getType();

            // 修饰域（public, private, protected）
            int modifier = field.getModifiers();
            String modifierName = Modifier.toString(modifier);

            field.setAccessible(true);

            Object value = field.get(testUser);

            if (value == null) {
                MLog.d(modifierName + " " + fieldClass + " " + field.getName());
            } else {
                MLog.d(modifierName + " " + fieldClass + " " + field.getName() + " = " + value.toString());
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

调用 `getReflectionField()` 后打印出来的日志：

```java
public class java.lang.String address
public class java.lang.String desc = userdesc
```

如果把 `getReflectionField()` 里的 `mClass.getFields()` 改成 `mClass.getDeclaredFields()`，打印出来的日志：

```java
public class java.lang.String address
private int age = 14
public class java.lang.String desc = userdesc
private class java.lang.String name = userName
```

---

### 获取类、属性、方法的修饰域

Class、Method、Constructor、Field 都有一个 public 的方法 `int getModifiers()`，这个方法会返回一个 int 类型的数，表示修饰类型的 **组合** 值。

修饰类型包含以下几种：

```
PUBLIC
PRIVATE
PROTECTED
STATIC
FINAL
SYNCHRONIZED
VOLATILE
TRANSIENT
NATIVE
INTERFACE
ABSTRACT
STRICT
```

Modifier 类提供了 isPrivate() 、isPublic() 等方法，用来判断修饰域是否含有这个修饰类型

通过 `Modifier.toString()` 可以将修饰域转成字符串。例如：

```java
public static final int MY_FINAL_INT = 233;
```

MY_FINAL_INT 的修饰域通过 Modifier.toString() 打印出来的是：

```
public static final
```

---

### 创建一个类的实例

在得到一个类的 Class 对象之后，我们可以利用类的 Constructor 去实例化该对象，也可以利用 Class 类本身的 newInstance() 创建一个实例。

```
// 利用 Constructor 实例化对象
Constructor constructor = mClass.getConstructor(null);
Object obj = constructor.newInstance(null);

// 利用 newInstance() 实例化对象
Object obj = mClass.newInstance();
```

---

### 执行方法

通过调用 Method 类中的 `Object invoke(Object receiver, Object... args)` 方法，可以执行对象中的方法。

`invoke()` 方法中的参数 receiver 指明了调用对象，参数 args 指明了该方法所需要的参数。

如果某个方法是静态方法，那么 receiver 可以传 null。

```java
public class TestUser {
    private int age;
    private String name;

    public TestUser(int age, String name) {
        this.age = age;
        this.name = name;
    }

    private String testPrivateUserDesc(String suffix) {
        return name + suffix;
    }
}

private void loadPrivateMethod() {
    TestUser testUser = new TestUser(15, "asdd");
    Class mClass = testUser.getClass();

    try {
        Method method = mClass.getDeclaredMethod("testPrivateUserDesc", String.class);
        method.setAccessible(true);
        method.invoke(testUser, "_suffix_str");
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

调用 `loadPrivateMethod()` 打印出来的日志：

```
methodReturnValue = asdd_suffix_str
```
