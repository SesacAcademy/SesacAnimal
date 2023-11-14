package com.project.animal.global.common.constant;

public abstract class ExpirationTime {

    // JWT 토큰
    public static final Long ACCESS_TOKEN_EXPIRATION_TIME = 1000 * 60 * 10L;           // 밀리 초 단위
    public static final Long REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 72L;     // 밀리 초 단위

    // HTTP 쿠키
    public static final Long ACCESS_TOKEN_COOKIE_EXPIRATION_TIME = 10L;                // 분 단위
    public static final Long REFRESH_TOKEN_COOKIE_EXPIRATION_TIME = 60L * 72;          // 분 단위

    // Redis - Mail 인증
    public static final Long REDIS_MAIL_AUTHCODE_TIMEOUT = 180L;                       // 초 단위
    
    // Redis - SMS 인증
    public static final Long REDIS_SMS_AUTHCODE_TIMEOUT = 180L;                        // 초 단위
}
