package com.xbcxs.exception.base;

/**
 * @author xiaosh
 * @date 2019/9/6
 */
public class UncheckedException extends RuntimeException{

    private String code;

    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UncheckedException() {
        super();
    }

}
