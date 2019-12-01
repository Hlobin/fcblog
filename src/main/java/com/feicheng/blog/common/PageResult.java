package com.feicheng.blog.common;

import java.util.List;

/**
 * 页面回显Result
 * @author Lenovo
 */
public class PageResult<T>{

    // 状态码
    private Integer code;

    // 消息
    private String msg;

    // 数量
    private Integer count;

    // 返回的集合
    private List<T> data;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public PageResult() {
    }

    public PageResult(Integer code, String msg, Integer count, List<T> data) {
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
    }
}
