package com.feicheng.blog.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;

/**
 * @author Lenovo
 */
public class BaseController {

    private Subject getSubject() {

        return SecurityUtils.getSubject();
    }

    protected void login(AuthenticationToken token) {

        getSubject().login(token);
    }

}
