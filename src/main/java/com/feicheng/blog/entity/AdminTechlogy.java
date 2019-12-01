package com.feicheng.blog.entity;

import java.io.Serializable;

/**
 * 管理员和技术的综合类
 * @author DrameCode
 */
public class AdminTechlogy implements Serializable {

    private Integer id;

    private Integer adminId;

    private Integer techlogyId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Integer getTechlogyId() {
        return techlogyId;
    }

    public void setTechlogyId(Integer techlogyId) {
        this.techlogyId = techlogyId;
    }
}
