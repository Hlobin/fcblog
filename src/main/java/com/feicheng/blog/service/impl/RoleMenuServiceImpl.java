package com.feicheng.blog.service.impl;

import com.feicheng.blog.entity.Role;
import com.feicheng.blog.entity.RoleMenu;
import com.feicheng.blog.mapper.RoleMenuMapper;
import com.feicheng.blog.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色菜单Service
 *
 * @author Lenovo
 */
@Service
public class RoleMenuServiceImpl implements RoleMenuService {

    @Autowired(required = false)
    private RoleMenuMapper roleMenuMapper;

    /**
     * 根据roleId查询菜单权限
     *
     * @param roleId
     * @return
     */
    @Override
    public List<Long> selectRoleMenuById(Long roleId) {

        // 创建模板
        Example example = new Example(RoleMenu.class);

        // 设置查询条件
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("roleId", roleId);

        // 执行查询
        List<RoleMenu> roleMenus = this.roleMenuMapper.selectByExample(example);

        // 若没有数据
        if (CollectionUtils.isEmpty(roleMenus)) {

            return null;
        }

        // 若有数据
        List<Long> roleMenusId = new ArrayList<Long>();

        roleMenus.forEach(roleMenu -> {

            roleMenusId.add(roleMenu.getMenuId());
        });

        return roleMenusId;
    }
}
