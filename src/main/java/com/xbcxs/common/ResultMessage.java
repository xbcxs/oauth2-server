package com.xbcxs.common;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by xiaosh on 2019/8/5.
 */
public class ResultMessage {

    /**
     * 0 失败；1成功
     */
    private int code;
    private StringBuilder msg;
    private JSONObject data;

    public ResultMessage() {
        code = 1;
        msg = new StringBuilder();
    }

    public void errorAppend(String msg){
        this.setCode(0);
        this.msg.append(msg).append(";");
    }

    public void success(String msg){
        this.setCode(1);
        this.msg.append(msg).append(";");
    }

    public void success(JSONObject data){
        this.setCode(1);
        this.msg.append("ok");
        this.setData(data);
    }

    public void message(int code, String msg){
        this.setCode(code);
        this.msg.append(msg).append(";");
    }

    public void message(int code, String msg, JSONObject data){
        this.setCode(code);
        this.msg.append(msg).append(";");
        this.setData(data);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return String.valueOf(JSONObject.toJSON(this));
    }
}