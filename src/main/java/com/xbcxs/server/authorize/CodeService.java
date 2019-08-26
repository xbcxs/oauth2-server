package com.xbcxs.server.authorize;

import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

/**
 * Code业务实现层
 * @author xiaosh
 * @date 2019/8/13
 */
public interface CodeService {

    /**
     * 生成code码并存入缓存
     * @param userId 用户ID
     * @param ClientId 客户端ID
     * @return
     * @throws OAuthSystemException
     */
    String generateCode(String userId, String ClientId) throws OAuthSystemException;

    /**
     * 判断code是否存在
     * @param code 编码
     * @return
     */
    boolean isExist(String code);

    /**
     * 得到CODE对象
     * @param code
     * @return
     */
    Code getCode(String code);

    /**
     * 删除
     * @param code
     */
    void evictCode(String code);

}
