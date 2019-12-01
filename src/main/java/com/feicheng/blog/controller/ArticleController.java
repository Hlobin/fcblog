package com.feicheng.blog.controller;

import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.common.ResponseResult;
import com.feicheng.blog.entity.Article;
import com.feicheng.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 文章控制器
 *
 * @author Lenovo
 */
@Controller
@RequestMapping("article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;


    /**
     * 查询所有的文章并实现分页
     *
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<PageResult<Article>> selectAllArticle(@RequestParam("page") Integer page,
                                                                @RequestParam("limit") Integer limit) {

        PageResult<Article> pageResult = this.articleService.selectAllArticle(page, limit);

        if (CollectionUtils.isEmpty(pageResult.getData())) {

            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(pageResult);
    }


    /**
     * 修改文章
     * @param article
     * @return
     */
    @PostMapping("edit")
    public ResponseEntity<ResponseResult> editArticle(Article article){

        // 修改文章
        this.articleService.editArticle(article);

        return ResponseEntity.ok(new ResponseResult(200, "修改成功"));
    }



    @PostMapping("add")
    public ResponseEntity<ResponseResult> addArticle(Article article){

        // 添加文章
        this.articleService.addArticle(article);

        return ResponseEntity.ok(new ResponseResult(200, "添加成功"));
    }
}
