# AnnotationDemo

注解的学习笔记

----------

### 相关链接

<https://medium.com/@iammert/annotation-processing-dont-repeat-yourself-generate-your-code-8425e60c6657>

<http://www.open-open.com/lib/view/open1423558996951.html>

<http://www.cnblogs.com/peida/archive/2013/04/24/3036689.html>

<http://www.cnblogs.com/peida/archive/2013/04/26/3038503.html>

<http://www.jianshu.com/p/07ef8ba80562>

---

### 定义注解

使用 @interface 定义一个注解

```java
public @interface TestAnnotation {
}
```

-----------

### 注解的参数

Annotation 只有成员变量，没有方法。Annotation 的成员变量在 Annotation 定义中以“无形参的方法”形式来声明，其方法名定义了该成员变量的名字，其返回值定义了该成员变量的类型。例如：

```java
public @interface TestAnnotation {
    string name();
    int age();
}
```

一旦在 Annotation 里定义了成员变量后，使用该 Annotation 时就应该为该 Annotation 的成员变量指定值。例如：

```java
@TestAnnotation(name="lalala"，age=30)
public void info(){
}
```

也可以在定义 Annotation 的成员变量时，使用 default 关键字为其指定默认值。

```java
public @interface TestAnnotation {
    string name() default "lalala";
    int age() default 18;
}
```

Annotation 类型里面的参数该怎么设定:

1. 只能用 public 或 default 这两个访问权修饰。

2. 参数成员只能用基本类型 byte,short,char,int,long,float,double,boolean 八种基本数据类型和 String, Enum, Class, annotations 等数据类型，以及这一些类型的数组。

3. 如果只有一个参数成员，最好把参数名称设为"value"，后加小括号。

如果 Annotation 里有一个名为“value“的成员变量，使用该 Annotation 时，可以直接使用 `XXX(val)` 形式为 value 成员变量赋值，无须使用 name=val 形式。例如：

```Java
public @interface TestAnnotation {
    string value();
}

@TestAnnotation("lalala")
public void info(){
}
```

注解元素必须有确定的值，要么在定义注解的默认值中指定，要么在使用注解时指定，非基本类型的注解元素的值不可为 null。因此, 使用空字符串或 0 作为默认值是一种常用的做法。这个约束使得处理器很难表现一个元素的存在或缺失的状态，因为每个注解的声明中，所有元素都存在，并且都具有相应的值，为了绕开这个约束，我们只能定义一些特殊的值，例如空字符串或者负数，以此表示某个元素不存在。在定义注解时，这已经成为一个习惯用法。

---------------

### 元注解

元注解就是用来定义注解的注解，其作用就是定义注解的作用范围，使用在什么元素上等等

元注解共有四种：@Retention, @Target, @Inherited, @Documented

##### @Retention
@Retention 保留的范围，默认值为CLASS。可选值有三种  
1. SOURCE: 在源文件中有效（即源文件保留）
2. CLASS: 在class文件中有效（即class保留）
3. RUNTIME: 在运行时有效（即运行时保留）

@Retention 定义了该 Annotation 被保留的时间长短：某些 Annotation 仅出现在源代码中，而被编译器丢弃；而另一些却被编译在 class 文件中；编译在 class 文件中的 Annotation 可能会被虚拟机忽略，而另一些在 class 被装载时将被读取（请注意并不影响 class 的执行，因为 Annotation 与 class 在使用上是被分离的）。@Retention 限制了 Annotation 的“生命周期”。

RetentionPolicy 的值为 RUNTIME 时，注解处理器可以通过反射，获取到该注解的属性，从而做一些运行时的逻辑处理。

示例：

```java
@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnnotation {
	String name() default "lalala";
}
```

##### @Target
@Target 可以用来注明修饰的对象范围，未标注则表示可修饰所有

ElementType取值：
1. CONSTRUCTOR: 用于描述构造器
2. FIELD: 用于描述域
3. LOCAL_VARIABLE: 用于描述局部变量
4. METHOD: 用于描述方法
5. PACKAGE: 用于描述包
6. PARAMETER: 用于描述参数
7. TYPE: 用于描述类、接口(包括注解类型) 或 enum 声明

示例：
```java
@Target(ElementType.FIELD)
public @interface TestAnnotation {
	String name();
}

@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface TestAnnotation {
	String name();
}
```

##### @Inherited
@Inherited 是否可以被继承，默认为false

如果一个使用了 @Inherited 修饰的 Annotation 类型被用于一个 class，则这个 Annotation 将被用于该 class 的子类。

示例：

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TestAnnotation {
}

@TestAnnotation
public class Base {
}

//SubClass只是继承了Base类
//并未直接使用@TestAnnotation注解修饰
public class SubClass extends Base {
	public static void main(String[] args) {
		System.out.println(SubClass.class.isAnnotationPresent(TestAnnotation.class));
	}
}
```

示例中 Base 使用 @TestAnnotation 修饰，SubClass 继承 Base，而且没有直接使用 @TestAnnotation 修饰，但是因为 TestAnnotation 定义时，使用了 @Inherited 修饰，具有了继承性，所以运行结果为 true。

如果 TestAnnotation 注解没有被 @Inherited 修饰，则运行结果为：false。

##### @Documented
@Documented 是否会保存到 Javadoc 文档中

-----

## 运行时注解

Retention 的值为 RUNTIME 时, 注解会保留到运行时, 因此可以使用反射来解析注解。

```Java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MyRuntimeAnnotation {
    String name() default "lalala";

    int age() default 18;
}

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.text_runtime)
    TextView textRuntime;

    @MyRuntimeAnnotation(name = "lala")
    private String test;

    @MyRuntimeAnnotation(name = "lalala", age = 30)
    private String test2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        processRuntimeAnnotation();
    }

    /**
     * 解析运行时的注解：MyRuntimeAnnotation
     */
    public void processRuntimeAnnotation() {
        // 获取要解析的类
        Class cls = this.getClass();
        // 拿到所有Field
        Field[] declaredFields = cls.getDeclaredFields();
        StringBuilder stringBuilder = new StringBuilder();
        for(Field field : declaredFields){
            // 获取Field上的注解
            MyRuntimeAnnotation annotation = field.getAnnotation(MyRuntimeAnnotation.class);
            if(annotation != null){
                // 获取注解值
                String name = annotation.name();
                stringBuilder.append("\nname = ").append(name).append(" age = ").append(annotation.age());
            }
        }
        textRuntime.setText(stringBuilder.toString());
    }
}
```

---

## 编译时注解

我们需要创建一个 **Java** 的 library。注意这里一定要选择 **Java** library

解析编译时注解需要继承 AbstractProcessor 类，实现其抽象方法。注解处理器可以生成 Java 代码，这些生成的 Java 代码会组成 .java 文件，但不能修改已经存在的 Java 类（即不能向已有的类中添加方法）。而这些生成的 Java 文件，会同时与其他普通的手写 Java 源代码一起被 javac 编译。

```java
@AutoService(Processor.class)
public class MyProcessor extends AbstractProcessor
{
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment)
    {
        super.init(processingEnvironment);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment)
    {
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes()
    {
        return super.getSupportedAnnotationTypes();
    }

    @Override
    public SourceVersion getSupportedSourceVersion()
    {
        return super.getSupportedSourceVersion();
    }
}
```

`@AutoService(Processor.class)` 是一个注解处理器，是 Google 开发的，用来生成 META-INF/services/javax.annotation.processing.Processor 文件的。

引入方式：

```
compile 'com.google.auto.service:auto-service:1.0-rc2'
```

`init(ProcessingEnvironment processingEnvironment)`:
它会被注解处理工具调用，并输入 ProcessingEnviroment 参数。ProcessingEnviroment 提供很多有用的工具类: Elements，Types 和 Filer。

`process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment)`: 这相当于每个处理器的主函数main()。你在这里写你的扫描、评估和处理注解的代码，以及生成 Java 文件。RoundEnviroment 可以让你查询出包含特定注解的被注解元素。

`getSupportedAnnotationTypes()`: 这里你必须指定，这个注解处理器是注册给哪个注解的。注意，它的返回值是一个字符串的集合，包含本处理器想要处理的注解类型的合法全称。换句话说，你在这里定义你的注解处理器注册到哪些注解上。

`getSupportedSourceVersion()`: 用来指定你使用的 Java 版本。通常这里返回 `SourceVersion.latestSupported()`。然而，如果你有足够的理由只支持 Java 7的话，你也可以返回 `SourceVersion.RELEASE_7`。我推荐你使用前者。

在Java 7以后，你也可以使用注解来代替 `getSupportedAnnotationTypes()` 和 `getSupportedSourceVersion()`，像这样：

```Java
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({”com.cashow.customprocessor.MyClassAnnotation})
public class MyProcessor extends AbstractProcessor {
}
```

### 基础工具解析

在 `init()` 中我们获得如下引用：

#### Elements

一个用来处理 Element 的工具类。源代码的每一个部分都是一个特定类型的 Element，例如：

```java
package com.example;    // PackageElement

public class Foo {        // TypeElement

    private int a;      // VariableElement
    private Foo other;  // VariableElement

    public Foo () {}    // ExecuteableElement

    public void setA (  // ExecuteableElement
        int newA   // TypeElement
    ) {}
}
```

举例来说，假如你有一个代表 public class Foo 类的 TypeElement 元素，你可以遍历它的子类，如下：

```java
TypeElement fooClass = ... ;  
for (Element e : fooClass.getEnclosedElements()){ // iterate over children  
    Element parent = e.getEnclosingElement();  // parent == fooClass
}
```

TypeElement 并不包含类本身的信息。你可以从 TypeElement 中获取类的名字，但是你获取不到类的信息，例如它的父类。这种信息需要通过 TypeMirror 获取。你可以通过调用 elements.asType() 获取元素的 TypeMirror。

#### Types

一个用来处理 TypeMirror 的工具类

#### Filer

使用 Filer 可以创建文件

### 搜索注解

```Java
@Override
public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
    //这里开始处理我们的注解解析了，以及生成Java文件

    Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(MyClassAnnotation.class);

    // 遍历所有被注解了 @MyClassAnnotation 的元素
    for (Element annotatedElement : elements) {
        ...
    }
    return false;
}
```

`roundEnvironment.getElementsAnnotatedWith(MyClassAnnotation.class)` 返回所有被注解了 @MyClassAnnotation 的元素的列表，这些 Element 可能是类、方法、变量等。所以，接下来，我们必须检查这些 Element 是否是一个类：

```java
// 遍历所有被注解了 @MyClassAnnotation 的元素
for (Element annotatedElement : elements) {

    // 检查被注解为 @MyClassAnnotation 的元素是否是一个类
    if (annotatedElement.getKind() != ElementKind.CLASS) {
        error(annotatedElement, "Only classes can be annotated with @%s",
                MyClassAnnotation.class.getSimpleName());
        return true; // 退出处理
    }

    ...
}

/**
 * 打印 error 日志
 * 日志等级有 ERROR, WARNING, MANDATORY_WARNING, NOTE, OTHER
 */
private void error(Element e, String msg, Object... args) {
    mMessager.printMessage(
            Diagnostic.Kind.ERROR,
            String.format(msg, args),
            e);
}
```


### 代码生成

```java
private void analysisAnnotated(Element classElement) {
    MyClassAnnotation annotation = classElement.getAnnotation(MyClassAnnotation.class);
    String name = annotation.name();
    String text = annotation.text();

    String newClassName = "MyGeneratedClass";

    StringBuilder builder = new StringBuilder()
            .append("package com.cashow.annotationdemo.auto;\n\n")
            .append("public class ")
            .append(newClassName)
            .append(" {\n\n") // open class
            .append("\tpublic String getMessage() {\n") // open method
            .append("\t\treturn \"");

    builder.append(text).append(name).append(" !\\n");


    builder.append("\";\n") // end return
            .append("\t}\n") // close method
            .append("}\n"); // close class


    try { // write the file
        JavaFileObject source = mFiler.createSourceFile("com.cashow.annotationdemo.auto." + newClassName);
        Writer writer = source.openWriter();
        writer.write(builder.toString());
        writer.flush();
        writer.close();
    } catch (IOException e) {
        // Note: calling e.printStackTrace() will print IO errors
        // that occur from the file already existing after its first run, this is normal
    }
    info(">>> analysisAnnotated is finish... <<<");
}
```

build之后会生成 MyGeneratedClass.java 文件，文件位置在 app/build/generated/source/apt/debug/com/cashow/annotationdemo/auto ：

```java
package com.cashow.annotationdemo.auto;

public class MyGeneratedClass {

	public String getMessage() {
		return "Hello World !\n";
	}
}
```

build之后，项目里的代码就能调用 MyGeneratedClass 了。
