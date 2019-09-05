package com.xbcxs.json;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author xiaosh
 * @date 2019/9/4
 */
public class HttpResult{

    private static final long serialVersionUID = 1L;

    private int code;
    private String msg;


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

    private HttpResult(int code, String msg/*, T data*/){
        this.code = code;
        this.msg = msg;
//        this.data = data;
    }

   /* public static <T> String success(String msg, T data){
        return new HttpResult(HttpServletResponse.SC_OK, msg, data).toJSONString();
    }*/

//    public static <T> String success(T data){
//        return new HttpResult(HttpServletResponse.SC_OK, "success!", data).toJSONString();
//    }
//
//    public static String error(String msg){
//        return new HttpResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, msg, null).toJSONString();
//    }

    public String toJSONString() {
        HttpResult httpResult = this;
        return JSONObject.toJSONString(httpResult, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue);
    }

    public static void main(String[] args){
        HttpResult httpResult = new HttpResult(1,"dsD");

        String str = JSONObject.toJSONString(httpResult);
        System.out.println("================str:" +str);
    }

}
