package com.xbcxs.server.authorize;

import com.xbcxs.common.OAuthConstants;
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
 *
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
    public AccessToken generateAccessToken(String code) throws OAuthSystemException {
        OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
        String accessToken = oauthIssuerImpl.accessToken();
        String refreshToken = oauthIssuerImpl.refreshToken();
        Cache accessTokenCache = cacheManager.getCache(OAuthConstants.CacheCase.ACCESS_TOKEN_CACHE);
        AccessToken accessTokenObj = new AccessToken();
        accessTokenObj.setAccessToken(accessToken);
        accessTokenObj.setRefreshToken(refreshToken);
        Code codeObj = codeService.getCode(code);
        accessTokenObj.setUserId(codeObj.getUserId());
        accessTokenObj.setClientId(codeObj.getClientId());
        // 两小时超时
        accessTokenObj.setAccessTokenExpire(1000 * 60 * 60 * 2);
        accessTokenCache.put(accessToken, accessTokenObj);
        // 销毁code
        codeService.evictCode(code);
        return accessTokenObj;
    }
}
