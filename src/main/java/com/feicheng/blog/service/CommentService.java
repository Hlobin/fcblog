package com.feicheng.blog.service;

import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.entity.Comment;
import com.feicheng.blog.pojo.CommentExpend;

import java.util.List;
import java.util.Map;

/**
 * @author Lenovo
 */
public interface CommentService {

    // 添加评论
    Map<String, Object> addComment(Comment comment);

    // 根据文章id查询该文章下的所有评论
    List<Comment> selectAllCommentByArticleId(Integer id);

    // 分页查询文章评论
    PageResult<CommentExpend> selectCommentByPage(Integer page, Integer limit, String commentUserName, String commentUserEmail, String commentContent);

    // 修改评论
    Map<String, Object> editComment(Comment comment);


    // 评论消息发送到邮箱
    void sendCommentToEmail(Integer id);

    // 根据id删除评论
    Map<String, Object> deleteComment(Integer commentId);
}
