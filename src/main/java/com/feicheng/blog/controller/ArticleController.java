package com.feicheng.blog.controller;

import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.common.ResponseResult;
import com.feicheng.blog.entity.Article;
import com.feicheng.blog.entity.Comment;
import com.feicheng.blog.service.ArticleService;
import com.feicheng.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Autowired
    private CommentService commentService;


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


    /**
     * 添加文章
     * @param article
     * @return
     */
    @PostMapping("add")
    public ResponseEntity<ResponseResult> addArticle(Article article){

        // 添加文章
        this.articleService.addArticle(article);

        return ResponseEntity.ok(new ResponseResult(200, "添加成功"));
    }


    /**
     * 根据id查询文章并跳转到详情页面
     * @param id
     * @return
     */
    @GetMapping("detail/{id}")
    public String selectArticleById(@PathVariable("id") Integer id, Model model){

        // 根据id查询文章信息
        Article article = this.articleService.selectArticleById(id);

        // 根据文章id查询所有的该文章下的评论
        List<Comment> comments = this.commentService.selectAllCommentByArticleId(id);

        if (article == null){

            model.addAttribute("commentCount", 0);

            model.addAttribute("article", null);

            model.addAttribute("comments", null);

            return "front/info";
        }

        if (CollectionUtils.isEmpty(comments)){


            model.addAttribute("commentCount", 0);

            model.addAttribute("comments", null);

            model.addAttribute("article", article);

            return "front/info";

        }

        model.addAttribute("comments", comments);

        model.addAttribute("commentCount", comments.size());

        model.addAttribute("article", article);

        return "front/info";
    }
}
