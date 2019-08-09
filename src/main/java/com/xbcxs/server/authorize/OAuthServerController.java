package com.xbcxs.server.authorize;

import com.alibaba.fastjson.JSONObject;
import com.xbcxs.common.OAuthConstants;
import com.xbcxs.common.OAuthUtils;
import com.xbcxs.common.ResultMessage;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by xiaosh on 2019/7/31.
 */
@RestController
@RequestMapping("auth")
public class OAuthServerController {

    Logger log = LoggerFactory.getLogger(OAuthServerController.class);

    @Autowired
    CodeService codeServiceImpl;

    @RequestMapping("/authorize")
    public void authorize(HttpServletRequest request, HttpServletResponse response) {
        ResultMessage resultMessage = new ResultMessage();
        JSONObject errorMessage = new JSONObject();
        try {
            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
            String clientId = oauthRequest.getClientId();
            String redirectUri = oauthRequest.getRedirectURI();
            /**
             * 客户端信息校验
             */
            if (!OAuthConstants.CLIENT_ID.equals(clientId)) {
                errorMessage.put("client_id", "不能为空");
            }
            if (!OAuthConstants.REDIRECT_URI.equals(redirectUri)) {
                errorMessage.put("redirect_uri", "不能为空");
            }
            String requestPrefix = OAuthUtils.getRequestPrefix(request);
            if (!OAuthConstants.REDIRECT_URI.startsWith(requestPrefix)){
                errorMessage.put("redirect_uri", "域与注册时不符");
            }

            if(true){

            }

            // 生成授权码
            OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
            String code = oauthIssuerImpl.authorizationCode();
            log.info("生成授权码code:" + code);
            // 构建OAuth响应
            OAuthResponse oauthResponse = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND)
                    .setCode(code)
                    .location(redirectUri)
                    .buildQueryMessage();
            log.info("回调客户端:" + oauthResponse.getLocationUri());
            response.sendRedirect(oauthResponse.getLocationUri());

           /* //  构建请求，跳转至登录页面
            OAuthClientRequest oAuthClientRequest = OAuthClientRequest
                    .authorizationLocation(AuthUtils.getRequestPrefix(request) + "/login.html")
                    .setClientId(AuthConstant.CLIENT_ID )
                    .setRedirectURI(AuthConstant.REDIRECT_URI)
                    .setResponseType(AuthConstant.CODE)
                    .buildQueryMessage();
            log.info("跳转至登录页面:" + oAuthClientRequest.getLocationUri());
            response.sendRedirect(oAuthClientRequest.getLocationUri());*/
        } catch (OAuthProblemException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OAuthSystemException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "/accessToken", method = RequestMethod.POST)
    public void accessToken(HttpServletRequest request, HttpServletResponse response) throws OAuthSystemException, IOException, OAuthProblemException {

        ResultMessage resultMessage = new ResultMessage();

        OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);

        String authzCode = oauthRequest.getCode();
        String clientId = oauthRequest.getClientId();
        String redirectUri = oauthRequest.getRedirectURI();

        /**
         * 客户端信息校验
         */
        if (!OAuthConstants.CLIENT_ID.equals(clientId)) {
            resultMessage.error("client_id is null");
        }
        if (!OAuthConstants.REDIRECT_URI.equals(redirectUri)) {
            resultMessage.error("redirect_uri is null");
        }
        String requestPrefix = OAuthUtils.getRequestPrefix(request);
        if (!OAuthConstants.REDIRECT_URI.startsWith(requestPrefix)){
            resultMessage.error("redirect_uri 域与注册时不符");
        }

        // TODO 校验CODE存在和销毁

        // 通过code转换token
        OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
        String accessToken = oauthIssuerImpl.accessToken();
        String refreshToken = oauthIssuerImpl.refreshToken();

        // 构建相应
        OAuthResponse oauthResponse = OAuthASResponse
                .tokenResponse(HttpServletResponse.SC_OK)
                .setAccessToken(accessToken)
                .setExpiresIn("3600")
                .setRefreshToken(refreshToken)
                .buildJSONMessage();

        response.setStatus(oauthResponse.getResponseStatus());
        PrintWriter pw = response.getWriter();
        pw.print(oauthResponse.getBody());
        pw.flush();
        pw.close();
    }


}
