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

    /**
     * 로그인 처리를 담당하는 Service로 Controller에서 전달받은 LoginFormDto 객체를 이용하여 DB에 해당 사용자가 있는지 확인하고
     * 사용자가 존재하면 JWT 토큰을 생성하여 리턴한다.
     *
     * 만약, DB에 해당 사용자가 없다면 LoginException 예외가 발생한다.
     * 
     * @version 0.1
     * @author 박성수
     * @param loginFormDto 로그인 폼 DTO
     * @return TokenDto 객체
     * @throws LoginException 로그인 실패 시, 해당 예외 발생
     */
    @Override
    @Transactional
    public TokenDto login(LoginFormDto loginFormDto) {
        // 회원 조회
        Optional<Member> findMember = memberRepository.findByEmailAndTypeAndIsActive(loginFormDto.getEmail(), MAIL.name(), 1);
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

        // 마지막 로그인 날짜 변경
        member.setLastLoginAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));

        MemberDto memberDto = new MemberDto(member);

        // Access 및 Refresh 토큰 생성 + Redis에 Refresh Token 저장
        String accessToken = jwtTokenProvider.generateToken(memberDto, JWT_ACCESS_TOKEN);
        String refreshToken = jwtTokenProvider.generateToken(memberDto, JWT_REFRESH_TOKEN);

        return new TokenDto(accessToken, refreshToken);
    }

    /**
     * 카카오 로그인을 담당하는 Service로 사업자 등록이 되어 있지 않아 필요로 하는 사용자 데이터를 받을 수 없기 때문에
     * 별도의 DTO를 만들지 않고 테스트 용도로 작성하였습니다.
     *
     * @version 0.1
     * @author 박성수
     * @param code 인증 코드
     * @return TokenDto 객체
     */
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

            // 마지막 로그인 날짜 변경
            findMember.get().setLastLoginAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        }

        // 카카오 계정을 통해 회원가입을 하지 않은 경우, 회원가입 진행
        else {
            log.info("카카오 계정을 통해 회원가입이 되지 않았음");
            LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

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

    /**
     * 로그아웃을 담당하는 Service로 Redis 서버에 저장된 Refresh 토큰과 사용자 브라우저 쿠키에 저장된
     * Access, Refresh 토큰 삭제를 담당한다.
     *
     * @version 0.1
     * @author 박성수
     * @param memberDto MemberDto 객체
     * @param response HttpServletResponse 객체
     */
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
