package com.feicheng.blog.controller;

import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.common.ResponseResult;
import com.feicheng.blog.entity.ArticleType;
import com.feicheng.blog.service.ArticleTypeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 文章类型管理Controller
 *
 * @author Lenovo
 */
@Controller
@RequestMapping("articleType")
public class ArticleTypeController {

    @Autowired
    private ArticleTypeService articleTypeService;


    /**
     * 查询所有的文章分类
     *
     * @return
     */
    @GetMapping("list")
    @RequiresPermissions("articleType:view")
    public ResponseEntity<PageResult<ArticleType>> selectAllArticleType(@RequestParam("page") Integer page,
                                                                        @RequestParam("limit") Integer limit) {

        PageResult<ArticleType> pageResult = this.articleTypeService.selectAllArticleType();

        if (CollectionUtils.isEmpty(pageResult.getData())) {

            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(pageResult);
    }

    /**
     * 更新文章类型
     *
     * @param articleType
     * @return
     */
    @PostMapping("edit")
    @RequiresPermissions("articleType:edit")
    public ResponseEntity<ResponseResult> editArticleType(ArticleType articleType) {

        Map<String, Object> map = this.articleTypeService.editArticleType(articleType);

        if (StringUtils.equals(map.get("message").toString(), "error")) {

            return ResponseEntity.ok(new ResponseResult(400, map.get("result").toString()));
        }

        return ResponseEntity.ok(new ResponseResult(200, map.get("result").toString()));
    }


    /**
     * 删除文章类型
     * @param articleTypeId
     * @return
     */
    @PostMapping("delete")
    @RequiresPermissions("articleType:delete")
    public ResponseEntity<ResponseResult> deleteArticleType(@RequestParam("articleTypeId") Integer articleTypeId){

        Map<String, Object> map = this.articleTypeService.deleteArticleType(articleTypeId);

        if (StringUtils.equals(map.get("message").toString(), "error")) {

            return ResponseEntity.ok(new ResponseResult(400, map.get("result").toString()));
        }

        return ResponseEntity.ok(new ResponseResult(200, map.get("result").toString()));
    }


    /**
     * 添加文章类型
     * @param articleTypeName
     * @return
     */
    @PostMapping("add")
    @RequiresPermissions("articleType:add")
    public ResponseEntity<ResponseResult> addArticleType(@RequestParam("articleTypeName") String articleTypeName){

        Map<String, Object> map = this.articleTypeService.addArticleType(articleTypeName);

        if (StringUtils.equals(map.get("message").toString(), "error")) {

            return ResponseEntity.ok(new ResponseResult(400, map.get("result").toString()));
        }

        return ResponseEntity.ok(new ResponseResult(200, map.get("result").toString()));
    }
}
