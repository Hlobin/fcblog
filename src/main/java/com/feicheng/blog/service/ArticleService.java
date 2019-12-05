package com.feicheng.blog.service;

import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.entity.Article;

import java.util.List;
import java.util.Map;

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

    // 增加文章的浏览量
    void addArticleLook(Integer id);

    // 根据时间排序并查询前十条文章记录
    PageResult<Article> selectArticleByDate(Integer page, Integer limit);

    // 根据id删除文章
    Map<String, Object> deleteArticle(Integer id);

    // 将文章发送到网站用户的邮箱
    void sendArticleToEmail(Integer id);
}
