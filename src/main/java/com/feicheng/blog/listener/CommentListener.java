package com.feicheng.blog.listener;

import com.feicheng.blog.service.CommentService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 评论
 * @author Lenovo
 */
@Component
public class CommentListener {


    @Autowired
    private CommentService commentService;

    /**
     * 用户评论消息发送消费者
     */
    @RabbitListener(bindings = @QueueBinding(

            value = @Queue(value = "BLOG.COMMENT.INSERT.QUEUE", durable = "true"),

            exchange = @Exchange(value = "BLOG.EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),

            key = {"comment.insert", "comment.update"}
    ))
    public void insert(Integer id){

        if (id == null){

            return;
        }

        this.commentService.sendCommentToEmail(id);
    }
}
