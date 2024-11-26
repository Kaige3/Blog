package com.kaige.entity;

import lombok.*;

@Data
// 记录一个bug TODO 使用getter setter 发起请求后报406，手动设置后请求正确
public class Result {
    private Integer code;
    private Object data;
    private String msg;

    public Result(Integer code, String msg, Object data) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static Result ok(String msg, Object data) {
        return new Result(200, msg,data);
    }

    public static Result ok(String msg) {
        return new Result(200, msg);
    }

    public static Result error(String msg) {
        return new Result(500, msg);
    }

    public static Result error(){
        return new Result(500, "异常错误");
    }

    public static Result create(Integer code , String msg , Object data) {
        return new Result(code, msg, data);
    }

    public static Result create(Integer code, String msg) {
        return new Result(code, msg);
    }
}
