package com.feicheng.blog.controller;

import com.feicheng.blog.entity.RoleMenu;
import com.feicheng.blog.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色菜单Controller
 *
 * @author Lenovo
 */
@Controller
@RequestMapping("roleMenu")
public class RoleMenuController {


    @Autowired
    private RoleMenuService roleMenuService;


    /**
     * 根据角色Id获取该角色的权限菜单
     *
     * @param roleId
     * @return
     */
    @GetMapping("select")
    public ResponseEntity<List<Long>> selectRoleMenuById(@RequestParam("roleId") Long roleId) {

        List<Long> roleMenus = this.roleMenuService.selectRoleMenuById(roleId);

        // 若数据为空
        if (CollectionUtils.isEmpty(roleMenus)) {

            // 全部权限
            roleMenus = new ArrayList<Long>();

            roleMenus.add(1L);

            return ResponseEntity.ok(roleMenus);
        }

        // 若数据不为空，则返回该权限
        return ResponseEntity.ok(roleMenus);
    }
}
