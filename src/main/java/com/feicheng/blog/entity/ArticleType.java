package com.feicheng.blog.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 文章分类
 * @author DrameCode
 */
public class ArticleType implements Serializable {
    
    private static final long serialVersionUID = -6128555229192708964L;

    private Integer id;

    private String articleTypeName;

    private Integer articleCount;

    private Integer articleTypeDelete;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getArticleTypeName() {
        return articleTypeName;
    }

    public void setArticleTypeName(String articleTypeName) {
        this.articleTypeName = articleTypeName;
    }

    public Integer getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(Integer articleCount) {
        this.articleCount = articleCount;
    }

    public Integer getArticleTypeDelete() {
        return articleTypeDelete;
    }

    public void setArticleTypeDelete(Integer articleTypeDelete) {
        this.articleTypeDelete = articleTypeDelete;
    }

    // 非数据库字段
    private List<Article> articles;

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
