package com.feicheng.blog.entity;

import javax.persistence.Table;

/**
 * 管理员角色类
 * @author Lenovo
 */
@Table(name = "admin_role")
public class AdminRole {

    // 管理员Id
    private Long adminId;

    // 角色Id
    private Long roleId;

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }


}
