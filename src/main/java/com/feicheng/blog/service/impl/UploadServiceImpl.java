package com.feicheng.blog.service.impl;

import com.feicheng.blog.service.UploadService;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 上传图片的Service
 *
 * @author Lenovo
 */
@Service
public class UploadServiceImpl implements UploadService {


    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private ThumbImageConfig thumbImageConfig;

    private static final List<String> CONTENT_TYPES = Arrays.asList("image/gif", "image/jpeg");

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadServiceImpl.class);

    /**
     * 上传文章图片
     *
     * @param file
     * @return
     */
    @Override
    public Map<String, Object> uploadArticleImage(MultipartFile file) {

        Map<String, Object> map = new HashMap<String, Object>();

        // 判断文件是否为空
        if (file == null) {

            map.put("result", "文件为空");

            return map;
        }
        // 校验文件类型
        String originalFilename = file.getOriginalFilename();

        String contentType = file.getContentType();

        if (!CONTENT_TYPES.contains(contentType)) {

            LOGGER.info("文件类型不合法： {}", originalFilename);

            map.put("result", "文件类型不合法");

            return map;

        }
        try {
            // 校验文件内容
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());

            if (bufferedImage == null) {

                LOGGER.info("文件内容不合法：{}" + originalFilename);

                map.put("result", "文件内容不合法");

                return map;
            }

            // 将文件写入对应的储存地址
            // 将图片上传到FastDFS
            // 获取文件后缀名
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");

            // 上传并且生成缩略图
            StorePath storePath = this.storageClient.uploadFile(
                    file.getInputStream(), file.getSize(), extension, null);

            String imageUrl = "http://123.57.64.9/" + storePath.getFullPath();

            map.put("result", "success");

            map.put("src", imageUrl);

            map.put("title", "文章图片");

            return map;

        } catch (IOException e) {

            LOGGER.info("服务器内部错误：" + originalFilename);

            e.printStackTrace();
        }

        return null;
    }
}
