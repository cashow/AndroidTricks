# CoordinatorLayoutDemo

CoordinatorLayout 的学习笔记

相关链接：

<https://www.jianshu.com/p/4a77ae4cd82f>  

---

### FloatingActionButton 与 Snackbar

CoordinatorLayout 可以用来配合 FloatingActionButton 的 layout_anchor 和 layout_gravity 属性创造出浮动效果。

* layout_anchor：指定参照物
* anchorGravity：指定相对于参照物的位置，设置为 bottom|right 则表示将 FloatingActionButton 放置于参照物的右下角。

FloatingActionButton 有一个默认的 behavior，在显示 Snackbar 的时候，FloatingActionButton 将自动产生向上移动的动画。

```xml
<android.support.design.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <android.support.design.widget.FloatingActionButton
        android:id="@+id/button_floating"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="16dp"
        android:src="@drawable/ic_done"
        android:layout_gravity="end|bottom"/>
</android.support.design.widget.CoordinatorLayout>
```

示例图：

![floating_action_button](https://github.com/cashow/AndroidTricks/blob/master/CoordinatorLayoutDemo/images/floating_action_button.gif)

---

### CoordinatorLayout 与 AppBarLayout

AppBarLayout 是一个垂直布局的 LinearLayout，它主要是为了实现 “Material Design” 风格的标题栏的特性。

```xml
<android.support.design.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <android.support.design.widget.AppBarLayout
        android:id="@+id/appbar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@color/colorPrimary">

        <android.support.v7.widget.Toolbar
            android:id="@+id/toolbar"
            android:layout_width="match_parent"
            android:layout_height="?attr/actionBarSize"
            app:titleTextColor="#fff"
            app:layout_scrollFlags="scroll|enterAlways" />
    </android.support.design.widget.AppBarLayout>

    <android.support.v7.widget.RecyclerView
        android:id="@+id/recyclerview"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_behavior="@string/appbar_scrolling_view_behavior"/>
</android.support.design.widget.CoordinatorLayout>
```

CoordinatorLayout 中可滚动的视图（如本例中的 RecyclerView），需要设置以下属性：

```xml
app:layout_behavior="@string/appbar_scrolling_view_behavior"
```

AppBarLayout 中的 View，如要想要滚动到屏幕外，必须设置 layout_scrollFlags：

```xml
app:layout_scrollFlags="scroll"
```

layout_scrollFlags 有以下几种属性：

#### scroll

隐藏的时候，先整体向上滚动，直到 AppBarLayout 完全隐藏，再开始滚动 Scrolling View；显示的时候，直到 Scrolling View 顶部完全出现后，再开始滚动 AppBarLayout 到完全显示。

![scroll](https://github.com/cashow/AndroidTricks/blob/master/CoordinatorLayoutDemo/images/scroll.gif)

#### enterAlways

与 scroll 类似（scroll|enterAlways），只不过向下滚动先显示 AppBarLayout 到完全，再滚动 Scrolling View。

![scroll_enterAlways](https://github.com/cashow/AndroidTricks/blob/master/CoordinatorLayoutDemo/images/scroll_enterAlways.gif)

#### enterAlwaysCollapsed

需要和 enterAlways 一起使用（scroll|enterAlways|enterAlwaysCollapsed），和 enterAlways 不一样的是，不会显示 AppBarLayout 到完全再滚动 Scrolling View，而是先滚动 AppBarLayout 到最小高度，再滚动 Scrolling View，最后再滚动 AppBarLayout 到完全显示。

注意：需要定义 View 的最小高度（minHeight）才有效果。

![scroll_enterAlways_enterAlwaysCollapsed](https://github.com/cashow/AndroidTricks/blob/master/CoordinatorLayoutDemo/images/scroll_enterAlways_enterAlwaysCollapsed.gif)

#### exitUntilCollapsed

顾名思义，定义了 AppBarLayout 消失的规则。发生向上滚动事件时，AppBarLayout 向上滚动退出直至最小高度（minHeight），然后 Scrolling View 开始滚动。也就是，AppBarLayout 不会完全退出屏幕。

![scroll_enterAlways_exitUntilCollapsed](https://github.com/cashow/AndroidTricks/blob/master/CoordinatorLayoutDemo/images/scroll_enterAlways_exitUntilCollapsed.gif)

enterAlwaysCollapsed 与 exitUntilCollapsed 在实际的使用中，更多的是与 CollapsingToolbarLayout 一起使用。
