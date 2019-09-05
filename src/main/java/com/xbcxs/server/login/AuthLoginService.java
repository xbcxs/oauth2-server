package com.xbcxs.server.login;

import com.xbcxs.server.authorize.Exception.AuthorizeException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.springframework.stereotype.Service;

/**
 * 登录业务接口层
 * @author xiaosh
 * @date 2019/9/5
 */
@Service
public interface AuthLoginService {

    /**
     * 登录并返回codeId
     * @param clientId
     * @param loginName
     * @param password
     * @return codeId
     */
    String authLogin(String clientId, String loginName, String password) throws AuthorizeException, OAuthSystemException;

}
