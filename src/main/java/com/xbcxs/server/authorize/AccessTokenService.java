package com.xbcxs.server.authorize;

import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

/**
 * @author xiaosh
 * @date 2019/8/13
 */
public interface AccessTokenService {

    /**
     * 生成accessToken
     * @param codeId
     * @return
     * @throws OAuthSystemException
     */
    AuthToken generateAccessToken(String codeId) throws OAuthSystemException;
}
