package com.project.animal.member.service;

import com.project.animal.global.common.constant.Role;
import com.project.animal.global.common.provider.MailAuthCodeProvider;
import com.project.animal.member.domain.Member;
import com.project.animal.member.dto.MemberFormDto;
import com.project.animal.member.exception.InvalidCodeException;
import com.project.animal.member.exception.NestedEmailException;
import com.project.animal.member.repository.MemberRepository;
import com.project.animal.member.service.inf.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class MemberServiceImp implements MemberService {

    private final MemberRepository memberRepository;

    private final MailAuthCodeProvider mailTokenProvider;

    private final PasswordEncoder encoder;

    @Override
    public void save(MemberFormDto memberFormDto) {
        // 이메일 중복 여부 체크
        checkNestedEmail(memberFormDto.getEmail());

        // 이메일 토큰 체크
        checkMailToken(memberFormDto.getEmail(), memberFormDto.getToken());

        LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("Asia/Tokyo"));

        Member member = Member.builder()
                .email(memberFormDto.getEmail())
                .name(memberFormDto.getName())
                .password(encoder.encode(memberFormDto.getPassword()))
                .phone(memberFormDto.getPhone())
                .type("MAIL")
                .role(Role.ROLE_USER)
                .isActive(1)
                .createdAt(dateTime)
                .updatedAt(dateTime)
                .lastLoginAt(dateTime)
                .build();

        memberRepository.save(member);
    }

    @Override
    @Transactional
    public void createMailToken(String email) {
        // 이메일 중복 여부 체크
        checkNestedEmail(email);

        // 토큰 발급
        mailTokenProvider.generateAuthCode(email);
    }

    @Override
    public void checkMailToken(String email, String token) {
        // 이메일 토큰 체크
        if(!mailTokenProvider.validateAuthCode(email, token)) {
            throw new InvalidCodeException("유효하지 않은 인증번호입니다.");
        }
    }

    private void checkNestedEmail(String email) {
        memberRepository.findByEmail(email)
                .ifPresent((x) -> {
                    throw new NestedEmailException("중복된 이메일입니다.");
                });
    }
}