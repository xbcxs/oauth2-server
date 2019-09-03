package com.xbcxs.server.authorize;

import com.xbcxs.common.OAuthConstants;
import com.xbcxs.common.OAuthUtils;
import com.xbcxs.common.ResponseWriter;
import com.xbcxs.common.ResultMessage;
import com.xbcxs.server.login.LoginService;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 授权认证服务控制层
 * @author xiaosh
 * @date 2019/8/13
 */
@RestController
@RequestMapping("auth")
public class OAuthServerController {

    Logger log = LoggerFactory.getLogger(OAuthServerController.class);

    @Resource
    private CacheManager cacheManager;

    @Autowired
    CodeService codeServiceImpl;

    @Autowired
    AccessTokenService accessTokenService;

    @Autowired
    LoginService loginServiceImpl;

    @RequestMapping("/authorize")
    public void authorize(HttpServletRequest request, HttpServletResponse response) {
        ResultMessage resultMessage = new ResultMessage();
        try {
            // 客户端信息校验
            resultMessage = authzRequestValidate(request);
            if (resultMessage.getCode() == 0) {
                throw new RuntimeException(resultMessage.toString());
            }
            // 校验是否已经登录
            String loginToken = request.getParameter("");
            if (loginServiceImpl.validate(loginToken)) {
                OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
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
                response.sendRedirect(oauthResponse.getLocationUri());
            } else {
                //  构建请求，跳转至登录页面
                OAuthClientRequest oAuthClientRequest = OAuthClientRequest
                        .authorizationLocation(OAuthUtils.getRequestPrefix(request) + "/views/user/login.html")
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

    @RequestMapping(value = "/accessToken", method = RequestMethod.POST)
    public void accessToken(HttpServletRequest request, HttpServletResponse response) {
        ResultMessage resultMessage = new ResultMessage();
        try {
            resultMessage = tokenRequestValidate(request);
            if(resultMessage.getCode() == 0){
                throw new RuntimeException(resultMessage.toString());
            }
            // 校验CODE是否有效
            String code = request.getParameter("code");
            boolean codeFlag = codeServiceImpl.isExist(code);
            if(!codeFlag){
                resultMessage.errorAppend("code无效！");
                throw new RuntimeException(resultMessage.toString());
            }
            // 获取accessToken并销毁code
            AccessToken accessTokenObj = accessTokenService.generateAccessToken(code);
            // 构建相应
            OAuthResponse oauthResponse = OAuthASResponse
                    .tokenResponse(HttpServletResponse.SC_OK)
                    .setAccessToken(accessTokenObj.getAccessToken())
                    .setExpiresIn(String.valueOf(accessTokenObj.getAccessTokenExpire()))
                    .setRefreshToken(accessTokenObj.getRefreshToken())
                    .buildJSONMessage();
            response.setStatus(oauthResponse.getResponseStatus());
            log.info(">>>>>>>>>>>>>>>>>>>oauthResponse.getBody():" + oauthResponse.getBody());
            ResponseWriter.writer(response, oauthResponse.getBody());

        } catch (OAuthSystemException e) {
            e.printStackTrace();
        } catch (OAuthProblemException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            ResponseWriter.writer(response, resultMessage.toString());
        }
    }

    /**
     * 客户端code请求信息验证
     * @param request
     * @return
     * @throws OAuthProblemException
     * @throws OAuthSystemException
     */
    private ResultMessage authzRequestValidate(HttpServletRequest request) throws OAuthProblemException, OAuthSystemException {
        OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
        String clientId = oauthRequest.getClientId();
        String redirectUri = oauthRequest.getRedirectURI();
        return getResultMessage(request, clientId, redirectUri);
    }

    /**
     * 客户端token请求信息验证
     * @param request
     * @return
     * @throws OAuthProblemException
     * @throws OAuthSystemException
     */
    private ResultMessage tokenRequestValidate(HttpServletRequest request) throws OAuthProblemException, OAuthSystemException {
        OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);
        String clientId = oauthRequest.getClientId();
        String redirectUri = oauthRequest.getRedirectURI();
        return getResultMessage(request, clientId, redirectUri);
    }

    /**
     * 请求信息验证
     * @param request
     * @param clientId
     * @param redirectUri
     * @return
     */
    private ResultMessage getResultMessage(HttpServletRequest request, String clientId, String redirectUri) {
        ResultMessage resultMessage = new ResultMessage();
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

}
