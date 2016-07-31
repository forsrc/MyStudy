package com.forsrc.exception;


/**
 * The type Token exception.
 */
public class TokenException extends ActionException {


    private static final long serialVersionUID = 3318372870167621933L;

    /**
     * Instantiates a new Token exception.
     */
    public TokenException() {

        super();
    }

    /**
     * Instantiates a new Token exception.
     *
     * @param message the message
     */
    public TokenException(String message) {

        super(message);
    }

    /**
     * Instantiates a new Token exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public TokenException(String message, Throwable cause) {

        super(message, cause);
    }

    /**
     * Instantiates a new Token exception.
     *
     * @param cause the cause
     */
    public TokenException(Throwable cause) {

        super(cause);
    }

}
