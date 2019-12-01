package com.feicheng.blog.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 管理员基本类
 * @author DrameCode
 */
public class Admin implements Serializable {

    private Integer id;

    private String adminName;

    private String adminPassword;

    private String adminPhone;

    private String adminEmail;

    private Date adminDate;

    private Integer adminStatus;

    private Integer adminDelete;

    private Integer adminExpendId;

    // 动态验证码
    private Integer code;

    // 非数据库字段

    // 管理员扩展表
    private AdminExpend adminExpend;

    // 管理员对应的技术领域
    private List<Techlogy> techlogies;

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

    public Integer getAdminExpendId() {
        return adminExpendId;
    }

    public void setAdminExpendId(Integer adminExpendId) {
        this.adminExpendId = adminExpendId;
    }

    public AdminExpend getAdminExpend() {
        return adminExpend;
    }

    public void setAdminExpend(AdminExpend adminExpend) {
        this.adminExpend = adminExpend;
    }

    public List<Techlogy> getTechlogies() {
        return techlogies;
    }

    public void setTechlogies(List<Techlogy> techlogies) {

        this.techlogies = techlogies;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", adminName='" + adminName + '\'' +
                ", adminPassword='" + adminPassword + '\'' +
                ", adminPhone='" + adminPhone + '\'' +
                ", adminEmail='" + adminEmail + '\'' +
                ", adminDate=" + adminDate +
                ", adminStatus=" + adminStatus +
                ", adminDelete=" + adminDelete +
                ", adminExpendId=" + adminExpendId +
                ", code=" + code +
                ", adminExpend=" + adminExpend +
                ", techlogies=" + techlogies +
                '}';
    }
}

