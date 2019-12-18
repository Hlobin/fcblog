package com.feicheng.blog.service.impl;

import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.entity.Article;
import com.feicheng.blog.entity.Comment;
import com.feicheng.blog.entity.User;
import com.feicheng.blog.mapper.ArticleMapper;
import com.feicheng.blog.mapper.CommentMapper;
import com.feicheng.blog.mapper.UserMapper;
import com.feicheng.blog.service.ArticleService;
import com.feicheng.blog.utils.SendEmailUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 文章管理Service
 *
 * @author Lenovo
 */
@Service
public class ArticleServiceImpl implements ArticleService {


    @Autowired(required = false)
    private ArticleMapper articleMapper;

    @Autowired(required = false)
    private CommentMapper commentMapper;

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    private static Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    // 本机使用IP地址
    @Value("${myself.address}")
    private String myselfAddress;

    /**
     * 查询所有的文章并实现分页
     *
     * @return
     */
    @Override
    public PageResult<Article> selectAllArticle(Integer page, Integer limit) {


        PageHelper.startPage(page, limit);

        List<Article> articles = this.articleMapper.selectAll();

        if (CollectionUtils.isEmpty(articles)) {

            return new PageResult<>(-1, "查询失败", 0, null);
        }

        PageInfo<Article> pageInfo = new PageInfo<>(articles);

        return new PageResult<>(0, "查询成功", articles.size(), pageInfo.getList());
    }

    /**
     * 修改文章
     *
     * @param article
     */
    @Override
    @Transactional
    public void editArticle(Article article) {


        // 修改基本信息
        article.setArticleLook(article.getArticleLook().equals("on") ? "开放浏览" : "私密浏览");

        article.setArticleTop(article.getArticleTop().equals("checked") ? "checked" : "");

        // 执行修改
        this.articleMapper.updateByPrimaryKeySelective(article);
    }


    /**
     * 添加文章
     *
     * @param article
     */
    @Override
    @Transactional
    public void addArticle(Article article) {

        // 修改基本信息
        article.setArticleLook(article.getArticleLook().equals("on") ? "开放浏览" : "私密浏览");

        article.setArticleTop(article.getArticleTop().equals("checked") ? "checked" : "");

        article.setArticleRead(1);

        article.setArticleLove(1);

        article.setArticleAuthor("王若飞");

        // 执行添加
        this.articleMapper.insertSelective(article);

        // 通过消息队列发送文章到各个用户的邮箱
        this.sendMsg("insert", article.getId());

    }

    /**
     * 发送消息到网站用户的邮箱
     *
     * @param type
     * @param id
     */
    private void sendMsg(String type, Integer id) {

        try {

            this.amqpTemplate.convertAndSend("article." + type, id);

        } catch (Exception e) {

            e.printStackTrace();

            logger.error("消息发送失败");
        }
    }

    /**
     * 根据id查询文章
     *
     * @param id
     * @return
     */
    @Override
    public Article selectArticleById(Integer id) {

        Article article = this.articleMapper.selectByPrimaryKey(id);

        return article;
    }

    /**
     * 增加文章的浏览量
     *
     * @param id
     */
    @Override
    public void addArticleLook(Integer id) {

        // 先根据id查询文章
        Article article = this.articleMapper.selectByPrimaryKey(id);

        // 修改文章访问量
        article.setArticleRead(article.getArticleRead() + 1);

        // 修改文章信息

        this.articleMapper.updateByPrimaryKeySelective(article);
    }

    /**
     * 根据时间排序并查询前十条文章记录
     *
     * @param page
     * @param limit
     * @return
     */
    @Override
    public PageResult<Article> selectArticleByDate(Integer page, Integer limit) {

        PageHelper.startPage(page, limit);

        // 创建模板
        Example example = new Example(Article.class);

        example.orderBy("articleDate").desc();

        // 执行查询
        List<Article> articles = this.articleMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(articles)) {

            return new PageResult<>(-1, "无数据", 0, null);
        }

        PageInfo<Article> pageInfo = new PageInfo<>(articles);

        return new PageResult<>(0, "查询成功", (int) pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 根据id删除文章
     *
     * @param id
     */
    @Override
    @Transactional
    public Map<String, Object> deleteArticle(Integer id) {

        Map<String, Object> map = new HashMap<String, Object>();

        // 判断id是否为空
        if (id == null) {

            map.put("message", "error");

            map.put("result", "参数错误");

            return map;
        }

        // 先删除该文章下的所有评论
        // 创建模板
        Example example = new Example(Comment.class);

        // 添加删除条件
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("commentArticleId", id);

        // 执行删除文章下的评论消息
        this.commentMapper.deleteByExample(example);

        // 执行删除该文章
        Integer count = this.articleMapper.deleteByPrimaryKey(id);

        // 判断是否删除成功
        if (count > 0) {

            map.put("message", "success");

            map.put("result", "删除成功");

            return map;
        }

        map.put("message", "error");

        map.put("result", "服务器错误");

        return map;

    }

    /**
     * 将文章发送到网站用户的邮箱
     *
     * @param id
     */
    @Override
    public void sendArticleToEmail(Integer id) {

        if (id == null) {

            return;
        }

        // 根据id查询文章
        Article article = this.articleMapper.selectByPrimaryKey(id);

        if (article == null) {

            return;
        }

        // 将信息转换成Html格式
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<html>");

        stringBuilder.append("<head>");

        stringBuilder.append("<title>新文章</title>");

        stringBuilder.append("</head>");

        stringBuilder.append("<body>");

        stringBuilder.append("<p>文章标题：" + article.getArticleName() + "</p>");

        stringBuilder.append("<p>文章发起人：" + article.getArticleAuthor() + "</p>");

        stringBuilder.append("<p>文章链接地址：" + myselfAddress + "article/detail/" + article.getId() + "</p>");

        stringBuilder.append("</body>");

        stringBuilder.append("</html>");

        Example example = Example.builder(User.class).select("userEmail").build();

        example.setDistinct(true);

        // 查询所有的网站用户
        List<User> users = this.userMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(users)) {

            logger.error("无网站用户");

            return;
        }

        // 循环发送
        for (User user : users
        ) {

            try {
                SendEmailUtil.sendEmail(user.getUserEmail(), stringBuilder.toString());

                // 休息1秒
                Thread.sleep(1000);

            } catch (Exception e) {

                e.printStackTrace();
            }

        }

    }

    /**
     * 根据文章类型查询文章并实现分页
     *
     * @param articleIndex
     * @param articleLimit
     * @param articleTypeId
     * @return
     */
    @Override
    public PageResult<Article> selectArticleByType(Integer articleIndex, Integer articleLimit, Integer articleTypeId) {

        // 创建分页查询起始页
        PageHelper.startPage(articleIndex, articleLimit);

        // 创建模板
        Example example = new Example(Article.class);

        example.orderBy("articleDate").desc();

        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("articleTypeId", articleTypeId);

        // 执行查询
        List<Article> articles = this.articleMapper.selectByExample(example);

        // 判断是否有值
        if (CollectionUtils.isEmpty(articles)) {

            return new PageResult<>(404, "无数据", 0, null);

        }

        PageInfo<Article> pageInfo = new PageInfo<>(articles);

        return new PageResult<>(0, "查询成功", (int) pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 查询浏览量前六的文章
     *
     * @param articleClickPage
     * @param articleClickLimit
     * @return
     */
    @Override
    public PageResult<Article> selectArticleByClick(Integer articleClickPage, Integer articleClickLimit) {

        PageHelper.startPage(articleClickPage, articleClickLimit);

        // 创建模板
        Example example = new Example(Article.class);

        // 设置排序
        example.orderBy("articleRead").desc();

        // 执行查询
        List<Article> articles = this.articleMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(articles)) {

            return new PageResult<>(404, "无数据", 0, null);
        }

        PageInfo<Article> pageInfo = new PageInfo<>(articles);

        return new PageResult<>(200, "查询成功", (int) pageInfo.getTotal(), pageInfo.getList());
    }
}
