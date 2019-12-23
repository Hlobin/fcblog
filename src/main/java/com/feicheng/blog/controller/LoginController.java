package com.feicheng.blog.controller;

import com.feicheng.blog.common.ResponseResult;
import com.feicheng.blog.entity.Admin;
import com.feicheng.blog.service.AdminService;
import com.feicheng.blog.utils.RandomValidateCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录Controller
 *
 * @author Lenovo
 */
@Controller
public class LoginController extends BaseController {


    @Autowired
    private AdminService adminService;

    private RandomValidateCode randomValidateCode = new RandomValidateCode();

    /**
     * 用户登录
     *
     * @param adminName
     * @param adminPassword
     * @return
     */
    @PostMapping("login")
    public ResponseEntity<ResponseResult> login(@RequestParam("adminName") String adminName,
                                                @RequestParam("adminPassword") String adminPassword,
                                                @RequestParam("captcha") String captcha,
                                                HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();

        try {

            // 判断验证码是否正确
            if (StringUtils.isBlank(captcha) || !RandomValidateCode.verify(request, captcha)) {

                return ResponseEntity.ok(new ResponseResult(400, "验证码错误"));
            }
        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.ok(new ResponseResult(500, "服务器错误"));
        }


        // 验证码校验正确
        UsernamePasswordToken token = new UsernamePasswordToken(adminName, adminPassword);

        try {

            // 登录
            super.login(token);

            // 将用户保存到Session中
            Session session = subject.getSession();

            Admin admin = (Admin) subject.getPrincipal();

            session.setAttribute("adminName", admin.getAdminName());

            return ResponseEntity.ok(new ResponseResult(200, "登录成功"));

            // 用户名密码错误
        } catch (IncorrectCredentialsException e) {

            e.printStackTrace();

            return ResponseEntity.ok(new ResponseResult(400, "用户名或密码错误"));
        }

        // 账号未注册
        catch (UnknownAccountException e) {

            e.printStackTrace();

            return ResponseEntity.ok(new ResponseResult(204, "账号未注册"));
        }

        // 账户未激活
        catch (AuthenticationException e) {

            e.printStackTrace();

            return ResponseEntity.ok(new ResponseResult(400, "账号未激活"));
        }
    }


    /**
     * 管理员注册
     *
     * @param adminName
     * @param adminPassword
     * @return
     */
    @PostMapping("register")
    public ResponseEntity<ResponseResult> register(@RequestParam("adminName") String adminName,
                                                   @RequestParam("adminPassword") String adminPassword,
                                                   @RequestParam("adminEmail") String adminEmail,
                                                   @RequestParam("captcha") String captcha) {

        ResponseResult responseResult = this.adminService.register(adminName, adminPassword, adminEmail, captcha);

        return ResponseEntity.ok(responseResult);
    }


    /**
     * 退出登录
     *
     * @return
     */
    @PostMapping("logout")
    public ResponseEntity<ResponseResult> logout() {

        Subject subject = SecurityUtils.getSubject();

        subject.logout();

        return ResponseEntity.ok(new ResponseResult(200, "退出登录成功"));
    }

    /**
     * 获取验证码
     *
     * @param request
     * @param response
     */
    @GetMapping("getCode")
    public void getCode(HttpServletRequest request, HttpServletResponse response) {

        try {

            randomValidateCode.getRandcode(request, response);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}

