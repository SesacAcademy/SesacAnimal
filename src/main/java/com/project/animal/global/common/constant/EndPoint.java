package com.project.animal.global.common.constant;

public abstract class EndPoint {
    // Member 관련
    public static final String LOGIN = "/v1/auth/login";
    public static final String LOGIN_API = "/v1/api/auth/login";
    public static final String KAKAO_LOGIN = "/v1/auth/login/kakao";
    public static final String LOGOUT = "/v1/auth/logout";
    public static final String SIGNUP = "/v1/auth/signup";
    public static final String SIGNUP_API = "/v1/api/auth/signup";
    public static final String EMAIL_API = "/v1/api/auth/email";
    public static final String PHONE_API = "/v1/api/auth/phone";
    public static final String FIND_EMAIL_API = "/v1/api/find/email";

    // OAuth2 (KaKao) 관련
    public static final String KAKAO_REDIRECT_URI = "http://localhost:8080/v1/auth/login/kakao";
    public static final String KAKAO_TOKEN_URI = "https://kauth.kakao.com/oauth/token";
    public static final String KAKAO_USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";


    
    // 입양 ADOPTION 영역
    public static final String ADOPTION_LIST = "/v1/adoption"; // 입양 게시판 메인 리스트
    public static final String ADOPTION_WRITE = "/v1/adoption/edit"; // 글쓰기 영역
    public static final String ADOPTION_EDIT = "/v1/adoption/edit/{id}"; // 수정 영역
    public static final String ADOPTION_READ = "/v1/adoption/{id}"; // 글 읽기 영역

    public static final String ADOPTION_COMMENT = "/v1/adoption/comment"; // 댓글 영역 (읽기 게시판 아래)

}
