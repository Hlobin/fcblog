package com.feicheng.blog.controller;

import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.common.ResponseResult;
import com.feicheng.blog.entity.FrontPicture;
import com.feicheng.blog.service.FrontPictureService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 首页图片管理Controller
 *
 * @author Lenovo
 */
@Controller
@RequestMapping("picture")
public class FrontPictureController {

    @Autowired
    private FrontPictureService frontPictureService;

    /**
     * 分页查询首页图片
     *
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("list")
    @RequiresPermissions("picture:view")
    public ResponseEntity<PageResult<FrontPicture>> selectPictureByPage(@RequestParam("page") Integer page,
                                                                        @RequestParam("limit") Integer limit,
                                                                        @RequestParam(value = "pictureTitle", required = false) String pictureTitle) {

        PageResult<FrontPicture> pageResult = this.frontPictureService.selectPictureByPage(page, limit, pictureTitle);

        if (CollectionUtils.isEmpty(pageResult.getData())) {

            return ResponseEntity.ok(pageResult);
        }

        return ResponseEntity.ok(pageResult);
    }


    /**
     * 添加首页图片
     *
     * @param frontPicture
     * @return
     */
    @PostMapping("add")
    @RequiresPermissions("picture:add")
    public ResponseEntity<ResponseResult> addPicture(FrontPicture frontPicture) {

        Map<String, Object> map = this.frontPictureService.addPicture(frontPicture);

        if (StringUtils.equals(map.get("message").toString(), "error")) {

            return ResponseEntity.ok(new ResponseResult(400, map.get("result").toString()));
        }

        return ResponseEntity.ok(new ResponseResult(200, map.get("result").toString()));
    }

    /**
     * 修改首页图片信息
     * @param frontPicture
     * @return
     */
    @PostMapping("edit")
    @RequiresPermissions("picture:edit")
    public ResponseEntity<ResponseResult> editPicture(FrontPicture frontPicture){

        Map<String, Object> map = this.frontPictureService.editPicture(frontPicture);

        if (StringUtils.equals(map.get("message").toString(), "error")) {

            return ResponseEntity.ok(new ResponseResult(400, map.get("result").toString()));
        }

        return ResponseEntity.ok(new ResponseResult(200, map.get("result").toString()));
    }

}
