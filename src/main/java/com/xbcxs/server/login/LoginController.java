package com.xbcxs.server.login;

import com.alibaba.fastjson.JSONObject;
import com.xbcxs.common.ResponseWriter;
import com.xbcxs.common.ResultMessage;
import com.xbcxs.server.authorize.CodeService;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xiaosh on 2019/8/6.
 */
@RestController
public class LoginController {

    private Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    CodeService codeServiceImpl;

    @Autowired
    LoginService loginService;

    /**
     * 模拟客户端对授权（码）服务发起请求
     *
     * @param request
     * @param response
     */
    @RequestMapping("/login")
    public void login(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 模拟登录并保存信息
            String loginName = request.getParameter("loginName");
            String password = request.getParameter("password");
            String tokenId = loginService.login(loginName, password);
            log.info("用户登录...");

            // 回调客户端
          /*  OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
            String redirectUri = oauthRequest.getRedirectURI();
            Cache cache = cacheManager.getCache(OAuthConstants.CacheCase.LOGIN_TOKEN_CACHE);
            String userId = cache.get(loginToken, String.class);
            String code = codeServiceImpl.generateCode(userId, oauthRequest.getClientId());
            // 构建OAuth响应
            OAuthResponse oauthResponse = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND)
                    .setCode(code)
                    .location(redirectUri)
                    .buildQueryMessage();
            log.info("回调客户端请求:" + oauthResponse.getLocationUri());
            response.sendRedirect(oauthResponse.getLocationUri());*/


            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
            String redirectUri = oauthRequest.getRedirectURI();
            boolean falg = true;
            if (falg) {
                String code = codeServiceImpl.generateCode(tokenId, oauthRequest.getClientId());
                // 构建OAuth响应
                OAuthResponse oauthResponse = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND)
                        .setCode(code)
                        .location(redirectUri)
                        .buildQueryMessage();
                log.info("回调客户端:" + oauthResponse.getLocationUri());
                // response.sendRedirect(oauthResponse.getLocationUri());
                JSONObject jsonData = new JSONObject();
                jsonData.put("redirectUri", oauthResponse.getLocationUri());
                resultMessage.setData(jsonData);
                ResponseWriter.writer(response, new ResultMessage(0,));

            } else {
                ResponseWriter.writer(response, resultMessage.toString());
            }
        } catch (OAuthSystemException e) {
            e.printStackTrace();
        } catch (OAuthProblemException e) {
            e.printStackTrace();
     /*   } catch (IOException e) {
            e.printStackTrace();*/
        }
    }
}