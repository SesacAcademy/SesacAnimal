package com.project.animal.member.service;

import com.project.animal.global.common.provider.MailTokenProvider;
import com.project.animal.member.exception.InvalidTokenException;
import com.project.animal.member.exception.NestedEmailException;
import com.project.animal.member.repository.MemberRepository;
import com.project.animal.member.service.inf.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final MailTokenProvider mailTokenProvider;

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