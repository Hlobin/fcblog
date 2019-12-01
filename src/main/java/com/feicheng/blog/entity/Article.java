package com.feicheng.blog.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author DrameCode
 * 文章基本类
 */
@Table(name = "article")
public class Article implements Serializable {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    private String articleName;

    private String articleAuthor;

    private String articleAbstract;

    private Integer articleStatus;

    private String articleImage;

    private String articleLook;

    private String articleTop;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date articleDate;

    private String articleContent;

    private Integer articleDelete;

    private Integer articleTypeId;

    private Integer articleRead;

    private Integer articleLove;

    public Integer getArticleDelete() {
        return articleDelete;
    }

    public void setArticleDelete(Integer articleDelete) {
        this.articleDelete = articleDelete;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getArticleAuthor() {
        return articleAuthor;
    }

    public void setArticleAuthor(String articleAuthor) {
        this.articleAuthor = articleAuthor;
    }

    public String getArticleAbstract() {
        return articleAbstract;
    }

    public void setArticleAbstract(String articleAbstract) {
        this.articleAbstract = articleAbstract;
    }

    public Integer getArticleStatus() {
        return articleStatus;
    }

    public void setArticleStatus(Integer articleStatus) {
        this.articleStatus = articleStatus;
    }

    public String getArticleImage() {
        return articleImage;
    }

    public void setArticleImage(String articleImage) {
        this.articleImage = articleImage;
    }

    public String getArticleLook() {
        return articleLook;
    }

    public void setArticleLook(String articleLook) {
        this.articleLook = articleLook;
    }

    public String getArticleTop() {
        return articleTop;
    }

    public void setArticleTop(String articleTop) {
        this.articleTop = articleTop;
    }

    public Date getArticleDate() {
        return articleDate;
    }

    public void setArticleDate(Date articleDate) {
        this.articleDate = articleDate;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public Integer getArticleRead() {
        return articleRead;
    }

    public void setArticleRead(Integer articleRead) {
        this.articleRead = articleRead;
    }

    public Integer getArticleLove() {
        return articleLove;
    }

    public void setArticleLove(Integer articleLove) {
        this.articleLove = articleLove;
    }

    public Integer getArticleTypeId() {
        return articleTypeId;
    }

    public void setArticleTypeId(Integer articleTypeId) {
        this.articleTypeId = articleTypeId;
    }
}
