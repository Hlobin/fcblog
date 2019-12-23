package com.feicheng.blog.service;

import com.feicheng.blog.common.MenuTree;
import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.entity.Menu;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * @author Lenovo
 */
public interface MenuService {

    // 根据父节点获取菜单树
    PageResult<Menu> selectMenus();

    // 修改菜单
    Map<String, Object> editMenu(Menu menu);


    // 添加菜单
    Map<String, Object> addMenu(Menu menu);

    // 删除菜单
    Map<String, Object> delete(Long authorityId, Integer isMenu);

    // 获取节点树的所有数据
    MenuTree<Menu> selectedNodes();

    // 根据管理员获取管理员的权限
    List<Menu> selectAdminPermission(String adminName);
}
