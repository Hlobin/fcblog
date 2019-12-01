package com.feicheng.blog.entity;

import java.io.Serializable;

/**
 * 富文本编辑器的类
 * @author DrameCode
 */
public class EditorArticleImage implements Serializable {

    // 图片路径
    private String src;

    // 图片标题
    private String title;

    // 图片样式
    private String style;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
