package com.feicheng.blog.controller;

import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.entity.User;
import com.feicheng.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 网站用户管理Controller
 *
 * @author Lenovo
 */
@Controller
@RequestMapping("user")
public class UserController {


    @Autowired
    private UserService userService;


    /**
     * 分页查询网站用户
     *
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<PageResult<User>> selectUserByPage(@RequestParam("page") Integer page,
                                                             @RequestParam("limit") Integer limit,
                                                             @RequestParam(name = "userName", required = false) String userName,
                                                             @RequestParam(name = "userEmail", required = false) String userEmail) {

        PageResult<User> pageResult = this.userService.selectUserByPage(page, limit, userName, userEmail);

        if (CollectionUtils.isEmpty(pageResult.getData())) {

            return ResponseEntity.ok(pageResult);
        }

        return ResponseEntity.ok(pageResult);

    }
}
