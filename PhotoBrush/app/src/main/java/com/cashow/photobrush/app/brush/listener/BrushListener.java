package com.cashow.photobrush.app.brush.listener;

public interface BrushListener {
    void onShowMyDialog();
    void onHideMyDialog();
    void onBrushCountChange(int brushCount);
    void onProcessBitmapSuccess();
}
