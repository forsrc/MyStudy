package com.forsrc.exception;


public class TokenException extends ActionException {


    private static final long serialVersionUID = 3318372870167621933L;

    public TokenException() {

        super();
    }

    public TokenException(String message) {

        super(message);
    }

    public TokenException(String message, Throwable cause) {

        super(message, cause);
    }

    public TokenException(Throwable cause) {

        super(cause);
    }

}
