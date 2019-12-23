package com.feicheng.blog.controller;

import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.common.ResponseResult;
import com.feicheng.blog.entity.*;
import com.feicheng.blog.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 文章控制器
 *
 * @author Lenovo
 */
@Controller
@RequestMapping("article")
public class ArticleController {

    // 查询首页图片文章的起始位置
    private static final Integer PICTURE_INDEX = 1;

    // 查询首页图片文章的数量
    private static final Integer PICTURE_LIMIT = 5;

    // 查询首页标签的起始位置
    private static final Integer ARTICLE_TYPE_INDEX = 1;

    // 查询首页标签的数量
    private static final Integer ARTICLE_TYPE_LIMIT = 10;

    // 查询首页文章的起始位置
    private static final Integer ARTICLE_INDEX = 1;

    // 查询首页文章的数量
    private static final Integer ARTICLE_LIMIT = 10;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private FrontPictureService frontPictureService;

    @Autowired
    private ArticleTypeService articleTypeService;

    @Autowired
    private MySelfService mySelfService;

    @Autowired
    private TechnologyService technologyService;

    // 点击排行文章的起始位置
    @Value("${article.click.page}")
    private String articleClickPage;

    // 点击排行文章的数量
    @Value("${article.click.limit}")
    private String articleClickLimit;

    // 推荐文章的起始位置
    @Value("${article.commond.page}")
    private String articleCommondPage;

    // 推荐文章的数量
    @Value("${article.commond.limit}")
    private String articleCommondLimit;

    /**
     * 查询所有的文章并实现分页
     *
     * @return
     */
    @GetMapping("list")
    @RequiresPermissions("article:view")
    public ResponseEntity<PageResult<Article>> selectAllArticle(@RequestParam("page") Integer page,
                                                                @RequestParam("limit") Integer limit) {

        PageResult<Article> pageResult = this.articleService.selectArticleByDate(page, limit);

        if (CollectionUtils.isEmpty(pageResult.getData())) {

            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(pageResult);
    }


    /**
     * 修改文章
     *
     * @param article
     * @return
     */
    @PostMapping("edit")
    @RequiresPermissions("article:edit")
    public ResponseEntity<ResponseResult> editArticle(Article article) {

        // 修改文章
        this.articleService.editArticle(article);

        return ResponseEntity.ok(new ResponseResult(200, "修改成功"));
    }


    /**
     * 添加文章
     *
     * @param article
     * @return
     */
    @PostMapping("add")
    @RequiresPermissions("article:add")
    public ResponseEntity<ResponseResult> addArticle(Article article) {

        // 添加文章
        this.articleService.addArticle(article);

        return ResponseEntity.ok(new ResponseResult(200, "添加成功"));
    }


    /**
     * 根据id查询文章并跳转到详情页面
     *
     * @param id
     * @return
     */
    @GetMapping("detail/{id}")
    public String selectArticleById(@PathVariable("id") Integer id, Model model) {

        // 根据id查询文章信息
        Article article = this.articleService.selectArticleById(id);

        // 根据文章id查询所有的该文章下的评论
        List<Comment> comments = this.commentService.selectAllCommentByArticleId(id);

        // 查询浏览量前六的六篇文章
        PageResult<Article> articlePageResult = this.articleService.selectArticleByClick(Integer.parseInt(articleClickPage), Integer.parseInt(articleClickLimit));

        // 查询推荐文章
        PageResult<Article> commondArticlePageResult = this.articleService.selectArticleByDate(Integer.parseInt(articleCommondPage), Integer.parseInt(articleCommondLimit));

        // 查询所有的10标签即分类--根据文章数量排序
        PageResult<ArticleType> typePageResult = this.articleTypeService.selectArticleByPageAndCount(ARTICLE_TYPE_INDEX, ARTICLE_TYPE_LIMIT);

        // 将该文章的浏览量加1
        this.articleService.addArticleLook(id);

        if (article == null) {

            model.addAttribute("commentCount", 0);

            model.addAttribute("article", null);

            model.addAttribute("comments", null);

            model.addAttribute("articleReads", articlePageResult.getData());

            model.addAttribute("articleCommonds", commondArticlePageResult.getData());

            model.addAttribute("articleTypes", typePageResult.getData());

            return "front/info";
        }

        if (CollectionUtils.isEmpty(comments)) {


            model.addAttribute("commentCount", 0);

            model.addAttribute("comments", null);

            model.addAttribute("article", article);

            model.addAttribute("articleReads", articlePageResult.getData());

            model.addAttribute("articleCommonds", commondArticlePageResult.getData());

            model.addAttribute("articleTypes", typePageResult.getData());

            return "front/info";
        }

        model.addAttribute("comments", comments);

        model.addAttribute("commentCount", comments.size());

        model.addAttribute("article", article);

        model.addAttribute("articleReads", articlePageResult.getData());

        model.addAttribute("articleCommonds", commondArticlePageResult.getData());

        model.addAttribute("articleTypes", typePageResult.getData());

        return "front/info";
    }


    /**
     * 根据id删除文章
     * @param id
     * @return
     */
    @PostMapping("delete")
    @RequiresPermissions("article:delete")
    public ResponseEntity<ResponseResult> deleteArticle(@RequestParam("articleId") Integer id) {

        Map<String, Object> map = this.articleService.deleteArticle(id);

        if (StringUtils.equals(map.get("message").toString(), "error")) {

            return ResponseEntity.ok(new ResponseResult(400, map.get("result").toString()));
        }
        return ResponseEntity.ok(new ResponseResult(200, map.get("result").toString()));
    }

    /**
     * 根据文章标签查询文章
     *
     * @param articleTypeId
     * @return
     */
    @GetMapping("type/{articleTypeId}")
    public String selectArticleByType(@PathVariable("articleTypeId") Integer articleTypeId, Model model) {


        // 根据文章类型查询文章并实现分页
        PageResult<Article> articlePageResult = this.articleService.selectArticleByType(ARTICLE_INDEX, ARTICLE_LIMIT, articleTypeId);

        // 查询首页图片文章时间排序的5条文章
        PageResult<FrontPicture> picturePageResult = this.frontPictureService.selectPictureByPageAndByDate(PICTURE_INDEX, PICTURE_LIMIT);

        // 查询所有的10标签即分类--根据文章数量排序
        PageResult<ArticleType> typePageResult = this.articleTypeService.selectArticleByPageAndCount(ARTICLE_TYPE_INDEX, ARTICLE_TYPE_LIMIT);

        // 查询个人信息
        MySelf mySelf = this.mySelfService.selectMySelf();

        // 根据技能Id查询技能
        Techlogy techlogy = this.technologyService.selectTechnologyById(mySelf.getTechnologyId());

        // 查询浏览量前六的六篇文章
        PageResult<Article> articleReads = this.articleService.selectArticleByClick(Integer.parseInt(articleClickPage), Integer.parseInt(articleClickLimit));

        // 查询推荐文章
        PageResult<Article> commondArticlePageResult = this.articleService.selectArticleByDate(Integer.parseInt(articleCommondPage), Integer.parseInt(articleCommondLimit));

        model.addAttribute("articleTypes", typePageResult.getData());

        model.addAttribute("pictures", picturePageResult.getData());

        model.addAttribute("articles", articlePageResult.getData());

        model.addAttribute("mySelf", mySelf);

        model.addAttribute("technologyName", techlogy.getTechnologyName());

        model.addAttribute("articleReads", articleReads.getData());

        model.addAttribute("articleCommonds", commondArticlePageResult.getData());

        return "front/index";
    }
}
