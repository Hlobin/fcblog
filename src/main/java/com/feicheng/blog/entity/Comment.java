package com.feicheng.blog.entity;

import java.util.Date;

/**
 * 评论基本类
 * @author DrameCode
 */
public class Comment {

    private Integer id;

    private String commentUserName;

    private String commentUserEmail;

    private String commentContent;

    private Date commentDate;

    private Integer commentArticleId;

    private Integer commentUserId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCommentUserName() {
        return commentUserName;
    }

    public void setCommentUserName(String commentUserName) {
        this.commentUserName = commentUserName;
    }

    public String getCommentUserEmail() {
        return commentUserEmail;
    }

    public void setCommentUserEmail(String commentUserEmail) {
        this.commentUserEmail = commentUserEmail;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    public Integer getCommentArticleId() {
        return commentArticleId;
    }

    public void setCommentArticleId(Integer commentArticleId) {

        this.commentArticleId = commentArticleId;
    }

    public Integer getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(Integer commentUserId) {

        this.commentUserId = commentUserId;
    }

    // 非数据库字段
    private Article article;

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
