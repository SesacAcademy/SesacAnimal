package com.project.animal.member.service.inf;

public interface MemberService {
    void createMailToken(String email);

    void checkMailToken(String email, String token);
}
