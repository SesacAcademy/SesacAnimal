package com.project.animal.member.controller;

import com.project.animal.global.common.annotation.Member;
import com.project.animal.global.common.constant.EndPoint;
import com.project.animal.global.common.constant.ViewName;
import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.global.common.dto.ResponseDto;
import com.project.animal.member.dto.LoginFormDto;
import com.project.animal.member.dto.TokenDto;
import com.project.animal.member.exception.LoginException;
import com.project.animal.member.service.inf.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

import static com.project.animal.global.common.constant.EndPoint.KAKAO_LOGIN;
import static com.project.animal.global.common.constant.ExpirationTime.ACCESS_TOKEN_COOKIE_EXPIRATION_TIME;
import static com.project.animal.global.common.constant.ExpirationTime.REFRESH_TOKEN_COOKIE_EXPIRATION_TIME;
import static com.project.animal.global.common.constant.TokenTypeValue.JWT_ACCESS_TOKEN;
import static com.project.animal.global.common.constant.TokenTypeValue.JWT_REFRESH_TOKEN;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    /**
     * 로그인 페이지로 이동하는 Controller이다.
     *
     * @version 0.1
     * @author 박성수
     * @return String (로그인 페이지 뷰 이름)
     */
    @GetMapping(EndPoint.LOGIN)
    public String loginForm() {
        return ViewName.LOGIN_VIEW;
    }

    /**
     * 로그인 처리를 담당하는 Controller로 로그인 폼에서 입력받은 데이터를 검증하고 생성된 JWT 토큰을 쿠키에 담아준다.
     *
     * @version 0.1
     * @author 박성수
     * @param loginFormDto 로그인 폼 DTO
     * @param response HttpServletResponse 객체
     * @return ResponseEntity<ResponseDto<String> (API 응답 DTO)
     * @throws LoginException 로그인 실패 시, 해당 예외 발생
     */
    @ResponseBody
    @PostMapping(EndPoint.LOGIN_API)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ResponseDto<String>> login(@RequestBody @Validated LoginFormDto loginFormDto,
                                                     HttpServletResponse response) {
        // 로그인 서비스 호출 
        TokenDto token = loginService.login(loginFormDto);

        log.info("{} 님이 로그인하였습니다.", loginFormDto.getEmail());

        // Response 객체에 JWT 토큰 추가
        HttpHeaders responseHeaders = addTokenInCookie(token);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(new ResponseDto<>(HttpStatus.OK.value(), "null", "로그인 되었습니다."));
    }

    /**
     * 소셜 로그인 (Kakao)을 담당하는 Controller로 카카오 인증 서버에서 전달받은 인증 코드를 서비스 계층에 넘겨주고
     * 생성된 JWT 토큰을 쿠키에 담아준다.
     *
     * @version 0.1
     * @author 박성수
     * @param code 카카오 인증 서버에서 발급한 인증 코드
     * @param response HttpServletResponse 객체
     * @return ResponseEntity<HttpHeaders> 
     */
    @GetMapping(KAKAO_LOGIN)
    @ResponseBody
    public ResponseEntity<HttpHeaders> kakaoLogin(@RequestParam String code, HttpServletResponse response) {
        
        // 로그인 서비스 호출 (카카오 로그인)
        TokenDto token = loginService.kakaoLogin(code);

        // Response 객체에 JWT 토큰 추가 및 Redirect 주소 설정 (Index)
        HttpHeaders responseHeaders = addTokenInCookie(token);
        responseHeaders.add("Location", "/");

        return new ResponseEntity<>(responseHeaders, HttpStatus.FOUND);
    }

    /**
     * 로그아웃을 담당하는 Controller로 Redis 서버에 저장된 Refresh 토큰과 사용자 브라우저 쿠키에 저장된
     * Access, Refresh 토큰 삭제를 담당한다.
     *
     * @version 0.1
     * @author 박성수
     * @param member MemberDto 객체
     * @param response HttpServletResponse 객체
     * @return String (로그인 페이지 뷰 이름)
     */
    @GetMapping(EndPoint.LOGOUT)
    public String logout(@Member MemberDto member, HttpServletResponse response) {
        // 로그아웃
        loginService.logout(member, response);

        log.info("{} 님이 로그아웃 하였습니다.", member.getEmail());

        return ViewName.LOGIN_VIEW;
    }

    /**
     * JWT 토큰을 쿠키에 저장하는 메소드로 Refresh 토큰의 경우, JS 참조를 막기 위해 httpOnly 속성을 설정한다.
     * 추가적으로 실 서버 배포 시, secure 옵션을 true로 바꿔주어야 한다.
     *
     * @version 0.1
     * @author 박성수
     * @param token TokenDto 객체 / Access 및 Refresh 토큰 포함
     * @return responseHeaders (HttpHeaders 객체)
     */
    private HttpHeaders addTokenInCookie(TokenDto token) {
        // Access 토큰 쿠키
        ResponseCookie accessToken = ResponseCookie.from(JWT_ACCESS_TOKEN, token.getAccessToken())
                .sameSite("Strict")                     // CSRF 방지
                .secure(false)                          // HTTPS 설정
                .httpOnly(false)
                .path("/")
                .maxAge(Duration.ofMinutes(ACCESS_TOKEN_COOKIE_EXPIRATION_TIME))
                .build();

        // Refresh 토큰 쿠키 (httpOnly 설정 필수!)
        ResponseCookie refreshToken = ResponseCookie.from(JWT_REFRESH_TOKEN, token.getRefreshToken())
                .sameSite("Strict")                     // CSRF 방지
                .secure(false)                          // HTTPS 설정
                .httpOnly(true)                         // JS 조회 방지
                .path("/")
                .maxAge(Duration.ofMinutes(REFRESH_TOKEN_COOKIE_EXPIRATION_TIME))
                .build();

        // HTTP 헤더에 쿠키 추가
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.SET_COOKIE, accessToken.toString());
        responseHeaders.add(HttpHeaders.SET_COOKIE, refreshToken.toString());

        return responseHeaders;
    }
}
