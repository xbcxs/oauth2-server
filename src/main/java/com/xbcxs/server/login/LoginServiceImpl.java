package com.xbcxs.server.login;

import com.xbcxs.common.OAuthConstants;
import net.sf.ehcache.Element;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by xiaosh on 2019/8/12.
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private CacheManager cacheManager;

    @Override
    public boolean validate(String loginToken) {
        boolean flag = false;
        Cache cache = cacheManager.getCache(OAuthConstants.CacheCase.LOGIN_TOKEN_CACHE);
        String value = cache.get(loginToken, String.class);
        if(value != null){
            flag = true;
        }
        return flag;
    }

    @Override
    public String login(String loginName, String password) throws OAuthSystemException {
        // 验证账号密码,省略...
        String userId = "userId_" + loginName;
        // 生成登录token存入缓存
        String tokenId = new MD5Generator().generateValue();
        Cache cache = cacheManager.getCache(OAuthConstants.CacheCase.LOGIN_TOKEN_CACHE);
        cache.put(tokenId, userId);
        return tokenId;
    }
}
