package com.xbcxs.server.authorize;

import com.xbcxs.common.OAuthConstants;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

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
    public String generateCode(String userId, String clientId) throws OAuthSystemException {
        OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
        String code = oauthIssuerImpl.authorizationCode();
        Cache cache = cacheManager.getCache(OAuthConstants.CacheCase.CODE_CACHE);
        Code codeObject = new Code();
        codeObject.setCode(code);
        codeObject.setUserId(userId);
        codeObject.setClientId(clientId);
        // TODO 查询授权范围
        codeObject.setScope("1");
        cache.put(code, codeObject);
        return code;
    }

    @Override
    public boolean isExist(String code) {
        boolean flag = false;
        Cache cache = cacheManager.getCache(OAuthConstants.CacheCase.CODE_CACHE);
        Code codeObject = cache.get(code, Code.class);
        if(codeObject != null){
            flag = true;
        }
        return flag;
    }

    @Override
    public Code getCode(String code) {
        Cache codeCache = cacheManager.getCache(OAuthConstants.CacheCase.CODE_CACHE);
        Code codeObj = codeCache.get(code, Code.class);
        return codeObj;
    }

    @Override
    public void evictCode(String code) {
        Cache codeCache = cacheManager.getCache(OAuthConstants.CacheCase.CODE_CACHE);
        codeCache.evict(code);
    }
}
