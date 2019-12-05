package com.feicheng.blog.service.impl;

import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.entity.Article;
import com.feicheng.blog.entity.Comment;
import com.feicheng.blog.entity.User;
import com.feicheng.blog.mapper.ArticleMapper;
import com.feicheng.blog.mapper.CommentMapper;
import com.feicheng.blog.mapper.UserMapper;
import com.feicheng.blog.pojo.CommentExpend;
import com.feicheng.blog.service.CommentService;
import com.feicheng.blog.utils.SendEmailUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 评论管理Service
 *
 * @author Lenovo
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired(required = false)
    private CommentMapper commentMapper;

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private ArticleMapper articleMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    // 判断邮箱格式
    String emailRegex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    // 本人email地址
    @Value("${myself.email}")
    private String myselfEmail;

    /**
     * 添加评论
     *
     * @param comment
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> addComment(Comment comment) {

        Map<String, Object> map = new HashMap<String, Object>();

        // 判断是否输入用户名
        if (StringUtils.isBlank(comment.getCommentUserName())) {

            map.put("message", "error");

            map.put("result", "请输入用户名");

            return map;
        }

        // 判断是否输入邮箱
        if (StringUtils.isBlank(comment.getCommentUserEmail())) {

            map.put("message", "error");

            map.put("result", "请输入邮箱");

            return map;
        }

        // 判断邮箱的格式是否正确
        if (!Pattern.matches(emailRegex, comment.getCommentUserEmail())) {

            map.put("message", "error");

            map.put("result", "邮箱格式错误");

            return map;
        }

        // 判断是否输入评论内容
        if (StringUtils.isBlank(comment.getCommentContent())) {

            map.put("message", "error");

            map.put("result", "你不说几句吗？");

            return map;
        }

        try {
            // 基本信息都输入

            // 创建用户
            User user = new User();

            // 设置基本信息

            user.setId(null);

            user.setUserEmail(comment.getCommentUserEmail());

            user.setUserName(comment.getCommentUserName());

            user.setUserDate(new Date());

            // 添加网站用户基本信息
            this.userMapper.insertSelective(user);

            // 添加评论
            // 设置基本信息
            comment.setCommentDate(new Date());

            comment.setCommentUserId(user.getId());

            // 添加评论
            this.commentMapper.insertSelective(comment);

            // 发送邮件消息
            this.sendMsg("insert", comment.getId());

            map.put("message", "success");

            map.put("result", "评论成功");

            return map;

        } catch (Exception e) {

            e.printStackTrace();

            map.put("message", "error");

            map.put("result", "评论失败");
        }

        map.put("message", "error");

        map.put("result", "评论失败");

        return map;
    }

    /**
     * 发送消息
     *
     * @param type
     * @param id
     */
    private void sendMsg(String type, Integer id) {

        try {

            this.amqpTemplate.convertAndSend("comment." + type, id);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    /**
     * 根据文章id查询该文章下的所有评论
     *
     * @param id
     * @return
     */
    @Override
    public List<Comment> selectAllCommentByArticleId(Integer id) {

        // 创建查询模板
        Example example = new Example(Comment.class);

        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("commentArticleId", id);

        // 执行查询
        List<Comment> comments = this.commentMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(comments)) {

            return null;
        }

        return comments;
    }

    /**
     * 分页查询文章评论
     *
     * @param page
     * @param limit
     * @return
     */
    @Override
    public PageResult<CommentExpend> selectCommentByPage(Integer page, Integer limit, String commentUserName, String commentUserEmail, String commentContent) {

        PageHelper.startPage(page, limit);

        // 创建模板
        Example example = new Example(Comment.class);

        Example.Criteria criteria = example.createCriteria();

        // 添加查询条件(根据网站用户名)
        if (StringUtils.isNoneBlank(commentUserName)) {

            criteria.andLike("commentUserName", "%" + commentUserName + "%");
        }

        // 添加查询条件（根据用户邮箱）
        if (StringUtils.isNoneBlank(commentUserEmail)) {

            criteria.andLike("commentUserEmail", "%" + commentUserEmail + "%");
        }

        // 添加查询条件（根据用户评论内容）
        if (StringUtils.isNoneBlank(commentContent)) {

            criteria.andLike("commentContent", "%" + commentContent + "%");
        }

        // 执行查询
        List<Comment> comments = this.commentMapper.selectByExample(example);


        if (CollectionUtils.isEmpty(comments)) {

            return new PageResult<>(-1, "无数据", 0, null);

        }

        PageInfo<Comment> pageInfo = new PageInfo<>(comments);

        // 将comment转换成commentExpend
        List<CommentExpend> commentExpends = comments.stream().map(comment -> {

            CommentExpend commentExpend = new CommentExpend();

            commentExpend.setId(comment.getId());

            commentExpend.setCommentUserName(comment.getCommentUserName());

            commentExpend.setCommentUserEmail(comment.getCommentUserEmail());

            commentExpend.setCommentContent(comment.getCommentContent());

            commentExpend.setCommentDate(comment.getCommentDate());

            commentExpend.setCommentArticleId(comment.getCommentArticleId());

            commentExpend.setCommentUserId(comment.getCommentUserId());

            // 根据文章id查询文章
            Article article = this.articleMapper.selectByPrimaryKey(comment.getCommentArticleId());

            // 设置文章标题
            commentExpend.setArticleTitle(article.getArticleName());


            return commentExpend;

        }).collect(Collectors.toList());


        return new PageResult<>(0, "查询成功", (int) pageInfo.getTotal(), commentExpends);

    }

    /**
     * 修改评论
     *
     * @param comment
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> editComment(Comment comment) {

        Map<String, Object> map = new HashMap<String, Object>();

        // 判断是否输入用户名
        if (StringUtils.isBlank(comment.getCommentUserName())) {

            map.put("message", "error");

            map.put("result", "请输入评论用户名");

            return map;
        }

        // 判断是输入评论内容
        if (StringUtils.isBlank(comment.getCommentContent())) {

            map.put("message", "error");

            map.put("result", "请输入评论内容");

            return map;
        }

        try {
            // 执行修改
            this.commentMapper.updateByPrimaryKey(comment);

            map.put("message", "success");

            map.put("result", "修改成功");

            return map;

        } catch (Exception e) {

            e.printStackTrace();

            map.put("message", "error");

            map.put("result", "操作失败");

            return map;
        }

    }


    /**
     * 评论消息发送到邮箱
     *
     * @param id
     */
    @Override
    public void sendCommentToEmail(Integer id) {


        // 根据id查询评论消息
        Comment comment = this.commentMapper.selectByPrimaryKey(id);

        // 根据文章id查询文章信息
        Article article = this.articleMapper.selectByPrimaryKey(comment.getCommentArticleId());

        // 将消息拼接成Html格式
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<html>");

        stringBuilder.append("<head>");

        stringBuilder.append("<title>用户评论</title>");

        stringBuilder.append("</head>");

        stringBuilder.append("<body>");

        stringBuilder.append("<p>用户ID：" + comment.getCommentUserId() + "</p>");

        stringBuilder.append("<br>");

        stringBuilder.append("<p>用户名：" + comment.getCommentUserName() + "</p>");

        stringBuilder.append("<br>");

        stringBuilder.append("<p>用户邮箱：" + comment.getCommentUserEmail() + "</p>");

        stringBuilder.append("<br>");

        stringBuilder.append("<p>文章标题：" + article.getArticleName() + "</p>");

        stringBuilder.append("<br>");

        stringBuilder.append("<p>评论内容：" + comment.getCommentContent() + "</p>");

        stringBuilder.append("</body>");

        stringBuilder.append("</html>");

        SendEmailUtil.sendEmail(myselfEmail,stringBuilder.toString());
    }

    /**
     * 根据commentId删除文章
     *
     * @param commentId
     * @return
     */
    @Override
    public Map<String, Object> deleteComment(Integer commentId) {

        Map<String, Object> map = new HashMap<String, Object>();

        // 判断id是否为空
        if (commentId == null) {

            map.put("message", "error");

            map.put("result", "参数错误");

            return map;
        }

        // 执行删除
        Integer count = this.commentMapper.deleteByPrimaryKey(commentId);

        if (count > 0) {

            map.put("message", "success");

            map.put("result", "删除成功");

            return map;
        }


        map.put("message", "error");

        map.put("result", "删除失败");

        return map;
    }
}
