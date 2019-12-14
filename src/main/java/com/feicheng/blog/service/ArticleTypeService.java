package com.feicheng.blog.service;

import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.entity.ArticleType;

import java.util.Map;

/**
 * @author Lenovo
 */
public interface ArticleTypeService {

    // 查询所有的文章分类
    PageResult<ArticleType> selectAllArticleType();

    // 修改文章类型
    Map<String, Object> editArticleType(ArticleType articleType);

    // 删除问猴子类型
    Map<String, Object> deleteArticleType(Integer articleTypeId);

    // 查询前10条标签，并按文章数量排序
    PageResult<ArticleType> selectArticleByPageAndCount(Integer articleTypeIndex, Integer articleTypeLimit);

    // 文章标签保存出错，发送邮件到我的邮箱
    void sendRedisErrorToEmail(String message);

    // 更新redis中的标签数据
    void updateRedisData();

    // 添加文章类型
    Map<String, Object> addArticleType(String articleTypeName);
}
