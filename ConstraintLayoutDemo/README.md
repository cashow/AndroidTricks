# ConstraintLayoutDemo

ConstraintLayout 的学习笔记

相关链接：

<https://developer.android.com/training/constraint-layout/index.html>  
<https://developer.android.com/reference/android/support/constraint/ConstraintLayout.html>  
<https://developer.android.com/reference/android/support/constraint/Guideline.html>  
<http://blog.csdn.net/guolin_blog/article/details/53122387>  
<http://blog.csdn.net/zxt0601/article/details/72683379>  
<http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2017/1019/8618.html>  
<https://constraintlayout.com/basics/barriers.html>

-------

### 相对定位

```
layout_constraintLeft_toLeftOf
layout_constraintLeft_toRightOf
layout_constraintRight_toLeftOf
layout_constraintRight_toRightOf
layout_constraintTop_toTopOf
layout_constraintTop_toBottomOf
layout_constraintBottom_toTopOf
layout_constraintBottom_toBottomOf
layout_constraintBaseline_toBaselineOf
layout_constraintStart_toEndOf
layout_constraintStart_toStartOf
layout_constraintEnd_toStartOf
layout_constraintEnd_toEndOf
```

属性都形如 layout_constraintXXX_toYYYOf，XXX 代表是这个子控件自身的哪条边(Left、Right、Top、Bottom、Baseline)，YYY 代表的是和约束控件的哪条边发生约束 (取值同样是 Left、Right、Top、Bottom、Baseline)。

### 当约束的控件为 GONE 时的 Margins

```
layout_goneMarginStart
layout_goneMarginEnd
layout_goneMarginLeft
layout_goneMarginTop
layout_goneMarginRight
layout_goneMarginBottom
```

在约束的布局 gone 时，控件自身的 marginXXX 会被 goneMarginXXX 替换掉

如果只设置了app:layout_goneMarginRight没有设置android:layout_marginRight，则无效。（alpha版本的bug，1.0.1版本已经修复）

### 居中定位

```
app:layout_constraintLeft_toLeftOf="parent"
app:layout_constraintRight_toRightOf="parent"
```

### 倾向

```
layout_constraintHorizontal_bias (0最左边 1最右边)
layout_constraintVertical_bias (0最上边 1 最底边)
```

默认是0.5。app:layout_constraintHorizontal_bias="0.9"，则会在水平方向上向右偏移至90%。

### 控件尺寸约束

控件的宽高有三种方式为其设置：

```
确定尺寸
WRAP_CONTENT
0dp，就等于MATCH_CONSTRAINT
```

ConstraintLayout 里的控件不支持 MATCH_PARENT。

当控件的宽度设置为 MATCH_CONSTRAINT 时，这个控件会在约束条件内占据所有空间。

### 只有一个维度的约束

可以通过 layout_constraintDimentionRatio 限制空间的宽高比，这需要将至少一个约束维度设置为 0dp，并将属性 layout_constraintDimentionRatio 设置为给定的比例。

```xml
<ImageView
    android:id="@+id/imageView"
    android:layout_width="200dp"
    android:layout_height="0dp"
    android:scaleType="fitXY"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintDimensionRatio="1:1"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="parent"
    app:srcCompat="@mipmap/ic_launcher" />
```

比例值有两种取值：
* 浮点值，表示宽度和高度之间的比率 （2,0.5）
* 宽度：高度 的形式 （5:1,1:5）

### 两个维度都有约束

如果两个维度均设置为 MATCH_CONSTRAINT（0dp），会占用满足所有约束条件和比率的最大尺寸。

例如在 200 x 200 的控件里限定宽高比是 1：2，控件最终的大小是 100 x 200

### 指定约束的维度

当一个控件的两个维度都有约束，可以使用 W 或 H 来指定哪个维度被约束（例如 "W,2:1" 或者 "H,2:1"）。最大边以指明的宽度或者高度为准。

### 圆形定位 (添加于 1.1 版本)

你可以把一个控件放到相对于另一个控件中心点的圆形区域内，例如把删除 icon 放到某个布局的右上角。

* layout_constraintCircle : 要对齐的控件的 id
* layout_constraintCircleRadius : 离控件中心点的距离
* layout_constraintCircleAngle : 距离控件中心点的角度

![circle](https://github.com/cashow/AndroidTricks/blob/master/ConstraintLayoutDemo/images/circle1.png)

### GONE 的处理逻辑

不同于其他时候，ConstraintLayout 里的 view 如果设置成了 GONE，那么这个 view 会隐藏，但是控件的约束还在，只是宽高会看做是0。这样的话，其他和这个 view 相关的控件的约束条件并不会变。

![visibility-behavior](https://github.com/cashow/AndroidTricks/blob/master/ConstraintLayoutDemo/images/visibility-behavior.png)

### MATCH_CONSTRAINT 的限制

当将控件的一个维度设置成 MATCH_CONSTRAINT 时，这个控件会尽量撑满这个维度。你可以通过以下属性去限制 MATCH_CONSTRAINT 的结果：

* layout_constraintWidth_min 和 layout_constraintHeight_min : 限定最低的宽度和高度
* layout_constraintWidth_max and layout_constraintHeight_max : 限定最高的宽度和高度
* layout_constraintWidth_percent and layout_constraintHeight_percent : 按比例限定宽度和高度

### Chains

如果两个控件之间有双向的约束，那么这两个控件可看做是 chain。

chains 的左上方的控件可看做 chain 的头部。

### Chains 的样式

你可以通过 chain 头部的 layout_constraintHorizontal_chainStyle 或者 layout_constraintVertical_chainStyle 属性，设置 chain 的样式（默认是 CHAIN_SPREAD）。

* CHAIN_SPREAD -- 所有控件都会分散开。如果这其中有控件设置成了 MATCH_CONSTRAINT，那这个控件会占据剩余空间
* CHAIN_SPREAD_INSIDE -- 和 CHAIN_SPREAD 类似，但是左右两端的控件不会散开
* CHAIN_PACKED -- 内部的控件会挤在一起

![chains-styles](https://github.com/cashow/AndroidTricks/blob/master/ConstraintLayoutDemo/images/chains-styles.png)

### Guideline

Guideline 是个工具类，只能用在 ConstraintLayout 里，是用来辅助布局的。Guideline 都是 View.GONE。

Guideline 可以是水平的或者垂直的：

* 水平的 Guidelines 宽度是 0，高度和 ConstraintLayout 父控件一致。
* 垂直的 Guidelines 高度是 0，宽度和 ConstraintLayout 父控件一致。

有 3 种方式可以定位 Guideline：

* 指定距离左边或者上边的固定间距：layout_constraintGuide_begin
* 指定距离左边或者上边的固定间距：layout_constraintGuide_end
* 指定横向或者纵向的百分比：layout_constraintGuide_percent

### Group (添加于 1.1 版本)

这个类可以控制一组 view 的 visibility。

```xml
<android.support.constraint.Group
         android:id="@+id/group"
         android:layout_width="wrap_content"
         android:layout_height="wrap_content"
         android:visibility="visible"
         app:constraint_referenced_ids="button4,button9" />
```

例如上面的 Group 将 id 是 button4 和 button9 的 view 设成了 visible。

需要注意的是，Group 目前不支持添加点击事件，所以如果需要对 Group 里的 view 加统一的点击事件，需要将 Group 里的每个 view 取出来单独设置。

```java
int buttonIds[] = groupButton.getReferencedIds();

for (int buttonId : buttonIds) {
    findViewById(buttonId).setOnClickListener(v -> {
        ...
    });
}
```

### Barrier (添加于 1.1 版本)

先来看这个例子：

![constraintlayout_barrier_1](https://github.com/cashow/AndroidTricks/blob/master/ConstraintLayoutDemo/images/constraintlayout_barrier_1.png)

左边有两个 TextView：text_top 和 text_bottom，右边有一个 TextView：text_right

其中，左边两个 TextView 的内容是不固定的，宽度是 wrap_content。

假如你把 text_right 置于 text_top 的右边，那么当 text_bottom 文字过长时，会和 text_right 重合。

![constraintlayout_barrier_2](https://github.com/cashow/AndroidTricks/blob/master/ConstraintLayoutDemo/images/constraintlayout_barrier_2.png)

假如你把 text_right 置于 text_bottom 的右边，那么当 text_top 文字过长时，会和 text_right 重合。

![constraintlayout_barrier_3](https://github.com/cashow/AndroidTricks/blob/master/ConstraintLayoutDemo/images/constraintlayout_barrier_3.png)

一般情况下，为了解决这类问题，你需要把 text_top 和 text_bottom 放到一个 ViewGroup 里，再将 text_right 置于 ViewGroup 的右边。通过添加 Barrier 的方式，你也可以实现以上的需求：

```xml
<android.support.constraint.Barrier
    android:id="@+id/barrier"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:barrierDirection="end"
    app:constraint_referenced_ids="text_top,text_bottom" />
```

以上代码添加了一个竖向的 Barrier，你可以理解成是辅助线，并且通过 constraint_referenced_ids 关联了一组 view (text_top 和 text_bottom)，通过 barrierDirection 指定了 Barrier 在这组 view 的右边。

这样的话，你就添加了一条在 text_top 和 text_bottom 右边的辅助线，你只需将 text_right 置于这条辅助线的右边即可。

![constraintlayout_barrier_4](https://github.com/cashow/AndroidTricks/blob/master/ConstraintLayoutDemo/images/constraintlayout_barrier_4.png)

---

### 利用 ConstraintSet 实现动画

假如你要实现以下效果：

![constraintset_demo](https://github.com/cashow/AndroidTricks/blob/master/ConstraintLayoutDemo/images/constraintset_demo.png)

利用 ConstraintSet，你只需要几行代码就能实现。

你需要准备两个布局文件，一个是动画初始状态，一个动画结束状态。

然后调用以下代码：

```java
private void updateConstraints(@LayoutRes int id) {
    ConstraintSet newConstraintSet = new ConstraintSet();
    newConstraintSet.clone(this, id);
    newConstraintSet.applyTo(root);

    ChangeBounds transition = new ChangeBounds();
    transition.setInterpolator(new OvershootInterpolator());
    TransitionManager.beginDelayedTransition(root, transition);
}
```

传入动画结束状态时的布局 id，会开启一个动画，将页面内的约束从初始状态改动结束状态。

需要注意的是，这个动画只能针对 ConstraintLayout.LayoutParams 里的参数，TextView 的文字大小并不能跟着动画自动变化，这个问题是个已知的 issue 并且是 Won't Fix 状态：

<https://issuetracker.google.com/issues/70740827>
