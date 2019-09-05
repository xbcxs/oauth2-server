package com.xbcxs.server.authorize;

import java.io.Serializable;

/**
 * @author xiaosh
 * @date 2019/8/13
 */
public class AuthToken implements Serializable {

    private String accessToken;
    private String refreshToken;
    private int accessTokenExpire;
    private int refreshTokenExpire;
    private String authUserId;
    private String clientId;
    private String scope;

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

    public int getRefreshTokenExpire() {
        return refreshTokenExpire;
    }

    public void setRefreshTokenExpire(int refreshTokenExpire) {
        this.refreshTokenExpire = refreshTokenExpire;
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

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
