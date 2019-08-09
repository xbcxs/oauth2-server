package com.xbcxs.common;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by xiaosh on 2019/8/5.
 */
public class ResultMessage {

    /**
     * 0 失败；1成功
     */
    private int code = 1;
    private String msg;
    private JSONObject data;

    public ResultMessage() {

    }

    public void error(String msg){
        this.setCode(0);
        this.setMsg(msg);
    }

    public void success(String msg){
        this.setCode(1);
        this.setMsg(msg);
    }

    public void success(String msg, JSONObject data){
        this.setCode(1);
        this.setMsg(msg);
        this.setData(data);
    }

    public void success(int code, String msg, JSONObject data){
        this.setCode(code);
        this.setMsg(msg);
        this.setData(data);
    }

    @Override
    public String toString() {
        return String.valueOf(JSONObject.toJSON(this));
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}