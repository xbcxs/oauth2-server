package com.xbcxs.server.authorize;

import com.xbcxs.common.OAuthConstants;
import com.xbcxs.common.OAuthUtils;
import com.xbcxs.common.ResponseWriter;
import com.xbcxs.common.ResultMessage;
import com.xbcxs.server.login.LoginService;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
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

    @Autowired
    LoginService loginServiceImpl;

    @RequestMapping("/authorize")
    public void authorize(HttpServletRequest request, HttpServletResponse response) {
        ResultMessage resultMessage = new ResultMessage();
        try {
            // 客户端信息校验
            resultMessage = validateClient(request);
            if (resultMessage.getCode() == 0) {
                throw new RuntimeException(resultMessage.toString());
            }
            // 校验是否已经登录
            String loginToken = request.getParameter("loginToken");
            if (loginServiceImpl.validate(loginToken)) {
                OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
                String redirectUri = oauthRequest.getRedirectURI();
                // 构建OAuth响应
                OAuthResponse oauthResponse = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND)
                        .setCode(codeServiceImpl.generateCode())
                        .location(redirectUri)
                        .buildQueryMessage();
                log.info("回调客户端请求:" + oauthResponse.getLocationUri());
                response.sendRedirect(oauthResponse.getLocationUri());
            } else {
                //  构建请求，跳转至登录页面
                OAuthClientRequest oAuthClientRequest = OAuthClientRequest
                        .authorizationLocation(OAuthUtils.getRequestPrefix(request) + "/login.html")
                        .setClientId(OAuthConstants.CLIENT_ID)
                        .setRedirectURI(OAuthConstants.REDIRECT_URI)
                        .setResponseType(OAuthConstants.CODE)
                        .buildQueryMessage();
                log.info("跳转至登录页面:" + oAuthClientRequest.getLocationUri());
                response.sendRedirect(oAuthClientRequest.getLocationUri());
            }

        } catch (OAuthProblemException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OAuthSystemException e) {
            e.printStackTrace();
        } catch (RuntimeException e){
            ResponseWriter.writer(response, resultMessage.toString());
        }
    }

    /**
     * 客户端信息校验
     *
     * @param request
     * @return
     * @throws OAuthProblemException
     * @throws OAuthSystemException
     */
    private ResultMessage validateClient(HttpServletRequest request) throws OAuthProblemException, OAuthSystemException {
        ResultMessage resultMessage = new ResultMessage();
        OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
        String clientId = oauthRequest.getClientId();
        String redirectUri = oauthRequest.getRedirectURI();
        if (!OAuthConstants.CLIENT_ID.equals(clientId)) {
            resultMessage.errorAppend("client_id:不能为空");
        }
        if (!OAuthConstants.REDIRECT_URI.equals(redirectUri)) {
            resultMessage.errorAppend("redirect_uri:不能为空");
        }
        String requestPrefix = OAuthUtils.getRequestPrefix(request);
        if (!OAuthConstants.REDIRECT_URI.startsWith(requestPrefix)) {
            resultMessage.errorAppend("redirect_uri:域与注册时不符");
        }
        return resultMessage;
    }

    @RequestMapping(value = "/accessToken", method = RequestMethod.POST)
    public void accessToken(HttpServletRequest request, HttpServletResponse response) {
        ResultMessage resultMessage = new ResultMessage();
        try {
            resultMessage = validateClient(request);
            if(resultMessage.getCode() == 0){
                throw new RuntimeException(resultMessage.toString());
            }
            // TODO 校验CODE存在和销毁
            String code = request.getParameter("code");
            // 通过code转换token
            OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
            String accessToken = oauthIssuerImpl.accessToken();
            String refreshToken = oauthIssuerImpl.refreshToken();

            // 构建相应
            OAuthResponse oauthResponse = null;

            oauthResponse = OAuthASResponse
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
        } catch (OAuthSystemException e) {
            e.printStackTrace();
        } catch (OAuthProblemException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            ResponseWriter.writer(response, resultMessage.toString());
        }
    }
}
