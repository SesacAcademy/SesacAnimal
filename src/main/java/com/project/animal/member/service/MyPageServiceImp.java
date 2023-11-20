package com.project.animal.member.service;

import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.member.domain.Member;
import com.project.animal.member.dto.ChangePasswordFormDto;
import com.project.animal.member.exception.LoginException;
import com.project.animal.member.exception.NotFoundException;
import com.project.animal.member.exception.WrongPasswordException;
import com.project.animal.member.repository.MemberRepository;
import com.project.animal.member.service.inf.LoginService;
import com.project.animal.member.service.inf.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPageServiceImp implements MyPageService {

    private final MemberRepository memberRepository;

    private final LoginService loginService;

    private final PasswordEncoder encoder;

    @Override
    public Member getMember(MemberDto memberDto) {
        return memberRepository.findById(memberDto.getId()).orElseThrow(NotFoundException::new);
    }

    @Override
    @Transactional
    public void deleteMember(MemberDto memberDto, HttpServletResponse response) {
        // Member 객체 조회
        Member findMember = memberRepository.findById(memberDto.getId()).orElseThrow(NotFoundException::new);

        // Member Status 변경 (삭제 대기)
        findMember.setIsActive(0);
        
        // 쿠키 삭제
        loginService.logout(memberDto, response);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordFormDto changePasswordFormDto, MemberDto memberDto) {
        // Member 객체 조회
        Member findMember = memberRepository.findById(memberDto.getId()).orElseThrow(NotFoundException::new);

        // 비밀번호 비교
        boolean matchPassword = encoder.matches(changePasswordFormDto.getOldPassword(), findMember.getPassword());

        // 비밀번호 잘못 입력했을 경우, 예외 발생
        if (!matchPassword) {
            throw new WrongPasswordException("아이디(" + memberDto.getEmail() + ") 비밀번호 변경 실패");
        }

        // 비밀번호 변경
        findMember.setPassword(encoder.encode(changePasswordFormDto.getNewPassword()));
    }
}
