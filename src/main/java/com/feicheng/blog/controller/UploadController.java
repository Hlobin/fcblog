package com.feicheng.blog.controller;

import com.feicheng.blog.common.MessageResult;
import com.feicheng.blog.common.ResponseResult;
import com.feicheng.blog.service.UploadService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 上传图片Controller
 * @author Lenovo
 */

@Controller
@RequestMapping("upload")
public class UploadController {


    @Autowired
    private UploadService uploadService;

    /**
     * 上传图片
     * @param file
     * @return
     */
    @PostMapping("file")
    public ResponseEntity<MessageResult<ResponseResult>> uploadArticleImage(@RequestParam("file")MultipartFile file){

        Map<String, Object> map = this.uploadService.uploadArticleImage(file);

        if (StringUtils.equals(map.get("result").toString(), "success")){

            return ResponseEntity.ok(new MessageResult<>(0, "上传成功", new ResponseResult(map.get("src").toString(), map.get("title").toString())));

        }
        return ResponseEntity.ok(new MessageResult<>(1, "上传失败", new ResponseResult(500, map.get("result").toString())));

    }
}
