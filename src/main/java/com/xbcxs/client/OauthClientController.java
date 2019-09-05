package com.xbcxs.client;

import com.alibaba.fastjson.JSONObject;
import com.xbcxs.common.OauthConstants;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xiaosh
 * @date 2019/7/29
 */
@RestController
@RequestMapping("client")
public class OauthClientController {

    private Logger log = LoggerFactory.getLogger(OauthClientController.class);

    /**
     * 模拟客户端对授权（码）服务发起请求
     * @param response
     */
    @RequestMapping("requestAuthorize")
    public void requestAuthorize(HttpServletResponse response) {
        try {
            OAuthClientRequest oAuthClientRequest = OAuthClientRequest
                    .authorizationLocation(OauthConstants.SERVER_LOGIN_AUTHORIZE_LOCATION)
                    .setClientId(OauthConstants.CLIENT_ID)
                    .setRedirectURI(OauthConstants.CLIENT_REDIRECT_URI)
                    .setResponseType(OauthConstants.CODE)
                    .buildQueryMessage();
            log.info("sendRedirect授权服务器:{}", oAuthClientRequest.getLocationUri());
            response.sendRedirect(oAuthClientRequest.getLocationUri());
        } catch (OAuthSystemException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("callbackAccessToken")
    public void callbackAccessToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            OAuthAuthzResponse oAuthAuthzResponse = OAuthAuthzResponse.oauthCodeAuthzResponse(request);
            String code = oAuthAuthzResponse.getCode();
            // 构建请求信息，请求token
            OAuthClientRequest oauthClientRequest = OAuthClientRequest
                    .tokenLocation(OauthConstants.SERVER_TOKEN_LOCATION)
                    .setGrantType(GrantType.AUTHORIZATION_CODE)
                    .setClientId(OauthConstants.CLIENT_ID)
                    .setClientSecret(OauthConstants.CLIENT_SECRET)
                    .setRedirectURI(OauthConstants.CLIENT_REDIRECT_URI)
                    .setCode(code)
                    .buildQueryMessage();

            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
            OAuthAccessTokenResponse oAuthResponse = oAuthClient.accessToken(oauthClientRequest, RequestMethod.POST.toString());
            String accessToken = oAuthResponse.getAccessToken();
            String authUserId = oAuthResponse.getParam("authUserId");
            log.debug("authUserId:{},accessToken:{},expiresIn:{},refreshToken:{},scope:{}", authUserId, accessToken, oAuthResponse.getExpiresIn(), oAuthResponse.getRefreshToken(), oAuthResponse.getScope());
            // 获取资源服务器的资源
            getAuthToken(accessToken, authUserId);
            // 根据获得的资源处理第三方应用的后续业务（例如获取用户信息）
            // ...
            // 跳转客户端（第三方）应用
            response.sendRedirect("https://www.baidu.com/");
        } catch (OAuthSystemException e) {
            e.printStackTrace();
        } catch (OAuthProblemException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取资源服务器资源
     * @param accessToken
     * @param authUserId
     * @return
     * @throws OAuthSystemException
     * @throws OAuthProblemException
     */
    private JSONObject getAuthToken(String accessToken, String authUserId) throws OAuthSystemException, OAuthProblemException {
        OAuthClient oAuthClient1 = new OAuthClient(new URLConnectionClient());
        OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest(OauthConstants.RESOURCE_REQUEST)
                .setAccessToken(accessToken).buildQueryMessage();
        bearerClientRequest.setHeader("authUserId", authUserId);
        OAuthResourceResponse resourceResponse = oAuthClient1.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
        JSONObject result = JSONObject.parseObject(resourceResponse.getBody());
        log.info("resourceResponse.getBody():{}", result);
        return result;
    }
}
