package com.xbcxs.resource;

import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.apache.oltu.oauth2.rs.response.OAuthRSResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xiaosh on 2019/8/7.
 */
@RestController
@RequestMapping("user")
public class UserController {

    @RequestMapping("getUser")
    public void getUserInfo(HttpServletRequest request, HttpServletResponse response) {

        try {
            // Make the OAuth Request out of this request and validate it
            // Specify where you expect OAuth access token (request header, body or query string)
            OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(request, ParameterStyle.HEADER);

            // Get the access token
            String accessToken = oauthRequest.getAccessToken();

            //... validate access token

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
                e.printStackTrace();
            }
        } catch (OAuthSystemException e) {
            e.printStackTrace();
        }

    }


}
