package com.feicheng.blog.controller;

import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.common.ResponseResult;
import com.feicheng.blog.entity.Comment;
import com.feicheng.blog.pojo.CommentExpend;
import com.feicheng.blog.service.CommentService;
import org.apache.commons.lang3.StringUtils;
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
 * 评论管理Controller
 *
 * @author Lenovo
 */
@Controller
@RequestMapping("comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 用户添加评论
     *
     * @param comment
     * @return
     */
    @PostMapping("add")
    public ResponseEntity<ResponseResult> addComment(Comment comment) {

        Map<String, Object> map = this.commentService.addComment(comment);

        if (StringUtils.equals(map.get("message").toString(), "error")) {

            return ResponseEntity.ok(new ResponseResult(500, map.get("result").toString()));

        }

        return ResponseEntity.ok(new ResponseResult(200, map.get("result").toString()));
    }

    /**
     * 分页查询文章评论
     *
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<PageResult<CommentExpend>> selectCommentByPage(@RequestParam("page") Integer page,
                                                                         @RequestParam("limit") Integer limit,
                                                                         @RequestParam(name = "commentUserName" ,required = false) String commentUserName,
                                                                         @RequestParam(name = "commentContent", required = false) String commentContent,
                                                                         @RequestParam(name = "commentUserEmail", required = false) String commentUserEmail) {

        PageResult<CommentExpend> pageResult = this.commentService.selectCommentByPage(page, limit, commentUserName, commentUserEmail, commentContent);

        if (CollectionUtils.isEmpty(pageResult.getData())) {

            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(pageResult);
    }

    /**
     * 修改评论
     *
     * @param comment
     * @return
     */
    @PostMapping("edit")
    public ResponseEntity<ResponseResult> editComment(Comment comment) {

        Map<String, Object> map = this.commentService.editComment(comment);

        if (StringUtils.equals(map.get("message").toString(), "error")) {

            return ResponseEntity.ok(new ResponseResult(400, map.get("result").toString()));

        }

        return ResponseEntity.ok(new ResponseResult(200, map.get("result").toString()));
    }
}
