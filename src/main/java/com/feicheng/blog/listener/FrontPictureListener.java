package com.feicheng.blog.listener;

import com.feicheng.blog.service.FrontPictureService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 首页图片保存消息Listener
 *
 * @author Lenovo
 */
@Component
public class FrontPictureListener {


    @Autowired
    private FrontPictureService frontPictureService;


    /**
     * redis中首页图片保存除出错消息发送
     */
    @RabbitListener(bindings = @QueueBinding(

            value = @Queue(value = "BLOG.REDIS.INDEX.PICTURE.ERROR.QUEUE", durable = "true"),

            exchange = @Exchange(value = "BLOG.EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),

            key = {"redis.index.picture.error"}
    ))
    public void error(String message) {

        // 通过邮件的发送到邮箱
        this.frontPictureService.sendRedisErrorToEmail(message);
    }


    /**
     * redis中首页图片更新数据消息发送
     */
    @RabbitListener(bindings = @QueueBinding(

            value = @Queue(value = "BLOG.REDIS.INDEX.PICTURE.INSERT.QUEUE", durable = "true"),

            exchange = @Exchange(value = "BLOG.EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),

            key = {"redis.index.picture.insert","redis.index.picture.update"}
    ))
    public void insert(String message) {

        try {

            if (StringUtils.isBlank(message)){

                return;
            }

            this.frontPictureService.updateRedisData();

        } catch (Exception e) {

        }
    }
}
