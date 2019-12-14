package com.feicheng.blog.entity;

import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 管理员技术表
 * @author DrameCode
 */
@Table(name = "techlogy")
public class Techlogy{

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    private String technologyName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTechnologyName() {
        return technologyName;
    }

    public void setTechnologyName(String technologyName) {
        this.technologyName = technologyName;
    }
}
