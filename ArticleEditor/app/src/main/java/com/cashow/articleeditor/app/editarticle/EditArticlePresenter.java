package com.cashow.articleeditor.app.editarticle;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.cashow.articleeditor.app.editarticle.model.ArticleContent;
import com.cashow.articleeditor.app.editarticle.model.ArticleInfo;
import com.google.gson.Gson;

import java.util.ArrayList;

public class EditArticlePresenter implements EditArticleContract.Presenter{
    private EditArticleContract.View mView;
    private Context context;

    @Override
    public void setView(EditArticleContract.View mView, Context context) {
        this.mView = mView;
        this.context = context;
    }

    /**
     * 保存草稿
     */
    @Override
    public void saveDraft(ArticleInfo articleInfo) {
        if (isArticleEmpty(articleInfo)) {
            return;
        }
        ArticleContent lastArticleContent = articleInfo.article_content.get(articleInfo.article_content.size() - 1);
        if (lastArticleContent.type == ArticleContent.TEXT && TextUtils.isEmpty(lastArticleContent.content)) {
            articleInfo.article_content.remove(lastArticleContent);
        }
        Gson gson = new Gson();
        String articleJson = gson.toJson(articleInfo);
        SharedPreferences sharedPreferences = context.getSharedPreferences("article", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("article_json", articleJson);
        editor.apply();
    }

    /**
     * 判断用户有没有输入内容
     */
    private boolean isArticleEmpty(ArticleInfo articleInfo) {
        for (int i = 0; i < articleInfo.article_content.size(); i++) {
            ArticleContent content = articleInfo.article_content.get(i);
            if (content.type == ArticleContent.IMAGE) {
                return false;
            }
            if (!TextUtils.isEmpty(content.content)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取草稿
     */
    @Override
    public ArticleInfo getArticleDraft() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("article", Context.MODE_PRIVATE);
        String articleJson = sharedPreferences.getString("article_json", "");
        ArticleInfo articleInfo = new Gson().fromJson(articleJson, ArticleInfo.class);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("article_json", "");
        editor.apply();
        return articleInfo;
    }
}