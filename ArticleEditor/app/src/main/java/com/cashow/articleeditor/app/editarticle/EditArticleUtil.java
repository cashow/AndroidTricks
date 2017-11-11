package com.cashow.articleeditor.app.editarticle;

import android.graphics.Bitmap;
import android.widget.EditText;

import com.cashow.articleeditor.utils.BitmapUtils;

public class EditArticleUtil {
    // 将图片缩放到宽度等于imageWidth
    public static Bitmap scaledBitmapToImageWidth(Bitmap bitmap, int newWidth) {
        int newHeight = (int) (bitmap.getHeight() * newWidth * 1.0 / bitmap.getWidth());
        Bitmap newBitmap = BitmapUtils.getResizedBitmap(bitmap, newWidth, newHeight);
        return newBitmap;
    }

    // 将焦点从editText转移到newEdittext
    public static void changeEdittextFocus(EditText editText, EditText newEdittext) {
        editText.clearFocus();
        newEdittext.requestFocus();
        newEdittext.setCursorVisible(true);
        // 将光标移到最后
        moveSelectionToLast(newEdittext);
    }

    // 将光标移到末尾
    public static void moveSelectionToLast(EditText edittext) {
        int length = edittext.getText().length();
        edittext.setSelection(length);
    }
}