package com.project.animal.member.exception;

public class NestedPhoneException extends RuntimeException{
    public NestedPhoneException() {
        super();
    }

    public NestedPhoneException(String message) {
        super(message);
    }

    public NestedPhoneException(String message, Throwable cause) {
        super(message, cause);
    }

    public NestedPhoneException(Throwable cause) {
        super(cause);
    }

    protected NestedPhoneException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
