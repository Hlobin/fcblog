package com.feicheng.blog.service.impl;


import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.entity.Contact;
import com.feicheng.blog.mapper.ContactMapper;
import com.feicheng.blog.service.ContactService;
import com.feicheng.blog.utils.SendEmailUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 留言Service
 *
 * @author Lenovo
 */
@Service
public class ContactServiceImpl implements ContactService {


    @Autowired(required = false)
    private ContactMapper contactMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    // 本人email地址
    @Value("${myself.email}")
    private String myselfEmail;

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactServiceImpl.class);
    /**
     * 查询所有的留言信息
     *
     * @param page
     * @param limit
     * @return
     */
    @Override
    public PageResult<Contact> selectAllContact(Integer page, Integer limit, String contactContent) {

        // 开启分页
        PageHelper.startPage(page, limit);

        // 创建模板
        Example example = new Example(Contact.class);

        // 设置模糊查询
        Example.Criteria criteria = example.createCriteria();

        // 判断contactContent是否为空
        if (!StringUtils.isEmpty(contactContent)){

            criteria.andLike("contactContent", "%" + contactContent + "%");
        }


        // 执行查询
        List<Contact> contacts = this.contactMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(contacts)) {

            return new PageResult<>(-1, "无数据", 0, null);
        }

        //使用分页
        PageInfo<Contact> pageInfo = new PageInfo<>(contacts);

        return new PageResult<>(0, "查询成功", (int) pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 修改留言信息
     *
     * @param contact
     * @return
     */
    @Override
    public Map<String, Object> editContact(Contact contact) {

        Map<String, Object> map = new HashMap<String, Object>();

        // 判断是否输入留言内容
        if (StringUtils.isEmpty(contact.getContactContent())) {

            map.put("message", "error");

            map.put("result", "请输入留言内容");

            return map;
        }

        // 执行修改
        Integer count = this.contactMapper.updateByPrimaryKey(contact);

        if (count > 0) {

            map.put("message", "success");

            map.put("result", "修改成功");

            return map;
        }


        map.put("message", "error");

        map.put("result", "操作失败");

        return map;
    }

    /**
     * 删除留言内容
     *
     * @param contactId
     * @return
     */
    @Override
    public Map<String, Object> deleteContact(Integer contactId) {

        Map<String, Object> map = new HashMap<String, Object>();

        // 判断id是否为空
        if (contactId == null) {

            map.put("message", "error");

            map.put("result", "参数错误");

            return map;
        }

        // 执行删除操作
        Integer count = this.contactMapper.deleteByPrimaryKey(contactId);

        if (count > 0) {

            map.put("message", "success");

            map.put("result", "删除成功");

            return map;
        }

        map.put("message", "error");

        map.put("result", "操作失败");

        return map;
    }

    /**
     * 添加留言
     * @param contactContent
     * @return
     */
    @Override
    public Map<String, Object> addContact(String contactContent) {

        Map<String, Object> map = new HashMap<String, Object>();

        // 判断是否输入有值
        if (StringUtils.isEmpty(contactContent)){

            map.put("message", "error");

            map.put("result", "请输入留言");

            return map;
        }

        // 创建对象
        Contact contact = new Contact();

        contact.setContactContent(contactContent);

        contact.setContactDate(new Date());

        // 执行添加
        Integer count = this.contactMapper.insertSelective(contact);

        if (count > 0){

            map.put("message", "success");

            map.put("result", "留言成功");

            // 发送消息
            this.sendMsg("insert", contact.getContactContent());

            return map;

        }
        map.put("message", "error");

        map.put("result", "操作失败");

        return map;
    }

    /**
     * 用户留言发送到邮箱
     * @param message
     */
    @Override
    public void sendCommentToEmail(String message) {

        // 将消息拼接成Html格式
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<html>");

        stringBuilder.append("<head>");

        stringBuilder.append("<title>用户留言</title>");

        stringBuilder.append("</head>");

        stringBuilder.append("<body>");

        stringBuilder.append("<p>留言内容：" + message + "</p>");

        stringBuilder.append("</body>");

        stringBuilder.append("</html>");

        SendEmailUtil.sendEmail(myselfEmail,stringBuilder.toString());
    }



    /**
     * 用户留言发送消息到本人邮箱（生产者）
     * @param type
     * @param message
     */
    private void sendMsg(String type, String message) {

        try {

            this.amqpTemplate.convertAndSend("contact." + type, message);

        }catch (Exception e){

            e.printStackTrace();

            LOGGER.error("消息队列发送失败");
        }
    }
}
