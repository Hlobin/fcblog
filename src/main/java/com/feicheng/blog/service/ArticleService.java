package com.feicheng.blog.service;

import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.entity.Article;

/**
 * @author Lenovo
 */
public interface ArticleService {

    // 查询所有的文章并实现分页
    PageResult<Article> selectAllArticle(Integer page, Integer limit);

    // 修改文章
    void editArticle(Article article);

    // 添加文章
    void addArticle(Article article);

    // 根据id查询文章
    Article selectArticleById(Integer id);
}
