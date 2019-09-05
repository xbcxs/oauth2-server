package com.xbcxs.server.authorize;

import com.xbcxs.common.OauthConstants;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xiaosh
 * @date 2019/8/13
 */
@Service
public class AccessTokenServiceImpl implements AccessTokenService {

    @Resource
    private CacheManager cacheManager;

    @Autowired
    private CodeService codeService;

    @Override
    public AuthToken generateAccessToken(String codeId) throws OAuthSystemException {
        OAuthIssuer oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
        String accessToken = oAuthIssuer.accessToken();
        String refreshToken = oAuthIssuer.refreshToken();
        Cache authTokenCache = cacheManager.getCache(OauthConstants.CacheCase.AUTH_TOKEN_CACHE);
        AuthToken authToken = new AuthToken();
        authToken.setAccessToken(accessToken);
        // 有效期2小时超时
        authToken.setAccessTokenExpire(1000 * 60 * 60 * 2);
        authToken.setRefreshToken(refreshToken);
        // 有效期30天
        authToken.setRefreshTokenExpire(1000 * 60 * 60 * 24 * 30);
        Code codeObj = codeService.getCode(codeId);
        authToken.setAuthUserId(codeObj.getAuthUserId());
        authToken.setClientId(codeObj.getClientId());
        authTokenCache.put(accessToken, authToken);
        // 销毁code
        codeService.evictCode(codeId);
        return authToken;
    }
}
