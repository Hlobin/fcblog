package com.feicheng.blog.entity;

import java.io.Serializable;

/**
 * 用户搜索基本类
 * @author DrameCode
 */
public class UserSearchExample implements Serializable {

    private String userName;

    private String userSex;

    private String userCity;

    private String userClassify;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public String getUserClassify() {
        return userClassify;
    }

    public void setUserClassify(String userClassify) {
        this.userClassify = userClassify;
    }
}
