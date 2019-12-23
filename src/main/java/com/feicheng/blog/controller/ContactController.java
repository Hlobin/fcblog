package com.feicheng.blog.controller;
import	java.nio.file.Path;
import java.util.Map;

import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.common.ResponseResult;
import com.feicheng.blog.entity.Contact;
import com.feicheng.blog.service.ContactService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 留言Controller
 * @author Lenovo
 */
@Controller
@RequestMapping("contact")
public class ContactController {

    @Autowired
    private ContactService contactService;


    /**
     * 分页查询留言内容
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("list")
    @RequiresPermissions("contact:view")
    public ResponseEntity<PageResult<Contact>> select(@RequestParam("page") Integer page,
                                                      @RequestParam("limit") Integer limit,
                                                      @RequestParam(value = "contactContent", required = false) String contactContent){

        PageResult<Contact> pageResult = this.contactService.selectAllContact(page, limit, contactContent);

        if (CollectionUtils.isEmpty(pageResult.getData())){

            return ResponseEntity.ok(pageResult);
        }

        return ResponseEntity.ok(pageResult);
    }

    /**
     * 修改留言内容
     * @param contact
     * @return
     */
    @PostMapping("edit")
    @RequiresPermissions("contact:edit")
    public ResponseEntity<ResponseResult> edit(Contact contact){

        Map<String, Object> map = this.contactService.editContact(contact);

        if (StringUtils.equals(map.get("message").toString(), "error")) {

            return ResponseEntity.ok(new ResponseResult(400, map.get("result").toString()));

        }

        return ResponseEntity.ok(new ResponseResult(200, map.get("result").toString()));
    }

    /**
     * 删除留言内容
     * @param contactId
     * @return
     */
    @PostMapping("delete")
    @RequiresPermissions("contact:delete")
    public ResponseEntity<ResponseResult> delete(@RequestParam("contactId") Integer contactId){

        Map<String, Object> map = this.contactService.deleteContact(contactId);

        if (StringUtils.equals(map.get("message").toString(), "error")) {

            return ResponseEntity.ok(new ResponseResult(400, map.get("result").toString()));

        }

        return ResponseEntity.ok(new ResponseResult(200, map.get("result").toString()));
    }

    /**
     * 添加留言
     * @param contactContent
     * @return
     */
    @PostMapping("add")
    @RequiresPermissions("contact:add")
    public ResponseEntity<ResponseResult> add(@RequestParam("contactContent") String contactContent){

        Map<String, Object> map = this.contactService.addContact(contactContent);

        if (StringUtils.equals(map.get("message").toString(), "error")) {

            return ResponseEntity.ok(new ResponseResult(400, map.get("result").toString()));

        }

        return ResponseEntity.ok(new ResponseResult(200, map.get("result").toString()));
    }

}
