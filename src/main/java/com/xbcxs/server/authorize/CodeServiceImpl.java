package com.xbcxs.server.authorize;

import com.xbcxs.common.OauthConstants;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Code业务实现层
 * @author xiaosh
 * @date 2019/8/13
 */
@Service
public class CodeServiceImpl implements CodeService {

    @Resource
    private CacheManager cacheManager;

    @Override
    public String generateCode(String authUserId, String clientId) throws OAuthSystemException {
        OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
        String codeId = oauthIssuerImpl.authorizationCode();
        Cache cache = cacheManager.getCache(OauthConstants.CacheCase.CODE_CACHE);
        Code codeObject = new Code();
        codeObject.setCodeId(codeId);
        codeObject.setAuthUserId(authUserId);
        codeObject.setClientId(clientId);
        cache.put(codeId, codeObject);
        return codeId;
    }

    @Override
    public boolean isExist(String code) {
        Cache cache = cacheManager.getCache(OauthConstants.CacheCase.CODE_CACHE);
        Code codeObject = cache.get(code, Code.class);
        return codeObject != null ? true : false;
    }

    @Override
    public Code getCode(String code) {
        Cache codeCache = cacheManager.getCache(OauthConstants.CacheCase.CODE_CACHE);
        return codeCache.get(code, Code.class);
    }

    @Override
    public void evictCode(String code) {
        Cache codeCache = cacheManager.getCache(OauthConstants.CacheCase.CODE_CACHE);
        codeCache.evict(code);
    }
}
