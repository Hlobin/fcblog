package com.feicheng.blog.service;

import com.feicheng.blog.entity.MySelf;

import java.util.Map;

/**
 * @author Lenovo
 */
public interface MySelfService {

    // 添加个人信息
    Map<String, Object> addMySelfMessage(MySelf mySelf);

    // 查询个人信息
    MySelf selectMySelf();
}
