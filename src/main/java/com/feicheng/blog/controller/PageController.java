package com.feicheng.blog.controller;

import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.entity.ArticleType;
import com.feicheng.blog.service.ArticleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 页面跳转控制器
 * @author Lenovo
 */
@Controller
public class PageController {

    @Autowired
    private ArticleTypeService articleTypeService;

    ////////////////////////////////////前台页面跳转/////////////////////////

    /**
     * 博客首页
     * @return
     */
    @RequestMapping("index.html")
    public String showIndex(){

        return "front/index";
    }


    /**
     * 内容页面
     * @return
     */
    @RequestMapping("info.html")
    public String showInfo(){

        return "front/info";
    }


    /**
     * 学无止境
     * @return
     */
    @RequestMapping("list.html")
    public String showStudy(){

        return "front/list";
    }

    /**
     * 关于自己
     * @return
     */
    @RequestMapping("about.html")
    public String showAbout(){

        return "front/about";
    }


    /**
     * 留言
     * @return
     */
    @RequestMapping("gbook.html")
    public String showContact(){

        return "front/gbook";
    }

    ///////////////////////////////////////后台页面跳转///////////////////////////

    /**
     * 后台管理主界面
     * @return
     */
    @RequestMapping("admin/index.html")
    public String showAdminIndex(){

        return "system/index";
    }

    /**
     * 欢迎界面
     * @return
     */
    @RequestMapping("admin/welcome.html")
    public String showWelcome(){

        return "system/welcome-1";
    }

    /**
     * 显示文章界面
     * @return
     */
    @RequestMapping("admin/article/list.html")
    public String showArticleList(){

        return "system/article-list";
    }

    /**
     * 预览文章界面
     * @return
     */
    @RequestMapping("admin/article/look.html")
    public String showArticleLook(){

        return "front/info";
    }

    /**
     * 文章编辑界面
     * @return
     */
    @RequestMapping("admin/article/edit.html")
    public String showArticleEdit(Model model){

        // 查询文章分类
        PageResult<ArticleType> pageResult = this.articleTypeService.selectAllArticleType();

        if (CollectionUtils.isEmpty(pageResult.getData())){

            model.addAttribute("articleTypes", null);

            return "system/article-edit";
        }

        model.addAttribute("articleTypes", pageResult.getData());

        return "system/article-edit";
    }


    /**
     * 添加文章界面
     * @return
     */
    @RequestMapping("admin/article/add.html")
    public String showAddArticle(Model model){

        // 查询文章分类
        PageResult<ArticleType> pageResult = this.articleTypeService.selectAllArticleType();

        if (CollectionUtils.isEmpty(pageResult.getData())){

            model.addAttribute("articleTypes", null);

            return "system/article-edit";
        }

        model.addAttribute("articleTypes", pageResult.getData());

        return "system/article-add";
    }
}
