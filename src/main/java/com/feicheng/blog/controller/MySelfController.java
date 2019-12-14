package com.feicheng.blog.controller;

import com.feicheng.blog.common.ResponseResult;
import com.feicheng.blog.entity.MySelf;
import com.feicheng.blog.service.MySelfService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * 个人信息Controller
 * @author Lenovo
 */
@Controller
@RequestMapping("myself")
public class MySelfController {

    @Autowired
    private MySelfService mySelfService;


    /**
     * 添加个人信息
     * @param mySelf
     * @return
     */
    @PostMapping("update")
    public ResponseEntity<ResponseResult> addMySelfMessage(MySelf mySelf){

        Map<String, Object> map = this.mySelfService.addMySelfMessage(mySelf);

        if (StringUtils.equals(map.get("message").toString(), "error")) {

            return ResponseEntity.ok(new ResponseResult(400, map.get("result").toString()));
        }
        return ResponseEntity.ok(new ResponseResult(200, map.get("result").toString()));
    }

}
