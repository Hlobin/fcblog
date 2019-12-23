package com.feicheng.blog.service;

import java.util.List;

/**
 * @author Lenovo
 */
public interface RoleMenuService {

    // 根据roleId查询菜单权限
    List<Long> selectRoleMenuById(Long roleId);
}
