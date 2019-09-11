package com.xbcxs.common.exception.advice;

import com.xbcxs.common.HttpResult;
import com.xbcxs.common.ResponseWriter;
import com.xbcxs.common.exception.base.CheckedException;
import com.xbcxs.common.exception.base.UncheckedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * @author xiaosh
 * @date 2019/9/9
 */
@ControllerAdvice
public class ExceptionAdvice {

    private Logger log = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler({CheckedException.class,})
    @ResponseBody
    public void handException(HttpServletResponse response, CheckedException e) {
        e.printStackTrace();
        log.error(e.getMessage());
        log.debug("异常:", e);
        ResponseWriter.writer(response, HttpResult.error(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler({UncheckedException.class})
    @ResponseBody
    public void handException(HttpServletResponse response, UncheckedException e) {
        e.printStackTrace();
        log.error(e.getMessage());
        log.debug("异常:", e);
        ResponseWriter.writer(response, HttpResult.error(e.getCode(), e.getMessage()));
    }
}
