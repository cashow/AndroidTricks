package com.cashow.photobrush.app.brush;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cashow.photobrush.R;
import com.cashow.photobrush.app.brush.listener.BrushListener;
import com.cashow.photobrush.app.brush.view.BrushView;
import com.cashow.photobrush.app.zoomphoto.ZoomPhotoActivity;
import com.cashow.photobrush.utils.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BrushActivity extends BaseActivity implements BrushListener {
    @BindView(R.id.image_brush_undo)
    ImageView imageBrushUndo;
    @BindView(R.id.image_brush_clear)
    ImageView imageBrushClear;
    @BindView(R.id.text_brush_undo)
    TextView textBrushUndo;
    @BindView(R.id.text_brush_clear)
    TextView textBrushClear;
    @BindView(R.id.brush_view)
    BrushView brushView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brush);

        setFullScreen();

        ButterKnife.bind(this);

        brushView.setBrushListener(this);

        loadOriBitmap();
    }

    /**
     * 加载要处理的图片
     */
    private void loadOriBitmap() {
        Drawable drawable = getResources().getDrawable(R.drawable.demo);
        Bitmap oriBitmap = ((BitmapDrawable) drawable).getBitmap();

        brushView.setOriBitmap(oriBitmap);
    }

    /**
     * 显示"撤销"和"清空"按钮
     */
    public void setBrushOptionActive() {
        imageBrushUndo.setImageResource(R.drawable.photo_brush_undo_active);
        textBrushUndo.setTextColor(getResources().getColor(android.R.color.white));

        imageBrushClear.setImageResource(R.drawable.photo_brush_clear_active);
        textBrushClear.setTextColor(getResources().getColor(android.R.color.white));
    }

    /**
     * 禁用"撤销"和"清空"按钮
     */
    public void clearBrushOption() {
        imageBrushUndo.setImageResource(R.drawable.photo_brush_undo_normal);
        textBrushUndo.setTextColor(context.getResources().getColor(R.color.photo_option_normal));

        imageBrushClear.setImageResource(R.drawable.photo_brush_clear_normal);
        textBrushClear.setTextColor(context.getResources().getColor(R.color.photo_option_normal));
    }

    /**
     * 撤销上一步操作
     */
    @OnClick(R.id.layout_option_brush_undo)
    void onUndoClick() {
        brushView.undo();
    }

    /**
     * 清空笔刷记录
     */
    @OnClick(R.id.layout_option_brush_clear)
    void onClearClick() {
        brushView.clear();
    }

    /**
     * 获取原图和笔刷图合成后的图片
     */
    @OnClick(R.id.text_confirm)
    void onConfirmClick() {
        brushView.onProcessBitmap();
    }

    /**
     * 获取合成图片成功
     */
    @Override
    public void onProcessBitmapSuccess() {
        mActivity.startActivity(new Intent(mActivity, ZoomPhotoActivity.class));
    }

    @Override
    public void onShowMyDialog() {
        showMyDialog();
    }

    @Override
    public void onHideMyDialog() {
        hideMyDialog();
    }

    @Override
    public void onBrushCountChange(int brushCount) {
        if (brushCount >= 1) {
            setBrushOptionActive();
        } else {
            clearBrushOption();
        }
    }
}