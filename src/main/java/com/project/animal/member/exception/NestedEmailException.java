package com.project.animal.member.exception;

public class NestedEmailException extends RuntimeException{
    public NestedEmailException() {
        super();
    }

    public NestedEmailException(String message) {
        super(message);
    }

    public NestedEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public NestedEmailException(Throwable cause) {
        super(cause);
    }

    protected NestedEmailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
