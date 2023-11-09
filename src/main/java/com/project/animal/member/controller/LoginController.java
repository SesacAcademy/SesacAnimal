package com.project.animal.member.controller;

import com.project.animal.global.common.constant.EndPoint;
import com.project.animal.global.common.constant.ViewName;
import com.project.animal.global.common.dto.ResponseDto;
import com.project.animal.member.dto.LoginFormDto;
import com.project.animal.member.dto.TokenDto;
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
import static com.project.animal.global.common.constant.ExpirationTime.ACCESS_TOKEN_COOKIE_EXPIRATION_TIME;
import static com.project.animal.global.common.constant.ExpirationTime.REFRESH_TOKEN_COOKIE_EXPIRATION_TIME;
import static com.project.animal.global.common.constant.TokenTypeValue.JWT_ACCESS_TOKEN;
import static com.project.animal.global.common.constant.TokenTypeValue.JWT_REFRESH_TOKEN;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @GetMapping(EndPoint.LOGIN)
    public String loginForm() {
        return ViewName.LOGIN_VIEW;
    }

    @ResponseBody
    @PostMapping(EndPoint.LOGIN_API)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ResponseDto<String>> login(@RequestBody @Validated LoginFormDto loginFormDto,
                                                     HttpServletResponse response) {
        TokenDto token = loginService.login(loginFormDto);

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

        log.info("Response 객체에 JWT 토큰 추가");

        log.info("토큰 {}, {}", accessToken.toString(), refreshToken.toString());

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(new ResponseDto<>(HttpStatus.OK.value(), "null", "로그인 되었습니다."));
    }
}
