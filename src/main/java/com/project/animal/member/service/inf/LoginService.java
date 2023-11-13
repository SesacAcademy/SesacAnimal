package com.project.animal.member.service.inf;

import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.member.dto.LoginFormDto;
import com.project.animal.member.dto.TokenDto;
import javax.servlet.http.HttpServletResponse;

public interface LoginService {

    TokenDto login(LoginFormDto loginFormDto);

    void logout(MemberDto memberDto, HttpServletResponse response);
}
