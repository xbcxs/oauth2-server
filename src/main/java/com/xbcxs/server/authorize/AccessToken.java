package com.xbcxs.server.authorize;

import java.io.Serializable;

/**
 * @author xiaosh
 * @date 2019/8/13
 */
public class AccessToken implements Serializable {

    private String accessToken;
    private String refreshToken;
    private int accessTokenExpire;
    private String userId;
    private String clientId;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public int getAccessTokenExpire() {
        return accessTokenExpire;
    }

    public void setAccessTokenExpire(int accessTokenExpire) {
        this.accessTokenExpire = accessTokenExpire;
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
}
