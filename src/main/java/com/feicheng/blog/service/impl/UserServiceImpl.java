package com.feicheng.blog.service.impl;

import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.entity.User;
import com.feicheng.blog.mapper.UserMapper;
import com.feicheng.blog.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 网站用户管理Service
 *
 * @author Lenovo
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired(required = false)
    private UserMapper userMapper;

    /**
     * 分页查询网站用户
     *
     * @param page
     * @param limit
     * @return
     */
    @Override
    public PageResult<User> selectUserByPage(Integer page, Integer limit, String userName, String userEmail) {

        PageHelper.startPage(page, limit);

        // 创建模板
        Example example = new Example(User.class);

        Example.Criteria criteria = example.createCriteria();

        // 添加查询条件（用户名）
        if (StringUtils.isNoneBlank(userName)){

            criteria.andLike("userName", "%" + userName + "%");

        }

        // 添加查询条件（用户邮箱）
        if (StringUtils.isNoneBlank(userEmail)){

            criteria.andLike("userEmail", "%" + userEmail + "%");

        }

        // 执行查询
        List<User> users = this.userMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(users)) {

            return new PageResult<>(-1, "无数据", 0, null);
        }

        PageInfo<User> pageInfo = new PageInfo<>(users);

        return new PageResult<>(0, "查询成功", (int)pageInfo.getTotal(), pageInfo.getList());
    }
}
