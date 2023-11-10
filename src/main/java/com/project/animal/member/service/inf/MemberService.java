package com.project.animal.member.service.inf;

import com.project.animal.member.dto.SignupFormDto;

public interface MemberService {

    void save(SignupFormDto signupFormDto);

    void createMailToken(String email) ;

    void checkMailToken(String email, String token);
}
