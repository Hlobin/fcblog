package com.feicheng.blog.service.impl;

import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.common.ResponseResult;
import com.feicheng.blog.entity.FrontPicture;
import com.feicheng.blog.mapper.FrontPictureMapper;
import com.feicheng.blog.service.FrontPictureService;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页图片管理Service
 *
 * @author Lenovo
 */
@Service
public class FrontPictureServiceImpl implements FrontPictureService {

    // 首页图片存入redis中的key
    private static final String INDEX_PICTURE = "INDEX_PICTURE";

    private static Logger logger = LoggerFactory.getLogger(FrontPictureServiceImpl.class);

    @Autowired(required = false)
    private FrontPictureMapper frontPictureMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AmqpTemplate amqpTemplate;

    // 本人email地址
    @Value("${myself.email}")
    private String myselfEmail;

    @Value("${myself.address}")
    private String myselfAddress;

    /**
     * 分页查询首页图片
     *
     * @param page
     * @param limit
     * @return
     */
    @Override
    public PageResult<FrontPicture> selectPictureByPage(Integer page, Integer limit, String pictureTitle) {

        // 设置分页的起始和条数
        PageHelper.startPage(page, limit);

        // 创建模板
        Example example = new Example(FrontPicture.class);

        Example.Criteria criteria = example.createCriteria();

        // 判断是否有查询条件
        if (StringUtils.isNoneBlank(pictureTitle)) {

            // 添加查询条件
            criteria.andLike("pictureTitle", "%" + pictureTitle + "%");
        }

        // 执行查询
        List<FrontPicture> frontPictures = this.frontPictureMapper.selectByExample(example);

        // 判断是否有值
        if (CollectionUtils.isEmpty(frontPictures)) {

            return new PageResult<>(-1, "无数据", 0, null);
        }

        // 获取指定的数据
        PageInfo<FrontPicture> pageInfo = new PageInfo<>(frontPictures);

        return new PageResult<>(0, "查询成功", (int) pageInfo.getTotal(), pageInfo.getList());

    }

    /**
     * 添加首页图片
     *
     * @param frontPicture
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> addPicture(FrontPicture frontPicture) {

        Map<String, Object> map = new HashMap<String, Object>();

        // 判断是否有输入文章标题
        if (StringUtils.isBlank(frontPicture.getPictureTitle())) {

            map.put("message", "error");

            map.put("result", "请输入图片标题");

            return map;
        }

        // 判断是否有文章图片路径
        if (StringUtils.isBlank(frontPicture.getPictureContentUrl())) {

            map.put("message", "error");

            map.put("result", "请选择一篇文章");

            return map;
        }

        // 判断是否有图片
        if (StringUtils.isBlank(frontPicture.getPictureUrl())) {

            map.put("message", "error");

            map.put("result", "请上传一张图片");

            return map;
        }

        // 添加基本信息
        frontPicture.setPictureDate(new Date());

        frontPicture.setPictureAuthor("王若飞");

        frontPicture.setPictureLook(frontPicture.getPictureLook().equals("on") ? "开放浏览" : "私密浏览");

        frontPicture.setPictureTop(frontPicture.getPictureTop().equals("checked") ? "checked" : "");

        frontPicture.setArticleId(Integer.parseInt(frontPicture.getPictureContentUrl()));

        frontPicture.setPictureContentUrl(myselfAddress + "/article/detail/" + frontPicture.getPictureContentUrl());

        frontPicture.setPictureStatus(1);

        // 执行插入数据
        this.frontPictureMapper.insertSelective(frontPicture);

        // 发送消息到消息队列通知已有数据更新
        this.sendRedisMsg("insert");

        map.put("message", "success");

        map.put("result", "添加成功");

        return map;
    }

    /**
     * 查询首页图片文章时间排序的5条文章
     *
     * @param pictureIndex
     * @param pictureLimit
     * @return
     */
    @Override
    public PageResult<FrontPicture> selectPictureByPageAndByDate(Integer pictureIndex, Integer pictureLimit) {

        try {
            // 先从redis中获取首页图片中的五条文章
            // 判断是否有这个Key
            Boolean hasKey = this.stringRedisTemplate.hasKey(INDEX_PICTURE);

            // 若有，则直接从redis中获取数据
            if (hasKey) {

                // 获取数据转换为json数据
                String json = this.stringRedisTemplate.opsForValue().get(INDEX_PICTURE);

                // 将json数据转化为list对象
                List<FrontPicture> frontPictures = JsonUtils.parseList(json, FrontPicture.class);

                // 直接返回数据
                return new PageResult<>(0, "查询成功", frontPictures.size(), frontPictures);

            }

        } catch (Exception e) {

            e.printStackTrace();

            return new PageResult<>(-1, "无数据", 0, null);
        }

        // 否则从数据库中获取信息
        PageHelper.startPage(pictureIndex, pictureLimit);

        // 创建模板
        Example example = new Example(FrontPicture.class);

        // 添加查询条件
        example.orderBy("pictureDate").desc();

        // 执行查询
        List<FrontPicture> frontPictures = this.frontPictureMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(frontPictures)) {

            return new PageResult<>(-1, "无数据", 0, null);

        }

        PageInfo<FrontPicture> pageInfo = new PageInfo<>(frontPictures);

        try {
            // 将从数据库中查询的数据放入到redis中
            // 将list集合转换成json对象
            String json = JsonUtils.serialize(pageInfo.getList());

            // 保存到redis中
            this.stringRedisTemplate.opsForValue().set(INDEX_PICTURE, json);

        } catch (Exception e) {

            e.printStackTrace();

            logger.error("数据保存到redis中出错");

            // 当保存到redis中出错的时候，向我的邮箱发送信息
            this.sendRedisMsg("error");

            return new PageResult<>(0, "查询成功", (int) pageInfo.getTotal(), pageInfo.getList());
        }

        return new PageResult<>(0, "查询成功", (int) pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 前台首页图片保存出错redis中发送消息
     *
     * @param type
     */
    private void sendRedisMsg(String type) {

        try {

            this.amqpTemplate.convertAndSend("redis.index.picture." + type, "redis中首页图片保存出错");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 修改首页图片
     *
     * @param frontPicture
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> editPicture(FrontPicture frontPicture) {

        Map<String, Object> map = new HashMap<String, Object>();

        // 判断是否有输入文章标题
        if (StringUtils.isBlank(frontPicture.getPictureTitle())) {

            map.put("message", "error");

            map.put("result", "请输入图片标题");

            return map;
        }

        // 判断是否有文章图片路径
        if (StringUtils.isBlank(frontPicture.getPictureContentUrl())) {

            map.put("message", "error");

            map.put("result", "请选择一篇文章");

            return map;
        }

        // 判断是否有图片
        if (StringUtils.isBlank(frontPicture.getPictureUrl())) {

            map.put("message", "error");

            map.put("result", "请上传一张图片");

            return map;
        }

        // 添加基本信息
        frontPicture.setPictureDate(new Date());

        frontPicture.setPictureAuthor("王若飞");

        frontPicture.setPictureLook(frontPicture.getPictureLook().equals("on") ? "开放浏览" : "私密浏览");

        frontPicture.setPictureTop(frontPicture.getPictureTop().equals("checked") ? "checked" : "");

        frontPicture.setPictureContentUrl(myselfAddress + "/article/detail/" + frontPicture.getPictureContentUrl());

        frontPicture.setPictureStatus(1);

        // 执行插入数据
        this.frontPictureMapper.updateByPrimaryKey(frontPicture);

        // 发送消息通知已有数据更新
        this.sendRedisMsg("update");

        map.put("message", "success");

        map.put("result", "添加成功");

        return map;
    }

    /**
     * 发送邮件信息到邮箱告知首页图片保存出错
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

            logger.error("邮件发送失败");
        }
    }

    /**
     * 更新redis中的数据信息
     */
    @Override
    public void updateRedisData() {

        // 判断是否有key值
        Boolean hasKey = this.stringRedisTemplate.hasKey(INDEX_PICTURE);

        // 若存在，则删除key值，重新载入数据
        if (hasKey) {

            this.stringRedisTemplate.delete(INDEX_PICTURE);

            // 从数据库中查询数据
            PageHelper.startPage(1, 5);

            // 创建模板
            Example example = new Example(FrontPicture.class);

            // 添加查询条件
            example.orderBy("pictureDate").desc();

            // 执行查询
            List<FrontPicture> frontPictures = this.frontPictureMapper.selectByExample(example);

            if (CollectionUtils.isEmpty(frontPictures)) {

                return;

            }

            PageInfo<FrontPicture> pageInfo = new PageInfo<>(frontPictures);

            try {
                // 将从数据库中查询的数据放入到redis中
                // 将list集合转换成json对象
                String json = JsonUtils.serialize(pageInfo.getList());

                // 保存到redis中
                this.stringRedisTemplate.opsForValue().set(INDEX_PICTURE, json);

            } catch (Exception e) {

                e.printStackTrace();

                logger.error("数据保存到redis中出错");

                // 当保存到redis中出错的时候，向我的邮箱发送信息
                this.sendRedisMsg("error");
            }
        }
    }

    /**
     * 删除文章
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult deletePicture(Integer id) {

        if (id == null) {

            return new ResponseResult(400, "参数错误");

        }

        // 执行删除
        Integer count = this.frontPictureMapper.deleteByPrimaryKey(id);

        if (count > 0) {

            // 发送消息通知已有数据更新
            this.sendRedisMsg("update");

            return new ResponseResult(200, "删除成功");
        }

        return new ResponseResult(500, "删除失败");
    }
}
