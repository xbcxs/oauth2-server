package com.xbcxs.server.authorize;

import java.io.Serializable;

/**
 * @author xiaosh
 * @date 2019/8/13
 */
public class Code implements Serializable {

    private String code;
    private String userId;
    private String clientId;
    private String scope;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
