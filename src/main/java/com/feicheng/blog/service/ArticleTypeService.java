package com.feicheng.blog.service;

import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.entity.ArticleType;

/**
 * @author Lenovo
 */
public interface ArticleTypeService {

    // 查询所有的文章分类
    PageResult<ArticleType> selectAllArticleType();
}
