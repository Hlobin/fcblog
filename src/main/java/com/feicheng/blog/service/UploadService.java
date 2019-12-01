package com.feicheng.blog.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author Lenovo
 */
public interface UploadService {

    // 上传文章图片
    Map<String, Object> uploadArticleImage(MultipartFile file);
}
