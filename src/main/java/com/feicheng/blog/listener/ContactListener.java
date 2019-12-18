package com.feicheng.blog.listener;

import com.feicheng.blog.service.CommentService;
import com.feicheng.blog.service.ContactService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 留言Listener
 *
 * @author Lenovo
 */
@Component
public class ContactListener {


    @Autowired
    private ContactService contactService;

    /**
     * 用户留言消息发送消费者
     */
    @RabbitListener(bindings = @QueueBinding(

            value = @Queue(value = "BLOG.CONTACT.INSERT.QUEUE", durable = "true"),

            exchange = @Exchange(value = "BLOG.EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),

            key = {"contact.insert", "contact.update"}
    ))
    public void insert(String message) {

        if (StringUtils.isBlank(message)) {

            return;
        }

        this.contactService.sendCommentToEmail(message);
    }
}
