package com.xbcxs.server.login;

import com.alibaba.fastjson.JSONObject;
import com.xbcxs.common.HttpResult;
import com.xbcxs.common.ResponseWriter;
import com.xbcxs.server.authorize.CodeService;
import com.xbcxs.server.authorize.Exception.AuthorizeException;
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
 * 登录控制层实现
 * @author xiaosh
 * @date 2019/9/5
 */
@RestController
public class AuthLoginController {

    private Logger log = LoggerFactory.getLogger(AuthLoginController.class);

    @Autowired
    CodeService codeServiceImpl;

    @Autowired
    AuthLoginService authLoginService;

    /**
     * 模拟客户端对授权（码）服务发起请求
     *
     * @param request
     * @param response
     */
    @RequestMapping("oauthLogin")
    public void oauthLogin(HttpServletRequest request, HttpServletResponse response) {
        String clientId = request.getParameter("clientId");
        String loginName = request.getParameter("loginName");
        String password = request.getParameter("password");
        OAuthAuthzRequest oauthRequest;
        String code;
        String redirectUri;
        OAuthResponse oauthResponse = null;
        try {
            oauthRequest = new OAuthAuthzRequest(request);
            redirectUri = oauthRequest.getRedirectURI();
            code = authLoginService.authLogin(clientId, loginName, password);
            oauthResponse = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND)
                    .setCode(code)
                    .location(redirectUri)
                    .buildQueryMessage();
            // 构建OAuth响应
            JSONObject jsonData = new JSONObject();
            jsonData.put("redirectUri", oauthResponse.getLocationUri());
            log.debug("登录成功后回调:{}" ,oauthResponse.getLocationUri());
            ResponseWriter.writer(response, HttpResult.success(jsonData));
        } catch (AuthorizeException e){
            ResponseWriter.writer(response, HttpResult.error("账号密码不正确"));
        } catch (OAuthSystemException e) {
            e.printStackTrace();
        } catch (OAuthProblemException e) {
            e.printStackTrace();
        }
    }
}
