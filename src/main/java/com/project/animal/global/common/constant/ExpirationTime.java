package com.project.animal.global.common.constant;

public abstract class ExpirationTime {

    // JWT 토큰 --> 쿠키와 달리 JWT 토큰 만료시간은 1분을 추가로 더해준다. (ExpiredJwtException 예외 방지용)
    public static final Long ACCESS_TOKEN_EXPIRATION_TIME = 1000 * 60 * 3L;           // (밀리 초 단위 / 총 2분)
    public static final Long REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 11L;         // (밀리 초 단위 / 총 10분)

    // HTTP 쿠키
    public static final Long ACCESS_TOKEN_COOKIE_EXPIRATION_TIME = 2L;                // 분 단위
    public static final Long REFRESH_TOKEN_COOKIE_EXPIRATION_TIME = 10L;              // 분 단위

    // Redis - Mail 토큰
    public static final Long REDIS_MAIL_TOKEN_TIMEOUT = 180L;                         // 초 단위
}
