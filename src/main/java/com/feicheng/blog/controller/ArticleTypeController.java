package com.feicheng.blog.controller;

import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.entity.ArticleType;
import com.feicheng.blog.service.ArticleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 文章类型管理Controller
 * @author Lenovo
 */
@Controller
@RequestMapping("articleType")
public class ArticleTypeController {

    @Autowired
    private ArticleTypeService articleTypeService;


    /**
     * 查询所有的文章分类
     * @return
     */
    @GetMapping("select")
    public ResponseEntity<PageResult<ArticleType>> selectAllArticleType(){

        PageResult<ArticleType> pageResult = this.articleTypeService.selectAllArticleType();

        if (CollectionUtils.isEmpty(pageResult.getData())){

            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(pageResult);
    }
}
