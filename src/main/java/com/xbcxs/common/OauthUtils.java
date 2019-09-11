package com.xbcxs.common;

import javax.servlet.http.HttpServletRequest;

/**
 * oauth工具类
 *
 * @author xiaosh
 * @date 2019/9/5
 */
public class OauthUtils {

    private OauthUtils() {

    }

    /**
     * 获取WEB项目请求地址
     *
     * @param request
     * @return
     */
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
