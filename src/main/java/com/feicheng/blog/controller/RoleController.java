package com.feicheng.blog.controller;

import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.common.ResponseResult;
import com.feicheng.blog.entity.Role;
import com.feicheng.blog.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 角色管理Controller
 *
 * @author Lenovo
 */
@Controller
@RequestMapping("role")
public class RoleController {


    @Autowired
    private RoleService roleService;


    /**
     * 分页查询所有数据
     *
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("list")
    @RequiresPermissions("role:view")
    public ResponseEntity<PageResult<Role>> selectAllRole(@RequestParam("page") Integer page,
                                                          @RequestParam("limit") Integer limit) {

        PageResult<Role> pageResult = this.roleService.selectAllRole(page, limit);

        if (CollectionUtils.isEmpty(pageResult.getData())) {

            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(pageResult);
    }

    /**
     * 修改角色信息及权限
     *
     * @param role
     * @return
     */
    @PostMapping("edit")
    @RequiresPermissions("role:edit")
    public ResponseEntity<ResponseResult> editRole(Role role) {

        ResponseResult responseResult = this.roleService.editRole(role);


        return ResponseEntity.ok(responseResult);
    }


    /**
     * 添加角色信息及权限
     * @param role
     * @return
     */
    @PostMapping("add")
    @RequiresPermissions("role:add")
    public ResponseEntity<ResponseResult> addRole(Role role) {

        ResponseResult responseResult = this.roleService.addRole(role);

        return ResponseEntity.ok(responseResult);
    }
}
