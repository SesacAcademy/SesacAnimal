package com.project.animal.member.service;

import com.project.animal.global.common.constant.Role;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.project.animal.global.common.constant.AuthType.KAKAO;
import static com.project.animal.global.common.constant.AuthType.MAIL;
import static com.project.animal.global.common.constant.EndPoint.*;
import static com.project.animal.global.common.constant.TokenTypeValue.JWT_ACCESS_TOKEN;
import static com.project.animal.global.common.constant.TokenTypeValue.JWT_REFRESH_TOKEN;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImp implements LoginService {

    private final MemberRepository memberRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder encoder;

    @Value("${oauth2.kakao.client_id}")
    private String kakao_client_id;

    @Value("${oauth2.kakao.secret}")
    private String kakao_secret;

    @Override
    public TokenDto login(LoginFormDto loginFormDto) {
        // 회원 조회
        Optional<Member> findMember = memberRepository.findByEmailAndType(loginFormDto.getEmail(), MAIL.name());
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
    @Transactional
    public TokenDto kakaoLogin(String code) {

        // 1. (KaKao) Access Token 발급 진행
        RestTemplate restTemplate = new RestTemplate();

        // Header 설정 (x-www-url-encoded)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Post 방식 파라미터 설정
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakao_client_id);
        body.add("redirect_uri", KAKAO_REDIRECT_URI);
        body.add("code", code);
        body.add("client_secret", kakao_secret);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        // Post 전송 --> 토큰 발급 (Access, Refresh)
        ResponseEntity<TokenDto> token = restTemplate.exchange(KAKAO_TOKEN_URI, HttpMethod.POST, requestEntity, TokenDto.class);

        // 2. (KaKao) 발급받은 Access Token으로 사용자 정보 조회

        // Header 설정 (Authorization)
        headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token.getBody().getAccessToken());
        headers.add("Accept", "application/json");

        // Get 전송
        ResponseEntity<String> kakaoUserInfo = restTemplate.exchange(KAKAO_USER_INFO_URI,
                                                HttpMethod.GET, new HttpEntity<>(headers), String.class);

        // 사용자 정보 조회 (소셜 로그인은 사업자 등록이 되어 있지 않아 따로 DTO를 만들지 않고 직접 파싱하였습니다)
        List<String> userData = Arrays.stream(kakaoUserInfo.getBody().split(","))
                .filter(x -> { return x.contains("\"profile\"") || x.contains("\"email\""); })
                .map(x -> { return x.contains("nickname") ? x.split("\"")[5] : x.split("\"")[3]; })
                .toList();

        // 3. 회원가입 여부 조회
        Optional<Member> findMember = memberRepository.findByEmailAndType(userData.get(1), KAKAO.name());

        MemberDto memberDto = null;

        // 이미 카카오 계정을 통해 회원가입이 된 경우
        if (findMember.isPresent()) {
            log.info("카카오 계정을 통해 회원가입이 되었음");
            memberDto = new MemberDto(findMember.get());
        }

        // 카카오 계정을 통해 회원가입을 하지 않은 경우, 회원가입 진행
        else {
            log.info("카카오 계정을 통해 회원가입이 되지 않았음");
            LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("Asia/Tokyo"));

            Member member = Member.builder()
                    .email(userData.get(1))
                    .nickname(userData.get(0))
                    .name(KAKAO.name())
                    .type(KAKAO.name())
                    .role(Role.ROLE_USER)
                    .isActive(1)
                    .lastLoginAt(dateTime)
                    .build();

            memberRepository.save(member);

            memberDto = new MemberDto(member);
        }
        // 4. 로그인

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
