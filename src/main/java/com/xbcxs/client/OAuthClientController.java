package com.xbcxs.client;

import com.xbcxs.common.OAuthConstants;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
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
 * Created by xiaosh on 2019/7/31.
 */
@RestController
@RequestMapping("client")
public class OAuthClientController {

    private Logger log = LoggerFactory.getLogger(OAuthClientController.class);

    /**
     * 模拟客户端对授权（码）服务发起请求
     *
     * @param request
     * @param response
     */
    @RequestMapping("/requestAuth")
    public void requestAuth(HttpServletRequest request, HttpServletResponse response){
        try {
            OAuthClientRequest oAuthClientRequest = OAuthClientRequest
                    .authorizationLocation("http://localhost:8888/as/auth/authorize")
                    .setClientId(OAuthConstants.CLIENT_ID )
                    .setRedirectURI(OAuthConstants.REDIRECT_URI)
                    .setResponseType(OAuthConstants.CODE)
                    .buildQueryMessage();
            log.info("向授权服务器发起请求:" + oAuthClientRequest.getLocationUri());
            response.sendRedirect(oAuthClientRequest.getLocationUri());
        } catch (OAuthSystemException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/getToken")
    public void getToken(HttpServletRequest request, HttpServletResponse response) throws OAuthProblemException, OAuthSystemException, IOException {
        // TODO 不知道是否对CODE有过期处理机制
        OAuthAuthzResponse oAuthAuthzResponse = OAuthAuthzResponse.oauthCodeAuthzResponse(request);
        String code = oAuthAuthzResponse.getCode();

        // TODO 验证state防止其他人CODE获取权限
        // 构建客户端请求信息
        OAuthClientRequest oauthClientRequest = OAuthClientRequest
                .tokenLocation("http://localhost:8888/as/auth/accessToken")
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                .setClientId(OAuthConstants.CLIENT_ID )
                .setClientSecret(OAuthConstants.CLIENT_SECRET)
                .setRedirectURI(OAuthConstants.REDIRECT_URI)
                .setCode(code)
                .buildQueryMessage();
        // 发起token请求
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        OAuthAccessTokenResponse oAuthResponse = oAuthClient.accessToken(oauthClientRequest, RequestMethod.POST.toString());

        String accessToken = oAuthResponse.getAccessToken();
        String refreshToken = oAuthResponse.getRefreshToken();
        String scope = oAuthResponse.getScope();
        Long expiresIn = oAuthResponse.getExpiresIn();
        // TODO 发起API接口请求
        log.info("》》》》》》accessToken:" + accessToken);
        log.info("》》》》》》expiresIn:" + expiresIn);
        log.info("》》》》》》refreshToken:" + refreshToken);
        log.info("》》》》》》scope:" + scope);

        // TODO 通过TOKEN获取人员信息
        // TODO 用这个人在第三方系统初始化账号并且登录，然后进入系统

    }

    @RequestMapping("getUserInfo")
    public void getUserInfo(HttpServletRequest req, HttpServletResponse resq){
        log.info("【getUserInfo】：......");
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        // GitHubTokenResponse oAuthResponse = oAuthClient.accessToken(req);

        // String accessToken = oAuthResponse.getAccessToken();

    }
}
