package com.feicheng.blog.listener;

import com.feicheng.blog.service.AdminService;
import com.feicheng.blog.service.ArticleService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 管理员发送消息Listener
 *
 * @author Lenovo
 */
@Component
public class AdminListener {

    @Autowired
    private AdminService adminService;

    /**
     * 文章添加发送消费者1
     */
    @RabbitListener(bindings = @QueueBinding(

            value = @Queue(value = "BLOG.ADMIN.ACTIVATE.QUEUE", durable = "true"),

            exchange = @Exchange(value = "BLOG.EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),

            key = {"admin.activate"}
    ))
    public void activate(Integer adminId) {

        try {

            if (adminId == null) {

                return;
            }

            this.adminService.sendMsgToEmail(adminId);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
