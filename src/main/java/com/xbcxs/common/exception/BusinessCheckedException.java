package com.xbcxs.common.exception;

import com.xbcxs.common.exception.base.CheckedException;

/**
 * @author xiaosh
 * @date 2019/9/9
 */
public class BusinessCheckedException extends CheckedException {

    public BusinessCheckedException(String message) {
        super(1002, message);
    }

    public BusinessCheckedException(Integer code, String message) {
        super(code, message);
    }

}
