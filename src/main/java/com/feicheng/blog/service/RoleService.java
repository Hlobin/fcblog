package com.feicheng.blog.service;

import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.common.ResponseResult;
import com.feicheng.blog.entity.Admin;
import com.feicheng.blog.entity.Role;

import java.util.List;
import java.util.Map;

/**
 * @author Lenovo
 */
public interface RoleService {

    // 查询所有的角色
    PageResult<Role> selectAllRole(Integer page, Integer limit);

    // 修改角色信息及权限
    ResponseResult editRole(Role role);

    // 添加角色信息及权限
    ResponseResult addRole(Role role);

    // 查询管理员的角色
    List<Role> selectAdminRole(String adminName);
}
