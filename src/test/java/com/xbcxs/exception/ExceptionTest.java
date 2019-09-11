package com.xbcxs.exception;

import com.xbcxs.exception.base.CheckedException;
import com.xbcxs.exception.base.UncheckedException;
import com.xbcxs.json.HttpResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xiaosh
 * @date 2019/9/6
 */
public class ExceptionTest {

    private static final Logger log = LoggerFactory.getLogger(ExceptionTest.class);

    /**
     * 模拟Client请求
     * @param args
     */
    public static void main(String[] args){
        try {
            ExceptionTest et = new ExceptionTest();
            et.proxyAction();
        } catch (Exception e) {
            // 模拟ResponWriter返回到页面的数据
            log.error("500", e);
        }
    }

    /**
     * 模拟控制层代理
     */
    public void proxyAction() {
        try {
            action();
        } catch (UncheckedException be) {
            // be.printStackTrace();
            log.error(be.getMessage());
            log.debug("异常:", be);
            log.info("返回请求数据：{}",HttpResult.error(be.getCode(), be.getMessage()));
        } catch (CheckedException be) {
            // be.printStackTrace();
            log.error(be.getMessage());
            log.debug("异常:", be);
            log.info("返回请求数据：{}",HttpResult.error(be.getCode(), be.getMessage()));
        }
    }

    /**
     * 控制层
     */
    public void action() throws BusinessCheckedException {
        // service1();
        service2();
    }

    /**
     * 业务1层
     */
    private void service1() {
        throw new BusinessUncheckedException("业务不一致！");
    }

    private void service2() throws BusinessCheckedException {
        throw new BusinessCheckedException("受检测异常！");
    }

}
