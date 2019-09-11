package com.xbcxs.common.exception.base;

import com.xbcxs.common.i18n.InternationalizationConfig;

/**
 * @author xiaosh
 * @date 2019/9/6
 */
public class UncheckedException extends RuntimeException{

    private Integer code;

    public Integer getCode() {
        return code;
    }

    public UncheckedException(String message) {
        super(InternationalizationConfig.getString(message));
    }

    public UncheckedException(Integer code, String message) {
        super(InternationalizationConfig.getString(message));
        this.code = code;
    }

}
