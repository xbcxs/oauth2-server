package com.xbcxs.server.authorize;

import java.io.Serializable;

/**
 * @author xiaosh
 * @date 2019/8/13
 */
public class Code implements Serializable {

    private String codeId;
    private String authUserId;
    private String clientId;

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getAuthUserId() {
        return authUserId;
    }

    public void setAuthUserId(String authUserId) {
        this.authUserId = authUserId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
