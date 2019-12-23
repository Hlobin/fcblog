package com.feicheng.blog.controller;

import com.feicheng.blog.common.MessageResult;
import com.feicheng.blog.common.ResponseResult;
import com.feicheng.blog.entity.Admin;
import com.feicheng.blog.service.AdminService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 管理员Controller
 *
 * @author Lenovo
 */
@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private AdminService adminService;


    /**
     * 根据用户名获取管理员
     *
     * @param adminName
     * @return
     */
    @GetMapping("select")
    public ResponseEntity<MessageResult<Admin>> selectAdminByName(@RequestParam("adminName") String adminName) {

        MessageResult<Admin> messageResult = this.adminService.selectAdminByName(adminName);

        return ResponseEntity.ok(messageResult);
    }

    /**
     * 管理员激活
     * @param adminId
     * @return
     */
    @GetMapping("activate/{adminId}")
    public String activate(@PathVariable("adminId") Integer adminId, Model model) {

        ResponseResult responseResult = this.adminService.activate(adminId);

        // 激活失败
        if (responseResult.getCode() == 400) {

            model.addAttribute("msg", responseResult.getInfo());

            return "system/error/result";
        }

        // 激活成功
        if (responseResult.getCode() == 200) {

            model.addAttribute("msg", responseResult.getInfo());

            return "system/success/result";
        }

        model.addAttribute("msg", responseResult.getInfo());

        return "system/error/result";
    }
}
