package com.cashow.articleeditor.app.editarticle;

import android.content.Context;

import com.cashow.articleeditor.app.editarticle.model.ArticleInfo;

public interface EditArticleContract {
    interface View {

    }
    interface Presenter {
        void setView(View mView, Context context);

        // 保存草稿
        void saveDraft(ArticleInfo articleInfo);

        ArticleInfo getArticleDraft();
    }
}