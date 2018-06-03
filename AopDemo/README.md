# AopDemo

利用 AspectJ 实现 AOP（面向切面编程）的 demo。

--------

### 相关链接

<https://www.jianshu.com/p/0fa8073fd144>  
<https://blog.csdn.net/crazy__chen/article/details/52014672>  
<https://blog.csdn.net/innost/article/details/49387395>

--------

## 接入步骤

### 引入 AspectJ

1. 创建一个名为 buildSrc 的 module；
2. 在 src/main 中添加 groovy 文件夹，在包名 com.cashow.aspectjplugin 下创建文件 AspectJPlugin.groovy；
3. 在 src/resources/META-INF/gradle-plugins 中创建 com.cashow.gradle.properties 文件；
4. 修改 build.gradle 文件。

AspectJPlugin.groovy 文件的代码：

```
package com.cashow.aspectjplugin
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main

public class AspectJPlugin implements Plugin<Project> {

    void apply(Project project) {
        println "dddd******************d"

        def hasApp = project.plugins.withType(AppPlugin)
        def hasLib = project.plugins.withType(LibraryPlugin)
        if (!hasApp && !hasLib) {
            throw new IllegalStateException("'android' or 'android-library' plugin required.")
        }

        final def log = project.logger
        final def variants
        if (hasApp) {
            variants = project.android.applicationVariants
        } else {
            variants = project.android.libraryVariants
        }

        project.dependencies {
            // TODO this should come transitively
            compile 'org.aspectj:aspectjrt:1.8.6'
        }

        variants.all { variant ->

            JavaCompile javaCompile = variant.javaCompile
            javaCompile.doLast {
                String[] args = ["-showWeaveInfo",
                                 "-1.5",
                                 "-inpath", javaCompile.destinationDir.toString(),
                                 "-aspectpath", javaCompile.classpath.asPath,
                                 "-d", javaCompile.destinationDir.toString(),
                                 "-classpath", javaCompile.classpath.asPath,
                                 "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)]
                log.debug "ajc args: " + Arrays.toString(args)

                MessageHandler handler = new MessageHandler(true);
                new Main().run(args, handler);
                for (IMessage message : handler.getMessages(null, true)) {
                    switch (message.getKind()) {
                        case IMessage.ABORT:
                        case IMessage.ERROR:
                        case IMessage.FAIL:
                            log.error message.message, message.thrown
                            break;
                        case IMessage.WARNING:
                            log.warn message.message, message.thrown
                            break;
                        case IMessage.INFO:
                            log.info message.message, message.thrown
                            break;
                        case IMessage.DEBUG:
                            log.debug message.message, message.thrown
                            break;
                    }
                }
            }
        }

    }
}
```

com.cashow.gradle.properties 文件的代码：

```
implementation-class=com.cashow.aspectjplugin.AspectJPlugin
```

build.gradle 文件的代码：

```
apply plugin: 'groovy'

dependencies {
    compile gradleApi()
    compile localGroovy()
    compile 'com.android.tools.build:gradle:3.0.1'
    compile 'org.aspectj:aspectjtools:1.8.6'
    compile 'org.aspectj:aspectjrt:1.8.6'
}

repositories {
    jcenter()
    google()
}
```

### 创建自定义的注解

1. 创建一个名为 aspectjannoation 的 module；
2. 创建 DebugLog.java 注解；
3. 创建处理 DebugLog 注解的类 Hugo.java；

DebugLog.java 的代码：

```java
package com.cashow.aspectjannoation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

@Target({TYPE, METHOD, CONSTRUCTOR}) @Retention(CLASS)
public @interface DebugLog {
}
```

Hugo.java 的代码：

```java
package com.cashow.aspectjannoation;

import android.os.Build;
import android.os.Looper;
import android.os.Trace;
import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.concurrent.TimeUnit;

/**
 * Created by 835127729qq.com on 16/7/20.
 */
@Aspect
public class Hugo {
    @Pointcut("within(@com.example.aoplib.DebugLog *)")
    public void withinAnnotatedClass() {}

    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {}

    @Pointcut("execution(!synthetic *.new(..)) && withinAnnotatedClass()")
    public void constructorInsideAnnotatedType() {}

    @Pointcut("execution(@com.example.aoplib.DebugLog * *(..)) || methodInsideAnnotatedType()")
    public void method() {}

    @Pointcut("execution(@com.example.aoplib.DebugLog *.new(..)) || constructorInsideAnnotatedType()")
    public void constructor() {}

    @Around("method() || constructor()")
    public Object logAndExecute(ProceedingJoinPoint joinPoint) throws Throwable {
        enterMethod(joinPoint);

        long startNanos = System.nanoTime();
        Object result = joinPoint.proceed();
        long stopNanos = System.nanoTime();
        long lengthMillis = TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos);

        exitMethod(joinPoint, result, lengthMillis);

        return result;
    }

    private static void enterMethod(JoinPoint joinPoint) {

        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();

        Class<?> cls = codeSignature.getDeclaringType();
        String methodName = codeSignature.getName();
        String[] parameterNames = codeSignature.getParameterNames();
        Object[] parameterValues = joinPoint.getArgs();

        StringBuilder builder = new StringBuilder("\u21E2 ");
        builder.append(methodName).append('(');
        for (int i = 0; i < parameterValues.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(parameterNames[i]).append('=');
            builder.append(Strings.toString(parameterValues[i]));
        }
        builder.append(')');

        if (Looper.myLooper() != Looper.getMainLooper()) {
            builder.append(" [Thread:\"").append(Thread.currentThread().getName()).append("\"]");
        }

        Log.v(asTag(cls), builder.toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            final String section = builder.toString().substring(2);
            Trace.beginSection(section);
        }
    }

    private static void exitMethod(JoinPoint joinPoint, Object result, long lengthMillis) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Trace.endSection();
        }

        Signature signature = joinPoint.getSignature();

        Class<?> cls = signature.getDeclaringType();
        String methodName = signature.getName();
        boolean hasReturnType = signature instanceof MethodSignature
                && ((MethodSignature) signature).getReturnType() != void.class;

        StringBuilder builder = new StringBuilder("\u21E0 ")
                .append(methodName)
                .append(" [")
                .append(lengthMillis)
                .append("ms]");

        if (hasReturnType) {
            builder.append(" = ");
            builder.append(Strings.toString(result));
        }

        Log.v(asTag(cls), builder.toString());
    }

    private static String asTag(Class<?> cls) {
        if (cls.isAnonymousClass()) {
            return asTag(cls.getEnclosingClass());
        }
        return cls.getSimpleName();
    }
}
```

Strings.java 的代码：

```java
package com.cashow.aspectjannoation;

/**
 * Created by 835127729qq.com on 16/7/22.
 */
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

final class Strings {
    static String toString(Object obj) {
        if (obj == null) {
            return "null";
        }
        if (obj instanceof CharSequence) {
            return '"' + printableToString(obj.toString()) + '"';
        }

        Class<?> cls = obj.getClass();
        if (Byte.class == cls) {
            return byteToString((Byte) obj);
        }

        if (cls.isArray()) {
            return arrayToString(cls.getComponentType(), obj);
        }
        return obj.toString();
    }

    private static String printableToString(String string) {
        int length = string.length();
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length;) {
            int codePoint = string.codePointAt(i);
            switch (Character.getType(codePoint)) {
                case Character.CONTROL:
                case Character.FORMAT:
                case Character.PRIVATE_USE:
                case Character.SURROGATE:
                case Character.UNASSIGNED:
                    switch (codePoint) {
                        case '\n':
                            builder.append("\\n");
                            break;
                        case '\r':
                            builder.append("\\r");
                            break;
                        case '\t':
                            builder.append("\\t");
                            break;
                        case '\f':
                            builder.append("\\f");
                            break;
                        case '\b':
                            builder.append("\\b");
                            break;
                        default:
                            builder.append("\\u").append(String.format("%04x", codePoint).toUpperCase(Locale.US));
                            break;
                    }
                    break;
                default:
                    builder.append(Character.toChars(codePoint));
                    break;
            }
            i += Character.charCount(codePoint);
        }
        return builder.toString();
    }

    private static String arrayToString(Class<?> cls, Object obj) {
        if (byte.class == cls) {
            return byteArrayToString((byte[]) obj);
        }
        if (short.class == cls) {
            return Arrays.toString((short[]) obj);
        }
        if (char.class == cls) {
            return Arrays.toString((char[]) obj);
        }
        if (int.class == cls) {
            return Arrays.toString((int[]) obj);
        }
        if (long.class == cls) {
            return Arrays.toString((long[]) obj);
        }
        if (float.class == cls) {
            return Arrays.toString((float[]) obj);
        }
        if (double.class == cls) {
            return Arrays.toString((double[]) obj);
        }
        if (boolean.class == cls) {
            return Arrays.toString((boolean[]) obj);
        }
        return arrayToString((Object[]) obj);
    }

    /** A more human-friendly version of Arrays#toString(byte[]) that uses hex representation. */
    private static String byteArrayToString(byte[] bytes) {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < bytes.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(byteToString(bytes[i]));
        }
        return builder.append(']').toString();
    }

    private static String byteToString(Byte b) {
        if (b == null) {
            return "null";
        }
        return "0x" + String.format("%02x", b).toUpperCase(Locale.US);
    }

    private static String arrayToString(Object[] array) {
        StringBuilder buf = new StringBuilder();
        arrayToString(array, buf, new HashSet<Object[]>());
        return buf.toString();
    }

    private static void arrayToString(Object[] array, StringBuilder builder, Set<Object[]> seen) {
        if (array == null) {
            builder.append("null");
            return;
        }

        seen.add(array);
        builder.append('[');
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }

            Object element = array[i];
            if (element == null) {
                builder.append("null");
            } else {
                Class elementClass = element.getClass();
                if (elementClass.isArray() && elementClass.getComponentType() == Object.class) {
                    Object[] arrayElement = (Object[]) element;
                    if (seen.contains(arrayElement)) {
                        builder.append("[...]");
                    } else {
                        arrayToString(arrayElement, builder, seen);
                    }
                } else {
                    builder.append(toString(element));
                }
            }
        }
        builder.append(']');
        seen.remove(array);
    }

    private Strings() {
        throw new AssertionError("No instances.");
    }
}
```

### 使用注解

1. 在 app/build.gradle 中引入 aspectjannoation 库：`implementation project(':aspectjannoation')` 和 aspjectj 的库 `apply plugin: 'com.cashow.gradle'`；
2. 在 MainActivity.java 的 onCreate() 上面加上 `@DebugLog` 注解；
3. 运行项目，可看到两条日志：

```
MainActivity: ⇢ onCreate(savedInstanceState=null)
MainActivity: ⇠ onCreate [83ms]
```

### 生成的代码

这是项目中 MainActivity.java 的代码：

```java
public class MainActivity extends AppCompatActivity {
    @DebugLog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
```

通过 dex2jar 和 jd-gui 对生成的 apk 进行反编译，可看到最终生成的 MainActivity.class 是这样的：

```java
public class MainActivity extends AppCompatActivity {
  private static final JoinPoint.StaticPart ajc$tjp_0;

  static {}

  private static void ajc$preClinit() {
    Factory localFactory = new Factory("MainActivity.java", MainActivity.class);
    ajc$tjp_0 = localFactory.makeSJP("method-execution", localFactory.makeMethodSig("4", "onCreate", "com.cashow.aopdemo.MainActivity", "android.os.Bundle", "savedInstanceState", "", "void"), 13);
  }

  static final void onCreate_aroundBody0(MainActivity paramMainActivity, Bundle paramBundle, JoinPoint paramJoinPoint) {
    paramMainActivity.onCreate(paramBundle);
    paramMainActivity.setContentView(2131296283);
  }

  @DebugLog
  protected void onCreate(Bundle paramBundle) {
    JoinPoint localJoinPoint = Factory.makeJP(ajc$tjp_0, this, this, paramBundle);
    Hugo.aspectOf().logAndExecute(new MainActivity.AjcClosure1(new Object[] { this, paramBundle, localJoinPoint }).linkClosureAndJoinPoint(69648));
  }
}
```

其中，MainActivity$AjcClosure1 的代码：

```java
public class MainActivity$AjcClosure1 extends AroundClosure {
  public MainActivity$AjcClosure1(Object[] paramArrayOfObject) {
    super(paramArrayOfObject);
  }

  public Object run(Object[] paramArrayOfObject) {
    paramArrayOfObject = this.state;
    MainActivity.onCreate_aroundBody0((MainActivity)paramArrayOfObject[0], (Bundle)paramArrayOfObject[1], (JoinPoint)paramArrayOfObject[2]);
    return null;
  }
}
```

由上面的代码可以看出，MainActivity 里原先在 `onCreate()` 的代码被放入了 `onCreate_aroundBody0()` 方法里，而 `onCreate_aroundBody0()` 方法会在 MainActivity$AjcClosure1 的 `run()` 方法里被调用。MainActivity 里新的 `onCreate()` 调用的是 Hugo.aspectOf().logAndExecute()，在这里会执行 Hugo.java 里的 `logAndExecute()` 方法。


-----------------

## AspectJ 语法

### Join Points

Join Points 就是程序运行时的一些执行点，比如：

* 一个函数的调用可以是一个 JPoint。比如 Log.e() 这个函数。e 的执行可以是一个 JPoint，而调用 e 的函数也可以认为是一个 JPoint。
* 设置一个变量，或者读取一个变量，也可以是一个 JPoint。
* for 循环可以看做是 JPoint。

理论上说，一个程序中很多地方都可以被看做是 JPoint，但是 AspectJ 中，只有以下所示的几种执行点被认为是 JPoints：

Join Points | 说明 | 示例
------------ | ----- | -----
method call | 函数调用 | 比如调用 Log.e()，这是一处 JPoint
method execution | 函数执行 | 比如 Log.e() 的执行内部，是一处 JPoint。注意它和 method call 的区别。method call 是调用某个函数的地方。而 execution 是某个函数执行的内部。
constructor call | 构造函数调用 | 和 method call 类似
constructor execution | 构造函数执行 | 和 method execution 类似
field get | 获取某个变量 | 比如读取 DemoActivity.debug 成员
field set | 设置某个变量 | 比如设置 DemoActivity.debug 成员
pre-initialization | Object 在构造函数中做得一些工作。 | 很少使用
initialization | Object 在构造函数中做得工作 |
static initialization | 类初始化 | 比如类的 static{}
handler | 异常处理 | 比如 try catch(xxx) 中，对应 catch 内的执行
advice execution | 这个是 AspectJ 的内容，稍后再说 |  

### Pointcuts

一个程序会有很多的 JPoints，即使是同一个函数，还分为 call 类型和 execution 类型的 JPoint。显然，不是所有的 JPoint，也不是所有类型的 JPoint 都是我们关注的。

怎么从一堆一堆的 JPoints 中选择自己想要的 JPoints 呢？恩，这就是 Pointcuts 的功能。一句话，Pointcuts 的目标是提供一种方法使得开发者能够选择自己感兴趣的 JoinPoints。

如果想把代码中所有调用 println 的地方找到，代码该这么写：

```
public pointcut testAll(): call(public * *.println(..)) && !within(TestAspect);
```

注意，aspectj 的语法和 Java 一样，只不过多了一些关键词。

在上述代码中：

* 第一个 public：表示这个 pointcut 是 public 访问。这主要和 aspect 的继承关系有关，属于 AspectJ 的高级玩法，本文不考虑。
* pointcut：关键词，表示这里定义的是一个 pointcut。pointcut 定义有点像函数定义。总之，在 AspectJ 中，你得定义一个 pointcut。
* testAll()：pointcut 的名字。在 AspectJ 中，定义 Pointcut 可分为有名和匿名两种办法。个人建议使用 named 方法。因为在后面，我们要使用一个 pointcut 的话，就可以直接使用它的名字就好。testAll 后面有一个冒号，这是 pointcut 定义名字后，必须加上。冒号后面是这个 pointcut 怎么选择 Joinpoint 的条件。

本例中，call(public * *.println(..)) 是一种选择条件。call 表示我们选择的 Joinpoint 类型为 call 类型。

public * *.println(..)：这小行代码使用了通配符。由于我们这里选择的 JoinPoint 类型为 call 类型，它对应的目标 JPoint 一定是某个函数。所以我们要找到这个函数。public 表示目标 JPoint 的访问类型（public/private/protect）。第一个 * 表示返回值的类型是任意类型。第二个 * 用来指明包名。此处不限定包名。紧接其后的 println 是函数名。这表明我们选择的函数是任何包中定义的名字叫 println 的函数。当然，唯一确定一个函数除了包名外，还有它的参数。在 (..) 中，就指明了目标函数的参数应该是什么样子的。比如这里使用了通配符..，代表任意个数的参数，任意类型的参数。

再来看 call 后面的 &&：AspectJ 可以把几个条件组合起来，目前支持 &&，||，以及！这三个条件。这三个条件的意思和 Java 中的是一样的。

来看最后一个 !within(TestAspectJ)：前面的! 表示不满足某个条件。within 是另外一种类型选择方法，特别注意，这种类型和前面讲到的 joinpoint 的那几种类型不同。within 的类型是数据类型，而 joinpoint 的类型更像是动态的，执行时的类型。

上例中的 pointcut 合起来就是：

* 选择那些调用 println（而且不考虑 println 函数的参数是什么）的 Joinpoint。
* 另外，调用者的类型不要是 TestAspect 的。
