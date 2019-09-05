package com.xbcxs.common;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * HTTP/JSON数据封装
 * @author xiaosh
 * @date 2019/9/4
 */
public class HttpResult<M, D> implements Serializable {

    private static final long serialVersionUID = 1L;
    private int code;
    private M msg;
    private D data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public M getMsg() {
        return msg;
    }

    public void setMsg(M msg) {
        this.msg = msg;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }

    private HttpResult(int code, M msg, D data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <M, D> String success(M msg, D data) {
        return new HttpResult(HttpServletResponse.SC_OK, msg, data).toJSONString();
    }

    public static <D> String success(D data) {
        return new HttpResult(HttpServletResponse.SC_OK, "success!", data).toJSONString();
    }

    public static <M> String error(M msg) {
        return new HttpResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, msg, null).toJSONString();
    }

    public String toJSONString() {
        return JSONObject.toJSONString(this, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue);
    }

}
