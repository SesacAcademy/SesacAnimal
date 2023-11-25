package com.project.animal.member.service.inf;

import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.member.domain.Board;
import com.project.animal.member.domain.Member;
import com.project.animal.member.dto.ChangePasswordFormDto;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface MyPageService {

    Member getMember(MemberDto memberDto);

    void deleteMember(MemberDto memberDto, HttpServletResponse response);

    void changePassword(ChangePasswordFormDto changePasswordFormDto, MemberDto memberDto);

    Long getMyBoardCount(MemberDto memberDto);

    List<Board> getMyBoardList(MemberDto memberDto, int startRow, int boardSize);
}
