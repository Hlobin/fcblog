package com.feicheng.blog.entity;

import javax.persistence.Table;

/**
 * 用户角色类
 * @author Lenovo
 */
@Table(name = "user_role")
public class UserRole {

    private Long userId;

    private Long roleId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
