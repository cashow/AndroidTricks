# LayoutManagerDemo

LayoutManager 学习笔记

以下内容里提到的 view，指的都是 RecyclerView 的子 view。

---

### 相关资料

<http://wiresareobsolete.com/2014/09/building-a-recyclerview-layoutmanager-part-1/>  
<http://wiresareobsolete.com/2014/09/recyclerview-layoutmanager-2/>  
<http://wiresareobsolete.com/2015/02/recyclerview-layoutmanager-3/>  
<http://blog.csdn.net/zxt0601/article/details/52948009>  
<http://blog.csdn.net/zxt0601/article/details/52956504>  
<http://blog.csdn.net/zxt0601/article/details/53730908>  
<https://github.com/mcxtzhang/ZLayoutManager>  
<https://www.jianshu.com/p/08d998d047d8>  

---

## Recycler

当你需要回收旧的 view，或者从已回收的 view 中获取新的 view 时，你的 LayoutManager 会得到一个 Recycler 实例。

这个 Recycler 不需要你直接访问具体的 adapter。当你的 LayoutManager 需要一个新的 view 时，只需要调用 `getViewForPosition()`，Recycler 会决定是创建一个新的 view，还是从已回收的 view 里取一个出来。

你的 LayoutManager 需要做的是，将不再显示的 view 及时传递给 Recycler，避免重复创建不必要的 view。

### Detach 和 Remove

在布局更新时，有两种方法去处理现有的 view：detach 或者 remove。

Detach 的 view 在未来有可能会被复用。当你以后需要用到这个 view 时，不需要重新创建和绑定。

Remove 意味着 view 不再被需要了，被移除了。任何被移除的 view 都应该传给 Recycler，方便下次复用，但是 API 并没有强制要求你实现这一点。

### Scrap 和 Recycle

Recycler 有两种 view 的缓存机制：scrap heap 和 recycle pool。

在 scrap heap 中的 view 可以不经过 adapter 直接返回给 LayoutManager。当 view 被 detach 时，view 被缓存在这里，如果同一个布局出现了，这个 view 会被重用。比如说，在你调用 `notifyDataSetChanged()` 时，已经在屏幕中显示并且数据没有变动的 view 不需要重新调用 `onCreateViewHolder()`，也不需要重新调用 `onBindViewHolder()`，因为这些 view 是存放在 scrap heap 中的。

在 recycle pool 中的 view 都是被看作是有错误数据的 view，在重用前需要重新调用 `onBindViewHolder()`。比如说，在你滑动 RecyclerView 时，滑出屏幕的 view 会放到 recycle pool 中，滑入屏幕的 view 会复用之前的 view 并通过 `onBindViewHolder()` 更新 view 显示的内容。
 
当 LayoutManager 需要一个新的 view 时，Recycler 会先检查 scrap heap 中有没有对应 position/id 的 view，如果有，会直接返回这个 view，不需要重新绑定。如果没有，Recycler 会去 recycle pool 里找一个合适的 view，并重新绑定数据（`onBindViewHolder()`会被调用）。如果还是没有合适的 view，会创建一个新的 view（`onCreateViewHolder()`会被调用）。

### 使用技巧

如果你只是需要临时调整 view 并且在之后会重新 attach 到同一个布局，你可以调用 `detachAndScrapView()` 方法。

如果你不再需要某个 view，你可以调用 `removeAndRecycleView()` 方法。

---

## 创建自定义的 LayoutManager

LayoutManager 负责实时添加、测量 view，以及对子 view 进行布局。在用户滑动的时候，LayoutManager 需要判断是否需要创建新的 view 或者复用旧的 view，是将旧的 view 放到 scrap heap 里还是 recycle pool 里。

要创建自定义的 LayoutManager，你需要继承 RecyclerView.LayoutManager 并实现以下方法：

### generateDefaultLayoutParams()

这个方法返回的 RecyclerView.LayoutParams 将用在 RecyclerView 里的所有子 view 上。在 `getViewForPosition()` 返回之前，这些 LayoutParams 会被设置到子 view 上。

```java
@Override
public RecyclerView.LayoutParams generateDefaultLayoutParams() {
    return new RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT, 
            RecyclerView.LayoutParams.WRAP_CONTENT);
}
```

### onLayoutChildren()

这个方法负责对 view 布局，是 LayoutManager 的入口。它会在如下情况下被调用： 
1. 在 RecyclerView 初始化时，会被调用两次； 
2. 在调用 adapter.notifyDataSetChanged() 时，会被调用； 
3. 在调用 setAdapter 替换 Adapter 时,会被调用； 
4. 在 RecyclerView 执行动画时，它也会被调用。 

即 RecyclerView 初始化、 数据源改变时都会被调用。 

如果要实现一个垂直方向的 LayoutManager：

```java
@Override
public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
    // 在布局之前，将所有的子 View 先 Detach 掉，放入到 Scrap 缓存中
    detachAndScrapAttachedViews(recycler);

    // 定义竖直方向的偏移量
    int offsetY = 0;
    for (int i = 0; i < getItemCount(); i++) {
        // 这里就是从缓存里面取出
        View view = recycler.getViewForPosition(i);
        // 将 View 加入到 RecyclerView 中
        addView(view);
        // 对子 View 进行测量
        measureChildWithMargins(view, 0, 0);
        // 把宽高拿到，宽高都是包含 ItemDecorate 的尺寸
        int width = getDecoratedMeasuredWidth(view);
        int height = getDecoratedMeasuredHeight(view);
        // 最后，将 View 布局
        layoutDecorated(view, 0, offsetY, width, offsetY + height);
        // 将竖直方向偏移量增大 height
        offsetY += height;
    }
}
```

通常情况下，你需要在 `onLayoutChildren()` 里处理以下几件事：

1. 在滑动结束后检查已添加的 view 的偏移量

2. 如果在滑动时有空白地方出现，需要添加 view 进去

3. 如果有已添加的 view 不再显示，将这个 view 移除并放入 Recycler 里

4. 判断剩余的 view 要不要整理。这时候可能你需要调整每个 view 对应在 adapter 里的 position

注意到，我们在最开始先执行了 `detachAndScrapAttachedViews(recycler)`，即将所有的 view 先 detach 掉，放入到 scrap heap 中，这样做主要是考虑到，屏幕上可能还有一些 view 是继续要留在屏幕上的，我们不直接 remove，而是选择 detach。

### canScrollHorizontally() 和 canScrollVertically()

如果你想让 RecyclerView 能在横向或者纵向滑动，需要返回 true

### scrollHorizontallyBy() 和 scrollVerticallyBy()

在这里你需要处理 view 在滑动过程中的逻辑：

1. 将所有的 view 移动到合适的位置

2. 滑动后判断是否要添加和移除 view

3. 返回 view 的移动距离。框架会通过这个判断这个 view 是否已到边界

```java
public class MyLayoutManager2 extends LayoutManager {
    // 垂直方向的滑动偏移量
    private int verticalScrollOffset;
    // 所有子 view 的高度之和
    private int totalHeight;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() <= 0) {
            // 如果没有item，直接返回
            return;
        }
        if (state.isPreLayout()) {
            // 跳过 preLayout。preLayout 主要用于支持动画
            return;
        }
        // 在布局之前，将所有的子 View 先 Detach 掉，放入到 Scrap 缓存中
        detachAndScrapAttachedViews(recycler);

        // 定义竖直方向的偏移量
        int offsetY = 0;
        totalHeight = 0;
        for (int i = 0; i < getItemCount(); i++) {
            // 这里就是从缓存里面取出
            View view = recycler.getViewForPosition(i);
            // 将 View 加入到 RecyclerView 中
            addView(view);
            // 对子 View 进行测量
            measureChildWithMargins(view, 0, 0);
            // 把宽高拿到，宽高都是包含 ItemDecorate 的尺寸
            int width = getDecoratedMeasuredWidth(view);
            int height = getDecoratedMeasuredHeight(view);
            // 最后，将 View 布局
            layoutDecorated(view, 0, offsetY, width, offsetY + height);
            // 将竖直方向偏移量增大 height
            offsetY += height;
            totalHeight += height;
        }
        // 如果所有子 View 的高度和没有填满 RecyclerView 的高度，
        // 则将高度设置为 RecyclerView 的高度
        totalHeight = Math.max(totalHeight, getVerticalSpace());
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        // 实际要滑动的距离
        int travel = dy;

        // 如果滑动到最顶部
        if (verticalScrollOffset + dy < 0) {
            travel -= verticalScrollOffset;
        } else if (verticalScrollOffset + dy > totalHeight - getVerticalSpace()) {
            //如果滑动到最底部
            travel = totalHeight - getVerticalSpace() - verticalScrollOffset;
        }

        // 将竖直方向的偏移量加上 travel
        verticalScrollOffset += travel;

        // 平移容器内的 item
        offsetChildrenVertical(-travel);

        return travel;
    }

    /**
     * 获取 RecyclerView 在垂直方向上的可用空间
     */
    private int getVerticalSpace() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }
}
```

在这个方法里，我们需要自己手工移动这些视图。 `offsetChildrenVertical()` 和 `offsetChildrenHorizontal()` 这两个方法可以帮助我们处理匀速移动。 如果你不实现它，你的视图就不会滚动。 

---

## 回收 view

首先，记录下所有 view 的上下左右的偏移量。

```java
// 保存所有的 view 的上下左右的偏移信息
private SparseArray<Rect> allItemFrames = new SparseArray<>();
// 记录 view 是否出现过屏幕并且还没有回收
private SparseBooleanArray hasAttachedItems = new SparseBooleanArray();
```

在 `onLayoutChildren()` 里初始化这两个变量：

```java
@Override
public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
    if (getItemCount() <= 0) {
        // 如果没有item，直接返回
        return;
    }
    if (state.isPreLayout()) {
        // 跳过 preLayout。preLayout 主要用于支持动画
        return;
    }
    // 在布局之前，将所有的子 View 先 Detach 掉，放入到 Scrap 缓存中
    detachAndScrapAttachedViews(recycler);

    // 定义竖直方向的偏移量
    int offsetY = 0;
    totalHeight = 0;
    for (int i = 0; i < getItemCount(); i++) {
        // 这里就是从缓存里面取出
        View view = recycler.getViewForPosition(i);
        // 将 View 加入到 RecyclerView 中
        addView(view);
        // 对子 View 进行测量
        measureChildWithMargins(view, 0, 0);
        // 把宽高拿到，宽高都是包含 ItemDecorate 的尺寸
        int width = getDecoratedMeasuredWidth(view);
        int height = getDecoratedMeasuredHeight(view);
        // 将竖直方向偏移量增大 height
        totalHeight += height;

        Rect frame = allItemFrames.get(i);
        if (frame == null) {
            frame = new Rect();
        }
        frame.set(0, offsetY, width, offsetY + height);
        // 将当前 view 的 Rect 边界数据保存
        allItemFrames.put(i, frame);
        // 由于已经调用了detachAndScrapAttachedViews，因此需要将当前的Item设置为未出现过
        hasAttachedItems.put(i, false);
        offsetY += height;
    }
    // 如果所有子 View 的高度和没有填满 RecyclerView 的高度，
    // 则将高度设置为 RecyclerView 的高度
    totalHeight = Math.max(totalHeight, getVerticalSpace());
    recycleAndFillItems(recycler, state);
}

/**
 * 回收不需要的 view，并且将需要显示的 view 从缓存中取出
 */
private void recycleAndFillItems(RecyclerView.Recycler recycler, RecyclerView.State state) {
    if (state.isPreLayout()) {
        // 跳过 preLayout。preLayout 主要用于支持动画
        return;
    }
    // 当前scroll offset状态下的显示区域
    Rect displayFrame = new Rect(0, verticalScrollOffset, getHorizontalSpace(), verticalScrollOffset + getVerticalSpace());

    /**
     * 将滑出屏幕的 view 回收到 Recycler 中
     */
    Rect childFrame = new Rect();
    for (int i = 0; i < getChildCount(); i++) {
        View child = getChildAt(i);
        childFrame.left = getDecoratedLeft(child);
        childFrame.top = getDecoratedTop(child);
        childFrame.right = getDecoratedRight(child);
        childFrame.bottom = getDecoratedBottom(child);
        // 如果 view 没有在显示区域，说明需要回收
        if (!Rect.intersects(displayFrame, childFrame)) {
            // 回收掉滑出屏幕的 view
            removeAndRecycleView(child, recycler);
        }
    }

    // 重新显示需要出现在屏幕中的 view
    for (int i = 0; i < getItemCount(); i++) {
        if (Rect.intersects(displayFrame, allItemFrames.get(i))) {
            View scrap = recycler.getViewForPosition(i);
            measureChildWithMargins(scrap, 0, 0);
            addView(scrap);

            Rect frame = allItemFrames.get(i);
            // 将这个 item 布局出来
            layoutDecorated(scrap, frame.left, frame.top - verticalScrollOffset, frame.right, frame.bottom - verticalScrollOffset);
        }
    }
}
```

在 `scrollVerticallyBy()` 中添加 `recycleAndFillItems()`，因为在滑动过程中，需要重新对Item进行布局，即从缓存中取出Item进行数据绑定后放在新出现的Item的位置上。并且，还需要在scrollVerticallyBy最开始调用 `detachAndScrapAttachedViews(recycler)`

```java
@Override
public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
    // 先 detach 掉所有的子 view
    detachAndScrapAttachedViews(recycler);

    // 实际要滑动的距离
    int travel = dy;

    // 如果滑动到最顶部
    if (verticalScrollOffset + dy < 0) {
        travel -= verticalScrollOffset;
    } else if (verticalScrollOffset + dy > totalHeight - getVerticalSpace()) {
        //如果滑动到最底部
        travel = totalHeight - getVerticalSpace() - verticalScrollOffset;
    }

    // 将竖直方向的偏移量加上 travel
    verticalScrollOffset += travel;

    // 平移容器内的 item
    offsetChildrenVertical(-travel);

    recycleAndFillItems(recycler, state);

    return travel;
}
```

---

### 流式布局的 LayoutManager

修改自 <https://github.com/mcxtzhang/ZLayoutManager>

```java
/**
 * 整理了 {@link FlowLayoutManager} 的代码，加了点注释，应该是没有修改原文件里的代码逻辑
 * <p>
 * 页面加载后 adapter 会调用 13 次 onCreateViewHolder() 和 onBindViewHolder()
 * 页面加载后 RecyclerView 的子 view 有 13 个
 * 在滑动中获取到的 RecyclerView 的子 view 数有 13、14 个
 */
public class MyFlowLayoutManager extends LayoutManager {
    // 竖直偏移量。每次换行时，要根据这个 offset 判断
    private int mVerticalOffset;
    // 屏幕可见的第一个 view 的 Position
    private int mFirstVisiPos;
    // 屏幕可见的最后一个 view 的 Position
    private int mLastVisiPos;

    // key 是 view 的 position，value 是 view 的显示区域
    private SparseArray<Rect> mItemRects;

    public MyFlowLayoutManager() {
        // 如果 LayoutManager 要支持 WRAP_CONTENT 的布局，需要调用 setAutoMeasureEnabled(true)
        setAutoMeasureEnabled(true);
        mItemRects = new SparseArray<>();
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 这个方法负责对 view 布局，是 LayoutManager 的入口。它会在如下情况被调用：
     * 1. 在 RecyclerView 初始化时，会被调用两次；
     * 2. 在调用 adapter.notifyDataSetChanged() 时，会被调用；
     * 3. 在调用 setAdapter 替换 Adapter 时,会被调用；
     * 4. 在 RecyclerView 执行动画时，它也会被调用。
     *
     * 即 RecyclerView 初始化、 数据源改变时都会被调用。
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0) {
            // 没有 Item，界面空着吧
            detachAndScrapAttachedViews(recycler);
            return;
        }
        if (getChildCount() == 0 && state.isPreLayout()) {
            // state.isPreLayout() 是支持动画的
            return;
        }
        // 在布局之前，将所有的子 View 先 Detach 掉，放入到 Scrap 缓存中
        detachAndScrapAttachedViews(recycler);

        // 初始化
        mVerticalOffset = 0;
        mFirstVisiPos = 0;
        mLastVisiPos = getItemCount();

        // 填充 childView
        fill(recycler, state, 0);
    }

    /**
     * 填充 childView 的核心方法，应该先填充，再移动。
     * 在填充时，预先计算 dy 的在内，如果 View 越界，回收掉。
     * 一般情况是返回 dy，如果出现 View 数量不足，则返回修正后的 dy.
     *
     * @param recycler
     * @param state
     * @param dy       RecyclerView 给我们的位移量,+,显示底端， -，显示头部
     * @return 修正以后真正的dy（可能剩余空间不够移动那么多了 所以return <|dy|）
     */
    private int fill(RecyclerView.Recycler recycler, RecyclerView.State state, int dy) {
        // 回收越界的子 View
        recyclerInvisibleView(dy, recycler);

        // 布局子 View 阶段
        if (dy >= 0) {
            return addChildViewWhenScrollDown(recycler, dy);
        } else {
            return addChildViewWhenScrollUp(recycler, dy);
        }
    }

    /**
     * 如果 dy 大于等于 0，表示 RecyclerView 往下滑了，这时从 mFirstVisiPos 开始往后遍历，
     * 把所有能在屏幕中显示的 view 并且没有添加进来的 view 都添加进来
     */
    private int addChildViewWhenScrollDown(RecyclerView.Recycler recycler, int dy) {
        int leftOffset = getPaddingLeft();
        int topOffset = getPaddingTop();
        int lineMaxHeight = 0;

        // RecyclerView 的子 view 的数量，未添加或者暂时 detach 的 view 不包含在内
        int childCount = getChildCount();

        // 遍历子 view 的开始位置
        int minPos = mFirstVisiPos;
        // 遍历子 view 的结束位置
        mLastVisiPos = getItemCount() - 1;
        if (childCount > 0) {
            // 如果 RecyclerView 的子 view 数量大于 0，说明从 mFirstVisiPos 开始的可见的 view 已经添加进 RecyclerView 里了，
            // 这时只需要在滑动过后的剩余空间里补上新的 view 即可。

            // 当前可见的最后一个 view
            View lastView = getChildAt(childCount - 1);
            // 当前可见的最后一个 view 的位置 + 1
            minPos = getPosition(lastView) + 1;
            topOffset = getDecoratedTop(lastView);
            leftOffset = getDecoratedRight(lastView);
            lineMaxHeight = Math.max(lineMaxHeight, getDecoratedMeasurementVertical(lastView));
        }
        // 顺序添加子 view 进 RecyclerView
        for (int i = minPos; i <= mLastVisiPos; i++) {
            // 找 recycler 要一个 view，我们不管它是从 scrap 里取，还是从 RecyclerViewPool 里取，亦或是 onCreateViewHolder 里拿。
            View child = recycler.getViewForPosition(i);
            // 添加子 view
            addView(child);
            // 测量子 view
            measureChildWithMargins(child, 0, 0);
            // 子 view 在水平方向占用的空间
            int decoratedMeasurementHorizontal = getDecoratedMeasurementHorizontal(child);
            // 子 view 在垂直方向占用的空间
            int decoratedMeasurementVertical = getDecoratedMeasurementVertical(child);
            if (leftOffset + decoratedMeasurementHorizontal <= getHorizontalSpace()) {
                // 如果上一个 view 的右侧位置加上要添加的子 view 的宽度小于当前行的宽度，说明这一行还能装得下新添加的子 view，不需要换行
                layoutDecoratedWithMargins(child, leftOffset, topOffset, leftOffset + decoratedMeasurementHorizontal, topOffset + decoratedMeasurementVertical);

                // 保存 Rect 供逆序 layout 用
                Rect rect = new Rect(leftOffset, topOffset + mVerticalOffset, leftOffset + decoratedMeasurementHorizontal, topOffset + decoratedMeasurementVertical + mVerticalOffset);
                mItemRects.put(i, rect);

                // 计算新的 leftOffset 和 lineMaxHeight
                leftOffset += decoratedMeasurementHorizontal;
                lineMaxHeight = Math.max(lineMaxHeight, decoratedMeasurementVertical);
            } else {
                // 如果当前行排列不下，需要另起一行
                // 更新 leftOffset、topOffset 和 lineMaxHeight
                leftOffset = getPaddingLeft();
                topOffset += lineMaxHeight;
                lineMaxHeight = 0;

                // 新起一行的时候要判断一下边界
                if (topOffset - dy > getHeight() - getPaddingBottom()) {
                    // 越界了就回收
                    removeAndRecycleView(child, recycler);
                    mLastVisiPos = i - 1;
                } else {
                    layoutDecoratedWithMargins(child, leftOffset, topOffset, leftOffset + decoratedMeasurementHorizontal, topOffset + decoratedMeasurementVertical);

                    // 保存 Rect 供逆序 layout 用
                    Rect rect = new Rect(leftOffset, topOffset + mVerticalOffset, leftOffset + decoratedMeasurementHorizontal, topOffset + decoratedMeasurementVertical + mVerticalOffset);
                    mItemRects.put(i, rect);

                    // 计算新的 leftOffset 和 lineMaxHeight
                    leftOffset += decoratedMeasurementHorizontal;
                    lineMaxHeight = Math.max(lineMaxHeight, decoratedMeasurementVertical);
                }
            }
        }
        // 添加完后，判断是否已经没有更多的 ItemView。此时如果屏幕仍有空白，则需要修正dy
        View lastChild = getChildAt(getChildCount() - 1);
        if (getPosition(lastChild) == getItemCount() - 1) {
            int gap = getHeight() - getPaddingBottom() - getDecoratedBottom(lastChild);
            if (gap > 0) {
                dy -= gap;
            }
        }
        return dy;
    }

    /**
     * 利用Rect保存子 view 边界
     * 正序排列时，保存每个子 view 的 Rect，逆序时，直接拿出来 layout。
     * 当 RecyclerView 往下滑了后再往上拉，要添加的子 view 应该都是在下滑过程中已经计算过显示区域的，
     * 所以在那个时候保存下来的子 view 的显示区域可以直接拿来用，不需要再重新计算一遍
     */
    private int addChildViewWhenScrollUp(RecyclerView.Recycler recycler, int dy) {
        int maxPos = getItemCount() - 1;
        mFirstVisiPos = 0;
        if (getChildCount() > 0) {
            View firstView = getChildAt(0);
            maxPos = getPosition(firstView) - 1;
        }
        for (int i = maxPos; i >= mFirstVisiPos; i--) {
            Rect rect = mItemRects.get(i);

            if (rect.bottom - mVerticalOffset - dy < getPaddingTop()) {
                mFirstVisiPos = i + 1;
                break;
            } else {
                View child = recycler.getViewForPosition(i);
                // 将 view 添加至 RecyclerView 中，childIndex 为 1，但是 view 的位置还是由 layout 的位置决定
                addView(child, 0);
                measureChildWithMargins(child, 0, 0);

                layoutDecoratedWithMargins(child, rect.left, rect.top - mVerticalOffset, rect.right, rect.bottom - mVerticalOffset);
            }
        }
        return dy;
    }

    /**
     * 在滑动过程中，需要回收没有显示在屏幕中的 View
     */
    private void recyclerInvisibleView(int dy, RecyclerView.Recycler recycler) {
        if (getChildCount() <= 0) {
            return;
        }
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (dy > 0) {
                // 如果 view 的下边界减去垂直方向的偏移量，小于 RecyclerView 顶端的位置，
                // 说明这个 view 已经滑到屏幕上方并且不可见，这时需要回收掉这个 view
                if (getDecoratedBottom(child) - dy < getPaddingTop()) {
                    removeAndRecycleView(child, recycler);
                    mFirstVisiPos++;
                    continue;
                }
            } else if (dy < 0) {
                // 如果 view 的上边界减去垂直方向的偏移量，小于 RecyclerView 底部的位置，
                // 说明这个 view 已经滑到屏幕下方并且不可见，这时需要回收掉这个 view
                if (getDecoratedTop(child) - dy > getHeight() - getPaddingBottom()) {
                    removeAndRecycleView(child, recycler);
                    mLastVisiPos--;
                    continue;
                }
            }
        }
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        // 位移 0 或者没有子 view，当然不移动
        if (dy == 0 || getChildCount() == 0) {
            return 0;
        }

        int realOffset = dy;//实际滑动的距离， 可能会在边界处被修复
        // 边界修复代码
        if (mVerticalOffset + realOffset < 0) {
            // 修复上边界
            realOffset = -mVerticalOffset;
        } else if (realOffset > 0) {
            // 修复下边界
            // 利用最后一个子View比较修正
            View lastChild = getChildAt(getChildCount() - 1);
            if (getPosition(lastChild) == getItemCount() - 1) {
                int gap = getHeight() - getPaddingBottom() - getDecoratedBottom(lastChild);
                if (gap > 0) {
                    realOffset = -gap;
                } else if (gap == 0) {
                    realOffset = 0;
                } else {
                    realOffset = Math.min(realOffset, -gap);
                }
            }
        }

        // 先填充，再位移。
        realOffset = fill(recycler, state, realOffset);

        // 累加实际滑动距离
        mVerticalOffset += realOffset;

        // 滑动
        offsetChildrenVertical(-realOffset);

        return realOffset;
    }

    /**
     * 获取某个childView在水平方向所占的空间
     */
    public int getDecoratedMeasurementHorizontal(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        return getDecoratedMeasuredWidth(view) + params.leftMargin
                + params.rightMargin;
    }

    /**
     * 获取某个childView在竖直方向所占的空间
     */
    public int getDecoratedMeasurementVertical(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        return getDecoratedMeasuredHeight(view) + params.topMargin
                + params.bottomMargin;
    }

    public int getVerticalSpace() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    public int getHorizontalSpace() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }
}
```

---

### 项目里每个 LayoutManager 的区别

#### MyLayoutManager
只实现了简单的 onLayoutChildren() 的竖向 LayoutManager，没有实现 view 的回收，不可滑动  
页面加载后 adapter 会调用 2*50 次 onCreateViewHolder() 和 onBindViewHolder()  
页面加载后 RecyclerView 的子 view 有 50 个  

#### MyLayoutManager2
只实现了简单的 onLayoutChildren() 和 scrollVerticallyBy() 的竖向 LayoutManager，没有实现 view 的回收，可以竖向滑动  
页面加载后 adapter 会调用 2*50 次 onCreateViewHolder() 和 onBindViewHolder()  
页面加载后 RecyclerView 的子 view 有 50 个  
在滑动中获取到的 RecyclerView 的子 view 数也是 50 个  

#### MyLayoutManager3
只实现了简单的 onLayoutChildren() 和 scrollVerticallyBy() 的竖向 LayoutManager，实现了 view   的回收，可以竖向滑动  
页面加载后 adapter 会先调用 50 次 onCreateViewHolder() 和 onBindViewHolder()，然后调用 8 次   onCreateViewHolder() 和 13 次 onBindViewHolder()  
页面加载后 RecyclerView 的子 view 有 38 个  
在滑动中获取到的 RecyclerView 的子 view 数是 13、14 个  

#### MyLinearLayoutManager
继承自 LinearLayoutManager  
页面加载后 adapter 会调用 13 次 onCreateViewHolder() 和 onBindViewHolder()  
页面加载后 RecyclerView 的子 view 有 13 个  
在滑动中获取到的 RecyclerView 的子 view 数有 13、14 个  

#### MyFlowLayoutManager
流式布局，修改自 FlowLayoutManager  
页面加载后 adapter 会调用 13 次 onCreateViewHolder() 和 onBindViewHolder()  
页面加载后 RecyclerView 的子 view 有 13 个  
在滑动中获取到的 RecyclerView 的子 view 数有 13、14 个  

#### MyHorizontalLayoutManager
横向的 LayoutManager，修改自 MyFlowLayoutManager  
页面加载后 adapter 会调用 3 次 onCreateViewHolder() 和 onBindViewHolder()  
页面加载后 RecyclerView 的子 view 有 2 个  
在滑动中获取到的 RecyclerView 的子 view 数有 3 个  

#### MyAnimHorizontalLayoutManager
带有滑动效果的横向的 LayoutManager，修改自 MyHorizontalLayoutManager  
页面加载后 adapter 会调用 3 次 onCreateViewHolder() 和 onBindViewHolder()  
页面加载后 RecyclerView 的子 view 有 2 个  
在滑动中获取到的 RecyclerView 的子 view 数有 3 个  

#### MyAnimHorizontalLayoutManager2
带有滑动效果的横向的 LayoutManager，修改自 MyHorizontalLayoutManager  
页面加载后 adapter 会调用 3 次 onCreateViewHolder() 和 onBindViewHolder()  
页面加载后 RecyclerView 的子 view 有 2 个  
在滑动中获取到的 RecyclerView 的子 view 数有 3 个  

---

### 示例图

#### 每个 view 占满一行的 FlowLayoutManager

![flow_layout_manager_1](https://github.com/cashow/AndroidTricks/blob/master/LayoutManagerDemo/images/flow_layout_manager_1.gif)

#### 每个 view 宽度不定的 FlowLayoutManager

![flow_layout_manager_2](https://github.com/cashow/AndroidTricks/blob/master/LayoutManagerDemo/images/flow_layout_manager_2.gif)

#### MyHorizontalLayoutManager

![my_horizontal_layout_manager](https://github.com/cashow/AndroidTricks/blob/master/LayoutManagerDemo/images/my_horizontal_layout_manager.gif)

#### MyAnimHorizontalLayoutManager

![my_anim_horizontal_layout_manager](https://github.com/cashow/AndroidTricks/blob/master/LayoutManagerDemo/images/my_anim_horizontal_layout_manager.gif)

#### MyAnimHorizontalLayoutManager2

![my_anim_horizontal_layout_manager2](https://github.com/cashow/AndroidTricks/blob/master/LayoutManagerDemo/images/my_anim_horizontal_layout_manager2.gif)

#### MySwipeCardLayoutManager

![my_swipe_card_layout_manager](https://github.com/cashow/AndroidTricks/blob/master/LayoutManagerDemo/images/my_swipe_card_layout_manager.gif)
