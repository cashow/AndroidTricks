package com.cashow.photobrush.app.brush.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cashow.photobrush.R;
import com.cashow.photobrush.app.brush.listener.BrushListener;
import com.cashow.photobrush.utils.BitmapUtils;
import com.cashow.photobrush.utils.Utils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class BrushView extends RelativeLayout {
    /**
     * 上一次触摸的 x 坐标
     */
    private float preX;
    /**
     * 上一次触摸的 y 坐标
     */
    private float preY;
    /**
     * 目前的 x 坐标
     */
    private float currentX;
    /**
     * 目前的 y 坐标
     */
    private float currentY;

    /**
     * 笔刷使用次数
     */
    private int brushCount;

    /**
     * 要撤销的笔刷图片 id
     */
    private int undoId;

    /**
     * 图片缩放的比例。用来将屏幕上的坐标转换到最终生成的图片的坐标
     */
    public double bitmapResizedRatio;

    /**
     * 要处理的图片
     */
    private Bitmap oriBitmap;
    /**
     * 使用笔刷画过的图片，覆盖在原图上
     */
    private Bitmap currentBitmap;
    /**
     * currentBitmap 的画布
     */
    private Canvas canvas;
    /**
     * 笔刷的原图
     */
    private Bitmap brushOriBitmap;

    /**
     * 用来清除 canvas 的 Paint
     */
    private Paint clearPaint;

    /**
     * 记录笔刷的 bitmap 有没有保存到本地
     */
    private List<Boolean> isSave;

    private Context context;

    private BrushListener brushListener;

    private CompositeSubscription mCompositeSubscription;

    SquareImageView imagePhoto;
    SquareImageView imageBrush;

    private final static double MAX_WIDTH = 640.0;
    private final static double pi = Math.acos(-1.0);
    private final static int MINDIS = 10;
    private final static double EPS = 1e-8;

    public BrushView(Context context) {
        super(context);
        init(context);
    }

    public BrushView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BrushView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_brush, this, true);

        imagePhoto = findViewById(R.id.image_photo);
        imageBrush = findViewById(R.id.image_brush);

        mCompositeSubscription = new CompositeSubscription();

        undoId = -1;
        brushCount = 0;
        bitmapResizedRatio = Utils.getScreenWidth(context) / MAX_WIDTH;

        Utils.deleteBrushFile(context);

        clearPaint = new Paint();
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        isSave = new LinkedList<Boolean>();

        initBrushBitmap();
    }

    /**
     * 初始化笔刷的图片
     */
    private void initBrushBitmap() {
        Drawable drawable = context.getResources().getDrawable(R.drawable.brush);
        brushOriBitmap = ((BitmapDrawable) drawable).getBitmap();

        int newBrushWidth = (int) (brushOriBitmap.getWidth() / bitmapResizedRatio);
        int newBrushHeight = (int) (brushOriBitmap.getHeight() / bitmapResizedRatio);

        brushOriBitmap = BitmapUtils.getResizedBitmap(brushOriBitmap, newBrushWidth, newBrushHeight);

        currentBitmap = Bitmap.createBitmap((int) MAX_WIDTH, (int) MAX_WIDTH, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(currentBitmap);

        imageBrush.setImageBitmap(currentBitmap);
    }

    public void setBrushListener(BrushListener brushListener) {
        this.brushListener = brushListener;
    }

    private void addSubscription(Subscription subscription) {
        mCompositeSubscription.add(subscription);
    }

    /**
     * 设置要处理的图片
     */
    public void setOriBitmap(Bitmap oriBitmap) {
        this.oriBitmap = oriBitmap;
        imagePhoto.setImageBitmap(oriBitmap);
    }

    /**
     * 生成原图和笔刷图合成后的图片
     */
    public void onProcessBitmap() {
        brushListener.onShowMyDialog();
        Subscription subscription = Observable.just(Utils.getImagePath(context))
                .observeOn(Schedulers.computation())
                .map(imagePath -> {
                    Bitmap layoutBitmap = getProcessedBitmap();
                    if (layoutBitmap == null) {
                        return false;
                    }
                    Utils.savePNG(layoutBitmap, imagePath);
                    return true;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isSaveBitmap -> {
                    brushListener.onHideMyDialog();
                    if (!isSaveBitmap) {
                        Toast.makeText(context, R.string.process_photo_failed, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    brushListener.onProcessBitmapSuccess();
                });
        addSubscription(subscription);
    }

    /**
     * 合并原图和笔刷图
     */
    public Bitmap getProcessedBitmap() {
        // 最终要生成的图片
        Bitmap processedBitmap = Bitmap.createBitmap(oriBitmap.getWidth(), oriBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        int width = processedBitmap.getWidth();
        Canvas canvas = new Canvas(processedBitmap);

        // 画原图
        canvas.drawBitmap(oriBitmap, 0, 0, null);

        // 合成笔刷效果
        mergeBitmap(canvas, width, currentBitmap);
        return processedBitmap;
    }

    private void mergeBitmap(Canvas canvas, int width, Bitmap newBitmap) {
        newBitmap = BitmapUtils.getResizedBitmap(newBitmap, width, width);
        canvas.drawBitmap(newBitmap, 0, 0, null);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // 触摸开始时记录触摸点坐标
                preX = event.getRawX();
                preY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                // 移动时如果到达画笔刷的距离，绘制笔刷
                int[] viewCoords = new int[2];
                imagePhoto.getLocationOnScreen(viewCoords);

                currentX = event.getRawX();
                currentY = event.getRawY();

                double dx = currentX - preX;
                double dy = -currentY + preY;

                double fdx = -dy;
                double fdy = dx;

                if ((currentX - preX) * (currentX - preX) + (currentY - preY) * (currentY - preY) < MINDIS * MINDIS) {
                    // 如果移动距离小于给定值，忽略这次的触摸点
                    break;
                }
                if (!Utils.isInView(imagePhoto, currentX, currentY)) {
                    // 触摸点已经移到 BrushView 外面
                    break;
                }
                if (Math.abs(dx) < EPS && Math.abs(dy) < EPS) {
                    // 触摸点几乎没有移动
                    break;
                }

                int imageX = (int) (currentX - viewCoords[0]);
                int imageY = (int) (currentY - viewCoords[1]);

                // 根据原图的缩放比例计算触摸点坐标对应在原图上的坐标
                int bitmapX = imageX * oriBitmap.getWidth() / imagePhoto.getMeasuredWidth();
                int bitmapY = imageY * oriBitmap.getHeight() / imagePhoto.getMeasuredHeight();

                // 边界处理
                bitmapX = Math.max(0, bitmapX);
                bitmapX = Math.min(oriBitmap.getWidth() - 1, bitmapX);
                bitmapY = Math.max(0, bitmapY);
                bitmapY = Math.min(oriBitmap.getHeight() - 1, bitmapY);

                // 获取触摸点对应的原图坐标上的 rgb 值
                int rgbPixel = oriBitmap.getPixel(bitmapX, bitmapY);
                int red = Color.red(rgbPixel);
                int green = Color.green(rgbPixel);
                int blue = Color.blue(rgbPixel);

                // 复制一个笔刷图片并将笔刷的图片 rgb 值设置成触摸点的 rgb 值
                Bitmap brushTempBitmap = BitmapUtils.convertImg(brushOriBitmap, red, green, blue);

                // 笔刷随机向左右方向抖动
                Random random = new Random();
                int brushMoveDis = (int) (random.nextDouble() * 20.0);
                int moveX = (int) (Math.abs(fdx * brushMoveDis) / Math.sqrt(fdx * fdx + fdy * fdy));
                int moveY = (int) (fdy * moveX / fdx);

                if (random.nextBoolean()) {
                    moveX = -moveX;
                    moveY = -moveY;
                }

                double cos = Math.abs(dx) / Math.sqrt(dx * dx + dy * dy);
                cos = Math.min(cos, 1.0);
                cos = Math.max(cos, 0.0);

                // 根据触摸点和上次触摸点的位置，计算笔刷图片的旋转角度
                float diffRotate = (float) Math.acos(cos);
                diffRotate = (float) (diffRotate * 180.0f / pi);

                if (dx < -EPS && dy < -EPS) {
                    diffRotate = 180.0f - diffRotate;
                } else if (dx < -EPS) {
                    diffRotate = diffRotate - 180.0f;
                } else if (dy > -EPS) {
                    diffRotate = -diffRotate;
                }

                brushTempBitmap = BitmapUtils.adjustPhotoRotation(brushTempBitmap, diffRotate);

                // 计算笔刷最终的位置
                int brushLeft = (int) currentX - brushTempBitmap.getWidth() / 2;
                int brushTop = (int) currentY - brushTempBitmap.getHeight() / 2 - Utils.dp2px(context, 45);
                brushLeft += moveX;
                brushTop += moveY;

                brushLeft = (int) (brushLeft / bitmapResizedRatio);
                brushTop = (int) (brushTop / bitmapResizedRatio);

                // 绘制新的笔刷
                canvas.drawBitmap(brushTempBitmap, brushLeft, brushTop, null);

                preX = currentX;
                preY = currentY;

                imageBrush.setImageBitmap(currentBitmap);
                break;
            case MotionEvent.ACTION_UP:
                // 绘制结束后把笔刷的bitmap保存下来
                isSave.add(false);
                saveBrush(brushCount);

                brushCount++;
                if (brushCount == 1) {
                    brushListener.onBrushCountChange(brushCount);
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 将目前绘制的笔刷图片保存到本地，方便之后做撤销操作
     */
    private void saveBrush(int brushId) {
        Subscription subscription = Observable.just(brushId)
                .observeOn(Schedulers.computation())
                .map(id -> {
                    Bitmap bitmap = Bitmap.createBitmap(currentBitmap);
                    Utils.savePNG(bitmap, Utils.getBrushPath(context, id));
                    return id;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(id -> {
                    if (id == undoId) {
                        loadBrushBitmap(brushCount);
                    } else {
                        if (id < isSave.size()) {
                            isSave.set(id, true);
                        }
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 撤销上一步操作
     */
    public void undo() {
        if (brushCount == 0)
            return;
        brushCount--;

        isSave.remove(isSave.size() - 1);
        File file = new File(Utils.getBrushPath(context, brushCount));
        if (file.exists()) {
            file.delete();
        }
        if (brushCount == 0) {
            brushListener.onBrushCountChange(brushCount);
            canvas.drawPaint(clearPaint);
            imageBrush.setImageBitmap(currentBitmap);
            return;
        }
        brushListener.onShowMyDialog();
        // 如果要撤销的图片还在保存中，标记undoId，否则直接从本地读取图片文件
        if (isSave.get(brushCount - 1)) {
            loadBrushBitmap(brushCount);
        } else {
            undoId = brushCount - 1;
        }
    }

    /**
     * 清除绘制的所有笔刷图片
     */
    public void clear() {
        if (brushCount == 0)
            return;
        brushListener.onBrushCountChange(brushCount);

        brushCount = 0;
        isSave.clear();
        canvas.drawPaint(clearPaint);
        imageBrush.setImageBitmap(currentBitmap);
    }

    /**
     * 从本地加载笔刷图片
     */
    private void loadBrushBitmap(int tempBrushCount) {
        Subscription subscription = Observable.just(tempBrushCount)
                .observeOn(Schedulers.computation())
                .map(id -> {
                    return BitmapFactory.decodeFile(Utils.getBrushPath(context, id - 1));
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bitmap -> {
                    if(tempBrushCount != brushCount)
                        return;
                    if (bitmap == null) {
                        String fileName = Utils.getBrushPath(context, tempBrushCount - 1);
                        File file = new File(fileName);
                        brushListener.onHideMyDialog();
                        return;
                    }
                    brushListener.onHideMyDialog();
                    canvas.drawPaint(clearPaint);
                    canvas.drawBitmap(bitmap, 0, 0, null);
                    imageBrush.setImageBitmap(currentBitmap);
                });
        addSubscription(subscription);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mCompositeSubscription.unsubscribe();
    }
}
