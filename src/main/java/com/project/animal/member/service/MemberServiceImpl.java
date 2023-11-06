package com.project.animal.member.service;

import com.project.animal.global.common.provider.MailTokenProvider;
import com.project.animal.member.domain.Member;
import com.project.animal.member.dto.MemberFormDto;
import com.project.animal.member.exception.InvalidTokenException;
import com.project.animal.member.exception.NestedEmailException;
import com.project.animal.member.repository.MemberRepository;
import com.project.animal.member.service.inf.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final MailTokenProvider mailTokenProvider;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

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
                .password(bCryptPasswordEncoder.encode(memberFormDto.getPassword()))
                .phone(memberFormDto.getPhone())
                .type("MAIL")
                .grade(1)
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
        mailTokenProvider.createToken(email);
    }

    @Override
    public void checkMailToken(String email, String token) {
        // 이메일 토큰 체크
        if(!mailTokenProvider.validateToken(email, token)) {
            throw new InvalidTokenException("유효하지 않은 인증번호입니다.");
        }
    }

    private void checkNestedEmail(String email) {
        memberRepository.findByEmail(email)
                .ifPresent((x) -> {
                    throw new NestedEmailException("중복된 이메일입니다.");
                });
    }
}