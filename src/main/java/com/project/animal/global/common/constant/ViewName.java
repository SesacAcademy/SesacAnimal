package com.project.animal.global.common.constant;

public abstract class ViewName {

    // Home 관련
    public static final String INDEX_VIEW = "index";

    // Member 관련
    public static final String MEMBER_PREFIX = "member";
    public static final String LOGIN_VIEW = MEMBER_PREFIX + "/login";
    public static final String SIGNUP_VIEW = MEMBER_PREFIX + "/signup";

    // MyPage 관련
    public static final String MYPAGE_VIEW = MEMBER_PREFIX + "/mypage_info";
    public static final String CHANGE_PASSWORD_VIEW = MEMBER_PREFIX + "/mypage_password";
    public static final String MYPAGE_WISH_LIST = MEMBER_PREFIX + "/mypage_wishList";
    public static final String MYPAGE_BOARD_LIST = MEMBER_PREFIX + "/mypage_boardList";
    public static final String MYPAGE_ADOPTION_LIST = MEMBER_PREFIX + "/mypage_adoptionList";


    public static final String ADOPTION_PREFIX = "adoption";
    public static final String ADOPTION_LIST = ADOPTION_PREFIX +"/adoption_list"; // 입양 게시판 메인 리스트
    public static final String ADOPTION_WRITE = ADOPTION_PREFIX + "/adoption_write";
    public static final String ADOPTION_EDIT = ADOPTION_PREFIX + "/adoption_edit";
    public static final String ADOPTION_READ = ADOPTION_PREFIX + "/adoption_read";

}
