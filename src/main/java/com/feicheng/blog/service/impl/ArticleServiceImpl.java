package com.feicheng.blog.service.impl;

import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.entity.Article;
import com.feicheng.blog.mapper.ArticleMapper;
import com.feicheng.blog.service.ArticleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;


/**
 * 文章管理Service
 * @author Lenovo
 */
@Service
public class ArticleServiceImpl implements ArticleService {


    @Autowired(required = false)
    private ArticleMapper articleMapper;

    /**
     * 查询所有的文章并实现分页
     * @return
     */
    @Override
    public PageResult<Article> selectAllArticle(Integer page, Integer limit) {

        PageHelper.startPage(page, limit);

        List<Article> articles = this.articleMapper.selectAll();

        if (CollectionUtils.isEmpty(articles)) {

            return new PageResult<>(-1, "查询失败", 0, null);
        }

        PageInfo<Article> pageInfo = new PageInfo<>(articles);

        return new PageResult<>(0, "查询成功", articles.size(), pageInfo.getList());
    }

    /**
     * 修改文章
     * @param article
     */
    @Override
    @Transactional
    public void editArticle(Article article) {

        // 修改基本信息
        article.setArticleLook(article.getArticleLook().equals("on") ? "开放浏览" : "私密浏览");

        article.setArticleTop(article.getArticleTop().equals("checked") ? "checked" : "");

        // 执行修改
        this.articleMapper.updateByPrimaryKeySelective(article);
    }


    /**
     * 添加文章
     * @param article
     */
    @Override
    @Transactional
    public void addArticle(Article article) {

        // 修改基本信息
        article.setArticleLook(article.getArticleLook().equals("on") ? "开放浏览" : "私密浏览");

        article.setArticleTop(article.getArticleTop().equals("checked") ? "checked" : "");

        // 执行添加
        this.articleMapper.insert(article);
    }
}
