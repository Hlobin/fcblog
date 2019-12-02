package com.feicheng.blog.pojo;

import com.feicheng.blog.entity.Comment;

/**
 * comment扩展类
 * @author Lenovo
 */
public class CommentExpend extends Comment {

    private String articleTitle;

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleTitle() {
        return articleTitle;
    }
}
