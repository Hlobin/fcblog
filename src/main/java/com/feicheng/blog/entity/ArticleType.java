package com.feicheng.blog.entity;

import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

/**
 * 文章分类
 * @author DrameCode
 */
@Table(name = "article_type")
public class ArticleType implements Serializable {
    
    private static final long serialVersionUID = -6128555229192708964L;

    @Id
    @KeySql(useGeneratedKeys = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
