package com.feicheng.blog.service;

import com.feicheng.blog.common.MessageResult;
import com.feicheng.blog.common.ResponseResult;
import com.feicheng.blog.entity.Admin;

/**
 * @author Lenovo
 */
public interface AdminService {

    // 根据管理员名称查询管理员
    MessageResult<Admin> selectAdminByName(String adminName);

    // 管理员注册
    ResponseResult register(String adminName, String adminPassword,  String adminEmail, String captcha);

    // 发送激活邮件到用户邮箱
    void sendMsgToEmail(Integer adminId);

    // 激活管理员
    ResponseResult activate(Integer adminId);
}
