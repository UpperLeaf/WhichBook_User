package com.econovation.whichbook_user.domain.exception;

public class UnAuthorizedException extends RuntimeException{

    public UnAuthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnAuthorizedException(String message) {
        super(message);
    }
}
