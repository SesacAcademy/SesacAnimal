package com.project.animal.member.service;

import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.member.domain.Member;
import com.project.animal.member.exception.NotFoundException;
import com.project.animal.member.repository.MemberRepository;
import com.project.animal.member.service.inf.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPageServiceImp implements MyPageService {

    private final MemberRepository memberRepository;

    @Override
    public Member getMember(MemberDto memberDto) {
        return memberRepository.findById(memberDto.getId()).orElseThrow(() -> {
            throw new NotFoundException();
        });
    }
}
