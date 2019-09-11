package com.xbcxs.exception;

import com.xbcxs.exception.base.CheckedException;

/**
 * @author xiaosh
 * @date 2019/9/9
 */
public class BusinessCheckedException extends CheckedException{

    public BusinessCheckedException(String message) {
        super();
        this.setCode("1000");
        this.setMessage(message);
    }

    public BusinessCheckedException(String code, String message) {
        super();
        this.setCode(code);
        this.setMessage(message);
    }

}
