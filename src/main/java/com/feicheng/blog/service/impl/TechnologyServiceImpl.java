package com.feicheng.blog.service.impl;

import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.entity.Techlogy;
import com.feicheng.blog.mapper.TechnologyMapper;
import com.feicheng.blog.service.TechnologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 技能管理Service
 * @author Lenovo
 */
@Service
public class TechnologyServiceImpl implements TechnologyService {

    @Autowired(required = false)
    private TechnologyMapper technologyMapper;

    /**
     * 查询所有的技能信息
     * @return
     */
    @Override
    public PageResult<Techlogy> selectAllTechnology() {

        List<Techlogy> techlogies = this.technologyMapper.selectAll();

        if (CollectionUtils.isEmpty(techlogies)){

            return new PageResult<>(404, "无数据", 0, null);
        }

        return new PageResult<>(200, "查询成功", techlogies.size(), techlogies);
    }

    /**
     * 根据id查询技能
     * @param technologyId
     * @return
     */
    @Override
    public Techlogy selectTechnologyById(Integer technologyId) {

        Techlogy techlogy = this.technologyMapper.selectByPrimaryKey(technologyId);

        if (techlogy == null){

            return null;
        }

        return techlogy;
    }
}
