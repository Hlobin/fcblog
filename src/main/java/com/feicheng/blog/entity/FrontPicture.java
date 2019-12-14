package com.feicheng.blog.entity;

import org.springframework.format.annotation.DateTimeFormat;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 首页图片显示
 * @author Lenovo
 */
@Table(name = "front_picture")
public class FrontPicture {

    @Id
    @KeySql(useGeneratedKeys = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 图片标题
    private String pictureTitle;

    // 图片url地址
    private String pictureUrl;

    // 图片内容的url地址
    private String pictureContentUrl;

    // 发布者
    private String pictureAuthor;

    // 发布状态
    private Integer pictureStatus;

    // 浏览权限
    private String pictureLook;

    // 是否置顶
    private String pictureTop;

    // 发布时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date pictureDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPictureTitle() {
        return pictureTitle;
    }

    public void setPictureTitle(String pictureTitle) {
        this.pictureTitle = pictureTitle;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getPictureContentUrl() {
        return pictureContentUrl;
    }

    public void setPictureContentUrl(String pictureContentUrl) {
        this.pictureContentUrl = pictureContentUrl;
    }

    public String getPictureAuthor() {
        return pictureAuthor;
    }

    public void setPictureAuthor(String pictureAuthor) {
        this.pictureAuthor = pictureAuthor;
    }

    public Integer getPictureStatus() {
        return pictureStatus;
    }

    public void setPictureStatus(Integer pictureStatus) {
        this.pictureStatus = pictureStatus;
    }

    public String getPictureLook() {
        return pictureLook;
    }

    public void setPictureLook(String pictureLook) {
        this.pictureLook = pictureLook;
    }

    public String getPictureTop() {
        return pictureTop;
    }

    public void setPictureTop(String pictureTop) {
        this.pictureTop = pictureTop;
    }

    public Date getPictureDate() {
        return pictureDate;
    }

    public void setPictureDate(Date pictureDate) {
        this.pictureDate = pictureDate;
    }
}
