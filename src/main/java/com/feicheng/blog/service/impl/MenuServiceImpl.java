package com.feicheng.blog.service.impl;

import com.feicheng.blog.common.MenuTree;
import com.feicheng.blog.common.MessageResult;
import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.entity.Admin;
import com.feicheng.blog.entity.AdminRole;
import com.feicheng.blog.entity.Menu;
import com.feicheng.blog.entity.RoleMenu;
import com.feicheng.blog.mapper.AdminRoleMapper;
import com.feicheng.blog.mapper.MenuMapper;
import com.feicheng.blog.mapper.RoleMenuMapper;
import com.feicheng.blog.service.AdminService;
import com.feicheng.blog.service.MenuService;
import com.feicheng.blog.utils.IdWorker;
import com.feicheng.blog.utils.TreeUtil;
import com.sun.source.tree.Tree;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import javax.persistence.Id;
import java.util.*;

import static java.awt.Color.red;
import static java.awt.SystemColor.menu;

/**
 * 菜单管理Service
 *
 * @author Lenovo
 */
@Service
public class MenuServiceImpl implements MenuService {


    @Autowired(required = false)
    private MenuMapper menuMapper;

    @Autowired
    private AdminService adminService;

    @Autowired(required = false)
    private AdminRoleMapper adminRoleMapper;

    @Autowired(required = false)
    private RoleMenuMapper roleMenuMapper;


    /**
     * 根据父节点获取菜单树
     *
     * @return
     */
    @Override
    public PageResult<Menu> selectMenus() {

        // 获取所有的菜单
        List<Menu> menus = this.menuMapper.selectAll();

        if (CollectionUtils.isEmpty(menus)) {

            return new PageResult<>(-1, "无数据", 0, null);
        }

        return new PageResult<>(0, "查询成功", menus.size(), menus);
    }

    /**
     * 修改菜单
     *
     * @param menu
     * @return
     */
    @Override
    public Map<String, Object> editMenu(Menu menu) {

        Map<String, Object> map = new HashMap<String, Object>();

        // 设置基本信息
        menu.setCreateTime(new Date());

        menu.setUpdateTime(menu.getCreateTime());

        Integer count = this.menuMapper.updateByPrimaryKey(menu);

        if (count > 0) {

            map.put("message", "success");

            map.put("result", "更新成功");

            return map;
        }

        map.put("message", "error");

        map.put("result", "操作失败");

        return map;

    }

    /**
     * 添加菜单
     *
     * @param menu
     * @return
     */
    @Override
    public Map<String, Object> addMenu(Menu menu) {

        Map<String, Object> map = new HashMap<String, Object>();

        // 判断是否添加新的菜单
        if (menu.getAuthorityId() == -2) {

            // 设置为Null
            menu.setAuthorityId(null);

        } else {

            map.put("message", "error");

            map.put("result", "参数错误");

            return map;
        }

        // 设置基本信息
        menu.setCreateTime(new Date());

        menu.setUpdateTime(menu.getCreateTime());

        // 执行添加操作
        Integer count = this.menuMapper.insert(menu);

        if (count > 0) {

            // 更新数据
            menu.setOrderNumber(menu.getAuthorityId());

            count = this.menuMapper.updateByPrimaryKey(menu);

            if (count > 0) {

                map.put("message", "success");

                map.put("result", "error");

                return map;
            }
        }

        map.put("message", "error");

        map.put("result", "添加失败");

        return map;
    }

    /**
     * 删除菜单
     *
     * @param authorityId
     * @param isMenu
     * @return
     */
    @Override
    public Map<String, Object> delete(Long authorityId, Integer isMenu) {

        Map<String, Object> map = new HashMap<String, Object>();

        // 判断参数是否为空
        if (authorityId == null || isMenu == null) {

            map.put("message", "error");

            map.put("result", "参数错误");

            return map;
        }

        // 若是菜单
        if (isMenu == 0) {

            // 先删除子菜单
            // 创建模板
            Example example = new Example(Menu.class);

            Example.Criteria criteria = example.createCriteria();

            criteria.andEqualTo("parentId", authorityId);

            // 先查询是否有子菜单
            List<Menu> menus = this.menuMapper.selectByExample(example);

            // 若没有子菜单
            if (CollectionUtils.isEmpty(menus)) {

                // 直接删除主菜单
                Integer count = this.menuMapper.deleteByPrimaryKey(authorityId);

                if (count > 0) {

                    map.put("message", "success");

                    map.put("result", "删除成功");

                    return map;
                }

                map.put("message", "error");

                map.put("result", "删除失败");

                return map;
            }

            // 若有子菜单
            // 先删除子菜单
            Integer count = this.menuMapper.deleteByExample(example);

            if (count > 0) {

                // 删除主菜单
                count = this.menuMapper.deleteByPrimaryKey(authorityId);

                if (count > 0) {

                    map.put("message", "success");

                    map.put("result", "删除成功");

                    return map;
                }

                map.put("message", "error");

                map.put("result", "删除失败");

                return map;

            }

            map.put("message", "error");

            map.put("result", "删除失败");

            return map;
        }

        // 若是按钮
        if (isMenu == 1) {

            // 直接删除按钮菜单
            Integer count = this.menuMapper.deleteByPrimaryKey(authorityId);

            if (count > 0) {

                map.put("message", "success");

                map.put("result", "删除成功");

                return map;

            }

            map.put("message", "error");

            map.put("result", "删除失败");

            return map;
        }

        map.put("message", "error");

        map.put("result", "操作失败");

        return map;
    }

    /**
     * 获取节点树的所有节点
     *
     * @return
     */
    @Override
    public MenuTree<Menu> selectedNodes() {

        // 获取所有数据
        List<Menu> menus = this.menuMapper.selectAll();


        // 转换成节点树
        List<MenuTree<Menu>> trees = this.covertMenus(menus);

        // 返回构建好的节点树
        return TreeUtil.buildMenuTree(trees);
    }

    /**
     * 根据管理员获取管理员的权限
     *
     * @param adminName
     * @return
     */
    @Override
    public List<Menu> selectAdminPermission(String adminName) {

        List<Menu> menus = new ArrayList<>();

        // 若adminName为空
        if (StringUtils.isBlank(adminName)) {

            return menus = null;
        }

        // 根据管理员名称查询管理员
        MessageResult<Admin> messageResult = this.adminService.selectAdminByName(adminName);

        if (messageResult.getData() == null) {

            return menus = null;
        }

        // 根据管理员Id查询管理员角色
        // 创建模板
        Example example = new Example(AdminRole.class);

        Example.Criteria criteria = example.createCriteria();

        // 设置查询条件
        criteria.andEqualTo("adminId", messageResult.getData().getId());

        // 执行查询
        List<AdminRole> adminRoles = this.adminRoleMapper.selectByExample(example);

        // 若没有角色
        if (CollectionUtils.isEmpty(adminRoles)) {

            return menus = null;
        }

        // 根据角色Id查询菜单权限
        for (AdminRole adminRole : adminRoles
        ) {

            // 创建模板
            Example adminRoleExample = new Example(RoleMenu.class);

            Example.Criteria adminRoleCriteria = adminRoleExample.createCriteria();

            // 设置查询参数
            adminRoleCriteria.andEqualTo("roleId", adminRole.getRoleId());

            // 执行查询
            List<RoleMenu> roleMenus = this.roleMenuMapper.selectByExample(adminRoleExample);

            // 循环遍历该角色下的所有菜单权限
            for (RoleMenu roleMenu: roleMenus
                 ) {

                // 执行查询
                Menu menu = this.menuMapper.selectByPrimaryKey(roleMenu.getMenuId());

                // 添加到menus中
                menus.add(menu);
            }
        }

        return menus;
    }


    /**
     * 转化成节点树
     *
     * @param menus
     * @return
     */
    private List<MenuTree<Menu>> covertMenus(List<Menu> menus) {

        List<MenuTree<Menu>> trees = new ArrayList<>();

        menus.forEach(menu -> {

            MenuTree<Menu> tree = new MenuTree<>();

            tree.setId(String.valueOf(menu.getAuthorityId()));

            tree.setParentId(String.valueOf(menu.getParentId()));

            tree.setTitle(menu.getAuthorityName());

            tree.setHref(menu.getMenuUrl());

            tree.setData(menu);

            trees.add(tree);
        });

        return trees;
    }

}
