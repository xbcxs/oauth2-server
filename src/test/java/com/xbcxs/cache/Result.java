package com.xbcxs.cache;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaosh
 * @date 2019/8/30
 */

public class Result<E> {
    private String message;
    private int retCode;
    private E data;
    private Result(E data) {
        this.retCode = 0;
        this.message = "成功";
        this.data = data;
    }

    public static void main(String[] args){
        List<String> dd = new ArrayList();

    }

    public  void a (E dd){

    }

    /**
     * 成功时候的调用
     * @return
     */
    public static <T> Result<T> success(T data){
        return new Result<T>(data);
    }
    /**
     * 成功，不需要传入参数
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Result<T> success(){
        return (Result<T>) success("");
    }
    public String getMessage() {
        return message;
    }
    public int getRetCode() {
        return retCode;
    }
}