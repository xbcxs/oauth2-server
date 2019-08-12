package com.xbcxs.server.authorize;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Hashtable;

/**
 * Created by xiaosh on 2019/8/5.
 */
@Component
public class CodeServiceImpl implements CodeService {

    /**
     *  CODE超期时间
     */
    private int EXPIRES_TIME = 10 * 60 * 1000;

    @Resource
    private CacheManager cacheManager;

    @Override
    public String generateCode() throws OAuthSystemException {
        // 生成授权码
        OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
        return oauthIssuerImpl.authorizationCode();
    }


}
