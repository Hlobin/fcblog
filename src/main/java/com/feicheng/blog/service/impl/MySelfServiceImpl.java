package com.feicheng.blog.service.impl;

import com.feicheng.blog.entity.MySelf;
import com.feicheng.blog.mapper.MySelfMapper;
import com.feicheng.blog.service.MySelfService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人信息Service
 *
 * @author Lenovo
 */
@Service
public class MySelfServiceImpl implements MySelfService {

    @Autowired(required = false)
    private MySelfMapper mySelfMapper;

    /**
     * 添加个人信息
     *
     * @param mySelf
     * @return
     */
    @Override
    public Map<String, Object> addMySelfMessage(MySelf mySelf) {

        Map<String, Object> map = new HashMap<String, Object>();

        // 判断是否有输入值
        if (StringUtils.isBlank(mySelf.getEnglishName())) {

            map.put("message", "error");

            map.put("result", "请输入英文名");

            return map;
        }

        if (StringUtils.isBlank(mySelf.getRealName())) {

            map.put("message", "error");

            map.put("result", "请输入真实名");

            return map;
        }

        if (StringUtils.isBlank(mySelf.getHeadSculpture())) {

            map.put("message", "error");

            map.put("result", "请上传你的头像");

            return map;
        }

        if (StringUtils.isBlank(mySelf.getTechnologyId() + "")) {

            map.put("message", "error");

            map.put("result", "请选择你的技能");

            return map;
        }

        if (StringUtils.isBlank(mySelf.getIntroduction())) {

            map.put("message", "error");

            map.put("result", "请输入你的简介");

            return map;
        }

        // 更新数据
        mySelf.setId(1);

        Integer count = this.mySelfMapper.updateByPrimaryKeySelective(mySelf);

        if (count > 0) {

            map.put("message", "success");

            map.put("result", "更新成功");

            return map;
        }

        map.put("message", "error");

        map.put("result", "操作失败");

        return map;
    }

    /**
     * 查询个人信息
     *
     * @return
     */
    @Override
    public MySelf selectMySelf() {

        List<MySelf> mySelves = this.mySelfMapper.selectAll();

        if (CollectionUtils.isEmpty(mySelves)) {

            return null;
        }

        return mySelves.get(0);
    }
}
