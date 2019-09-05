package com.xbcxs.resource;

import com.alibaba.fastjson.JSONObject;
import com.xbcxs.common.ResponseWriter;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.apache.oltu.oauth2.rs.response.OAuthRSResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xiaosh on 2019/8/7.
 */
@RestController
@RequestMapping("resource")
public class ResourceController {

    private Logger log = LoggerFactory.getLogger(ResourceController.class);

    @RequestMapping("getUserToken")
    public void getUserToken(HttpServletRequest request, HttpServletResponse response) {

        try {
            // Make the OAuth Request out of this request and validate it
            // Specify where you expect OAuth access token (request header, body or query string)
            OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(request, ParameterStyle.QUERY);

            // Get the access token
            String accessToken = oauthRequest.getAccessToken();
            String authUserId = request.getHeader("authUserId");
            log.debug("accessToken:{},authUserId{}", accessToken, authUserId);
            //... validate access token
            JSONObject userObject = new JSONObject();
            userObject.put("userId", "xxxxxx");
            userObject.put("userName", "yyyyy");
            ResponseWriter.writer(response, userObject.toString());

            //if something goes wrong
        } catch (OAuthProblemException ex) {
            //build error response
            OAuthResponse oauthResponse = null;
            try {
                oauthResponse = OAuthRSResponse
                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                        .setRealm("Album Example")
                        .buildHeaderMessage();
                response.addDateHeader(OAuth.HeaderType.WWW_AUTHENTICATE, Long.parseLong(oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE)));
            } catch (OAuthSystemException e) {
                log.error("OAuthSystemException", e);
            }
        } catch (OAuthSystemException e) {
            log.error("OAuthSystemException", e);
        }

    }


}
