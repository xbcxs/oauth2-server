package com.xbcxs.exception;

import com.xbcxs.exception.base.UncheckedException;

/**
 * @author xiaosh
 * @date 2019/9/9
 */
public class BusinessUncheckedException extends UncheckedException {

    public BusinessUncheckedException(String message) {
        super();
        this.setCode("1000");
        this.setMessage(message);
    }

    public BusinessUncheckedException(String code, String message) {
        super();
        this.setCode(code);
        this.setMessage(message);
    }
}
