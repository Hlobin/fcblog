package com.feicheng.blog.service.impl;

import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.entity.ArticleType;
import com.feicheng.blog.entity.FrontPicture;
import com.feicheng.blog.mapper.ArticleTypeMapper;
import com.feicheng.blog.service.ArticleTypeService;
import com.feicheng.blog.utils.JsonUtils;
import com.feicheng.blog.utils.SendEmailUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.CheckedOutputStream;

/**
 * 文章类型管理Service
 *
 * @author Lenovo
 */
@Service
public class ArticleTypeServiceImpl implements ArticleTypeService {

    @Autowired(required = false)
    private ArticleTypeMapper articleTypeMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AmqpTemplate amqpTemplate;

    // 本地邮箱地址
    @Value("${myself.email}")
    private String myselfEmail;

    // 文章标签分类保存到redis中的key值
    private static final String ARTICLE_TYPE_INDEX = "ARTICLE_TYPE_INDEX";

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleTypeServiceImpl.class);

    /**
     * 查询所有的文章分类
     *
     * @return
     */
    @Override
    public PageResult<ArticleType> selectAllArticleType() {

        List<ArticleType> articleTypes = this.articleTypeMapper.selectAll();

        if (CollectionUtils.isEmpty(articleTypes)) {

            return new PageResult<>(-1, "无数据", 0, null);
        }

        return new PageResult<>(0, "查询成功", articleTypes.size(), articleTypes);
    }

    /**
     * 修改文章类型
     *
     * @param articleType
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> editArticleType(ArticleType articleType) {

        Map<String, Object> map = new HashMap<String, Object>();

        // 判断是否输入文章的类型名
        if (StringUtils.isBlank(articleType.getArticleTypeName())) {

            map.put("message", "error");

            map.put("result", "请输入分类名称");

            return map;
        }

        // 执行更新
        Integer count = this.articleTypeMapper.updateByPrimaryKeySelective(articleType);

        if (count > 0) {

            // 发送消息数据已更新

            this.sendRedisMsg("update","数据已更新");

            map.put("message", "success");

            map.put("result", "修改成功");

            return map;
        }

        map.put("message", "success");

        map.put("result", "修改成功");

        return map;

}

    /**
     * 删除文章类型
     *
     * @param articleTypeId
     * @return
     */
    @Override
    public Map<String, Object> deleteArticleType(Integer articleTypeId) {

        Map<String, Object> map = new HashMap<String, Object>();

        if (articleTypeId == null) {

            map.put("message", "error");

            map.put("result", "参数错误");

            return map;
        }

        // 执行删除
        Integer count = this.articleTypeMapper.deleteByPrimaryKey(articleTypeId);
        if (count > 0) {

            map.put("message", "success");

            map.put("result", "删除成功");

            // 发送消息数据已更新
            this.sendRedisMsg("delete", "数据已更新");

            return map;
        }

        map.put("message", "error");

        map.put("result", "删除失败");

        return map;
    }

    /**
     * 查询前10条标签，并按文章数量排序
     *
     * @param articleTypeIndex
     * @param articleTypeLimit
     * @return
     */
    @Override
    public PageResult<ArticleType> selectArticleByPageAndCount(Integer articleTypeIndex, Integer articleTypeLimit) {

        // 先从redis中查询
        // 判断redis中是否有键
        Boolean hasKey = this.stringRedisTemplate.hasKey(ARTICLE_TYPE_INDEX);

        // 若有，直接从redis中获取数据
        if (hasKey) {

            String json = this.stringRedisTemplate.opsForValue().get(ARTICLE_TYPE_INDEX);

            // 将json转换成list集合
            List<ArticleType> articleTypes = JsonUtils.parseList(json, ArticleType.class);

            // 返回数据
            return new PageResult<>(0, "查询成功", articleTypes.size(), articleTypes);
        }
        // 没有则，从数据库中获取数据
        PageHelper.startPage(articleTypeIndex, articleTypeLimit);

        // 查询数据
        // 创建模板
        Example example = new Example(ArticleType.class);

        // 按文章实现降序排序
        example.orderBy("articleCount").desc();

        // 执行查询
        List<ArticleType> articleTypes = this.articleTypeMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(articleTypes)) {

            return new PageResult<>(-1, "无数据", 0, null);
        }

        // 实现分页
        PageInfo<ArticleType> pageInfo = new PageInfo<>(articleTypes);

        // 保存到redis中
        try {

            // 将list对象序列化成json字符串
            String json = JsonUtils.serialize(pageInfo.getList());

            // 保存数据
            this.stringRedisTemplate.opsForValue().set(ARTICLE_TYPE_INDEX, json);

        } catch (Exception e) {

            e.printStackTrace();

            LOGGER.error("redis中标签保存出错");

            // redis中数据保存错误，发送邮箱
            this.sendRedisMsg("error","redis中文章标签保存出错");

            // 保存出错，也返回数据
            return new PageResult<>(0, "保存成功", (int) pageInfo.getTotal(), pageInfo.getList());
        }

        // 返回数据
        return new PageResult<>(0, "保存成功", (int) pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 文章标签保存出错，发送邮件到我的邮箱
     *
     * @param message
     */
    @Override
    public void sendRedisErrorToEmail(String message) {

        // 将消息转换成Html格式
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<html>");

        stringBuilder.append("<head>");

        stringBuilder.append("<title>redis缓存出错</title>");

        stringBuilder.append("</head>");

        stringBuilder.append("<body>");

        stringBuilder.append("<p>出错信息：" + message + "</p>");

        stringBuilder.append("</body>");

        stringBuilder.append("</html>");

        try {
            // 发送信息
            SendEmailUtil.sendEmail(myselfEmail, stringBuilder.toString());

        } catch (Exception e) {

            e.printStackTrace();

            LOGGER.error("邮件发送失败");
        }
    }

    /**
     * 更新redis中标签中的数据
     */
    @Override
    public void updateRedisData() {

        // 判断是否有key值
        Boolean hasKey = this.stringRedisTemplate.hasKey(ARTICLE_TYPE_INDEX);

        // 若存在，则删除key值，重新载入数据
        if (hasKey){

            this.stringRedisTemplate.delete(ARTICLE_TYPE_INDEX);

            // 从数据库中查询数据
            PageHelper.startPage(1, 10);

            // 创建模板
            Example example = new Example(ArticleType.class);

            // 添加查询条件
            example.orderBy("articleCount").desc();

            // 执行查询
            List<ArticleType> articleTypes = this.articleTypeMapper.selectByExample(example);

            // 判断是否有值
            if (CollectionUtils.isEmpty(articleTypes)) {

                return;

            }

            PageInfo<ArticleType> pageInfo = new PageInfo<>(articleTypes);

            try {
                // 将从数据库中查询的数据放入到redis中
                // 将list集合转换成json对象
                String json = JsonUtils.serialize(pageInfo.getList());

                // 保存到redis中
                this.stringRedisTemplate.opsForValue().set(ARTICLE_TYPE_INDEX, json);

            } catch (Exception e) {

                e.printStackTrace();

                LOGGER.error("数据保存到redis中出错");

                // 当保存到redis中出错的时候，向我的邮箱发送信息
                this.sendRedisMsg("error","redis中文章标签保存出错");
            }
        }
    }

    /**
     * 添加文章类型
     * @param articleTypeName
     * @return
     */
    @Override
    public Map<String, Object> addArticleType(String articleTypeName) {

        // 创建对象
        ArticleType articleType = new ArticleType();

        Map<String, Object> map = new HashMap<String, Object>();

        // 判断是否输入文章的类型名
        if (StringUtils.isBlank(articleTypeName)) {

            map.put("message", "error");

            map.put("result", "请输入分类名称");

            return map;
        }

        // 设置基本信息
        articleType.setArticleTypeName(articleTypeName);

        articleType.setArticleCount(1);

        articleType.setArticleTypeDelete(0);

        // 执行插入
        Integer count = this.articleTypeMapper.insert(articleType);

        if (count > 0) {

            // 发送消息数据已更新

            this.sendRedisMsg("insert","数据已更新");

            map.put("message", "success");

            map.put("result", "添加成功");

            return map;
        }

        map.put("message", "success");

        map.put("result", "添加失败");

        return map;
    }


    /**
     * 消息队列发送消息
     *
     * @param type
     */
    private void sendRedisMsg(String type, String message) {

        try {

            this.amqpTemplate.convertAndSend("redis.index.article.type." + type, message);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
