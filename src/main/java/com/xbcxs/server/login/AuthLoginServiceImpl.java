package com.xbcxs.server.login;

import com.xbcxs.server.authorize.CodeService;
import com.xbcxs.server.authorize.exception.AuthorizeException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 登录业务层实现
 * @author xiaosh
 * @date 2019/9/5
 */
@Service
public class AuthLoginServiceImpl implements AuthLoginService {

    @Autowired
    CodeService codeService;

    private boolean validate(String loginName, String password) {
        // 简化验证过程
        return loginName!=null && "123456".equals(password) ? true : false;
    }

    @Override
    public String authLogin(String clientId, String loginName, String password) throws OAuthSystemException {
        if(!validate(loginName, password)){
            throw new AuthorizeException();
        }
        String authUserId = getAuthUserId(loginName);
        return codeService.generateCode(authUserId, clientId);
    }

    private String getAuthUserId(String loginName){
        // 省略loginName查库转换成userId的过程
        return loginName;
    }

}
