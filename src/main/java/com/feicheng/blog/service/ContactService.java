package com.feicheng.blog.service;

import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.entity.Contact;

import java.util.Map;

/**
 * @author Lenovo
 */
public interface ContactService {

    // 查询所有的留言信息
    PageResult<Contact> selectAllContact(Integer page, Integer limit, String contactContent);

    // 修改留言信息
    Map<String, Object> editContact(Contact contact);

    // 删除留言内容
    Map<String, Object> deleteContact(Integer contactId);

    // 添加留言
    Map<String, Object> addContact(String contactContent);

    // 用户留言发送到邮箱
    void sendCommentToEmail(String message);
}
