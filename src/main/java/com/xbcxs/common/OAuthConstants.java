package com.xbcxs.common;

/**
 * Created by xiaosh on 2019/8/1.
 */
public class OAuthConstants {

    public static String CLIENT_ID = "clientId";

    public static String CLIENT_SECRET = "clientSecret";

    public static String REDIRECT_URI = "http://localhost:8888/as/client/getToken";

    public static String CODE = "code";

    public class CacheCase {
        public static final String LOGIN_TOKEN_CACHE = "loginTokenCache";
        public static final String CODE_CACHE = "codeCache";
        public static final String ACCESS_TOKEN_CACHE = "accessTokenCache";
        public static final String REFRESH_TOKEN_CACHE = "refreshTokenCache";
    }

}
