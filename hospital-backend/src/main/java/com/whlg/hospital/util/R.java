package com.whlg.hospital.util;

import java.io.Serializable;

/**
 * @Author: wanjianhong
 * @Version: 1.0
 * @Description: 统一响应结果标准类
 */
public class R<T> implements Serializable {

    private boolean flag;//是否成功
    private Integer code;//返回码
    private String message;//返回消息

    private T data;//返回数据

    private R(boolean flag, Integer code, String message, Object data) {
        this.flag = flag;
        this.code = code;
        this.message = message;
        this.data = (T)data;
    }

    private R(boolean flag, Integer code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

    private R() {
        this.flag = true;
        this.code = StatusCode.OK;
        this.message = "操作成功";
    }

    public static<T> R<T> createSuccess(){
        return new R(true, StatusCode.OK, "操作成功", null);
    }

    public static<T> R<T> createSuccess(T data){
        return new R(true, StatusCode.OK, "操作成功", data);
    }

    public static<T> R<T> createError(){
        return new R(false, StatusCode.ERROR, "操作失败");
    }

    public static<T> R<T> createError(String message){
        return new R(false, StatusCode.ERROR, message);
    }

    public static<T> R<T> createError(Integer code, String message){
        return new R(false, code, message);
    }

    public static<T> R<T> createError(Integer code, String message, T data){
        return new R(false, code, message, data);
    }

    //getter和setter省略
    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}