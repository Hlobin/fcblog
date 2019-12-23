package com.feicheng.blog.entity;

import javax.persistence.Table;

/**
 *  菜单角色类
 * @author Lenovo
 */
@Table(name = "role_menu")
public class RoleMenu {


    private Long roleId;

    private Long menuId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
}
