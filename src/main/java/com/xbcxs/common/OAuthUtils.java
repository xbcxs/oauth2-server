package com.xbcxs.common;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by xiaosh on 2019/8/6.
 */
public class OAuthUtils {

    public static String getRequestPrefix(HttpServletRequest request) {
        // 获取网络协议
        String networkProtocol = request.getScheme();
        // 网络IP
        String ip = request.getServerName();
        //端口号
        int port = request.getServerPort();
        //项目发布路径
        String webApp = request.getContextPath();
        String urlPrefix = networkProtocol + "://" + ip + ":" + port + webApp;
        return urlPrefix;
    }
}
