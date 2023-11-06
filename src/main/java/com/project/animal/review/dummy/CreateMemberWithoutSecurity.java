package com.project.animal.review.dummy;

import com.project.animal.member.domain.Member;

public class CreateMemberWithoutSecurity {
    public Member createMember(){
        Member member = new Member(1L,"test","pasword", "name");
        return member;
    }
}
