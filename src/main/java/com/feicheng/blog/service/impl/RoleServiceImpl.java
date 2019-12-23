package com.feicheng.blog.service.impl;

import com.feicheng.blog.common.MessageResult;
import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.common.ResponseResult;
import com.feicheng.blog.entity.Admin;
import com.feicheng.blog.entity.AdminRole;
import com.feicheng.blog.entity.Role;
import com.feicheng.blog.entity.RoleMenu;
import com.feicheng.blog.mapper.AdminRoleMapper;
import com.feicheng.blog.mapper.RoleMapper;
import com.feicheng.blog.mapper.RoleMenuMapper;
import com.feicheng.blog.service.AdminService;
import com.feicheng.blog.service.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 角色管理Service
 *
 * @author Lenovo
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired(required = false)
    private RoleMapper roleMapper;

    @Autowired(required = false)
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private AdminService adminService;

    @Autowired(required = false)
    private AdminRoleMapper adminRoleMapper;

    /**
     * 查询所有的角色
     *
     * @param page
     * @param limit
     * @return
     */
    @Override
    public PageResult<Role> selectAllRole(Integer page, Integer limit) {

        // 设置分页起始
        PageHelper.startPage(page, limit);

        // 查询数据
        List<Role> roles = this.roleMapper.selectAll();

        if (CollectionUtils.isEmpty(roles)) {

            return new PageResult<>(-1, "无数据", 0, null);

        }

        // 设置分页数据
        PageInfo<Role> pageInfo = new PageInfo<>(roles);

        return new PageResult<>(0, "查询成功", (int) pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 修改角色信息及权限
     *
     * @param role
     * @return
     */
    @Override
    @Transactional
    public ResponseResult editRole(Role role) {

        // 判断是否输入数据
        if (StringUtils.isEmpty(role.getRoleName())) {

            return new ResponseResult(400, "请输入角色名称");
        }

        if (StringUtils.isEmpty(role.getRemark())) {

            return new ResponseResult(400, "请输入角色描述");
        }

        // 设置基本信息
        role.setCreateTime(new Date());

        role.setModifyTime(role.getCreateTime());

        // 修改角色信息
        Integer count = this.roleMapper.updateByPrimaryKey(role);

        if (count > 0) {

            // 插入新的权限数据
            // 角色有的权限id转换成list集合
            List<Long> menuIds = changeStringToList(role.getRoleMenu());

            // 先删除数据库中对应角色的权限
            // 创建模板
            Example example = new Example(RoleMenu.class);

            Example.Criteria criteria = example.createCriteria();

            criteria.andEqualTo("roleId", role.getRoleId());

            if (CollectionUtils.isEmpty(menuIds)) {

                // 不执行删除，直接添加新的权限数据
                for (Long menuId : menuIds
                ) {

                    // 创建对象
                    RoleMenu roleMenu = new RoleMenu();

                    roleMenu.setRoleId(role.getRoleId());

                    roleMenu.setMenuId(menuId);

                    // 执行插入数据
                    this.roleMenuMapper.insert(roleMenu);
                }

                return new ResponseResult(200, "修改成功");

            }

            // 执行删除
            count = this.roleMenuMapper.deleteByExample(example);

            if (count >= 0) {

                // 插入新的权限数据
                // 角色有的权限id转换成list集合

                // 若为空
                if (CollectionUtils.isEmpty(menuIds)) {

                    return new ResponseResult(200, "修改成功");

                }
                // 不为空
                // 循环插入数据
                for (Long menuId : menuIds
                ) {

                    // 创建对象
                    RoleMenu roleMenu = new RoleMenu();

                    roleMenu.setRoleId(role.getRoleId());

                    roleMenu.setMenuId(menuId);

                    // 执行插入数据
                    this.roleMenuMapper.insert(roleMenu);
                }
                return new ResponseResult(200, "修改成功");
            }

            return new ResponseResult(500, "修改失败");
        }

        return new ResponseResult(500, "修改失败");
    }


    /**
     * 添加角色信息及权限
     *
     * @param role
     * @return
     */
    @Override
    public ResponseResult addRole(Role role) {

        // 判断是否输入数据
        if (StringUtils.isEmpty(role.getRoleName())) {

            return new ResponseResult(400, "请输入角色名称");
        }

        if (StringUtils.isEmpty(role.getRemark())) {

            return new ResponseResult(400, "请输入角色描述");
        }

        // 设置基本信息
        role.setCreateTime(new Date());

        role.setModifyTime(role.getCreateTime());

        // 插入角色信息
        Integer count = this.roleMapper.insertSelective(role);

        if (count > 0) {

            // 插入新的权限数据
            // 角色有的权限id转换成list集合
            List<Long> menuIds = changeStringToList(role.getRoleMenu());

            // 判若为空
            if (CollectionUtils.isEmpty(menuIds)) {

                // 不添加任何信息
                return new ResponseResult(200, "添加成功");
            }

            // 若不为空

            // 循环插入数据
            for (Long menuId : menuIds
            ) {

                // 创建对象
                RoleMenu roleMenu = new RoleMenu();

                roleMenu.setRoleId(role.getRoleId());

                roleMenu.setMenuId(menuId);

                // 执行插入数据
                this.roleMenuMapper.insert(roleMenu);
            }

            return new ResponseResult(200, "修改成功");
        }

        return new ResponseResult(500, "修改失败");

    }

    /**
     * 查询管理员的角色
     *
     * @param adminName
     * @return
     */
    @Override
    public List<Role> selectAdminRole(String adminName) {

        List<Role> roles = new ArrayList<Role>();

        // 根据管理员名称查询管理员
        MessageResult<Admin> messageResult = this.adminService.selectAdminByName(adminName);

        if (messageResult.getData() == null) {

            return roles;
        }

        roles = listAdminRoles(messageResult.getData());

        if (CollectionUtils.isEmpty(roles)) {

            return roles;
        }

        return roles;
    }


    /**
     * 查询管理员的角色
     *
     * @param admin
     * @return
     */
    private List<Role> listAdminRoles(Admin admin) {

        List<Role> roles = new ArrayList<>();

        // 创建模板
        Example example = new Example(AdminRole.class);

        Example.Criteria criteria = example.createCriteria();

        // 设置查询条件
        criteria.andEqualTo("adminId", admin.getId());

        // 执行查询
        List<AdminRole> adminRoles = this.adminRoleMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(adminRoles)) {

            return roles;
        }

        // 循环查询角色信息
        for (AdminRole adminRole : adminRoles) {

            Role role = this.roleMapper.selectByPrimaryKey(adminRole.getRoleId());

            // 添加到List集合中
            roles.add(role);
        }

        // 返回数据
        return roles;
    }


    /**
     * 将用逗号隔开的字符串转换成List对象
     *
     * @param roleMenu
     * @return
     */
    private List<Long> changeStringToList(String roleMenu) {

        // 若没有添加权限信息
        if (StringUtils.isEmpty(roleMenu)) {

            return null;
        }

        // 添加了权限信息

        // 将字符串分割成数组
        String[] split = roleMenu.split(",");

        // 循环遍历将String装换成Long

        List<Long> roleMenuIds = new ArrayList<Long>();

        for (String menuId : split
        ) {

            roleMenuIds.add(Long.parseLong(menuId));
        }

        // 移除数据为1的数据
        roleMenuIds.remove(0);

        return roleMenuIds;
    }

}
