package com.cashow.articleeditor.app.editarticle.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ArticleContent {
    public static final int IMAGE = 0;
    public static final int TEXT = 1;

    @IntDef({IMAGE, TEXT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ArticleContentType{}

    @ArticleContentType
    public int type;

    /**
     * type 等于 IMAGE 时，content 是图片的路径
     * type 等于 TEXT 时，content 是文字内容
     */
	public String content;

	public long id;
}
