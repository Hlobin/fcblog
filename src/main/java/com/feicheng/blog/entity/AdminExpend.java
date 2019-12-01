package com.feicheng.blog.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 管理员扩展表
 * @author DrameCode
 */
public class AdminExpend implements Serializable {

    private Integer id;

    private String adminRealName;

    private String adminSex;

    private Date adminBirthday;

    private Integer adminProvince;

    private Integer adminCity;

    private Integer adminArea;

    private String adminSelfEvaluation;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAdminRealName() {
        return adminRealName;
    }

    public void setAdminRealName(String adminRealName) {
        this.adminRealName = adminRealName;
    }

    public String getAdminSex() {
        return adminSex;
    }

    public void setAdminSex(String adminSex) {
        this.adminSex = adminSex;
    }

    public Date getAdminBirthday() {
        return adminBirthday;
    }

    public void setAdminBirthday(Date adminBirthday) {
        this.adminBirthday = adminBirthday;
    }


    public String getAdminSelfEvaluation() {
        return adminSelfEvaluation;
    }

    public void setAdminSelfEvaluation(String adminSelfEvaluation) {
        this.adminSelfEvaluation = adminSelfEvaluation;
    }

    public Integer getAdminProvince() {
        return adminProvince;
    }

    public void setAdminProvince(Integer adminProvince) {
        this.adminProvince = adminProvince;
    }

    public Integer getAdminCity() {
        return adminCity;
    }

    public void setAdminCity(Integer adminCity) {
        this.adminCity = adminCity;
    }

    public Integer getAdminArea() {
        return adminArea;
    }

    public void setAdminArea(Integer adminArea) {
        this.adminArea = adminArea;
    }
}
