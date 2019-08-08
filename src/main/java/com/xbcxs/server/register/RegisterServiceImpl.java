package com.xbcxs.server.register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by xiaosh on 2019/8/8.
 */
@Service
public class RegisterServiceImpl implements RegisterService{

    @Autowired
    RegisterMapper registerMapper;

    @Override
    public List<Map> listRegisters(Map parameterMap) {
        return registerMapper.selectAll(parameterMap);
    }
}
