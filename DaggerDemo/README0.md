# DaggerDemo

Dagger 的学习笔记

相关链接：

<https://google.github.io/dagger/>  
<https://fernandocejas.com/2015/04/11/tasting-dagger-2-on-android/>  
<http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0519/2892.html>  

---

### 引入

```
dependencies {
    compile 'com.google.dagger:dagger:2.14.1'
    annotationProcessor 'com.google.dagger:dagger-compiler:2.14.1'
}
```

### Inject

给构造函数添加 `@Inject` 注解后，当你需要这个类的新的实例时，Dagger 会获取到构造函数里需要的值并调用构造函数生成一个新的实例。

```java
class Thermosiphon implements Pump {
  private final Heater heater;

  @Inject
  Thermosiphon(Heater heater) {
    this.heater = heater;
  }

  ...
}
```

Dagger 可以直接注入变量。下面的例子中，Dagger 会生成一个 Heater 实例和一个 Pump 实例。

如果你的类里有 `@Inject` 注解的变量，但是没有 `@Inject` 注解的构造函数，Dagger 会在需要时注入这些变量，但是不会创建新的实例。添加一个无参数的构造函数并加上 `@Inject` 注解，Dagger 就会创建一个新的实例。

```java
class CoffeeMaker {
  @Inject Heater heater;
  @Inject Pump pump;

  ...
}

class CoffeeMaker {
  @Inject Heater heater;
  @Inject Pump pump;

  ...
  @Inject
  CoffeeMaker(){
  }
}
```

### Provide

Dagger 会满足大部分的依赖需求，例如当你需要一个 CoffeeMaker 实例时，Dagger 会调用 `new CoffeeMaker()` 创建一个实例并设置到你要注入的变量里。

但是 `@Inject` 也有局限性：

1. 不能创建 Interfaces；
2. 第三方库的类不能加注解；
3. 有些对象需要经过配置后再使用。

对于以上几种 `@Inject` 无法满足的情况，可以使用带有 `@Provides` 注解的方法，Dagger 会根据方法返回值的类型去满足需要注入的地方。

例如，当 Dagger 需要一个 Heater 实例时，`provideHeater()` 会被调用：

```java
@Provides static Heater provideHeater() {
  return new ElectricHeater();
}
```

`@Provides` 注解的方法可以有对自己的依赖。当你需要 Pump 实例时，会返回一个 Thermosiphon 实例：

```java
@Provides static Pump providePump(Thermosiphon pump) {
  return pump;
}
```

所有 `@Provides` 注解的方法都必须放在 module 类里，这些类要有 `@Module` 注解。

```java
@Module
class DripCoffeeModule {
  @Provides static Heater provideHeater() {
    return new ElectricHeater();
  }

  @Provides static Pump providePump(Thermosiphon pump) {
    return pump;
  }
}
```

方便起见，`@Provides` 方法的方法名通常是带有 provide 前缀，`@Module` 类的类名通常是带有 Module 后缀。

### Component

带有 `@Inject` 和 `@Provides` 注解的类提供了一系列的实例对象，通过 component 可以将他们和消费依赖对象的组件连接起来。

要定义一个 component，需要创建一个 interface 并添加 `@Component` 注解，在注解里指定 module 类。component 需要什么类型的数据，就在 component 里添加一个返回值是该类型的无参数的方法。

```java
@Component(modules = DripCoffeeModule.class)
interface CoffeeShop {
  CoffeeMaker maker();
}
```

Dagger 会实现你定义的 component 并生成一个 "Dagger" 前缀加上类名的类文件。调用 builder() 方法并指定 Module 实例后，就能获得 component 的实例。

```java
CoffeeShop coffeeShop = DaggerCoffeeShop.builder()
    .dripCoffeeModule(new DripCoffeeModule())
    .build();
```

需要注意的是，如果你的 `@Component` 不是在文件的顶层，那么生成的 component 文件名会带有每一层的名称，并用下划线连接起来，例如以下代码生成出来的 component 名称是 DaggerFoo_Bar_BazComponent。

```java
class Foo {
  static class Bar {
    @Component
    interface BazComponent {}
  }
}
```

如果你的 module 类有可访问的默认的构造函数，那么可以不给 builder 指定 module 实例，builder 会自动创建这个 module 的实例。

如果你的 module 类的所有 `@Provides` 方法都是静态的，那么 builder 不需要指定 module 的实例。

如果你的依赖关系都不需要指定 module 实例，那么生成的 component 类会有一个 `create()` 方法，你可以通过这个方法生成 component 类的实例。

```java
CoffeeShop coffeeShop = DaggerCoffeeShop.create();
```

现在，你可以通过 CoffeeShop 去获取一个已注入好的 CoffeeMaker：

```java
CoffeeShop coffeeShop = DaggerCoffeeShop.create();
coffeeShop.maker().brew();
```

---
