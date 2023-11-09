package com.project.animal.member.service.inf;

import com.project.animal.member.dto.LoginFormDto;
import com.project.animal.member.dto.TokenDto;

public interface LoginService {

    TokenDto login(LoginFormDto loginFormDto);
}
