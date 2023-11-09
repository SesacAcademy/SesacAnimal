package com.project.animal.member.exception;


public class InvalidCodeException extends RuntimeException {

    public InvalidCodeException() {
        super();
    }

    public InvalidCodeException(String message) {
        super(message);
    }

    public InvalidCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCodeException(Throwable cause) {
        super(cause);
    }

    protected InvalidCodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
