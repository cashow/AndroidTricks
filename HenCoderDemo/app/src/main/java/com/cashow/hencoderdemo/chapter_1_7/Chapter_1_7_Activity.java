package com.cashow.hencoderdemo.chapter_1_7;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cashow.hencoderdemo.common.BaseActivity;

import org.adrianwalker.multilinestring.Multiline;

public class Chapter_1_7_Activity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addMyDemoView(new ArgbEvaluator1View(context), INFO_0);
        addMyDemoView(new ArgbEvaluator2View(context), INFO_1);
        addMyDemoView(new HsvEvaluatorView(context), INFO_2);
        addMyDemoView(new PointFEvaluatorView(context), INFO_3);
        addMyDemoView(new PropertyValuesHolderView(context), INFO_4);
        addMyDemoView(new AnimatorSet1View(context), INFO_5);
        addMyDemoView(new AnimatorSet2View(context), INFO_6);
        addMyDemoView(new KeyFrame1View(context), INFO_7);
        addMyDemoView(new KeyFrame2View(context), INFO_8);
        addMyDemoView(new ValueAnimatorView(context), INFO_9);
    }

    @Override
    protected Class[] getBaseViewClasses() {
        return new Class[]{};
    }

    /**
     * ArgbEvaluator: 颜色渐变的动画
     *
     * public class ArgbEvaluator1View extends View {
     *     private int color;
     *     private Paint paint;
     *
     *     ...
     *
     *     private void startAnim() {
     *         ObjectAnimator objectAnimator = ObjectAnimator.ofInt(this, "color", 0xffff0000, 0xff00ff00);
     *         objectAnimator.start();
     *     }
     *
     *     public void setColor(int color) {
     *         this.color = color;
     *         invalidate();
     *     }
     *
     *     @Override
     *     protected void onDraw(Canvas canvas) {
     *         super.onDraw(canvas);
     *         paint.setColor(color);
     *         canvas.drawCircle(100, 100, 100, paint);
     *     }
     * }
     */
    @Multiline
    static String INFO_0;

    /**
     * 在上面的基础上加上了：
     * objectAnimator.setEvaluator(new ArgbEvaluator());
     */
    @Multiline
    static String INFO_1;

    /**
     * objectAnimator.setEvaluator(new HsvEvaluator());
     *
     * public class HsvEvaluator implements TypeEvaluator<Integer> {
     *     float[] startHsv = new float[3];
     *     float[] endHsv = new float[3];
     *     float[] outHsv = new float[3];
     *
     *     @Override
     *     public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
     *         // 把 ARGB 转换成 HSV
     *         Color.colorToHSV(startValue, startHsv);
     *         Color.colorToHSV(endValue, endHsv);
     *
     *         if (endHsv[0] - startHsv[0] > 180) {
     *             endHsv[0] -= 360;
     *         } else if (endHsv[0] - startHsv[0] < -180) {
     *             endHsv[0] += 360;
     *         }
     *         outHsv[0] = startHsv[0] + (endHsv[0] - startHsv[0]) * fraction;
     *         if (outHsv[0] > 360) {
     *             outHsv[0] -= 360;
     *         } else if (outHsv[0] < 0) {
     *             outHsv[0] += 360;
     *         }
     *         outHsv[1] = startHsv[1] + (endHsv[1] - startHsv[1]) * fraction;
     *         outHsv[2] = startHsv[2] + (endHsv[2] - startHsv[2]) * fraction;
     *
     *         // 计算当前动画完成度（fraction）所对应的透明度
     *         int alpha = startValue >> 24 + (int) ((endValue >> 24 - startValue >> 24) * fraction);
     *
     *         // 把 HSV 转换回 ARGB 返回
     *         return Color.HSVToColor(alpha, outHsv);
     *     }
     * }
     */
    @Multiline
    static String INFO_2;

    /**
     * 借助于 TypeEvaluator，属性动画就可以通过 ofObject() 来对不限定类型的属性做动画了。方式很简单：
     * 1. 为目标属性写一个自定义的 TypeEvaluator
     * 2. 使用 ofObject() 来创建 Animator，并把自定义的 TypeEvaluator 作为参数填入
     *
     * public class PointFEvaluatorView extends View {
     *     private PointF pointF;
     *     private Paint paint;
     *
     *     ...
     *
     *     private void startAnim() {
     *         PointF startPoint = new PointF(100, 100);
     *         PointF endPoint = new PointF(300, 300);
     *
     *         ObjectAnimator objectAnimator = ObjectAnimator.ofObject(this, "pointF", new PointFEvaluator(), startPoint, endPoint);
     *         objectAnimator.start();
     *     }
     *
     *     public void setPointF(PointF pointF) {
     *         this.pointF = pointF;
     *         invalidate();
     *     }
     *
     *     @Override
     *     protected void onDraw(Canvas canvas) {
     *         super.onDraw(canvas);
     *         canvas.drawCircle(pointF.x, pointF.y, 100, paint);
     *     }
     * }
     */
    @Multiline
    static String INFO_3;

    /**
     * 很多时候，你在同一个动画中会需要改变多个属性，例如在改变透明度的同时改变尺寸。如果使用 ViewPropertyAnimator，你可以直接用连写的方式来在一个动画中同时改变多个属性：
     * view.animate()
     *    .scaleX(1)
     *    .scaleY(1)
     *    .alpha(1);
     *
     * 而对于 ObjectAnimator，是不能这么用的。不过你可以使用 PropertyValuesHolder 来同时在一个动画中改变多个属性。
     * 如果有多个属性需要修改，可以把它们放在不同的 PropertyValuesHolder 中，然后使用  ofPropertyValuesHolder() 统一放进 Animator。这样你就不用为每个属性单独创建一个 Animator 分别执行了。
     *
     * public class PropertyValuesHolderView extends View {
     *     private PointF pointF;
     *     private Paint paint;
     *     private int color;
     *
     *     ...
     *
     *     private void startAnim() {
     *         PointF startPoint = new PointF(100, 100);
     *         PointF endPoint = new PointF(300, 300);
     *
     *         PropertyValuesHolder holder1 = PropertyValuesHolder.ofObject("color", new HsvEvaluator(), 0xffff0000, 0xff00ff00);
     *         PropertyValuesHolder holder2 = PropertyValuesHolder.ofObject("pointF", new PointFEvaluator(), startPoint, endPoint);
     *
     *         ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(this, holder1, holder2);
     *         objectAnimator.start();
     *     }
     *
     *     public void setColor(int color) {
     *         this.color = color;
     *         invalidate();
     *     }
     *
     *     public void setPointF(PointF pointF) {
     *         this.pointF = pointF;
     *         invalidate();
     *     }
     *
     *     @Override
     *     protected void onDraw(Canvas canvas) {
     *         super.onDraw(canvas);
     *         paint.setColor(color);
     *         canvas.drawCircle(pointF.x, pointF.y, 100, paint);
     *     }
     * }
     */
    @Multiline
    static String INFO_4;

    /**
     * AnimatorSet 多个动画配合执行
     * 有的时候，你不止需要在一个动画中改变多个属性，还会需要多个动画配合工作，
     * 比如，在内容的大小从 0 放大到 100% 大小后开始移动。
     * 这种情况使用 PropertyValuesHolder 是不行的，因为这些属性如果放在同一个动画中，
     * 需要共享动画的开始时间、结束时间、Interpolator 等等一系列的设定，这样就不能有先后次序地执行动画了。
     * 这就需要用到 AnimatorSet 了。
     *
     * public class AnimatorSet1View extends ImageView {
     *     private PointF pointF;
     *     private Paint paint;
     *     private int color;
     *
     *     ...
     *
     *     private void startAnim() {
     *         PointF startPoint = new PointF(100, 100);
     *         PointF endPoint = new PointF(300, 300);
     *
     *         ObjectAnimator objectAnimator1 = ObjectAnimator.ofObject(this, "color", new HsvEvaluator(), 0xffff0000, 0xff00ff00);
     *         ObjectAnimator objectAnimator2 = ObjectAnimator.ofObject(this, "pointF", new PointFEvaluator(), startPoint, endPoint);
     *
     *         AnimatorSet animatorSet = new AnimatorSet();
     *         animatorSet.playSequentially(objectAnimator1, objectAnimator2);
     *         animatorSet.start();
     *     }
     *
     *     public void setColor(int color) {
     *         this.color = color;
     *         invalidate();
     *     }
     *
     *     public void setPointF(PointF pointF) {
     *         this.pointF = pointF;
     *         invalidate();
     *     }
     *
     *     @Override
     *     protected void onDraw(Canvas canvas) {
     *         super.onDraw(canvas);
     *         paint.setColor(color);
     *         canvas.drawCircle(pointF.x, pointF.y, 100, paint);
     *     }
     * }
     */
    @Multiline
    static String INFO_5;

    /**
     * public class AnimatorSet2View extends ImageView {
     *     private PointF pointF;
     *     private Paint paint;
     *     private int color;
     *
     *     ...
     *
     *     private void startAnim() {
     *         PointF startPoint = new PointF(100, 100);
     *         PointF endPoint = new PointF(300, 300);
     *
     *         ObjectAnimator objectAnimator1 = ObjectAnimator.ofObject(this, "color", new HsvEvaluator(), 0xffff0000, 0xff00ff00);
     *         ObjectAnimator objectAnimator2 = ObjectAnimator.ofObject(this, "pointF", new PointFEvaluator(), startPoint, endPoint);
     *
     *         AnimatorSet animatorSet = new AnimatorSet();
     *         animatorSet.playTogether(objectAnimator1, objectAnimator2);
     *         animatorSet.start();
     *     }
     *
     *     public void setColor(int color) {
     *         this.color = color;
     *         invalidate();
     *     }
     *
     *     public void setPointF(PointF pointF) {
     *         this.pointF = pointF;
     *         invalidate();
     *     }
     *
     *     @Override
     *     protected void onDraw(Canvas canvas) {
     *         super.onDraw(canvas);
     *         paint.setColor(color);
     *         canvas.drawCircle(pointF.x, pointF.y, 100, paint);
     *     }
     * }
     */
    @Multiline
    static String INFO_6;

    /**
     * PropertyValuesHolders.ofKeyframe() 把同一个属性拆分
     * 除了合并多个属性和调配多个动画，你还可以在 PropertyValuesHolder 的基础上更进一步，
     * 通过设置  Keyframe （关键帧），把同一个动画属性拆分成多个阶段。例如，你可以让一个进度增加到 100% 后再「反弹」回来。
     *
     * Keyframe ofFloat(float fraction, float value)
     * fraction：表示当前的显示进度，即从加速器中 getInterpolation()函数的返回值；
     * value：表示当前应该在的位置 比如 Keyframe.ofFloat(0, 0)表示动画进度为 0 时，动画所在的数值位置为 0；
     * Keyframe.ofFloat(0.25f, -20f)表示动画进度为 25%时，动画所在的数值位置为-20；
     * Keyframe.ofFloat(1f,0)表示动画结束时，动画所在的数值位置为 0；
     *
     * public class KeyFrame1View extends ImageView {
     *
     *    ...
     *
     *     private void startAnim() {
     *         Keyframe keyframe1 = Keyframe.ofFloat(0, 0);
     *         Keyframe keyframe2 = Keyframe.ofFloat(0.5f, 100);
     *         Keyframe keyframe3 = Keyframe.ofFloat(1f, 60);
     *
     *         PropertyValuesHolder holder = PropertyValuesHolder.ofKeyframe("rotation", keyframe1, keyframe2, keyframe3);
     *
     *         ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(this, holder);
     *         objectAnimator.start();
     *     }
     * }
     */
    @Multiline
    static String INFO_7;

    /**
     * public class KeyFrame2View extends ImageView {
     *     private PointF pointF;
     *     private Paint paint;
     *
     *     ...
     *
     *     private void startAnim() {
     *         PointF startPoint = new PointF(100, 100);
     *         PointF middlePoint = new PointF(300, 300);
     *         PointF endPoint = new PointF(200, 200);
     *
     *         Keyframe keyframe1 = Keyframe.ofObject(0.0f, startPoint);
     *         Keyframe keyframe2 = Keyframe.ofObject(0.5f, middlePoint);
     *         Keyframe keyframe3 = Keyframe.ofObject(1.0f, endPoint);
     *
     *         PropertyValuesHolder holder = PropertyValuesHolder.ofKeyframe("pointF", keyframe1, keyframe2, keyframe3);
     *         holder.setEvaluator(new PointFEvaluator());
     *
     *         ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(this, holder);
     *         objectAnimator.start();
     *     }
     *
     *     public void setPointF(PointF pointF) {
     *         this.pointF = pointF;
     *         invalidate();
     *     }
     *
     *     @Override
     *     protected void onDraw(Canvas canvas) {
     *         super.onDraw(canvas);
     *         canvas.drawCircle(pointF.x, pointF.y, 100, paint);
     *     }
     * }
     */
    @Multiline
    static String INFO_8;

    /**
     * public class ValueAnimatorView extends View {
     *     private Paint paint;
     *     private int circleX;
     *     private int circleY;
     *
     *     ...
     *
     *     private void startAnim() {
     *         ValueAnimator imageBodyAnimator = ValueAnimator.ofInt(0, 100);
     *         imageBodyAnimator.setInterpolator(new AccelerateInterpolator());
     *         imageBodyAnimator.addUpdateListener(animation -> {
     *             int val = (Integer) animation.getAnimatedValue();
     *             int circleX = 100 + val;
     *             int circleY = 100 + val;
     *             setCirclePosition(circleX, circleY);
     *         });
     *         imageBodyAnimator.setDuration(2000);
     *         imageBodyAnimator.setTarget(this);
     *         imageBodyAnimator.addListener(new AnimatorListenerAdapter() {
     *             @Override
     *             public void onAnimationEnd(Animator animation) {
     *                 postDelayed(() -> startAnim(), 1000);
     *             }
     *         });
     *         imageBodyAnimator.start();
     *     }
     *
     *     public void setCirclePosition(int circleX, int circleY) {
     *         this.circleX = circleX;
     *         this.circleY = circleY;
     *         invalidate();
     *     }
     *
     *     @Override
     *     protected void onDraw(Canvas canvas) {
     *         super.onDraw(canvas);
     *         canvas.drawCircle(circleX, circleY, 100, paint);
     *     }
     * }
     */
    @Multiline
    static String INFO_9;
}
