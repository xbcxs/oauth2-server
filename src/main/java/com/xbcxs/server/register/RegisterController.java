package com.xbcxs.server.register;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaosh on 2019/8/8.
 */
@RequestMapping("register")
@RestController
public class RegisterController {

    Logger log = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    RegisterService registerService;

    @RequestMapping("/apply")
    public void apply(HttpServletRequest request) {
        Map parameterMap = new HashMap();
        parameterMap.put("id", "11");
        List<Map> list = registerService.listRegisters(parameterMap);
        log.info(">>>>>>>>>>" + JSON.toJSONString(list));
    }

}
