package com.project.animal.member.exception;

public class NestedNicknameException extends RuntimeException{

    public NestedNicknameException() {
        super();
    }

    public NestedNicknameException(String message) {
        super(message);
    }

    public NestedNicknameException(String message, Throwable cause) {
        super(message, cause);
    }

    public NestedNicknameException(Throwable cause) {
        super(cause);
    }

    protected NestedNicknameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
