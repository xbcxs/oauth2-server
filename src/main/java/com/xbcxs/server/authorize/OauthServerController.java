package com.xbcxs.server.authorize;

import com.alibaba.fastjson.JSONObject;
import com.xbcxs.common.HttpResult;
import com.xbcxs.common.OauthConstants;
import com.xbcxs.common.OauthUtils;
import com.xbcxs.common.ResponseWriter;
import com.xbcxs.server.authorize.Exception.AuthorizeException;
import com.xbcxs.server.login.AuthLoginService;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
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

/**
 * 授权认证服务控制层
 *
 * @author xiaosh
 * @date 2019/8/13
 */
@RestController
@RequestMapping("server")
public class OauthServerController {

    private Logger log = LoggerFactory.getLogger(OauthServerController.class);

    @Autowired
    CodeService codeServiceImpl;

    @Autowired
    AccessTokenService accessTokenService;

    @Autowired
    AuthLoginService authLoginServiceImpl;

    @RequestMapping("loginAuthorize")
    public void loginAuthorize(HttpServletRequest request, HttpServletResponse response) {
        JSONObject validateResult = null;
        try {
            validateResult = authorizeRequestValidate(request);
            if (validateResult.size() > 0) {
                throw new AuthorizeException(validateResult.toString());
            }
            //  构建请求至登录页面
            OAuthClientRequest oAuthClientRequest = OAuthClientRequest
                    .authorizationLocation(OauthUtils.getRequestPrefix(request) + "/views/user/login.html")
                    .setClientId(OauthConstants.CLIENT_ID)
                    .setRedirectURI(OauthConstants.CLIENT_REDIRECT_URI)
                    .setResponseType(OauthConstants.CODE)
                    .buildQueryMessage();
            log.debug("sendRedirect登录页面:{}", oAuthClientRequest.getLocationUri());
            response.sendRedirect(oAuthClientRequest.getLocationUri());
        } catch (OAuthProblemException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OAuthSystemException e) {
            e.printStackTrace();
        } catch (AuthorizeException e) {
            ResponseWriter.writer(response, HttpResult.error(validateResult));
        }
    }

    @RequestMapping("authorize")
    public void authorize(HttpServletRequest request, HttpServletResponse response) {
        JSONObject resultMessage = null;
        try {
            resultMessage = authorizeRequestValidate(request);
            if (resultMessage.size() > 0) {
                throw new AuthorizeException(resultMessage.toString());
            }
            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
            String redirectUri = oauthRequest.getRedirectURI();
            String code = codeServiceImpl.generateCode("userId", oauthRequest.getClientId());
            // 构建OAuth响应
            OAuthResponse oauthResponse = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND)
                    .setCode(code)
                    .location(redirectUri)
                    .buildQueryMessage();
            log.info("sendRedirect客户端请求:{}", oauthResponse.getLocationUri());
            response.sendRedirect(oauthResponse.getLocationUri());
        } catch (OAuthProblemException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OAuthSystemException e) {
            e.printStackTrace();
        } catch (AuthorizeException e) {
            ResponseWriter.writer(response, HttpResult.error(resultMessage));
        }
    }

    @RequestMapping(value = "accessToken", method = RequestMethod.POST)
    public void accessToken(HttpServletRequest request, HttpServletResponse response) {
        JSONObject resultMessage;
        try {
            resultMessage = tokenRequestValidate(request);
            if (resultMessage.size() > 0) {
                throw new AuthorizeException(resultMessage.toString());
            }
            String code = request.getParameter("code");
            if (!codeServiceImpl.isExist(code)) {
                throw new AuthorizeException("code无效！");
            }
            // 获取accessToken并销毁code
            AuthToken authToken = accessTokenService.generateAccessToken(code);
            // 构建响应
            OAuthResponse oauthResponse = OAuthASResponse
                    .tokenResponse(HttpServletResponse.SC_OK)
                    .setAccessToken(authToken.getAccessToken())
                    .setExpiresIn(String.valueOf(authToken.getAccessTokenExpire()))
                    .setRefreshToken(authToken.getRefreshToken())
                    .setParam("authUserId", authToken.getAuthUserId())
                    .buildJSONMessage();
            response.setStatus(oauthResponse.getResponseStatus());
            log.info("oauthResponse.getBody():{}", oauthResponse.getBody());
            ResponseWriter.writer(response, oauthResponse.getBody());
        } catch (OAuthSystemException e) {
            e.printStackTrace();
        } catch (OAuthProblemException e) {
            e.printStackTrace();
        } catch (AuthorizeException e) {
            ResponseWriter.writer(response, HttpResult.error(e.getMessage()));
        }
    }

    @RequestMapping(value = "refreshToken", method = RequestMethod.POST)
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        // TODO
    }

    /**
     * 客户端code请求信息验证
     *
     * @param request
     * @return
     * @throws OAuthProblemException
     * @throws OAuthSystemException
     */
    private JSONObject authorizeRequestValidate(HttpServletRequest request) throws OAuthProblemException, OAuthSystemException {
        OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
        String clientId = oauthRequest.getClientId();
        String redirectUri = oauthRequest.getRedirectURI();
        return getResultMessage(request, clientId, redirectUri);
    }

    /**
     * 客户端token请求信息验证
     *
     * @param request
     * @return
     * @throws OAuthProblemException
     * @throws OAuthSystemException
     */
    private JSONObject tokenRequestValidate(HttpServletRequest request) throws OAuthProblemException, OAuthSystemException {
        OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);
        String clientId = oauthRequest.getClientId();
        String redirectUri = oauthRequest.getRedirectURI();
        return getResultMessage(request, clientId, redirectUri);
    }

    /**
     * 请求信息验证
     *
     * @param request
     * @param clientId
     * @param redirectUri
     * @return
     */
    private JSONObject getResultMessage(HttpServletRequest request, String clientId, String redirectUri) {
        JSONObject resultMessage = new JSONObject();
        if (!OauthConstants.CLIENT_ID.equals(clientId)) {
            resultMessage.put("client_id", "不能为空");
        }
        if (!OauthConstants.CLIENT_REDIRECT_URI.equals(redirectUri)) {
            resultMessage.put("redirect_uri", "不能为空");
        }
        String requestPrefix = OauthUtils.getRequestPrefix(request);
        if (!OauthConstants.CLIENT_REDIRECT_URI.startsWith(requestPrefix)) {
            resultMessage.put("redirect_uri", "域与注册时不符");
        }
        return resultMessage;
    }

}
