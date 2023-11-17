package com.project.animal.member.service.inf;

import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.member.domain.Member;

public interface MyPageService {

    public Member getMember(MemberDto memberDto);
}
