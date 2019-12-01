package com.feicheng.blog.entity;

import java.io.Serializable;

/**
 * 图片和文章结合类
 * @author DrameCode
 */
public class ArticleImage implements Serializable {

    private Integer id;

    private String articleImage;

    private Integer articleId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getArticleImage() {
        return articleImage;
    }

    public void setArticleImage(String articleImage) {
        this.articleImage = articleImage;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }
}
