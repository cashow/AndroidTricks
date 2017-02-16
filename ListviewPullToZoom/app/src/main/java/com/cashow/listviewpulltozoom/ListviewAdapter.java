package com.cashow.listviewpulltozoom;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListviewAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;

    private View headerView;
    private Bitmap avatarBitmap;

    // 图片目前的缩放比例
    private double currentRatio;
    // 图片接下来要缩放的比例
    private double nextRatio;

    // 判断有没有加载过背景图片
    private boolean isInit;

    // 背景图片初始的高度
    private int avatarOriHeight;

    public ListviewAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);;
    }

    public void setAvatarBitmap(Bitmap avatarBitmap) {
        this.avatarBitmap = avatarBitmap;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == 0) {
            if (headerView == null) {
                headerView = mLayoutInflater.inflate(R.layout.listview_header,
                        parent, false);
            }
            // view_placeholder和headerView里面的内容（头像、关注、粉丝等）
            // 处在LinearLayout里。在图片缩放的过程中修改view_placeholder的高度，
            // 可以使headerView的内容一直处在底部，实现headerView的内容随着手指下拉
            // 而下移的效果
            LinearLayout layout_info = (LinearLayout)
                    headerView.findViewById(R.id.layout_info);
            View view_placeholder =
                    headerView.findViewById(R.id.view_placeholder);
            ImageView image_background = (ImageView)
                    headerView.findViewById(R.id.image_background);

            if (!isInit && avatarBitmap != null) {
                updateImageBackground(layout_info, view_placeholder,
                        image_background);
            }
            return headerView;
        }
        ViewHolder holder;
        View view = convertView;
        if (convertView == null) {
            view = mLayoutInflater.inflate(R.layout.listview_item, parent,
                    false);
            holder = new ViewHolder();
            holder.text = (TextView) view.findViewById(R.id.text);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.text.setText("text " + position);
        return view;
    }

    private void updateImageBackground(final LinearLayout layout_info,
        final View view_placeholder, final ImageView image_background){
        isInit = true;
        // image_background的宽度和高度要和layout_info保持一致，
        // 所以需要在获取到layout_info高度后设置image_background的高度
        layout_info.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        // view_placeholder的高度大于0时表示正处于缩放状态，
                        // 不能使用这个时候layout_info的高度
                        if (view_placeholder.getHeight() > 0) {
                            return;
                        }
                        int layoutWidth = layout_info.getWidth();
                        int layoutHeight = layout_info.getHeight();

                        // 记录背景图片初始的高度
                        avatarOriHeight = layoutHeight;

                        // 设置image_background的宽度和高度
                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) image_background.getLayoutParams();
                        params.width = layoutWidth;
                        params.height = layoutHeight;
                        image_background.setLayoutParams(params);

                        // 生成宽高比和image_background一样的图片
                        Bitmap cropAvatarBitmap = getCropAvatarBitmap(avatarBitmap
                                , layoutWidth * 1.0 / layoutHeight);
                        image_background.setImageBitmap(cropAvatarBitmap);

                        avatarBitmap = null;

                        if (android.os.Build.VERSION.SDK_INT >=
                                android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            layout_info.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            layout_info.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    }
            });
    }

    // 设置图片的缩放比例
    // zoomDiff是现在的触摸点和初始的触摸点之间的距离
    // 图片最大可以缩放的比例是2倍
    // 图片的缩放比例是：
    // nextRatio = 1.0 + zoomDiff / avatarOriHeight / (1.0 + zoomDiff / avatarOriHeight)
    // 这个计算方法可以实现移动得越远，图片缩放的速度越慢
    public void setZoomDiff(double zoomDiff) {
        double ratio = 1.0 + zoomDiff / avatarOriHeight;
        this.nextRatio = 1.0 + zoomDiff / avatarOriHeight / ratio;
        this.nextRatio = Math.min(this.nextRatio, 2.0);

        // currentRatio不等于zoomRatio表示需要修改图片的高度
        if (Math.abs(currentRatio - nextRatio) > 1e-6) {
            zoomImage(currentRatio, nextRatio, 10);
            currentRatio = nextRatio;
        }
    }

    // 重置缩放状态。图片在200ms内从现在的缩放比例恢复到初始的比例
    // 如果图片处于缩放状态，需要拦截触摸事件的ACTION_UP
    // 返回值是true表示需要拦截触摸事件，false表示不需要拦截
    public boolean resetZoom() {
        if (this.currentRatio != 1.0) {
            zoomImage(currentRatio, 1.0, 200);
            currentRatio = 1.0;
            return true;
        }
        return false;
    }

    // 缩放图片，将图片的缩放比例从startRatio渐变到endRatio
    private void zoomImage(final double startRatio,
                           final double endRatio,
                           long time) {
        final ImageView image_background = (ImageView)
                headerView.findViewById(R.id.image_background);
        final View view_placeholder =
                headerView.findViewById(R.id.view_placeholder);

        ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 100);
        valueAnimator.addUpdateListener(
            new ValueAnimator.AnimatorUpdateListener() {
                private IntEvaluator mEvaluator = new IntEvaluator();

                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    //获得当前动画的进度值，整型，1-100之间
                    int currentValue = (Integer) animator.getAnimatedValue();

                    //计算当前进度占整个动画过程的比例，浮点型，0-1之间
                    float fraction = currentValue / 100f;

                    int imageHeight = mEvaluator.evaluate(
                            fraction,
                            (int) (avatarOriHeight * startRatio),
                            (int) (avatarOriHeight * endRatio));
                    image_background.getLayoutParams().height = imageHeight;
                    image_background.requestLayout();

                    int viewHeight =  mEvaluator.evaluate(
                            fraction,
                            (int) (avatarOriHeight * (startRatio - 1.0)),
                            (int) (avatarOriHeight * (endRatio - 1.0)));

                    view_placeholder.getLayoutParams().height = viewHeight;
                    view_placeholder.requestLayout();
                }
        });
        valueAnimator.setDuration(time).start();
    }

    // 将图片裁剪成宽高比为ratio的图片
    private Bitmap getCropAvatarBitmap(Bitmap bitmap, double ratio) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        int newWidth;
        int newHeight;
        if (bitmapWidth / ratio > bitmapHeight) {
            newWidth = (int) (bitmapHeight * ratio);
            newHeight = bitmapHeight;
        } else {
            newWidth = bitmapWidth;
            newHeight = (int) (bitmapWidth / ratio);
        }

        Bitmap newBitmap = Bitmap.createBitmap(
                bitmap,
                (bitmapWidth - newWidth) / 2,
                (bitmapHeight - newHeight) / 2,
                newWidth,
                newHeight);
        return newBitmap;
    }

    @Override
    public int getCount() {
        return 50;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return 0;
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder {
        TextView text;
    }
}