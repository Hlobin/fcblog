package com.feicheng.blog.service;

import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.entity.FrontPicture;

import java.util.Map;

/**
 * @author Lenovo
 */
public interface FrontPictureService {

    // 分页查询首页图片
    PageResult<FrontPicture> selectPictureByPage(Integer page, Integer limit, String pictureTitle);

    // 添加首页图片
    Map<String, Object> addPicture(FrontPicture frontPicture);

    // 查询首页图片文章时间排序的5条文章
    PageResult<FrontPicture> selectPictureByPageAndByDate(Integer pictureIndex, Integer pictureLimit);

    // 修改首页图片信息
    Map<String, Object> editPicture(FrontPicture frontPicture);

    // 发送邮件信息到邮箱告知首页图片保存出错
    void sendRedisErrorToEmail(String message);

    // 更新redis中的数据
    void updateRedisData();
}
