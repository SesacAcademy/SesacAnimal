package com.project.animal.member.service.inf;

import com.project.animal.member.domain.Member;
import com.project.animal.member.dto.FindMemberEmailFormDto;
import com.project.animal.member.dto.SignupFormDto;

public interface MemberService {

    void save(SignupFormDto signupFormDto);

    void createMailAuthCode(String email) ;

    void checkMailAuthCode(String email, String authCode);

    Member findEmail(FindMemberEmailFormDto memberEmailFormDto);
}
