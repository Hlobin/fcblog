package com.feicheng.blog.common;


/**
 * @author Lenovo
 * 信息返回Result
 */
public class ResponseResult {

    // 返回的状态码
    private Integer code;

    // 返回的信息
    private String info;

    private String src;

    private String title;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ResponseResult() {
    }

    /**
     * 返回状态码和信息
     * @param code
     * @param info
     */
    public ResponseResult(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    /**
     * 返回图片路径和标题
     * @param src
     * @param title
     */
    public ResponseResult(String src, String title) {
        this.src = src;
        this.title = title;
    }
}
