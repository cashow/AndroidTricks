# ConstraintLayoutDemo

ConstraintLayout 的学习笔记

相关链接：

<https://developer.android.com/training/constraint-layout/index.html>  
<https://developer.android.com/reference/android/support/constraint/ConstraintLayout.html>  
<https://developer.android.com/reference/android/support/constraint/Guideline.html>  
<http://blog.csdn.net/guolin_blog/article/details/53122387>  
<http://blog.csdn.net/zxt0601/article/details/72683379>  
<http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2017/1019/8618.html>

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
