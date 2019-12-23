package com.feicheng.blog.service.impl;

import com.feicheng.blog.common.MessageResult;
import com.feicheng.blog.common.ResponseResult;
import com.feicheng.blog.entity.Admin;
import com.feicheng.blog.entity.AdminRole;
import com.feicheng.blog.entity.Menu;
import com.feicheng.blog.entity.RoleMenu;
import com.feicheng.blog.mapper.AdminMapper;
import com.feicheng.blog.mapper.AdminRoleMapper;
import com.feicheng.blog.mapper.MenuMapper;
import com.feicheng.blog.mapper.RoleMenuMapper;
import com.feicheng.blog.service.AdminService;
import com.feicheng.blog.utils.SendEmailUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 管理员Service
 *
 * @author Lenovo
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired(required = false)
    private AdminMapper adminMapper;

    @Autowired(required = false)
    private AdminRoleMapper adminRoleMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired(required = false)
    private RoleMenuMapper roleMenuMapper;

    @Autowired(required = false)
    private MenuMapper menuMapper;

    // 判断邮箱格式
    private String emailRegex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";


    // 本机使用IP地址
    @Value("${myself.address}")
    private String myselfAddress;

    @Override
    public MessageResult<Admin> selectAdminByName(String adminName) {

        // 若参数为空
        if (StringUtils.isBlank(adminName)) {

            return new MessageResult<>(400, "参数错误", null);
        }

        // 创建模板
        Example example = new Example(Admin.class);

        Example.Criteria criteria = example.createCriteria();

        // 设置查询条件
        criteria.andEqualTo("adminName", adminName);

        // 执行查询
        List<Admin> admins = this.adminMapper.selectByExample(example);

        // 若为空
        if (CollectionUtils.isEmpty(admins)) {

            return new MessageResult<>(204, "无数据", null);
        }

        // 返回数据
        return new MessageResult<>(200, "查询成功", admins.get(0));
    }

    /**
     * 管理员注册
     *
     * @param adminName
     * @param adminPassword
     * @return
     */
    @Override
    public ResponseResult register(String adminName, String adminPassword, String adminEmail, String captcha) {
        ResponseResult responseResult = new ResponseResult();


        // 校验信息

        // 校验用户名
        if (StringUtils.isBlank(adminName)) {

            return new ResponseResult(400, "请输入管理员名称");

        }

        // 校验密码
        if (StringUtils.isBlank(adminPassword)) {

            return new ResponseResult(400, "请输入密码");
        }

        // 校验邮箱
        if (StringUtils.isBlank(adminEmail)) {

            return new ResponseResult(400, "请输入邮箱");
        }

        // 校验邮箱格式
        if (!Pattern.matches(emailRegex, adminEmail)) {

            return new ResponseResult(400, "邮箱格式错误");
        }

        // 判断管理员名称是否存在
        MessageResult<Admin> messageResult = this.selectAdminByName(adminName);

        if (messageResult.getData() != null) {

            return new ResponseResult(400, "管理员名称已存在");

        }

        // 执行插入管理员
        // 创建一个管理员对象
        Admin admin = new Admin();

        // 设置参数
        admin.setAdminName(adminName);

        admin.setAdminPassword(adminPassword);

        admin.setAdminEmail(adminEmail);

        admin.setAdminDate(new Date());

        admin.setAdminStatus(0);

        admin.setAdminDelete(1);

        // 执行插入
        Integer count = this.adminMapper.insertSelective(admin);

        if (count > 0) {

            // 设置新注册的管理员为注册账户角色
            // 创建一个管理员角色类
            AdminRole adminRole = new AdminRole();

            // 设置参数
            adminRole.setAdminId(admin.getId().longValue());

            adminRole.setRoleId(2L);

            // 执行插入
            count = this.adminRoleMapper.insertSelective(adminRole);

            if (count > 0) {

                // 增加注册用户的菜单权限
                // 获取所有的菜单
                // 创建模板
                Example example = new Example(Menu.class);

                Example.Criteria criteria = example.createCriteria();

                // 设置查询条件
                criteria.andEqualTo("isMenu", 0);

                // 执行查询
                List<Menu> menus = this.menuMapper.selectByExample(example);

                // 循环插入
                for (Menu menu : menus
                ) {

                    // 创建菜单角色对象
                    RoleMenu roleMenu = new RoleMenu();

                    // 设置信息
                    roleMenu.setRoleId(adminRole.getRoleId());

                    roleMenu.setMenuId(menu.getAuthorityId());

                    // 执行插入
                    this.roleMenuMapper.insert(roleMenu);
                }

                try {

                    // 发送消息到消息队列中，用户点击激活
                    this.sendMsg("activate", admin.getId());

                } catch (Exception e) {

                    e.printStackTrace();

                    return new ResponseResult(500, "注册失败");
                }

                return new ResponseResult(200, "注册成功");
            }

            return new ResponseResult(200, "注册成功");
        }

        return new ResponseResult(500, "注册失败");
    }

    /**
     * 发送激活消息到用户邮箱
     *
     * @param adminId
     */
    @Override
    public void sendMsgToEmail(Integer adminId) {

        if (adminId == null) {

            return;
        }

        // 根据id查询管理员
        Admin admin = this.adminMapper.selectByPrimaryKey(adminId);

        if (admin == null) {

            return;
        }

        // 将信息转换成Html格式
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<html>");

        stringBuilder.append("<head>");

        stringBuilder.append("<title>管理员激活</title>");

        stringBuilder.append("</head>");

        stringBuilder.append("<body>");

        stringBuilder.append("<p>你已成功注册，请点击下列连接进行激活</p>");

        stringBuilder.append("<p>激活地址：" + myselfAddress + "admin/activate/" + admin.getId() + "</p>");

        stringBuilder.append("</body>");

        stringBuilder.append("</html>");

        // 发送邮件

        try {

            SendEmailUtil.sendEmail(admin.getAdminEmail(), stringBuilder.toString());

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    /**
     * 激活管理员
     *
     * @param adminId
     * @return
     */
    @Override
    public ResponseResult activate(Integer adminId) {

        if (adminId == null) {

            return new ResponseResult(400, "参数错误");
        }

        // 根据id查询用户
        Admin admin = this.adminMapper.selectByPrimaryKey(adminId);

        if (admin == null) {

            return new ResponseResult(400, "无数据");
        }

        // 判断账户状态是否为1
        if (admin.getAdminStatus() == 1) {

            // 不用再激活
            return new ResponseResult(200, "你已经激活了");
        }

        // 将状态修改为1
        admin.setAdminStatus(1);

        // 执行修改
        Integer count = this.adminMapper.updateByPrimaryKeySelective(admin);

        if (count > 0) {

            return new ResponseResult(200, "激活成功");
        }

        return new ResponseResult(400, "激活失败");
    }


    /**
     * 发送消息
     *
     * @param type
     * @param adminId
     */
    private void sendMsg(String type, Integer adminId) {

        try {

            this.amqpTemplate.convertAndSend("admin." + type, adminId);

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}
