package com.feicheng.blog.service;

import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.entity.User;

/**
 * @author Lenovo
 */
public interface UserService {

    // 分页查询网站用户
    PageResult<User> selectUserByPage(Integer page, Integer limit, String userName, String userEmail);
}
