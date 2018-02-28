# NestedRecyclerView

这是我在开发过程中遇到的一个问题，有时候 RecyclerView 会莫名其妙地往下滑。在经过一系列的 debug 后我确认了这个问题的发生条件：

1.竖向的 RecyclerView 嵌套横向的 RecyclerView；

2.横向的 RecyclerView 在页面里露出了一部分，但并没有完全显示；

3.将竖向的 RecyclerView 隐藏后再显示。

demo 下载链接：

<http://7sbs06.com1.z0.glb.clouddn.com/nested_recycler_view.apk>

demo 二维码：

![qrcode](https://github.com/cashow/AndroidTricks/blob/master/NestedRecyclerView/qrcode.png)

示意图：

![demo](http://7sbs06.com1.z0.glb.clouddn.com/nested_recycler_view_scroll_down.gif?a=b)

在确定了复现步骤后，我 Google 到了这个回答：

<https://stackoverflow.com/questions/38949034/nested-recyclerview-scrolls-by-itself>

同时，这个问题也被提到 android 项目的 issue 里了：

<https://code.google.com/p/android/issues/detail?id=222911>

这个问题的解决办法就是，给竖向的 RecyclerView 加上以下代码：

```xml
android:descendantFocusability="blocksDescendants"
```
