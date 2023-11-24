package com.project.animal.global.common.constant;

public abstract class EndPoint {
    // 입양 ADOPTION 영역
    public static final String ADOPTION_LIST = "/v1/adoption"; // 입양 게시판 메인 리스트
    public static final String ADOPTION_WRITE = "/v1/adoption/edit"; // 글쓰기 영역
    public static final String ADOPTION_EDIT = "/v1/adoption/edit/{id}"; // 수정 영역
    public static final String ADOPTION_READ = "/v1/adoption/{id}"; // 글 읽기 영역

    public static final String ADOPTION_COMMENT = "/v1/adoption/comment"; // 댓글 영역 (읽기 게시판 아래)

    // To do : EndPoint 클래스 삭제 + 컨트롤러 URL 수정
}
