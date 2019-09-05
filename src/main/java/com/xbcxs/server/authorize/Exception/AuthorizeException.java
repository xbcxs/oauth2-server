package com.xbcxs.server.authorize.Exception;

/**
 * @author xiaosh
 * @date 2019/9/4
 */
public class AuthorizeException extends RuntimeException{

    public AuthorizeException(String message) {
        super(message);
    }

    public AuthorizeException() {
        super();
    }
}
