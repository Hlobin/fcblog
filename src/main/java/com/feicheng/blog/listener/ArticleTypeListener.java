package com.feicheng.blog.listener;

import com.feicheng.blog.service.ArticleTypeService;
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
 * 首页文章标签保存消息Listener
 *
 * @author Lenovo
 */
@Component
public class ArticleTypeListener {


    @Autowired
    private ArticleTypeService articleTypeService;


    /**
     * redis中首页图片保存除出错消息发送
     */
    @RabbitListener(bindings = @QueueBinding(

            value = @Queue(value = "BLOG.REDIS.INDEX.ARTICLETYPE.ERROR.QUEUE", durable = "true"),

            exchange = @Exchange(value = "BLOG.EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),

            key = {"redis.index.article.type.error"}
    ))
    public void error(String message) {

        // 通过邮件的发送到邮箱
        this.articleTypeService.sendRedisErrorToEmail(message);
    }


    /**
     * redis中首页标签更新数据消息发送
     */
    @RabbitListener(bindings = @QueueBinding(

            value = @Queue(value = "BLOG.REDIS.INDEX.ARTICLETYPE.INSERT.QUEUE", durable = "true"),

            exchange = @Exchange(value = "BLOG.EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),

            key = {"redis.index.article.type.insert","redis.index.article.type.update"}
    ))
    public void insert(String message) {

        try {

            if (StringUtils.isBlank(message)){

                return;
            }

            // 更新数据
            this.articleTypeService.updateRedisData();

        } catch (Exception e) {

            e.printStackTrace();


        }
    }

    /**
     * redis中首页标签更新数据消息发送
     */
    @RabbitListener(bindings = @QueueBinding(

            value = @Queue(value = "BLOG.REDIS.INDEX.ARTICLETYPE.DELETE.QUEUE", durable = "true"),

            exchange = @Exchange(value = "BLOG.EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),

            key = {"redis.index.article.type.delete"}
    ))
    public void delete(String message) {

        try {

            if (StringUtils.isBlank(message)){

                return;
            }

            // 更新数据
            this.articleTypeService.updateRedisData();

        } catch (Exception e) {

            e.printStackTrace();


        }
    }
}
