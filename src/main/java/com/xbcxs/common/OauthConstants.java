package com.xbcxs.common;

/**
 * 常量类
 * @author xiaosh
 * @date 2019/9/5
 */
public class OauthConstants {

    public static final String CLIENT_ID = "clientId";

    public static final String CLIENT_SECRET = "clientSecret";

    public static final String CODE = "code";

    public static final String CLIENT_REDIRECT_URI = "http://localhost:8888/oauth2/client/callbackAccessToken";

    public static final String SERVER_LOGIN_AUTHORIZE_LOCATION = "http://localhost:8888/oauth2/server/loginAuthorize";

    public static final String SERVER_TOKEN_LOCATION = "http://localhost:8888/oauth2/server/accessToken";

    public static final String RESOURCE_REQUEST = "http://localhost:8888/oauth2/resource/getUserToken";

    public class CacheCase {

        private CacheCase(){

        }

        public static final String CODE_CACHE = "codeCache";
        public static final String AUTH_TOKEN_CACHE = "authTokenCache";
    }

}
