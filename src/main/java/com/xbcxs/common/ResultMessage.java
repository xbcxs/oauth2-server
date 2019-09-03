package com.xbcxs.common;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 *
 * @author xiaosh
 * @date 2019/8/5
 */
public class ResultMessage<T> {

    /**
     * 1成功，非1失败
     */
    private int code;
    private String msg;
    private T data;

    public ResultMessage(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <F>  ResultMessage<F> packager(int code, String msg, F data){
        return new ResultMessage<F>(code, msg, data);
    }

    @Override
    public String toString() {
        return String.valueOf(JSONObject.toJSONString(this, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue));
    }
}