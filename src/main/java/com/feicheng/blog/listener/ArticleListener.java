package com.feicheng.blog.listener;

import com.feicheng.blog.service.ArticleService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 文章发送消息Listener
 *
 * @author Lenovo
 */
@Component
public class ArticleListener {

    @Autowired
    private ArticleService articleService;

    /**
     * 文章添加发送消费者1
     */
    @RabbitListener(bindings = @QueueBinding(

            value = @Queue(value = "BLOG.ARTICLE.INSERT.QUEUE", durable = "true"),

            exchange = @Exchange(value = "BLOG.EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),

            key = {"article.insert"}
    ))
    public void insertOne(Integer id) {

        try {

            if (id == null) {

                return;
            }

            this.articleService.sendArticleToEmail(id);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    /**
     * 文章添加发送消费者2
     */
    @RabbitListener(bindings = @QueueBinding(

            value = @Queue(value = "BLOG.ARTICLE.INSERT.QUEUE", durable = "true"),

            exchange = @Exchange(value = "BLOG.EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),

            key = {"article.insert"}
    ))
    public void insertTwo(Integer id) {

        try {

            if (id == null) {

                return;
            }

            this.articleService.sendArticleToEmail(id);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
