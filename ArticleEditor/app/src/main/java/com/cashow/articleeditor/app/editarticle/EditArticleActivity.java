package com.cashow.articleeditor.app.editarticle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.cashow.articleeditor.R;
import com.cashow.articleeditor.app.editarticle.model.ArticleInfo;
import com.cashow.articleeditor.app.pickphoto.PickPhotoActivity;
import com.cashow.articleeditor.utils.BaseActivity;
import com.cashow.articleeditor.utils.KeyboardListener;
import com.cashow.articleeditor.utils.KeyboardManager;
import com.cashow.articleeditor.app.editarticle.views.ArticleLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditArticleActivity extends BaseActivity implements EditArticleContract.View {

    @BindView(R.id.button_pick_photo)
    View buttonPickPhoto;
    @BindView(R.id.layout_main)
    View layoutMain;
    @BindView(R.id.layout_article)
    ArticleLayout layoutArticle;

    private KeyboardManager keyboardManager;

    private EditArticlePresenter mPresenter;

    private static final int PICK_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_article);
        ButterKnife.bind(this);

        keyboardManager = new KeyboardManager();

        mPresenter = new EditArticlePresenter();
        mPresenter.setView(this, context);

        checkArticleDrafts();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 添加软键盘显示和隐藏的监听
        keyboardManager.addKeyboardHeightListener(this, new KeyboardListener() {
            @Override
            public void onShowKeyboard() {
                buttonPickPhoto.setVisibility(View.VISIBLE);
            }

            @Override
            public void onHideKeyboard() {
                buttonPickPhoto.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveDraft();
    }

    @Override
    public void onPause() {
        super.onPause();
        keyboardManager.removeKeyboardHeightListener();

        // 退出页面时保存草稿
        saveDraft();
    }

    @Override
    protected void onDestroy() {
        layoutArticle.onDestroy();
        super.onDestroy();
    }

    /**
     * 检查是否有草稿。如果有，恢复草稿
     */
    private void checkArticleDrafts() {
        ArticleInfo tempArticleInfo = mPresenter.getArticleDraft();

        if (tempArticleInfo != null) {
            // 恢复草稿的内容
            layoutArticle.setArticleInfo(tempArticleInfo);
        } else {
            EditText editText = layoutArticle.getNewEdittext();
            layoutArticle.addView(editText);
        }
    }

    /**
     * 点击空白位置时，找到最后一个 edittext 并将焦点移动到这个 edittext
     */
    @OnClick(R.id.layout_article)
    void onLayoutArticleClick() {
        layoutArticle.setLastEdittextFocused();
    }

    @OnClick(R.id.button_pick_photo)
    void onPickPhotoClick() {
        // 打开选取图片页面时，隐藏掉正在显示的软键盘
        KeyboardManager.hideSoftKeyBoard(mActivity, layoutArticle.getFocusedEdittext());
        startPickPhotoActivity();
    }

    /**
     * 打开选取图片页面
     */
    private void startPickPhotoActivity() {
        Intent intent = new Intent();
        intent.setClass(mActivity, PickPhotoActivity.class);
        mActivity.startActivityForResult(intent, PICK_PHOTO);
    }

    /**
     * 保存草稿
     */
    private void saveDraft() {
        ArticleInfo articleInfo = layoutArticle.getArticleInfo();
        mPresenter.saveDraft(articleInfo);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == PICK_PHOTO) {
            String photoPath = data.getStringExtra("photoPath");
            layoutArticle.addImage(photoPath);
        }
    }
}