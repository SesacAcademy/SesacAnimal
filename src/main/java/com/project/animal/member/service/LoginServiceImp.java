package com.project.animal.member.service;

import com.project.animal.global.common.constant.TokenType;
import com.project.animal.global.common.provider.JwtTokenProvider;
import com.project.animal.member.domain.Member;
import com.project.animal.member.dto.LoginFormDto;
import com.project.animal.member.dto.TokenDto;
import com.project.animal.member.exception.LoginException;
import com.project.animal.member.repository.MemberRepository;
import com.project.animal.member.service.inf.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImp implements LoginService {

    private final MemberRepository memberRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder encoder;

    @Override
    public TokenDto login(LoginFormDto loginFormDto) {
        // 회원 조회
        Optional<Member> findMember = memberRepository.findByEmail(loginFormDto.getEmail());
        log.info("LoginService - 회원 조회 {}", loginFormDto.getEmail());

        // 회원이 존재하지 않는 경우, 예외 발생
        findMember.orElseThrow(() -> {
            throw new LoginException("아이디(" + loginFormDto.getEmail() + ") 로그인 실패");
        });

        // 비밀번호 비교
        Member member = findMember.get();
        boolean matchPassword = encoder.matches(loginFormDto.getPassword(), member.getPassword());
        log.info("비밀번호 비교 결과 : {}", matchPassword);

        // 비밀번호 잘못 입력했을 경우, 예외 발생
        if (!matchPassword) {
            throw new LoginException("아이디(" + loginFormDto.getEmail() + ") 로그인 실패");
        }

        // Access 및 Refresh 토큰 생성 + Redis에 Refresh Token 저장
        String accessToken = jwtTokenProvider.createAccessToken(member);
        String refreshToken = jwtTokenProvider.createRefreshToken(member);

        return new TokenDto(accessToken, refreshToken);
    }
}
