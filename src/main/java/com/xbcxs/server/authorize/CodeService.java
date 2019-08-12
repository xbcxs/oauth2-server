package com.xbcxs.server.authorize;

import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

/**
 * Created by xiaosh on 2019/8/5.
 */
public interface CodeService {

   /* Hashtable saveCode(String id);

    boolean removeCode(String id);

    boolean exist(String id);

    boolean expires();*/

    /**
     * 生成code码
     * @return
     * @throws OAuthSystemException
     */
    String generateCode() throws OAuthSystemException;

}
