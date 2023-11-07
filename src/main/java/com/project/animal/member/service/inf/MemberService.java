package com.project.animal.member.service.inf;

import com.project.animal.member.dto.MemberFormDto;

public interface MemberService {
    void save(MemberFormDto memberFormDto);

    void createMailToken(String email);

    void checkMailToken(String email, String token);
}
