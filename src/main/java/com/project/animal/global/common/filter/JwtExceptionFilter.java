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

            else if (e instanceof SecurityException || e instanceof MalformedJwtException) {
                log.error("유효하지 않은 JWT 토큰입니다.");
            }

            else if (e instanceof ExpiredJwtException) {
                log.error("Refresh 토큰이 만료되었습니다.");
            }

            else if (e instanceof UnsupportedJwtException) {
                log.error("지원되지 않는 형식의 JWT 토큰입니다.");
            }

            else if (e instanceof IllegalArgumentException) {
                log.error("JWT 토큰 내의 Claims이 비어있습니다.");
            }

            // Cookie에서 토큰 삭제 및 에러 페이지 호출
            if (e instanceof JwtException) {
                removeTokenInCookie(response);
                response.sendError(HttpStatus.UNAUTHORIZED.value());
            }
        }
    }

    /**
     * AccessToken, Refresh 토큰이 저장된 쿠키를 삭제하는 메소드
     *
     * @version 0.1
     * @author 박성수
     * @param response
     */
    private void removeTokenInCookie(HttpServletResponse response) {
        Cookie accessToken = new Cookie(JWT_ACCESS_TOKEN, "");
        Cookie refreshToken = new Cookie(JWT_REFRESH_TOKEN, "");

        accessToken.setMaxAge(0);
        refreshToken.setMaxAge(0);

        response.addCookie(accessToken);
        response.addCookie(refreshToken);
    }
}
