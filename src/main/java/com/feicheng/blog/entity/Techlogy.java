package com.feicheng.blog.entity;

import java.io.Serializable;

/**
 * 管理员技术表
 * @author DrameCode
 */
public class Techlogy implements Serializable {

    private Integer id;

    private String techlogyName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTechlogyName() {
        return techlogyName;
    }

    public void setTechlogyName(String techlogyName) {
        this.techlogyName = techlogyName;
    }
}
