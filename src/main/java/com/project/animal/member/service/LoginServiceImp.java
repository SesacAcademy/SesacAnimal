package com.project.animal.member.service;

import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.global.common.provider.JwtTokenProvider;
import com.project.animal.member.domain.Member;
import com.project.animal.member.dto.LoginFormDto;
import com.project.animal.member.dto.TokenDto;
import com.project.animal.member.exception.LoginException;
import com.project.animal.member.repository.MemberRepository;
import com.project.animal.member.service.inf.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.project.animal.global.common.constant.TokenTypeValue.JWT_ACCESS_TOKEN;
import static com.project.animal.global.common.constant.TokenTypeValue.JWT_REFRESH_TOKEN;

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
        Member member = findMember.orElseThrow(() -> new LoginException("아이디(" + loginFormDto.getEmail() + ") 로그인 실패"));

        // 비밀번호 비교
        boolean matchPassword = encoder.matches(loginFormDto.getPassword(), member.getPassword());
        log.info("비밀번호 비교 결과 : {}", matchPassword);

        // 비밀번호 잘못 입력했을 경우, 예외 발생
        if (!matchPassword) {
            throw new LoginException("아이디(" + loginFormDto.getEmail() + ") 로그인 실패");
        }

        MemberDto memberDto = new MemberDto(member);

        // Access 및 Refresh 토큰 생성 + Redis에 Refresh Token 저장
        String accessToken = jwtTokenProvider.generateToken(memberDto, JWT_ACCESS_TOKEN);
        String refreshToken = jwtTokenProvider.generateToken(memberDto, JWT_REFRESH_TOKEN);

        return new TokenDto(accessToken, refreshToken);
    }

    @Override
    public void logout(MemberDto memberDto, HttpServletResponse response) {
        // Redis 서버에 저장된 Refresh 토큰 삭제
        jwtTokenProvider.removeToken(memberDto);
        log.info("로그아웃 - Redis에 저장된 Refresh 토큰 삭제!");

        // 클라이언트 쿠키에 저장된 Access 토큰 삭제
        Cookie accessCookie = new Cookie(JWT_ACCESS_TOKEN, null);
        accessCookie.setMaxAge(0);
        accessCookie.setPath("/");

        // 클라이언트 쿠키에 저장된 Refresh 토큰 삭제
        Cookie refreshCookie = new Cookie(JWT_REFRESH_TOKEN, null);
        refreshCookie.setMaxAge(0);
        refreshCookie.setPath("/");

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
        log.info("로그아웃 - 클라이언트 쿠키에 저장된 Access, Refresh 토큰 삭제!");
    }
}
