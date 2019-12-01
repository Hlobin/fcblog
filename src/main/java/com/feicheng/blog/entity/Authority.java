package com.feicheng.blog.entity;

import java.io.Serializable;

/**
 * 用户类别
 * @author DrameCode
 */

public class Authority implements Serializable {

    private Integer id;

    private String authorityName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }
}
