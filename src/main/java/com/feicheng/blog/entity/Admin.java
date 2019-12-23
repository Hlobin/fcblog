package com.feicheng.blog.entity;

import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 管理员基本类
 * @author DrameCode
 */
@Table(name = "admin")
public class Admin implements Serializable {

    @Id
    @KeySql(useGeneratedKeys = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String adminName;

    private String adminPassword;

    private String adminPhone;

    private String adminEmail;

    private Date adminDate;

    private Integer adminStatus;

    private Integer adminDelete;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminPhone() {
        return adminPhone;
    }

    public void setAdminPhone(String adminPhone) {
        this.adminPhone = adminPhone;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public Date getAdminDate() {
        return adminDate;
    }

    public void setAdminDate(Date adminDate) {
        this.adminDate = adminDate;
    }

    public Integer getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(Integer adminStatus) {
        this.adminStatus = adminStatus;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public Integer getAdminDelete() {
        return adminDelete;
    }

    public void setAdminDelete(Integer adminDelete) {
        this.adminDelete = adminDelete;
    }


}

