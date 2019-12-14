package com.feicheng.blog.service;

import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.entity.Techlogy;

/**
 * @author Lenovo
 */
public interface TechnologyService {

    // 查询所有的技能信息
    PageResult<Techlogy> selectAllTechnology();

    // 根据id查询技能
    Techlogy selectTechnologyById(Integer technologyId);
}
