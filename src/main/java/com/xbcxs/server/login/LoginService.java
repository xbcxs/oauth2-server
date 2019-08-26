package com.xbcxs.server.login;

import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.springframework.stereotype.Service;

/**
 * Created by xiaosh on 2019/8/12.
 */
@Service
public interface LoginService{

    /**
     * 根据loginToken判断是否登录且有效
     * @param loginToken
     * @return
     */
    boolean validate(String loginToken);

    /**
     * 账号密码登录并生成登录token
     * @param loginName
     * @param password
     * @return
     * @throws OAuthSystemException
     */
    String login(String loginName, String password) throws OAuthSystemException;
}
