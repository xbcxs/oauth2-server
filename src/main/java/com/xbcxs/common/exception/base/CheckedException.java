package com.xbcxs.common.exception.base;

import com.xbcxs.common.i18n.InternationalizationConfig;

/**
 * @author xiaosh
 * @date 2019/9/9
 */
public class CheckedException extends Exception{

    private Integer code;

    public Integer getCode() {
        return code;
    }

    public CheckedException(String message) {
        super(InternationalizationConfig.getString(message));
    }

    public CheckedException(Integer code, String message) {
        super(InternationalizationConfig.getString(message));
        this.code = code;
    }
}
