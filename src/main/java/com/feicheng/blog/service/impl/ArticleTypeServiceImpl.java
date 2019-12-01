package com.feicheng.blog.service.impl;

import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.entity.ArticleType;
import com.feicheng.blog.mapper.ArticleTypeMapper;
import com.feicheng.blog.service.ArticleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.zip.CheckedOutputStream;

/**
 * 文章类型管理Service
 *
 * @author Lenovo
 */
@Service
public class ArticleTypeServiceImpl implements ArticleTypeService {

    @Autowired(required = false)
    private ArticleTypeMapper articleTypeMapper;

    /**
     * 查询所有的文章分类
     *
     * @return
     */
    @Override
    public PageResult<ArticleType> selectAllArticleType() {

        List<ArticleType> articleTypes = this.articleTypeMapper.selectAll();

        if (CollectionUtils.isEmpty(articleTypes)) {

            return new PageResult<>(404, "无数据", 0, null);
        }

        return new PageResult<>(200, "查询成功", articleTypes.size(), articleTypes);
    }
}
