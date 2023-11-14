package com.project.animal.global.common.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SecurityException;
import io.jsonwebtoken.security.SignatureException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.project.animal.global.common.constant.TokenTypeValue.JWT_ACCESS_TOKEN;
import static com.project.animal.global.common.constant.TokenTypeValue.JWT_REFRESH_TOKEN;

@Slf4j
@NoArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            // JwtAuthenticationFilter 호출
            filterChain.doFilter(request, response);

        } catch (RuntimeException e) {

            // JWT 예외 로그 출력
            if (e instanceof SignatureException) {
                log.error("JWT 서명이 조작되었습니다.");
            }

            else if (e instanceof MalformedJwtException) {
                log.error("유효하지 않은 JWT 토큰입니다.");
            }

            else if (e instanceof UnsupportedJwtException) {
                log.error("지원되지 않는 형식의 JWT 토큰입니다.");
            }

            else if (e instanceof IllegalArgumentException) {
                log.error("JWT 토큰 내의 Claims이 비어있습니다.");
            }

            // Cookie에서 토큰 삭제 및 에러 페이지 호출
            if (e instanceof JwtException || e instanceof IllegalArgumentException) {
                log.info("사용자 Access Token 및 Refresh Token 삭제");
                removeTokenInCookie(request, response);
                response.sendError(HttpStatus.UNAUTHORIZED.value());
            }
        }
    }

    /**
     * JWT Access, Refresh 토큰이 저장된 쿠키를 삭제 하는 메소드
     *
     * @version 0.1
     * @author 박성수
     * @param response
     */
    private void removeTokenInCookie(HttpServletRequest request, HttpServletResponse response) {
        // request 객체에서 JWT Token이 담긴 Cookie를 List 형태로 가져 온다.
        List<Cookie> cookielist = Arrays.stream(request.getCookies())
                .filter(cookie -> {
                    return cookie.getName().equals(JWT_ACCESS_TOKEN) || cookie.getName().equals(JWT_REFRESH_TOKEN);})
                .toList();

        // cookie의 라이프 사이클을 0으로 만들고 다시 response 객체에 저장한다.
        cookielist.forEach(cookie -> {
            cookie.setMaxAge(0);
            cookie.setPath("/");
            cookie.setValue(null);
            response.addCookie(cookie);
        });
    }
}
