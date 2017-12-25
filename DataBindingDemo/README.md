# DataBindingDemo

Data Binding 的学习笔记

以下内容绝大部分翻译自官方文档：

<https://developer.android.com/topic/libraries/data-binding/index.html>

少部分来自：

<http://connorlin.github.io/2016/07/02/Android-Data-Binding-%E7%B3%BB%E5%88%97-%E4%B8%80-%E8%AF%A6%E7%BB%86%E4%BB%8B%E7%BB%8D%E4%B8%8E%E4%BD%BF%E7%94%A8/>

----

### 使用条件

1. Android 2.1 (API level 7+) 及以上的系统
2. Android Gradle 的版本号不低于 1.5.0-alpha1 （root 目录下的 build.gradle 的 `com.android.tools.build:gradle:xxxxx`）
3. Android Studio 版本号不低于 1.3

### 开始使用

app 文件夹里的 build.gradle 加上：

```
android {
    dataBinding {
        enabled = true
    }
}
```

如果你项目里的 module 引用了需要用到 data binding 的库，你同样需要在 module 的 build.gradle 里加上这些配置

### Data Binding 编译器 V2

Android Gradle 在 3.1.0 Canary 6 版本发布了一个新的 Data Binding 编译器。如果要使用新版本的编译器，你需要在 gradle.properties 里加入以下配置:

```
android.databinding.enableV2=true
```

在新版本的编译器里:

* ViewBinding 类在编译 java 之前就生成好了。这样的话你在编译 Data Binding 文件时，会先抛出 Data Binding 的错误，避免了编译 Data Binding 时被其他的 java 编译错误所干扰。

* 在 V1 版本里，引用的库的 binding 类在 app 编译时会重新生成。在 V2 版本，引用的库会保留他们生成的 binding 类以及对应的 mapper 信息。当你的项目里引用了多个用到 data binding 的库时，这样可以提高编译的效率。

需要注意的是，新的编译器和旧版本不兼容，所以在 V1 版本编译的库不能在 V2 版本的编译器里使用，反之亦然。

------

### 开始编写 Data Binding 的布局文件

Data-binding 的布局文件包裹在一个叫做 layout 的标签里，里面有 data 元素和一个 root view。root view 其实就是平时写的布局文件。

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <variable
            name="user"
            type="com.cashow.databindingdemo.User" />
    </data>
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        android:gravity="center">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@{user.firstName}"/>

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:text="@{user.fullName}"/>
    </LinearLayout>
</layout>
```
```java
public class User {
    public String firstName;
    public String lastName;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
```

#### 定义变量

下面这段代码是在 data 里定义了一个 user 变量，这个 user 变量可以在 view 里被引用

```xml
<variable
    name="user"
    type="com.cashow.databindingdemo.User" />
```

#### 使用变量

view 里通过 "@{}" 调用 data 里定义的变量

```xml
<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="@{user.firstName}"/>

<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="@{user.fullName}"/>
```

如果你的 User 类里含有 `firstName` 变量，或者 `getFirstName()` 方法，或者 `firstName()` 方法，你可以使用 `@{user.firstName}` 调用他们

---

### 绑定数据

默认情况下，Binding 类会根据布局文件的名称自动生成，生成规则是将布局文件名转成大骆驼拼写法，并加上 "Binding" 后缀。如果你的布局文件名是 `activity_main.xml`，那么会自动生成名为 `ActivityMainBinding.java` 的 Binding 类。这个类包含了所有的布局文件里的所有属性（比如 user 变量），并且自带给这些变量赋值的方法。实现数据绑定的方法如下：

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

    User user = new User("Hello", "world");
    binding.setUser(user);
}
```

如果要将布局 inflate 出来但不设置成 ContentView：

```java
ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
```

如果你要在 ListView 或者 RecyclerView 里 inflate 布局文件：

```java
ListItemBinding binding = ListItemBinding.inflate(layoutInflater, viewGroup, false);
//or
ListItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item, viewGroup, false);
```

---

### 绑定方法

view 里的事件可以和变量的方法绑定起来，就像是 android:onClick 可以赋值成 Activity 内的方法。这个和 View#onClick 的主要优势在于，这里的绑定是发生在编译期间，所以如果绑定的方法不存在或者传的参数不对，会发生编译错误。

如果你的 User 类里有 `onClickFriend()` 方法：

```java
public class User {
    public void onClickFriend(View view) {
        Log.d("User", "onClickFriend");
    }
}
```

你可以通过 `@{user::onClickFriend}` 将点击事件和这个方法进行绑定：

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <variable
            name="user"
            type="com.cashow.databindingdemo.User" />
    </data>
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        android:gravity="center">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:onClick="@{user::onClickFriend}"
            android:text="@{user.fullName}"/>
    </LinearLayout>
</layout>
```

需要注意的是，绑定事件的参数必须要和方法的参数一致。

### 绑定 Listener

绑定 Listener 只要求 Listener 的返回值和事件的返回值一致即可。这个功能是在 Android Gradle 2.0 引入的。

```java
public class Presenter {
    public void onSaveClick(Task task){}
}
```

```xml
<?xml version="1.0" encoding="utf-8"?>
 <layout xmlns:android="http://schemas.android.com/apk/res/android">
     <data>
         <variable name="task" type="com.android.example.Task" />
         <variable name="presenter" type="com.android.example.Presenter" />
     </data>
     <LinearLayout
       android:layout_width="match_parent"
       android:layout_height="match_parent">
         <Button
           android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           android:onClick="@{() -> presenter.onSaveClick(task)}" />
     </LinearLayout>
 </layout>
```

在上面的例子中，我们在绑定 Listener 时没有传递 `onClick(View view)` 方法里的 view 变量。有两种绑定的方法可供你选择：你可以忽略所有的参数，或者指明所有的参数。

```
public class Presenter {
    public void onSaveClick(Task task){}
}
android:onClick="@{(view) -> presenter.onSaveClick(task)}"

public class Presenter {
    public void onSaveClick(View view, Task task){}
}
android:onClick="@{(theView) -> presenter.onSaveClick(theView, task)}"

public class Presenter {
    public void onCompletedChanged(Task task, boolean completed){}
}
<CheckBox
    android:onCheckedChanged="@{(cb, isChecked) -> presenter.completeChanged(task, isChecked)}" />
```

如果变量是 null 导致表达式不能正常执行，Data Binding 会返回返回值对应的 java 类型的默认值，例如返回值是 int 类型的返回 0，boolean 类型的返回 false。

如果你需要在事件发生时加一些判断条件，可以返回 void

```
android:onClick="@{(v) -> v.isVisible() ? doSomething() : void}"
```

----

### 布局详情

#### Imports

在 data 标签里加上 import 标签后，就可以在 view 里调用这些类

```xml
<data>
    <import type="android.view.View"/>
</data>
```

当有多个类的名称重复时，需要通过 "alias:" 将类重命名

```xml
<import type="android.view.View"/>
<import type="com.example.real.estate.View"
        alias="Vista"/>
```

现在，你可以通过 Vista 调用 com.example.real.estate.View，通过 View 调用 android.view.View。

import 进来的类可以用在变量的 type 上：

```xml
<data>
    <import type="com.example.User"/>
    <import type="java.util.List"/>
    <variable name="user" type="User"/>
    <variable name="userList" type="List&lt;User&gt;"/>
</data>
```

需要注意的是，Android Studio 目前还不能完全处理 imports 的类，所以可能不会出现自动补全，但你的应用还是能正常编译。

另外，我在开发过程中移动了 User.java 文件，userList 的 type 从 `List&lt;com.cashow.databindingdemo.model.User&gt;` 变成了 `com.cashow.databindingdemo.util.User`，所以当你移动文件后需要仔细检查变量的 type 有没有问题。

#### Variables

data binding 编译期间会检查变量的 types，如果你的变量实现了 Observable 或者是个 observable collection，那这个变量会被观察，在数据变动时更新 UI。

当你有不同配置下的布局文件（例如横屏或者竖屏），这些文件里的变量会被合并。在这些布局文件里不能有冲突的变量。

生成的 binding 类对于每个变量都会有一个 set 方法和 get 方法。

有一个特殊的变量是 context，这个变量是在进行数据绑定时生成的，context 的值是 root View 的 getContext()。如果你定义了名为 context 的变量，原先的 context 变量会被覆盖。

---

### 自定义 Binding 类的名称

默认情况下，Binding 类会根据布局文件的名称自动生成，生成规则是将布局文件名转成大骆驼拼写法，并加上 "Binding" 后缀。

生成的 Binding 类文件可以在 app/build/generated/source/apt/debug/你的包名/databinding 里找到。

如果你的文件名是 activity_main.xml，那么会生成 ActivityMainBinding.java 文件。如果你的包名是  com.example.my.app, 那么这个文件会放在 com.example.my.app.databinding 里，即这个类的包名是 com.example.my.app.databinding。

Binding 类可以被重命名或者放到其他的包里，例如:

```xml
<data class="ContactItem">
</data>
```

这会生成一个名叫 ContactItem 的 binding 类，这个类仍然在 com.example.my.app.databinding 里。

```xml
<data class=".ContactItem">
</data>
```

这会生成一个名叫 ContactItem 的 binding 类，这个类在 com.example.my.app 里。

```xml
<data class="com.example.ContactItem">
</data>
```

这会生成一个名叫 ContactItem 的 binding 类，这个类在 com.example 里。

---

### Includes

Variables 可以传进 included 的布局里：

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:bind="http://schemas.android.com/apk/res-auto">
    <data>
        <variable
            name="user"
            type="com.cashow.databindingdemo.User" />
    </data>
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        android:gravity="center">

        <include layout="@layout/layout_first_name"
            bind:user="@{user}"/>
    </LinearLayout>
</layout>

<!-- layout_first_name.xml -->
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <variable
            name="user"
            type="com.cashow.databindingdemo.User" />
    </data>
    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@{user.firstName}"/>
</layout>
```

Data binding 并不支持 include 作为 merge 的直接子元素：

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:bind="http://schemas.android.com/apk/res-auto">
   <data>
       <variable name="user" type="com.example.User"/>
   </data>
   <merge>
       <include layout="@layout/name"
           bind:user="@{user}"/>
       <include layout="@layout/contact"
           bind:user="@{user}"/>
   </merge>
</layout>
```

---

### 表达式

java 中的加减乘除以及位运算等等的操作符都可以正常使用。

#### 不能使用的操作符

* this
* super
* new
* 显式泛型调用 &lt;T&gt;

#### Null 处理

"??" 前面的值是 null 时，返回 "??" 后面的值

```
android:text="@{user.displayName ?? user.lastName}"
```

上面的代码等同于下面的代码：

```
android:text="@{user.displayName != null ? user.displayName : user.lastName}"
```

#### 关于空指针异常

生成好的 data binding 代码会自动检查 null 值，避免出现 NullPointerException。例如，在 `@{user.name}` 中，假如 user 是 null，user.name 会被赋值成 string 类型的默认值 null。如果你引用的是 int 类型的 user.age，那么会返回 int 类型的默认值 0。

#### 容器类

以下类型的容器可以用 `[]` 去获取里面的数据：

arrays, lists, sparse lists 和 maps

```xml
<data>
    <import type="android.util.SparseArray"/>
    <import type="java.util.Map"/>
    <import type="java.util.List"/>
    <variable name="list" type="List&lt;String&gt;"/>
    <variable name="sparse" type="SparseArray&lt;String&gt;"/>
    <variable name="map" type="Map&lt;String, String&gt;"/>
    <variable name="index" type="int"/>
    <variable name="key" type="String"/>
</data>
…
android:text="@{list[index]}"
…
android:text="@{sparse[index]}"
…
android:text="@{map[key]}"
```

#### 字符串常量

当你要引用字符串常量时，可以使用以下几种方式：

1. 单引号里使用双引号
2. 双引号里使用 \`
3. 双引号里使用单引号

```xml
android:text='@{map["firstName"]}'
android:text="@{map[`firstName`}"

// 下面这个我自己测试的时候编译不过
android:text="@{map['firstName']}"
```

#### 引用资源

你可以在表达式中使用普通的语法来引用资源：

```
android:padding="@{large? @dimen/largePadding : @dimen/smallPadding}"
```

对于普通字符串和 plurals 类型的字符串，可以这样调用：

```
android:text="@{@string/nameFormat(firstName, lastName)}"
android:text="@{@plurals/banana(bananaCount)}"
```

当 plurals 字符串需要多个参数时：

```
Have an orange
Have %d oranges

android:text="@{@plurals/orange(orangeCount, orangeCount)}"
```

---

### 数据对象

任何简单的Java对象 (POJO) 都可以用在 data binding 上。如果你想要让你的数据对象在每次修改时同步更新 UI，有3种方式可以实现： Observable objects，observable fields 和 observable collections。

#### Observable Objects

你的类如果实现了 Observable 接口，在其他的地方可以向你的类添加数据变更的监听，但是你需要自己处理什么时候发数据更新的回调。如果你的类继承 BaseObservable，你的类会自动监听带有 `@Bindable` 注释的变量或方法。

```java
public class ObservableUser extends BaseObservable {
    public String firstName;
    public String lastName;

    @Bindable
    public String getFirstName() {
        return firstName;
    }

    @Bindable
    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        notifyPropertyChanged(BR.firstName);
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(BR.lastName);
    }
}
```

`@Bindable` 注释会在编译时生成一个 BR 类，在数据更新时你需要调用 `notifyPropertyChanged()` 去更新 BR 类里面的变量。

需要注意的是，加了 `@Bindable` 后要先 build 一次项目，才能调用 `BR.firstName` 和 `BR.lastName`

#### ObservableFields

如果你并不想修改你的类，或者类里面只有少数几个变量需要被监听，你可以使用 ObservableField, ObservableBoolean, ObservableByte, ObservableChar, ObservableShort, ObservableInt, ObservableLong, ObservableFloat, ObservableDouble 和 ObservableParcelable。

```Java
public class ObservableFieldsUser {
    public final ObservableField<String> firstName = new ObservableField<>();
    public final ObservableField<String> lastName = new ObservableField<>();
    public final ObservableInt age = new ObservableInt();
}
```

设置值与获取值：

```java
user.firstName.set("Google");
int age = user.age.get();
```

就是这么简单~

#### Observable Collections

当你要使用 Map 存储数据时，可以使用 ObservableArrayMap 去监听 Map 里数据的变化

```java
ObservableArrayMap<String, Object> user = new ObservableArrayMap<>();
user.put("firstName", "Google");
user.put("lastName", "Inc.");
user.put("age", 17);
```

```xml
<data>
    <import type="android.databinding.ObservableMap"/>
    <variable name="user" type="ObservableMap&lt;String, Object&gt;"/>
</data>

<TextView
   android:text='@{user["lastName"]}'
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"/>
<TextView
   android:text='@{String.valueOf(1 + (Integer)user["age"])}'
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"/>
```

当你要用 List 存储数据时，你可以使用 ObservableArrayList:

```java
ObservableArrayList<Object> user = new ObservableArrayList<>();
user.add("Google");
user.add("Inc.");
user.add(17);
```

```xml
<data>
    <import type="android.databinding.ObservableList"/>
    <import type="com.example.my.app.Fields"/>
    <variable name="user" type="ObservableList&lt;Object&gt;"/>
</data>
…
<TextView
   android:text='@{user[Fields.LAST_NAME]}'
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"/>
<TextView
   android:text='@{String.valueOf(1 + (Integer)user[Fields.AGE])}'
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"/>
```

---

### Binding 类

生成的 binding 类会将布局里声明的变量和布局里的 view 绑定起来。所有生成的 binding 类都继承于 ViewDataBinding。

#### 创建 Binding 类

数据绑定应该在布局的 inflation 之后立即进行。有几种不同的方式进行数据绑定，其中最普遍的是使用 Binding 类的静态方法 `inflate()`，这会将布局 inflate 出来，并将变量和 view 绑定：

```java
MyLayoutBinding binding = MyLayoutBinding.inflate(layoutInflater);
MyLayoutBinding binding = MyLayoutBinding.inflate(layoutInflater, viewGroup, false);
```

如果布局是通过其他方式 inflate 出来的，可以调用 `bind()` 进行绑定:

```Java
MyLayoutBinding binding = MyLayoutBinding.bind(viewRoot);
```

有时候绑定关系不能提前确定下来，这时候可以使用 DataBindingUtil :

```Java
ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, layoutId,
    parent, attachToParent);
ViewDataBinding binding = DataBindingUtil.bindTo(viewRoot, layoutId);
```

#### 带有 id 的 View

每一个带有 id 的 view，在 binding 类里都会有一个对应的 public final 变量，通过这个变量可以获取到这个 view。相比于多次调用 `findViewById()`，使用这些变量效率会更高。

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android">
   <data>
       <variable name="user" type="com.example.User"/>
   </data>
   <LinearLayout
       android:orientation="vertical"
       android:layout_width="match_parent"
       android:layout_height="match_parent">
       <TextView android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           android:text="@{user.firstName}"
   android:id="@+id/firstName"/>
       <TextView android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           android:text="@{user.lastName}"
  android:id="@+id/lastName"/>
   </LinearLayout>
</layout>
```

上面的布局会生成以下变量：

```java
public final TextView firstName;
public final TextView lastName;

...
// 你可以直接调用 binding 里的这些变量
ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
binding.firstName.setText("aa");
binding.lastName.setText("bb");
```

#### Variables

每一个变量都会有一个 set 方法和 get 方法。

```xml
<data>
    <import type="android.graphics.drawable.Drawable"/>
    <variable name="user"  type="com.example.User"/>
    <variable name="image" type="Drawable"/>
    <variable name="note"  type="String"/>
</data>
```

上面的布局会生成以下方法：

```java
public abstract com.example.User getUser();
public abstract void setUser(com.example.User user);
public abstract Drawable getImage();
public abstract void setImage(Drawable image);
public abstract String getNote();
public abstract void setNote(String note);
```

---

### ViewStubs

你需要使用 ViewStubProxy 类来代替 ViewStub。当你 inflate 一个新的布局时，必须为新的布局创建一个 binding。因此，ViewStubProxy 需要监听 ViewStub 的 ViewStub.OnInflateListener，并及时建立 binding。由于 ViewStub 只能有一个 OnInflateListener，你可以将你自己的 listener 设置在 ViewStubProxy 上，在 binding 建立之后， listener 就会被触发。

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <LinearLayout >
        <ViewStub
            android:id="@+id/view_stub"
            android:layout="@layout/include" />
    </LinearLayout>
</layout>
```

在 Java 代码中获取 binding 实例，为 ViewStubProy 注册 ViewStub.OnInflateListener 事件：

```java
mActivityViewStubBinding = DataBindingUtil.setContentView(this, R.layout.activity_view_stub);
mActivityViewStubBinding.viewStub.setOnInflateListener(new ViewStub.OnInflateListener() {
    @Override
    public void onInflate(ViewStub stub, View inflated) {
        IncludeBinding viewStubBinding = DataBindingUtil.bind(inflated);
        User user = new User("Connor", "Lin", 28);
        viewStubBinding.setUser(user);
    }
});
```

通过 ViewStubProxy 来 inflate ViewStub :

```java
public void inflate(View view) {
    if (!mActivityViewStubBinding.viewStub.isInflated()) {
        mActivityViewStubBinding.viewStub.getViewStub().inflate();
    }
}
```

---

### 一些数据绑定的技巧

#### 动态绑定

有时候，你并不知道你想要的 binding 类的名称。例如，RecyclerView.Adapter 需要处理不同的布局，这样的话他并不知道应该使用哪个具体的 binding 类，但在 `onBindViewHolder(VH, int)` 的时候，又需要进行数据绑定。在这个例子里，所有 RecyclerView 内部绑定的布局都有一个 "item" 变量。通过调用 BindingHolder 内置的 `getBinding()` 方法可以获得你要的 ViewDataBinding 类。

```java
public void onBindViewHolder(BindingHolder holder, int position) {
   final T item = mItems.get(position);
   holder.getBinding().setVariable(BR.item, item);
   holder.getBinding().executePendingBindings();
}
```

#### 立即绑定

当变量或者 observable 发生变化时，binding 类会在下一帧触发数据变化。如果你需要立即执行，可以调用 `executePendingBindings()` 方法。

#### 后台线程

只要你的数据不是容器类，你可以在后台线程里修改你的数据。Data binding 会将变量暂时转成局部量，防止出现同步问题。

---

### 属性设置

当绑定的数据发生变化时，binding 类会调用对应的 set 方法。

#### 自动设置属性

对于布局里的属性，data binding 会去找对应的 set 方法。例如，在布局里设置 android:text 时，会去找 `setText(String)` 方法。如果你的表达式返回的是 int，会去找 `setText(int)` 方法。需要注意的是，你的返回值必须要和找到的方法的参数保持一致。即使 data binding 没有找到对应的 set 方法，data binding 还是能正常工作。通过 data binding，你可以很方便地创建自定义的属性并在布局文件里配置这个属性。

```xml
<android.support.v4.widget.DrawerLayout
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:scrimColor="@{@color/scrim}"
    app:drawerListener="@{fragment.drawerListener}"/>
```

#### 重命名 set 方法

有些属性的 set 方法并没有和属性的名称对应上。对于这些属性，你可以通过 BindingMethods 注解将 set 方法和属性名对应起来。例如 `android:tint` 属性对应的 set 方法是 `setImageTintList(ColorStateList)`，而不是 `setTint()`。

```java
@BindingMethods({
       @BindingMethod(type = "android.widget.ImageView",
                      attribute = "android:tint",
                      method = "setImageTintList"),
})
```

大部分 android framework 里的属性都已经有对应的 set 方法了，所以你并不需要重命名 android framework 里属性的 set 方法。

#### 自定义 set 方法

有时候你想要自定义 set 方法，例如如果要修改 android:paddingLeft 属性，没有对应的 `setPaddingLeft()` 方法，而是需要调用 `setPadding(left, top, right, bottom)` 方法去实现。通过 `@BindingAdapter` 注解你可以实现自定义的 set 方法。

同样的，大部分的 android 属性都已经有 set 方法了。例如设置 paddingLeft：

```java
@BindingAdapter("android:paddingLeft")
public static void setPaddingLeft(View view, int padding) {
   view.setPadding(padding,
                   view.getPaddingTop(),
                   view.getPaddingRight(),
                   view.getPaddingBottom());
}
```

当存在冲突时，你自定义的 set 方法会覆盖掉默认的 set 方法。

你可以创建有多个参数的 adapters。

```java
@BindingAdapter({"bind:imageUrl", "bind:error"})
public static void loadImage(ImageView view, String url, Drawable error) {
   Picasso.with(view.getContext()).load(url).error(error).into(view);
}
```

```xml
<ImageView app:imageUrl="@{venue.imageUrl}"
app:error="@{@drawable/venueError}"/>
```

当同时设置了 imageUrl 和 error 并且和属性的类型 `loadImage()` 方法里的参数对应得上，那么 `loadImage()` 方法会被调用。

需要注意的是，自定义的命名空间会被忽略。

Binding adapter 方法可以在参数里指明旧值，你只需要把旧值放在新值前面即可：

```java
@BindingAdapter("android:paddingLeft")
public static void setPaddingLeft(View view, int oldPadding, int newPadding) {
   if (oldPadding != newPadding) {
       view.setPadding(newPadding,
                       view.getPaddingTop(),
                       view.getPaddingRight(),
                       view.getPaddingBottom());
   }
}
```

事件的 handlers 只能用于接口或者只有一个抽象方法的抽象类

```java
@BindingAdapter("android:onLayoutChange")
public static void setOnLayoutChangeListener(View view, View.OnLayoutChangeListener oldValue,
       View.OnLayoutChangeListener newValue) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        if (oldValue != null) {
            view.removeOnLayoutChangeListener(oldValue);
        }
        if (newValue != null) {
            view.addOnLayoutChangeListener(newValue);
        }
    }
}
```

当一个 listener 有多个方法时，必须要将里面的方法单独拆成 listener。例如 View.OnAttachStateChangeListener 有两个方法: `onViewAttachedToWindow()` 和 `onViewDetachedFromWindow()`，我们需要创建两个 listener。

```java
@TargetApi(VERSION_CODES.HONEYCOMB_MR1)
public interface OnViewDetachedFromWindow {
    void onViewDetachedFromWindow(View v);
}

@TargetApi(VERSION_CODES.HONEYCOMB_MR1)
public interface OnViewAttachedToWindow {
    void onViewAttachedToWindow(View v);
}
```

因为这个两个 listener 修改一个会影响到另外一个，我们必须要有三个不同的 BindingAdapter：

```java
@BindingAdapter("android:onViewAttachedToWindow")
public static void setListener(View view, OnViewAttachedToWindow attached) {
    setListener(view, null, attached);
}

@BindingAdapter("android:onViewDetachedFromWindow")
public static void setListener(View view, OnViewDetachedFromWindow detached) {
    setListener(view, detached, null);
}

@BindingAdapter({"android:onViewDetachedFromWindow", "android:onViewAttachedToWindow"})
public static void setListener(View view, final OnViewDetachedFromWindow detach,
        final OnViewAttachedToWindow attach) {
    if (VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR1) {
        final OnAttachStateChangeListener newListener;
        if (detach == null && attach == null) {
            newListener = null;
        } else {
            newListener = new OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                    if (attach != null) {
                        attach.onViewAttachedToWindow(v);
                    }
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    if (detach != null) {
                        detach.onViewDetachedFromWindow(v);
                    }
                }
            };
        }
        final OnAttachStateChangeListener oldListener = ListenerUtil.trackListener(view,
                newListener, R.id.onAttachStateChangeListener);
        if (oldListener != null) {
            view.removeOnAttachStateChangeListener(oldListener);
        }
        if (newListener != null) {
            view.addOnAttachStateChangeListener(newListener);
        }
    }
}
```

上面这个例子比较复杂，因为 View 是通过 add 方法和 remove 方法去设置 View.OnAttachStateChangeListener的。通过 android.databinding.adapters.ListenerUtil 类可以记住上一次的 listener，之后在调用 remove 方法时就能找到之前设置的 listener。

通过 `@TargetApi` 注解，可以指定这些方法运行的系统版本。


---

### 转换器

#### 转换对象

当你在表达式里返回了一个对象，会根据属性名去找到合适的 set 方法，并将对象转换成这个 set 方法里参数的类型。

```xml
<TextView
   android:text='@{userMap["lastName"]}'
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"/>
```

上面的 userMap 返回了一个对象，这个对象会被强制转换成 `setText(CharSequence)` 方法里的 CharSequence 类型。如果你要调用 `setText(int resid)`，需要自己转换对象的类型。

#### 自定义转换

有时候在某些特定类型里会进行自动转换。例如，在设置 background 时：

```xml
<View
   android:background="@{isError ? @color/red : @color/white}"
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"/>
```

在这里，background 接受的是 Drawable 类型的数据，但是传的 color 是 int 类型。当你需要接收 Drawable 类型但接收到的是 int 类型时，这个 int 类型的数据会自动转成 ColorDrawable。这个转变是通过 `@BindingConversion` 注解实现的：

```java
@BindingConversion
public static ColorDrawable convertColorToDrawable(int color) {
   return new ColorDrawable(color);
}
```

需要注意的是，在设置属性时，你不能混用不同的类型：

```xml
<View
   android:background="@{isError ? @drawable/error : @color/white}"
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"/>
```
