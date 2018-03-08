# CoordinatorLayoutDemo

CoordinatorLayout 的学习笔记

相关链接：

<https://www.jianshu.com/p/4a77ae4cd82f>  
<https://www.jianshu.com/p/360fd368936d>  

---

demo 下载链接：

<http://7sbs06.com1.z0.glb.clouddn.com/coordinator_layout_demo.apk>

demo 二维码：

![qrcode](https://github.com/cashow/AndroidTricks/blob/master/CoordinatorLayoutDemo/qrcode.png)

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

与 enterAlwaysCollapsed 类似，定义了 AppBarLayout 消失的规则。发生向上滚动事件时，AppBarLayout 向上滚动退出直至最小高度（minHeight），然后 Scrolling View 开始滚动。也就是，AppBarLayout 不会完全退出屏幕。

![scroll_enterAlways_exitUntilCollapsed](https://github.com/cashow/AndroidTricks/blob/master/CoordinatorLayoutDemo/images/scroll_enterAlways_exitUntilCollapsed.gif)

enterAlwaysCollapsed 与 exitUntilCollapsed 在实际的使用中，更多的是与 CollapsingToolbarLayout 一起使用。

---

### CollapsingToolbarLayout

使用 CollapsingToolbarLayout 可以实现 Toolbar 的折叠效果。

```xml
<android.support.design.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <android.support.design.widget.AppBarLayout
        android:id="@+id/appbar"
        android:layout_width="match_parent"
        android:layout_height="150dp"
        android:fitsSystemWindows="true"
        android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar">

        <android.support.design.widget.CollapsingToolbarLayout
            android:id="@+id/collapsingtoolbarlayout"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:layout_scrollFlags="scroll|enterAlways">

            <android.support.v7.widget.Toolbar
                android:id="@+id/toolbar"
                android:layout_width="match_parent"
                android:layout_height="?attr/actionBarSize"
                app:layout_collapseMode="none"/>
        </android.support.design.widget.CollapsingToolbarLayout>
    </android.support.design.widget.AppBarLayout>

    <android.support.v7.widget.RecyclerView
        android:id="@+id/recyclerview"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_behavior="@string/appbar_scrolling_view_behavior" />
</android.support.design.widget.CoordinatorLayout>
```

上面有 2 个关键的属性：layout_scrollFlags 和 layout_collapseMode。

layout_collapseMode 有以下几种属性：
1. none：在滑动过程中，Toolbar 也会跟着上滑；
2. pin：当 CollapsingToolbarLayout 滑动到 Toolbar 的高度后，Toolbar 再开始往上滑；
3. parallax：在滑动过程中，Toolbar 也会跟着上滑，但是会有一定的视差，即 Toolbar 的滑动速度和 RecyclerView 的滑动速度不一样。可以通过 layout_collapseParallaxMultiplier 设置两个滑动速度的比例，默认是 0.5。

以下是不同情况下的示例图：

如果 layout_scrollFlags 是 `scroll|enterAlways`，layout_collapseMode 是 `none`：

![scroll_enterAlways_none](https://github.com/cashow/AndroidTricks/blob/master/CoordinatorLayoutDemo/images/scroll_enterAlways_none.gif)

如果 layout_scrollFlags 是 `scroll|enterAlways|enterAlwaysCollapsed`，layout_collapseMode 是 `none`：

![scroll_enterAlways_enterAlwaysCollapsed_none](https://github.com/cashow/AndroidTricks/blob/master/CoordinatorLayoutDemo/images/scroll_enterAlways_enterAlwaysCollapsed_none.gif)

如果 layout_scrollFlags 是 `scroll|enterAlways|exitUntilCollapsed`，layout_collapseMode 是 `none`：

![scroll_enterAlways_exitUntilCollapsed_none](https://github.com/cashow/AndroidTricks/blob/master/CoordinatorLayoutDemo/images/scroll_enterAlways_exitUntilCollapsed_none.gif)

如果 layout_scrollFlags 是 `scroll|enterAlways`，layout_collapseMode 是 `pin`：

![scroll_enterAlways_pin](https://github.com/cashow/AndroidTricks/blob/master/CoordinatorLayoutDemo/images/scroll_enterAlways_pin.gif)

如果 layout_scrollFlags 是 `scroll|enterAlways`，layout_collapseMode 是 `parallax`，layout_collapseParallaxMultiplier 是默认值(0.5)：

![scroll_enterAlways_parallax](https://github.com/cashow/AndroidTricks/blob/master/CoordinatorLayoutDemo/images/scroll_enterAlways_parallax.gif)

如果 layout_scrollFlags 是 `scroll|enterAlways`，layout_collapseMode 是 `parallax`，layout_collapseParallaxMultiplier 是 0.7：

![scroll_enterAlways_parallax_07](https://github.com/cashow/AndroidTricks/blob/master/CoordinatorLayoutDemo/images/scroll_enterAlways_parallax_07.gif)

如果 layout_scrollFlags 是 `scroll|enterAlways`，layout_collapseMode 是 `parallax`，layout_collapseParallaxMultiplier 是 0.2：

![scroll_enterAlways_parallax_02](https://github.com/cashow/AndroidTricks/blob/master/CoordinatorLayoutDemo/images/scroll_enterAlways_parallax_02.gif)

如果 layout_scrollFlags 是 `scroll|enterAlways`，layout_collapseMode 是 `parallax`，layout_collapseParallaxMultiplier 设置成大于 1，那么 Toolbar 会往下滑。例如下图中设置的 layout_collapseParallaxMultiplier 是 1.7：

![scroll_enterAlways_parallax_17](https://github.com/cashow/AndroidTricks/blob/master/CoordinatorLayoutDemo/images/scroll_enterAlways_parallax_17.gif)

通过 contentScrim 属性，可以设置 CollapsingToolbarLayout 在折叠后的前景色。

```xml
<android.support.design.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <android.support.design.widget.AppBarLayout
        android:id="@+id/appbar"
        android:layout_width="match_parent"
        android:layout_height="150dp"
        android:fitsSystemWindows="true"
        android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar">

        <android.support.design.widget.CollapsingToolbarLayout
            android:id="@+id/collapsingtoolbarlayout"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:contentScrim="@color/colorPrimary"
            app:layout_scrollFlags="scroll|enterAlways">

            <ImageView
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:scaleType="centerCrop"
                app:layout_collapseParallaxMultiplier="0.7"
                app:layout_collapseMode="parallax"
                android:src="@drawable/ic_launcher_background"/>

            <android.support.v7.widget.Toolbar
                android:id="@+id/toolbar"
                android:layout_width="match_parent"
                android:layout_height="?attr/actionBarSize"
                app:layout_collapseMode="pin"/>
        </android.support.design.widget.CollapsingToolbarLayout>
    </android.support.design.widget.AppBarLayout>

    <android.support.v7.widget.RecyclerView
        android:id="@+id/recyclerview"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_behavior="@string/appbar_scrolling_view_behavior" />
</android.support.design.widget.CoordinatorLayout>
```

效果图：

![contentScrim](https://github.com/cashow/AndroidTricks/blob/master/CoordinatorLayoutDemo/images/contentScrim.gif)

通过 statusBarScrim 属性，可以设置折叠后状态栏的颜色。这个属性要和 `getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);` 一起使用。

```xml
<android.support.design.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true">

    <android.support.design.widget.AppBarLayout
        android:id="@+id/appbar"
        android:layout_width="match_parent"
        android:layout_height="150dp"
        android:fitsSystemWindows="true"
        android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar">

        <android.support.design.widget.CollapsingToolbarLayout
            android:id="@+id/collapsingtoolbarlayout"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:fitsSystemWindows="true"
            app:contentScrim="@color/colorPrimary"
            app:layout_scrollFlags="scroll|enterAlways">

            <ImageView
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:adjustViewBounds="true"
                android:fitsSystemWindows="true"
                android:scaleType="centerCrop"
                android:src="@drawable/ic_launcher_background"
                app:layout_collapseMode="parallax"
                app:layout_collapseParallaxMultiplier="0.7" />

            <android.support.v7.widget.Toolbar
                android:id="@+id/toolbar"
                android:layout_width="match_parent"
                android:layout_height="?attr/actionBarSize"
                app:layout_collapseMode="pin" />
        </android.support.design.widget.CollapsingToolbarLayout>
    </android.support.design.widget.AppBarLayout>

    <android.support.v7.widget.RecyclerView
        android:id="@+id/recyclerview"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_behavior="@string/appbar_scrolling_view_behavior" />
</android.support.design.widget.CoordinatorLayout>
```

效果图：

![statusBarScrim](https://github.com/cashow/AndroidTricks/blob/master/CoordinatorLayoutDemo/images/statusBarScrim.gif)

---

### 自定义 Behavior

自定义 Behavior 的代码：

```java
public class MyBehavior extends CoordinatorLayout.Behavior {

    public MyBehavior() {
    }

    public MyBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency.getId() == R.id.button_dependency;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        child.setY(dependency.getY() + child.getHeight());
        return true;
    }
}
```

通过 layout_behavior 属性指定 Behavior：

```xml
<android.support.design.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true">

    <Button
        android:id="@+id/button_dependency"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="dependency"/>

    <Button
        android:id="@+id/button_child"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        app:layout_behavior="com.cashow.coordinatorlayoutdemo.MyBehavior"
        android:text="child"/>
</android.support.design.widget.CoordinatorLayout>
```

button_dependency 添加触摸事件：

```java
buttonDependency.setOnTouchListener((v, event) -> {
    int x = (int) event.getRawX();
    int y = (int) event.getRawY();
    switch (event.getAction()) {
        case MotionEvent.ACTION_MOVE: {
            CoordinatorLayout.MarginLayoutParams layoutParams = (CoordinatorLayout.MarginLayoutParams) buttonDependency.getLayoutParams();
            int left = layoutParams.leftMargin + x - lastX;
            int top = layoutParams.topMargin + y - lastY;

            layoutParams.leftMargin = left;
            layoutParams.topMargin = top;
            buttonDependency.setLayoutParams(layoutParams);
            buttonDependency.requestLayout();
            break;
        }
    }
    lastX = x;
    lastY = y;
    return true;
});
```

效果图：

![behavior](https://github.com/cashow/AndroidTricks/blob/master/CoordinatorLayoutDemo/images/behavior.gif)
