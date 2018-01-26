### Singleton 作用域

`@Provides` 方法或者可注入的类加上 `@Singleton` 注解，Dagger 会给所有依赖的变量返回同一个实例。

你需要在 component 里添加 `@Singleton` 注解，表明其中有 Module 使用了 `@Singleton`。

```java
@Module
public class Demo4SingletonModule {
    @Singleton
    @Provides
    static TestObject2 providetestObject2() {
        return new TestObject2(String.valueOf((int)(Math.random() * 100)), 456);
    }
}

@Singleton
@Component(modules = Demo4SingletonModule.class)
public interface Demo4SingletonComponent {
    TestObject2 testObject2();
}
```

如果 component 添加了 `@Singleton` 注解，但是 Module 里有 Provides 方法没有添加 `@Singleton` 注解，那么这个方法每次返回的实例仍然是不一样的。

### Reusable 作用域

有时候你想要减少 `@Inject` 类实例化的次数，或者减少 `@Provides` 方法的调用次数，但又不需要保证在 component 的生命周期内使用同一个实例，你可以使用 `@Reusable` 作用域

`@Reusable` 作用域不像其他作用域那样要和 component 绑定。

对于 `@Reusable` 标记的类，每个 component 在调用绑定时会缓存生成的实例。

如果你的 component 里有 `@Reusable` 注解的绑定，但是只有 subcomponent 用到了这个绑定，那么只有 subcomponent 会缓存生成的实例。

如果两个 subcomponents 里带有 `@Reusable` 注解的绑定没有共同的父类，那么他们会各自缓存自己生成好的实例。

如果 component 已经缓存了，那么 subcomponent 会直接使用 component 缓存的实例。

```java
@Reusable // It doesn't matter how many scoopers we use, but don't waste them.
class CoffeeScooper {
  @Inject CoffeeScooper() {}
}

@Module
class CashRegisterModule {
  @Provides
  @Reusable // DON'T DO THIS! You do care which register you put your cash in.
            // Use a specific scope instead.
  static CashRegister badIdeaCashRegister() {
    return new CashRegister();
  }
}

@Reusable // DON'T DO THIS! You really do want a new filter each time, so this
          // should be unscoped.
class CoffeeFilter {
  @Inject CoffeeFilter() {}
}
```

### 可释放的引用

如果一个绑定指定了作用域，那么 component 会缓存绑定的值直到 component 被垃圾回收。在 Android 这种对内存比较敏感的环境下，你可能会希望在内存吃紧时，当前未被使用的实例被垃圾回收掉。这个时候你可以使用 `@CanReleaseReferences` 注解。

```java
@Documented
@Retention(RUNTIME)
@CanReleaseReferences
@Scope
public @interface MyScope {}
```

当你想要让实例在垃圾回收时被删除，你可以注入一个 ReleasableReferenceManager 对象，并且调用 `releaseStrongReferences()` 方法：

```java
@Inject @ForReleasableReferences(MyScope.class)
ReleasableReferenceManager myScopeReferenceManager;

void lowMemory() {
  myScopeReferenceManager.releaseStrongReferences();
}
```

当内存压力降低时，你可以调用 `restoreStrongReferences()` 方法恢复缓存对象的引用：

```java
void highMemory() {
  myScopeReferenceManager.restoreStrongReferences();
}
```

### 懒加载

你可以创建一个 Lazy<T> 变量，在这个 Lazy<T> 的 get() 方法第一次被调用时，会进行 T 的初始化操作。

```java
class GrindingCoffeeMaker {
  @Inject Lazy<Grinder> lazyGrinder;

  public void brew() {
    while (needsGrinding()) {
      // Grinder created once on first call to .get() and cached.
      lazyGrinder.get().grind();
    }
  }
}
```

### Provider 注入

使用 Provider<T> 可以实现一次注入多个实例。Provider<T> 在每次调用 .get() 方法时都会调用一次绑定逻辑，如果绑定逻辑是有 `@Inject` 注解的构造器，会返回新的 T 实例。

```java
class BigCoffeeMaker {
  @Inject Provider<Filter> filterProvider;

  public void brew(int numberOfPots) {
  ...
    for (int p = 0; p < numberOfPots; p++) {
      maker.addFilter(filterProvider.get()); //new filter every time.
      maker.addCoffee(...);
      maker.percolate();
      ...
    }
  }
}
```

### 限定符

有时候返回值类型不足以描述一个依赖。例如，你可能会要注入多个不同的 heaters。

这种情况下，可以使用限定符注解。限定符需要有 `@Qualifier` 注解。以下定义了一个名为 `@Named` 的限定符：

```java
@Qualifier
@Documented
@Retention(RUNTIME)
public @interface Named {
  String value() default "";
}
```

你可以创建你自己的限定符，或者直接使用 `@Named`。

```java
class ExpensiveCoffeeMaker {
  @Inject @Named("water") Heater waterHeater;
  @Inject @Named("hot plate") Heater hotPlateHeater;
  ...
}
```

给变量加上了 `@Named` 后，你可以在 `@Provides` 里通过 `@Named` 指定你想要的 Heater 类型，例如以下的 provideHotPlateHeater() 返回的就是 hotPlateHeater：

```java
@Provides @Named("hot plate") static Heater provideHotPlateHeater() {
  return new ElectricHeater(70);
}

@Provides @Named("water") static Heater provideWaterHeater() {
  return new ElectricHeater(93);
}
```

### 未满足条件的绑定

如果你想要 component 在没有满足某个绑定的情况下还能正常使用，你可以使用 `@BindsOptionalOf` 。

```java
@BindsOptionalOf abstract CoffeeCozy optionalCozy();
```

这意味着 @Inject 的构造函数和变量以及 @Provides 方法会依赖于一个 Optional<CoffeeCozy> 对象。如果 component 里有 CoffeeCozy 的绑定，那么这个 Optional 是 present；如果没有，那么这个 Optional 是 absent。

你可以 inject 以下几种类型：

```
Optional<CoffeeCozy> (unless there is a @Nullable binding for CoffeeCozy; see below)
Optional<Provider<CoffeeCozy>>
Optional<Lazy<CoffeeCozy>>
Optional<Provider<Lazy<CoffeeCozy>>>
```

如果有 CoffeeCozy 的绑定，但是绑定的是 null，那么会在编译时抛出错误。你可以换种方式进行绑定，Provider 和 Lazy 的 get() 方法是允许返回 null 的。

你可以使用 Guava 的 Optional 或者 Java 8 的 Optional。

### 注入对象

在你创建 component 时可能会有数据想要注入进去，例如，在用户登录后能拿到用户名, 你希望把用户名注入进去。你可以在 Builder 里添加带有 `@BindsInstance` 注释的方法，并在创建 component 时调用该方法注入用户名。

```java
@Component(modules = AppModule.class)
interface AppComponent {
  App app();

  @Component.Builder
  interface Builder {
    @BindsInstance Builder userName(@UserName String userName);
    AppComponent build();
  }
}

public static void main(String[] args) {
  if (args.length > 1) { exit(1); }
  App app = DaggerAppComponent
      .builder()
      .userName(args[0])
      .build()
      .app();
  app.run();
}
```

在创建 component 时，所有的带有 `@BindsInstance` 注解的方法都必须要调用。

### 编译时检查

Dagger 注解处理器如果检测到绑定异常或者未完成，会抛出编译错误。例如以下代码没有提供 Executor 的绑定:

```java
@Module
class DripCoffeeModule {
  @Provides static Heater provideHeater(Executor executor) {
    return new CpuHeater(executor);
  }
}
```

在编译时，会报出以下错误：

```
[ERROR] COMPILATION ERROR :
[ERROR] error: java.util.concurrent.Executor cannot be provided without an @Provides-annotated method.
```

### 编译时生成代码

Dagger 的注解处理器可能会生成名称类似 CoffeeMaker_Factory.java 或者 CoffeeMaker_MembersInjector.java 的类文件。你不需要使用这些文件，但是这些文件有助于你进行 debug。你唯一需要使用的文件就是带有 Dagger 前缀的 component。

---

## 使用 Android 特性

### 引入

```
dependencies {
    compile 'com.google.dagger:dagger-android:2.14.1'
    compile 'com.google.dagger:dagger-android-support:2.14.1' // if you use the support libraries
    annotationProcessor 'com.google.dagger:dagger-android-processor:2.14.1'
}
```

### dagger.android

在 Android 中使用 Dagger 的一个难点在于，Android framework 类是被 Android 系统创建的，比如 Activity 和 Fragment，如果要使用 Dagger 你必须要在生命周期的方法内手动注入对象进去。最终代码可能像这样：

```Java
public class FrombulationActivity extends Activity {
  @Inject Frombulator frombulator;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // DO THIS FIRST. Otherwise frombulator might be null!
    ((SomeApplicationBaseType) getContext().getApplicationContext())
        .getApplicationComponent()
        .newActivityComponentBuilder()
        .activity(this)
        .build()
        .inject(this);
    // ... now you can write the exciting code
  }
}
```

以上代码有几点问题：

1. 复制粘贴的代码会使得之后的重构变复杂。如果越来越多的人单纯的复制粘贴那段代码，越来越少的人会真正知道那段代码的作用；
2. 更重要的是，在注入时你的类（例如这里的 FrombulationActivity）需要知道它的注入器是什么。即使这些是通过 interfaces 而不是具体类型实现的，但这依然打破了依赖注入的核心原则：一个类不应该知道它是怎么注入的。

dagger.android 的类提供了一种办法简化这种情况：

### Injecting Activity objects

1.在你的 application component 里注册 AndroidInjectionModule；

2.创建一个继承自 AndroidInjector<YourActivity> 的 @Subcomponent，并在里面加上继承自 AndroidInjector.Builder<YourActivity> 的 @Subcomponent.Builder：

```Java
@Subcomponent(modules = ...)
public interface YourActivitySubcomponent extends AndroidInjector<YourActivity> {
  @Subcomponent.Builder
  public abstract class Builder extends AndroidInjector.Builder<YourActivity> {}
}
```

3.创建一个 YourActivityModule 并将 subcomponents 绑定到 YourActivitySubcomponent：

```java
@Module(subcomponents = YourActivitySubcomponent.class)
abstract class YourActivityModule {
  @Binds
  @IntoMap
  @ActivityKey(YourActivity.class)
  abstract AndroidInjector.Factory<? extends Activity>
      bindYourActivityInjectorFactory(YourActivitySubcomponent.Builder builder);
}

@Component(modules = {..., YourActivityModule.class})
interface YourApplicationComponent {}
```

注意：如果你的 subcomponent 和它的 builder 除了第 2 步提到的 Builder 外没有其他的方法或者类型，你可以使用 @ContributesAndroidInjector。你可以略过第 2 步和第 3 步，创建一个 abstract 的 module 方法，返回值设成你的 activity，并加上 @ContributesAndroidInjector 注解，并指定 modules。

```java
@ActivityScope
@ContributesAndroidInjector(modules = { /* modules to install into the subcomponent */ })
abstract YourActivity contributeYourActivityInjector();
```

4.让你的 Application 实现 HasActivityInjector， @Inject 一个 DispatchingAndroidInjector<Activity> 并在 `activityInjector()` 方法里返回：

```java
public class YourApplication extends Application implements HasActivityInjector {
  @Inject DispatchingAndroidInjector<Activity> dispatchingActivityInjector;

  @Override
  public void onCreate() {
    super.onCreate();
    DaggerYourApplicationComponent.create()
        .inject(this);
  }

  @Override
  public AndroidInjector<Activity> activityInjector() {
    return dispatchingActivityInjector;
  }
}
```

5.在你的 Activity 的 `onCreate()` 方法里，先调用 `AndroidInjection.inject(this)` 再调用 `super.onCreate()`：

```java
public class YourActivity extends Activity {
  public void onCreate(Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);
  }
}
```

### 实现原理

AndroidInjection.inject() 会从 Application 里获取一个 DispatchingAndroidInjector<Activity> 并把你的 activity 传给 inject(Activity)。

DispatchingAndroidInjector 会去 AndroidInjector.Factory 里找到你的 activity 的类的 Builder (在这个例子中是 YourActivitySubcomponent.Builder)，接着创建一个 AndroidInjector (在这个例子中是 YourActivitySubcomponent)，并把 activity 传给 inject(YourActivity)。

### Injecting Fragment objects

注入 Fragment 和注入 Activity 的流程类似。创建 subcomponent 的方式不变，把你的 Activity 类型参数改成 Fragment，把 @ActivityKey 改成 @FragmentKey，把 HasActivityInjector 改成 HasFragmentInjector。

你需要在 Fragments 的 onAttach() 事件里进行注入。

不像 Activitys 的 modules 那样，你可以给你的 Fragments 选择你想要安装的 modules。你可以设置你的 Fragment component 继承自其它 Fragment 的 component，Activity 的 component，或者 Application 的 component。在确定下你的 component 后，实现 HasFragmentInjector 接口。例如，如果你的 Fragment 需要 YourActivitySubcomponent 里的绑定:

```java
public class YourActivity extends Activity
    implements HasFragmentInjector {
  @Inject DispatchingAndroidInjector<Fragment> fragmentInjector;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);
    // ...
  }

  @Override
  public AndroidInjector<Fragment> fragmentInjector() {
    return fragmentInjector;
  }
}

public class YourFragment extends Fragment {
  @Inject SomeDependency someDep;

  @Override
  public void onAttach(Activity activity) {
    AndroidInjection.inject(this);
    super.onAttach(activity);
    // ...
  }
}

@Subcomponent(modules = ...)
public interface YourFragmentSubcomponent extends AndroidInjector<YourFragment> {
  @Subcomponent.Builder
  public abstract class Builder extends AndroidInjector.Builder<YourFragment> {}
}

@Module(subcomponents = YourFragmentSubcomponent.class)
abstract class YourFragmentModule {
  @Binds
  @IntoMap
  @FragmentKey(YourFragment.class)
  abstract AndroidInjector.Factory<? extends Fragment>
      bindYourFragmentInjectorFactory(YourFragmentSubcomponent.Builder builder);
}

@Subcomponent(modules = { YourFragmentModule.class, ... }
public interface YourActivityOrYourApplicationComponent { ... }
```

### 基本的 Framework 类型

因为 DispatchingAndroidInjector 会在运行期查找合适的 AndroidInjector.Factory，基类需要像调用 AndroidInjection.inject() 那样实现 HasActivityInjector/HasFragmentInjector/etc。

你的子类需要做的是绑定合适的 @Subcomponent。如果你没有复杂的逻辑，你可以直接使用 Dagger 提供的一些基础的类型，例如 DaggerActivity 和 DaggerFragment。Dagger 还提供了 DaggerApplication，你所需要做的是继承 DaggerApplication，重写 applicationInjector() 方法并返回对应的 component。

Dagger 还提供了以下类：

* DaggerService 和 DaggerIntentService
* DaggerBroadcastReceiver
* DaggerContentProvider

注意：DaggerBroadcastReceiver 只能用在注册在 AndroidManifest.xml 里的 BroadcastReceiver。如果是用代码生成的，要 inject 构造函数。

### Support 库

如果你用了 Android support 库，对应的 Dagger 类放在 dagger.android.support 里。support Fragment 需要绑定 AndroidInjector.Factory<? extends android.support.v4.app.Fragment>, AppCompat 需要实现 AndroidInjector.Factory<? extends Activity> 而不是 <? extends AppCompatActivity> (或者 FragmentActivity)。

### inject 时机

要尽可能的使用构造函数的注入，因为 javac 会确保所有变量在设置好后再被引用，这样能避免出现 NullPointerExceptions。DaggerActivity 应该在 onCreate() 里立即调用 AndroidInjection.inject()，接着再调用 super.onCreate()，DaggerFragment 在 onAttach() 的调用时机也是一样的。

在 super.onCreate() 之前调用 AndroidInjection.inject() 是很重要的，因为在 configuration change 时，super.onCreate() 会从上一个 activity 实例里取出 Fragments 并进行 attach，在这个时候会注入 Fragments。为了让 Fragment 能成功注入，Activity 必须已经完成注入。

---
