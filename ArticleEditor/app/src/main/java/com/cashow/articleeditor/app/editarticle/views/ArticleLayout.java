package com.cashow.articleeditor.app.editarticle.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cashow.articleeditor.R;
import com.cashow.articleeditor.app.editarticle.EditArticleUtil;
import com.cashow.articleeditor.app.editarticle.model.ArticleContent;
import com.cashow.articleeditor.app.editarticle.model.ArticleInfo;
import com.cashow.articleeditor.utils.BitmapUtils;
import com.cashow.articleeditor.utils.Constants;
import com.cashow.articleeditor.utils.KeyboardManager;
import com.cashow.articleeditor.utils.Utils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class ArticleLayout extends LinearLayout {
    // 图片的宽度
    private int imageWidth;

    // 目前聚焦的 edittext
    private EditText currentFocusedEdittext;
    // 要删除的 edittext
    private EditText curDeleteEditText;

    private Context context;

    public CompositeSubscription mCompositeSubscription;

    public ArticleLayout(Context context) {
        super(context);
        init();
    }

    public ArticleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ArticleLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        context = getContext();
        imageWidth = Utils.getScreenWidth(context) - 2 * Utils.dp2px(context, 10) - Utils.dp2px(context, 1);
        mCompositeSubscription = new CompositeSubscription();
    }

    /**
     * 生成一个新的editText
     */
    public EditText getNewEdittext() {
        EditText editText = (EditText) LayoutInflater.from(context).inflate(R.layout.layout_article_edittext, this, false);

        editText.setTag(String.valueOf(System.currentTimeMillis()));
        editText.setText(Constants.ZERO_WIDTH_SPACE);
        editText.setOnFocusChangeListener(focusListener);
        editText.setOnEditorActionListener(onEditorActionListener);
        editText.addTextChangedListener(new ArticleTextWatcher(editText));

        setFocusedEdittext(editText);
        return editText;
    }

    private View.OnFocusChangeListener focusListener = (View v, boolean hasFocus) -> {
        if (hasFocus) {
            setFocusedEdittext((EditText) v);
        }
    };

    /**
     * 按回车的时候生成一个新的 edittext 并把焦点放在新的 edittext
     */
    private TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (event == null || event.getAction() != KeyEvent.ACTION_DOWN) {
                return false;
            }
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                int insertPosition = getInsertPosition();
                insertNewEdittext(insertPosition);
                return true;
            }
            return false;
        }
    };

    /**
     * 系统不能监听删除事件，所以在每个editText前面加了没有宽度的符号ZERO_WIDTH_SPACE
     * 如果edittext删除了ZERO_WIDTH_SPACE，那说明这个edittext已经没有内容，并且用户按下了删除按钮
     * 这时就可以把edittext删除掉
     */
    private class ArticleTextWatcher implements TextWatcher {
        int preLength;
        EditText editText;

        public ArticleTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            preLength = s.length();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            int length = text.length();
            // 如果 layout_article 里有多个 edittext 并且删除掉了现在的 edittext 的最后一个字符，那就把这个 edittext 删掉
            if (getEdittextCount() > 1 && preLength == 1 && length == 0) {
                deleteEdittext(editText);
            }
            // 如果 edittext 不是以 ZERO_WIDTH_SPACE 开头的，手动加上 ZERO_WIDTH_SPACE
            // 这种情况一般发生在用户手动将光标位置调节到最前面并输入了一些内容时
            if (!text.startsWith(Constants.ZERO_WIDTH_SPACE)) {
                text = text.replace(Constants.ZERO_WIDTH_SPACE, "");
                text = Constants.ZERO_WIDTH_SPACE + text;
                editText.setText(text);
                EditArticleUtil.moveSelectionToLast(editText);
            }
        }
    }

    /**
     * 插入一个新的editText
     */
    private EditText insertNewEdittext(int insertPosition) {
        EditText newEdittext = getNewEdittext();
        addView(newEdittext, insertPosition);
        EditArticleUtil.changeEdittextFocus(currentFocusedEdittext, newEdittext);
        return newEdittext;
    }

    /**
     * 删除 edittext
     */
    private void deleteEdittext(EditText editText) {
        // 删除 edittext 后，如果被删除的 edittext 前面有 edittext，将焦点移动到前面的 edittext
        // 如果没有，则将焦点移动到后面的 edittext
        String tag = (String) editText.getTag();
        if (curDeleteEditText != null) {
            //删除edittext时 edittext会afterchanged会多次调用 比较tag防止多次删除
            String curTag = (String) curDeleteEditText.getTag();
            if (tag.equals(curTag))
                return;
        }
        curDeleteEditText = editText;

        EditText newEdittext = findNextFocusEdittext();
        if (newEdittext == null) {
            return;
        }
        setFocusedEdittext(newEdittext);
        editText.clearFocus();

        newEdittext.requestFocus();
        newEdittext.setCursorVisible(true);
        // 将光标移到最后
        EditArticleUtil.moveSelectionToLast(newEdittext);

        postDelayed(new Runnable() {
            @Override
            public void run() {
                removeView(editText);
            }
        }, 100);
    }

    /**
     * 找到删除 edittext 后的下一个聚焦位置
     */
    private EditText findNextFocusEdittext() {
        int currentPosition = getInsertPosition() - 1;
        EditText newEdittext = null;
        for (int i = currentPosition - 1; i >= 0; i--) {
            View view = getChildAt(i);
            if (view instanceof EditText) {
                newEdittext = (EditText) view;
                break;
            }
        }
        if (newEdittext == null) {
            for (int i = currentPosition + 1; i < getChildCount(); i++) {
                View view = getChildAt(i);
                if (view instanceof EditText) {
                    newEdittext = (EditText) view;
                    break;
                }
            }
        }
        return newEdittext;
    }

    /**
     * 新插入的edittext和图片要放在光标所在的下一行，因此需要获取到光标所在的edittext的位置
     */
    public int getInsertPosition() {
        String tag = (String) getFocusedEdittext().getTag();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof ImageView) {
                continue;
            }
            String viewTag = (String) view.getTag();
            if (viewTag.equals(tag)) {
                return i + 1;
            }
        }
        return getChildCount();
    }

    /**
     * 向edittext里插入选择好的图片
     */
    public void addImage(String photoPath) {
        View view = getArticleImageView(photoPath, BitmapFactory.decodeResource(getResources(), R.drawable.load_article_edit));
        addView(view, getInsertPosition());
        Subscription subscription = Observable.just(photoPath)
                .observeOn(Schedulers.io())
                .map(path -> BitmapUtils.getCompressedBitmap(path))
                .map(bitmap -> EditArticleUtil.scaledBitmapToImageWidth(bitmap, imageWidth))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bitmap -> {
                    ImageView imageArticle = (ImageView) view.findViewById(R.id.image_article);
                    imageArticle.setImageBitmap(bitmap);
                });
        mCompositeSubscription.add(subscription);

        int insertPosition = getInsertPosition() + 1;

        if (getFocusedEdittext().getText().toString().equals(Constants.ZERO_WIDTH_SPACE)) {
            removeView(getFocusedEdittext());
            insertPosition -= 1;
        }
        if (insertPosition >= getChildCount()) {
            insertNewEdittext(getChildCount());
        }
    }

    private View getArticleImageView(String imagePath, Bitmap bitmap) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_article_edit_image, this, false);
        ImageView imageArticle = (ImageView) view.findViewById(R.id.image_article);
        ImageView imageDelete = (ImageView) view.findViewById(R.id.image_delete);

        imageArticle.setImageBitmap(bitmap);
        imageDelete.setOnClickListener(v -> removeView(view));

        view.setTag(imagePath);
        return view;
    }

    /**
     * 找到最后一个 edittext 并将焦点移动到这个 edittext
     */
    public void setLastEdittextFocused() {
        EditText editText = getLastEditText();
        if (editText == null) {
            // 讲道理的话，应该不会走到这一步
            return;
        }
        EditArticleUtil.changeEdittextFocus(getFocusedEdittext(), editText);
        setFocusedEdittext(editText);
        KeyboardManager.showSoftKeyBoard((Activity) getContext(), getFocusedEdittext());
    }

    /**
     * 找到 layoutArticle 里最后一个 edittext
     */
    private EditText getLastEditText() {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View view = getChildAt(i);
            if (view instanceof EditText) {
                EditText editText = (EditText) view;
                return editText;
            }
        }
        return null;
    }

    /**
     * 设置现在正在聚集的 edittext
     */
    public void setFocusedEdittext(EditText edittext) {
        currentFocusedEdittext = edittext;
    }

    /**
     * 获取正在聚焦的 edittext
     */
    public EditText getFocusedEdittext() {
        return currentFocusedEdittext;
    }

    /**
     * 获取目前添加的 edittext 数量
     */
    private int getEdittextCount() {
        int count = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof EditText) {
                count += 1;
            }
        }
        return count;
    }

    /**
     * 将 ArticleInfo 转换成 edittext 和 imageview
     */
    public void setArticleInfo(ArticleInfo articleInfo) {
        removeAllViews();
        if (articleInfo.article_content.size() == 0) {
            // 如果没有数据，添加一个空的 edittext
            addView(getNewEdittext());
        } else {
            for (int i = 0; i < articleInfo.article_content.size(); i++) {
                ArticleContent content = articleInfo.article_content.get(i);
                if (content.type == ArticleContent.TEXT) {
                    EditText newEdittext = (EditText) getNewEdittext();
                    newEdittext.setText(Constants.ZERO_WIDTH_SPACE + content.content);
                    addView(newEdittext);
                } else {
                    addImage(content.content);
                }
            }
        }
    }

    /**
     * 将目前添加的 edittext 和 imageview 转换成ArticleInfo
     */
    public ArticleInfo getArticleInfo() {
        ArticleInfo articleInfo = new ArticleInfo();
        articleInfo.article_content = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof EditText) {
                articleInfo.article_content.add(getTextArticleContent(view));
            } else {
                articleInfo.article_content.add(getImageArticleContent(view));
            }
        }
        return articleInfo;
    }


    /**
     * 将 imageview 转换成 ArticleContent
     */
    public ArticleContent getImageArticleContent(View view) {
        String imagePath = (String) view.getTag();

        ArticleContent articleContent = new ArticleContent();
        articleContent.type = ArticleContent.IMAGE;
        articleContent.content = imagePath;
        return articleContent;
    }

    /**
     * 将 edittext 转换成 ArticleContent
     */
    public ArticleContent getTextArticleContent(View view) {
        EditText editText = (EditText) view;
        String text = editText.getText().toString();
        text = text.replace(Constants.ZERO_WIDTH_SPACE, "");

        ArticleContent articleContent = new ArticleContent();
        articleContent.type = ArticleContent.TEXT;
        articleContent.content = text;

        return articleContent;
    }

    public void onDestroy() {
        mCompositeSubscription.unsubscribe();
    }
}
