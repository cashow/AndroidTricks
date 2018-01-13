package com.cashow.hencoderdemo.chapter_1_1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cashow.hencoderdemo.common.BaseView;

public class DrawPathView extends BaseView {
    private Paint paint;
    private Path path;

    public DrawPathView(Context context, Integer viewType) {
        super(context, viewType);
    }

    public DrawPathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        paint = new Paint();
        path = new Path();

        /**
         * 添加圆
         * addCircle(float x, float y, float radius, Direction dir)
         * x, y, radius 这三个参数是圆的基本信息，最后一个参数 dir 是画圆的路径的方向。
         * 路径方向有两种：顺时针 (CW clockwise) 和逆时针 (CCW counter-clockwise)
         *
         * 添加椭圆
         * addOval(float left, float top, float right, float bottom, Direction dir) / addOval(RectF oval, Direction dir)
         *
         * 添加矩形
         * addRect(float left, float top, float right, float bottom, Direction dir) / addRect(RectF rect, Direction dir)
         *
         * 添加圆角矩形
         * addRoundRect(RectF rect, float rx, float ry, Direction dir)
         * addRoundRect(float left, float top, float right, float bottom, float rx, float ry, Direction dir)
         * addRoundRect(RectF rect, float[] radii, Direction dir)
         * addRoundRect(float left, float top, float right, float bottom, float[] radii, Direction dir)
         *
         * 添加另一个 Path
         * addPath(Path path)
         *
         * 画直线
         * lineTo(float x, float y) / rLineTo(float x, float y)
         * 从当前位置向目标位置画一条直线， x 和 y 是目标位置的坐标。
         * lineTo(x, y) 的参数是绝对坐标
         * rLineTo(x, y) 的参数是相对当前位置的相对坐标 （前缀 r 指的就是 relatively「相对地」)。
         *
         * 画二次贝塞尔曲线
         * quadTo(float x1, float y1, float x2, float y2) / rQuadTo(float dx1, float dy1, float dx2, float dy2)
         * 这条二次贝塞尔曲线的起点就是当前位置，而参数中的 x1, y1 和 x2, y2 则分别是控制点和终点的坐标。和 rLineTo(x, y) 同理，rQuadTo(dx1, dy1, dx2, dy2) 的参数也是相对坐标
         *
         * 画三次贝塞尔曲线
         * cubicTo(float x1, float y1, float x2, float y2, float x3, float y3) / rCubicTo(float x1, float y1, float x2, float y2, float x3, float y3)
         *
         * 移动到目标位置
         * moveTo(float x, float y) / rMoveTo(float x, float y)
         * 不论是直线还是贝塞尔曲线，都是以当前位置作为起点，而不能指定起点。但你可以通过 moveTo(x, y) 或 rMoveTo() 来改变当前位置，从而间接地设置这些方法的起点。
         *
         * 画弧形
         * arcTo(RectF oval, float startAngle, float sweepAngle, boolean forceMoveTo)
         * arcTo(float left, float top, float right, float bottom, float startAngle, float sweepAngle, boolean forceMoveTo)
         * arcTo(RectF oval, float startAngle, float sweepAngle)
         * arcTo() 和 addArc() 也是用来画线的，但并不使用当前位置作为弧线的起点。
         * 这些方法和 Canvas.drawArc() 比起来，少了一个参数 useCenter，而多了一个参数 forceMoveTo 。
         * 少了 useCenter ，是因为 arcTo() 只用来画弧形而不画扇形，所以不再需要 useCenter 参数；
         * 而多出来的这个 forceMoveTo 参数的意思是，绘制是要「抬一下笔移动过去」，还是「直接拖着笔过去」，区别在于是否留下移动的痕迹。
         *
         * 画弧形
         * addArc(float left, float top, float right, float bottom, float startAngle, float sweepAngle)
         * addArc(RectF oval, float startAngle, float sweepAngle)
         * addArc() 只是一个直接使用了 forceMoveTo = true 的简化版 arcTo()
         *
         *
         * 封闭当前子图形
         * close()
         * 「子图形」：官方文档里叫做 contour 。但由于在这个场景下我找不到这个词合适的中文翻译（直译的话叫做「轮廓」），所以我换了个便于中国人理解的词：「子图形」。
         * 第一组方法是「添加子图形」，所谓「子图形」，指的就是一次不间断的连线。一个 Path 可以包含多个子图形。
         * 当使用第一组方法，即 addCircle() addRect() 等方法的时候，每一次方法调用都是新增了一个独立的子图形；
         * 而如果使用第二组方法，即 lineTo() arcTo() 等方法的时候，则是每一次断线（即每一次「抬笔」），都标志着一个子图形的结束，以及一个新的子图形的开始。
         * 另外，不是所有的子图形都需要使用 close() 来封闭。当需要填充图形时（即 Paint.Style 为  FILL 或 FILL_AND_STROKE），Path 会自动封闭子图形。
         *
         * 设置填充方式
         * Path.setFillType(Path.FillType ft)
         * FillType 的取值有四个：
         * 1. EVEN_ODD
         * 2. WINDING （默认值）
         * 3. INVERSE_EVEN_ODD
         * 4. INVERSE_WINDING
         * 其中后面的两个带有 INVERSE_ 前缀的，只是前两个的反色版本
         * WINDING 是「全填充」，而 EVEN_ODD 是「交叉填充」：https://ws1.sinaimg.cn/large/006tNc79ly1fig7zyzjtrj30kx0jugn9.jpg
         *
         * EVEN_ODD，即 even-odd rule （奇偶原则）：
         * 对于平面中的任意一点，向任意方向射出一条射线，这条射线和图形相交的次数（相交才算，相切不算哦）
         * 如果是奇数，则这个点被认为在图形内部，是要被涂色的区域；
         * 如果是偶数，则这个点被认为在图形外部，是不被涂色的区域。
         * 个人理解是重叠部分是奇数就绘制，偶数就不绘制，有点类似于名为 voi 的游戏。。。
         *
         * WINDING
         * 即 non-zero winding rule （非零环绕数原则）：首先，它需要你图形中的所有线条都是有绘制方向的
         * 然后，同样是从平面中的点向任意方向射出一条射线，但计算规则不一样：
         * 以 0 为初始值，对于射线和图形的所有交点，遇到每个顺时针的交点（图形从射线的左边向右穿过）把结果加 1，
         * 遇到每个逆时针的交点（图形从射线的右边向左穿过）把结果减 1，最终把所有的交点都算上，
         * 得到的结果如果不是 0，则认为这个点在图形内部，是要被涂色的区域；
         * 如果是 0，则认为这个点在图形外部，是不被涂色的区域。
         * 然后，同样是从平面中的点向任意方向射出一条射线，但计算规则不一样：
         * 以 0 为初始值，对于射线和图形的所有交点，遇到每个顺时针的交点（图形从射线的左边向右穿过）把结果加 1，
         * 遇到每个逆时针的交点（图形从射线的右边向左穿过）把结果减 1，最终把所有的交点都算上，
         * 得到的结果如果不是 0，则认为这个点在图形内部，是要被涂色的区域；
         * 如果是 0，则认为这个点在图形外部，是不被涂色的区域。
         * 图形的方向：
         * 对于添加子图形类方法（如 Path.addCircle() Path.addRect()）的方向，由方法的  dir 参数来控制。
         * 而对于画线类的方法（如 Path.lineTo() Path.arcTo()）就更简单了，线的方向就是图形的方向。
         */
        switch (viewType) {
            case 0:
                path.addCircle(100, 100, 100, Path.Direction.CW);
                path.addCircle(200, 100, 100, Path.Direction.CW);
                break;
            case 1:
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(5);
                path.lineTo(100, 100);
                path.rLineTo(100, 0);
                break;
            case 2:
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(5);
                path.quadTo(50, 50, 100, 200);
                path.quadTo(150, 150, 200, 0);
                break;
            case 3:
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(5);
                path.quadTo(50, 50, 100, 200);
                path.rMoveTo(100, 0);
                path.quadTo(150, 150, 200, 0);
                break;
            case 4:
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(5);
                path.lineTo(100, 100);
                path.arcTo(100, 100, 300, 300, -90, 90, true); // 强制移动到弧形起点（无痕迹）
                break;
            case 5:
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(5);
                path.lineTo(100, 100);
                path.arcTo(100, 100, 300, 300, -90, 90, false); // 直接连线连到弧形起点（有痕迹）
                break;
            case 6:
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(5);
                path.lineTo(100, 100);
                path.addArc(100, 100, 300, 300, -90, 90); // 强制移动到弧形起点（无痕迹）
                break;
            case 7:
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(5);
                path.moveTo(100, 100);
                path.lineTo(200, 100);
                path.lineTo(150, 150);
                break;
            case 8:
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(5);
                path.moveTo(100, 100);
                path.lineTo(200, 100);
                path.lineTo(150, 150);
                // 使用 close() 封闭子图形。等价于 path.lineTo(100, 100)
                path.close();
                break;
            case 9:
                paint.setStyle(Paint.Style.FILL);
                paint.setStrokeWidth(5);
                // 这里只绘制了两条边，但由于 Style 是 FILL ，所以绘制时会自动封口
                path.moveTo(100, 100);
                path.lineTo(200, 100);
                path.lineTo(150, 150);
                break;
            case 10:
                path.setFillType(Path.FillType.EVEN_ODD);
                path.addCircle(100, 100, 100, Path.Direction.CW);
                path.addCircle(200, 100, 100, Path.Direction.CW);
                break;
            case 11:
                path.setFillType(Path.FillType.WINDING);
                path.addCircle(100, 100, 100, Path.Direction.CW);
                path.addCircle(200, 100, 100, Path.Direction.CW);
                break;
            case 12:
                path.setFillType(Path.FillType.EVEN_ODD);
                path.addCircle(100, 100, 100, Path.Direction.CW);
                path.addCircle(200, 100, 100, Path.Direction.CCW);
                break;
            case 13:
                path.setFillType(Path.FillType.WINDING);
                path.addCircle(100, 100, 100, Path.Direction.CW);
                path.addCircle(200, 100, 100, Path.Direction.CCW);
                break;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 14;
    }

    @Override
    public String getViewTypeInfo(int viewType) {
        switch (viewType) {
            case 0:
                return "添加圆\n" +
                        "addCircle(float x, float y, float radius, Direction dir)：\n" +
                        "x, y, radius 这三个参数是圆的基本信息，" +
                        "dir 是画圆的路径的方向\n" +
                        "路径方向有两种：顺时针 (CW clockwise) 和逆时针 (CCW counter-clockwise)\n" +
                        "\n" +
                        "添加椭圆\n" +
                        "addOval(float left, float top, float right, float bottom, Direction dir) / addOval(RectF oval, Direction dir)\n" +
                        "\n" +
                        "添加矩形\n" +
                        "addRect(float left, float top, float right, float bottom, Direction dir) / addRect(RectF rect, Direction dir)\n" +
                        "\n" +
                        "添加圆角矩形\n" +
                        "addRoundRect(RectF rect, float rx, float ry, Direction dir)\n" +
                        "addRoundRect(float left, float top, float right, float bottom, float rx, float ry, Direction dir)\n" +
                        "addRoundRect(RectF rect, float[] radii, Direction dir)\n" +
                        "addRoundRect(float left, float top, float right, float bottom, float[] radii, Direction dir)\n" +
                        "\n" +
                        "添加另一个 Path\n" +
                        "addPath(Path path)\n\n" +
                        "path.addCircle(100, 100, 100, Path.Direction.CW)\n" +
                        "path.addCircle(200, 100, 100, Path.Direction.CW)\n" +
                        "canvas.drawPath(path, paint)";
            case 1:
                return "画直线\n" +
                        "lineTo(float x, float y) / rLineTo(float x, float y)：\n" +
                        "从当前位置向目标位置画一条直线， x 和 y 是目标位置的坐标。\n" +
                        "lineTo(x, y) 的参数是绝对坐标\n" +
                        "rLineTo(x, y) 的参数是相对当前位置的相对坐标（前缀 r 指的就是 relatively「相对地」)。\n\n" +
                        "paint.setStyle(Paint.Style.STROKE)\n" +
                        "paint.setStrokeWidth(5)\n" +
                        "path.lineTo(100, 100)\n" +
                        "path.rLineTo(100, 0)\n" +
                        "canvas.drawPath(path, paint)";
            case 2:
                return "画二次贝塞尔曲线\n" +
                        "quadTo(float x1, float y1, float x2, float y2) / rQuadTo(float dx1, float dy1, float dx2, float dy2)：\n" +
                        "这条二次贝塞尔曲线的起点就是当前位置，而参数中的 x1, y1 和 x2, y2 则分别是控制点和终点的坐标。\n" +
                        "和 rLineTo(x, y) 同理，rQuadTo(dx1, dy1, dx2, dy2) 的参数也是相对坐标。\n\n" +
                        "画三次贝塞尔曲线\n" +
                        "cubicTo(float x1, float y1, float x2, float y2, float x3, float y3) / rCubicTo(float x1, float y1, float x2, float y2, float x3, float y3)\n\n" +
                        "paint.setStyle(Paint.Style.STROKE)\n" +
                        "paint.setStrokeWidth(5)\n" +
                        "path.quadTo(50, 50, 100, 200)\n" +
                        "path.quadTo(150, 150, 200, 0)\n" +
                        "canvas.drawPath(path, paint)";
            case 3:
                return "移动到目标位置\n" +
                        "moveTo(float x, float y) / rMoveTo(float x, float y)\n" +
                        "不论是直线还是贝塞尔曲线，都是以当前位置作为起点，而不能指定起点。但你可以通过 moveTo(x, y) 或 rMoveTo() 来改变当前位置，从而间接地设置这些方法的起点。\n\n" +
                        "paint.setStyle(Paint.Style.STROKE)\n" +
                        "paint.setStrokeWidth(5)\n" +
                        "path.quadTo(50, 50, 100, 200)\n" +
                        "path.rMoveTo(100, 0)\n" +
                        "path.quadTo(150, 150, 200, 0)\n" +
                        "canvas.drawPath(path, paint)";
            case 4:
                return "画弧形\n" +
                        "arcTo(RectF oval, float startAngle, float sweepAngle, boolean forceMoveTo)\n" +
                        "arcTo(float left, float top, float right, float bottom, float startAngle, float sweepAngle, boolean forceMoveTo)\n" +
                        "arcTo(RectF oval, float startAngle, float sweepAngle)\n" +
                        "arcTo() 和 addArc() 也是用来画线的，但并不使用当前位置作为弧线的起点。\n" +
                        "这些方法和 Canvas.drawArc() 比起来，少了一个参数 useCenter，而多了一个参数 forceMoveTo 。\n" +
                        "少了 useCenter ，是因为 arcTo() 只用来画弧形而不画扇形，所以不再需要 useCenter 参数；\n" +
                        "而多出来的这个 forceMoveTo 参数的意思是，绘制是要「抬一下笔移动过去」，还是「直接拖着笔过去」，区别在于是否留下移动的痕迹。\n\n" +
                        "paint.setStyle(Paint.Style.STROKE)\n" +
                        "paint.setStrokeWidth(5)\n" +
                        "path.lineTo(100, 100)\n" +
                        "// 强制移动到弧形起点（无痕迹）\n" +
                        "path.arcTo(100, 100, 300, 300, -90, 90, true)\n" +
                        "canvas.drawPath(path, paint)";
            case 5:
                return "paint.setStyle(Paint.Style.STROKE)\n" +
                        "paint.setStrokeWidth(5)\n" +
                        "path.lineTo(100, 100)\n" +
                        "// 直接连线连到弧形起点（有痕迹）\n" +
                        "path.arcTo(100, 100, 300, 300, -90, 90, false)\n" +
                        "canvas.drawPath(path, paint)";
            case 6:
                return "画弧形\n" +
                        "addArc(float left, float top, float right, float bottom, float startAngle, float sweepAngle)\n" +
                        "addArc(RectF oval, float startAngle, float sweepAngle)\n" +
                        "addArc() 只是一个直接使用了 forceMoveTo = true 的简化版 arcTo()\n\n" +
                        "paint.setStyle(Paint.Style.STROKE)\n" +
                        "paint.setStrokeWidth(5)\n" +
                        "path.lineTo(100, 100)\n" +
                        "// 强制移动到弧形起点（无痕迹）\n" +
                        "path.addArc(100, 100, 300, 300, -90, 90)\n" +
                        "canvas.drawPath(path, paint)";
            case 7:
                return "封闭当前子图形\n" +
                        "close()\n" +
                        "「子图形」：官方文档里叫做 contour 。但由于在这个场景下我找不到这个词合适的中文翻译（直译的话叫做「轮廓」），所以我换了个便于中国人理解的词：「子图形」。\n" +
                        "第一组方法是「添加子图形」，所谓「子图形」，指的就是一次不间断的连线。一个 Path 可以包含多个子图形。\n" +
                        "当使用第一组方法，即 addCircle() addRect() 等方法的时候，每一次方法调用都是新增了一个独立的子图形；\n" +
                        "而如果使用第二组方法，即 lineTo() arcTo() 等方法的时候，则是每一次断线（即每一次「抬笔」），都标志着一个子图形的结束，以及一个新的子图形的开始。\n" +
                        "另外，不是所有的子图形都需要使用 close() 来封闭。当需要填充图形时（即 Paint.Style 为  FILL 或 FILL_AND_STROKE），Path 会自动封闭子图形。\n\n" +
                        "paint.setStyle(Paint.Style.STROKE)\n" +
                        "paint.setStrokeWidth(5)\n" +
                        "path.moveTo(100, 100)\n" +
                        "path.lineTo(200, 100)\n" +
                        "path.lineTo(150, 150)\n" +
                        "canvas.drawPath(path, paint)";
            case 8:
                return "paint.setStyle(Paint.Style.STROKE)\n" +
                        "paint.setStrokeWidth(5)\n" +
                        "path.moveTo(100, 100)\n" +
                        "path.lineTo(200, 100)\n" +
                        "path.lineTo(150, 150)\n" +
                        "// 使用 close() 封闭子图形。等价于path.lineTo(100, 100)\n" +
                        "path.close()\n" +
                        "canvas.drawPath(path, paint)";
            case 9:
                return "paint.setStyle(Paint.Style.FILL)\n" +
                        "paint.setStrokeWidth(5)\n" +
                        "// 这里只绘制了两条边，但由于 Style 是 FILL ，所以绘制时会自动封口\n" +
                        "path.moveTo(100, 100)\n" +
                        "path.lineTo(200, 100)\n" +
                        "path.lineTo(150, 150)\n" +
                        "canvas.drawPath(path, paint)";
            case 10:
                return "设置填充方式\n" +
                        "Path.setFillType(Path.FillType ft)\n" +
                        "FillType 的取值有四个：\n" +
                        "1. EVEN_ODD\n" +
                        "2. WINDING（默认值）\n" +
                        "3. INVERSE_EVEN_ODD\n" +
                        "4. INVERSE_WINDING\n" +
                        "其中后面的两个带有 INVERSE_ 前缀的，只是前两个的反色版本\n" +
                        "WINDING 是「全填充」，而 EVEN_ODD 是「交叉填充」\n" +
                        "\n" +
                        "EVEN_ODD，即 even-odd rule （奇偶原则）：\n" +
                        "对于平面中的任意一点，向任意方向射出一条射线，这条射线和图形相交的次数（相交才算，相切不算哦）\n" +
                        "如果是奇数，则这个点被认为在图形内部，是要被涂色的区域；\n" +
                        "如果是偶数，则这个点被认为在图形外部，是不被涂色的区域。\n" +
                        "个人理解是重叠部分是奇数就绘制，偶数就不绘制，有点类似于名为 voi 的游戏。。。\n" +
                        "\n" +
                        "WINDING\n" +
                        "即 non-zero winding rule （非零环绕数原则）：首先，它需要你图形中的所有线条都是有绘制方向的\n" +
                        "然后，同样是从平面中的点向任意方向射出一条射线，但计算规则不一样：\n" +
                        "以 0 为初始值，对于射线和图形的所有交点，遇到每个顺时针的交点（图形从射线的左边向右穿过）把结果加 1，\n" +
                        "遇到每个逆时针的交点（图形从射线的右边向左穿过）把结果减 1，最终把所有的交点都算上，\n" +
                        "得到的结果如果不是 0，则认为这个点在图形内部，是要被涂色的区域；\n" +
                        "如果是 0，则认为这个点在图形外部，是不被涂色的区域。\n" +
                        "然后，同样是从平面中的点向任意方向射出一条射线，但计算规则不一样：\n" +
                        "以 0 为初始值，对于射线和图形的所有交点，遇到每个顺时针的交点（图形从射线的左边向右穿过）把结果加 1，\n" +
                        "遇到每个逆时针的交点（图形从射线的右边向左穿过）把结果减 1，最终把所有的交点都算上，\n" +
                        "得到的结果如果不是 0，则认为这个点在图形内部，是要被涂色的区域；\n" +
                        "如果是 0，则认为这个点在图形外部，是不被涂色的区域。\n" +
                        "图形的方向：\n" +
                        "对于添加子图形类方法（如 Path.addCircle() Path.addRect()）的方向，由方法的  dir 参数来控制。\n" +
                        "而对于画线类的方法（如 Path.lineTo() Path.arcTo()）就更简单了，线的方向就是图形的方向。\n\n" +
                        "path.setFillType(Path.FillType.EVEN_ODD)\n" +
                        "path.addCircle(100, 100, 100, Path.Direction.CW)\n" +
                        "path.addCircle(200, 100, 100, Path.Direction.CW)\n" +
                        "canvas.drawPath(path, paint)";
            case 11:
                return "path.setFillType(Path.FillType.WINDING)\n" +
                        "path.addCircle(100, 100, 100, Path.Direction.CW)\n" +
                        "path.addCircle(200, 100, 100, Path.Direction.CW)\n" +
                        "canvas.drawPath(path, paint)";
            case 12:
                return "path.setFillType(Path.FillType.EVEN_ODD)\n" +
                        "path.addCircle(100, 100, 100, Path.Direction.CW)\n" +
                        "path.addCircle(200, 100, 100, Path.Direction.CCW)\n" +
                        "canvas.drawPath(path, paint)";
            case 13:
                return "path.setFillType(Path.FillType.WINDING)\n" +
                        "path.addCircle(100, 100, 100, Path.Direction.CW)\n" +
                        "path.addCircle(200, 100, 100, Path.Direction.CCW)\n" +
                        "canvas.drawPath(path, paint)";
        }
        return super.getViewTypeInfo(viewType);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
    }
}
