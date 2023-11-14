package com.project.animal.global.common.filter;

import com.project.animal.global.common.provider.JwtTokenProvider;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;

import static com.project.animal.global.common.constant.ExpirationTime.ACCESS_TOKEN_COOKIE_EXPIRATION_TIME;
import static com.project.animal.global.common.constant.TokenTypeValue.JWT_ACCESS_TOKEN;
import static com.project.animal.global.common.constant.TokenTypeValue.JWT_REFRESH_TOKEN;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 쿠키 안에 저장된 Access 토큰 및 Refresh 토큰 가져오기
        String accessToken = jwtTokenProvider.resolveToken(request, JWT_ACCESS_TOKEN);
        String refreshToken = jwtTokenProvider.resolveToken(request, JWT_REFRESH_TOKEN);

        // Authentication 객체 선언
        Authentication authentication;

        // Access 토큰 검증
        if (isValidAccessToken(accessToken)) {

            // Access 토큰이 유효하여 Authentication 객체 생성
            authentication = jwtTokenProvider.getAuthentication(accessToken);
        }

        // Refresh 토큰 검증
        else if (isValidRefreshToken(refreshToken)) {

            // Refresh 토큰이 유효하여 새로운 Access 토큰 재발급 진행
            String newAccessToken = jwtTokenProvider.generateToken(refreshToken);

            // 재발급한 Access 토큰으로 Authentication 객체 생성
            authentication = jwtTokenProvider.getAuthentication(newAccessToken);

            // 재발급한 Access 토큰 HTTP 헤더에 추가 (쿠키)
            saveAccessTokenInCookie(request, response, newAccessToken);
        }

        // Access, Refresh 토큰이 모두 만료된 경우
        else {
            authentication = null;
        }

        // Security Context에 Authentication 객체 저장 (인증 정보)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 다음 필터 호출
        filterChain.doFilter(request, response);
    }

    /**
     * Access Token이 유효한 상태인지 확인 하는 메소드이다.
     *
     * @version 0.1
     * @author 박성수
     * @param accessToken JWT Access 토큰
     * @return Boolean (토큰이 유효하면 true, 유효하지 않으면 false 리턴)
     * @throws UnsupportedJwtException if the claimsJws argument does not represent an Claims JWS
     * @throws MalformedJwtException if the claimsJws string is not a valid JWS
     * @throws IllegalArgumentException if the claimsJws string is null or empty or only whitespace
     * @throws SignatureException if the claimsJws JWS signature validation fails
     */
    private boolean isValidAccessToken(String accessToken) {
        return jwtTokenProvider.validateToken(accessToken);
    }

    /**
     * Refresh Token이 유효한 상태인지 확인 하는 메소드이며, Access Token과 달리 Redis 서버에 저장된 Refresh Token과 일치하는지
     * 확인 하는 로직이 추가적으로 포함되어 있다.
     *
     * @version 0.1
     * @author 박성수
     * @param refreshToken JWT Refresh 토큰
     * @return Boolean (토큰이 유효하면 true, 유효하지 않으면 false 리턴)
     * @throws UnsupportedJwtException if the claimsJws argument does not represent an Claims JWS
     * @throws MalformedJwtException if the claimsJws string is not a valid JWS
     * @throws IllegalArgumentException if the claimsJws string is null or empty or only whitespace
     * @throws SignatureException if the claimsJws JWS signature validation fails
     */
    private boolean isValidRefreshToken(String refreshToken) {
        return jwtTokenProvider.validateToken(refreshToken) && jwtTokenProvider.matchToken(refreshToken);
    }

    /**
     * 새로 발급 받은 Access Token을 HTTP 응답에 쿠키로 저장하는 메소드이며, 추가적으로 기존 HttpServletRequest 객체의 쿠키 안에
     * 저장되어 있던 만료된 Access Token을 새로 발급받은 Access Token으로 바꾼다.
     *
     * 만약, Access Token이 쿠키 안에 없다면 request.setAttribute() 메소드를 이용하여 request 영역에 Access Token을 저장한다.
     *
     * @version 0.1
     * @author 박성수
     * @param request HttpServletRequest 객체
     * @param response HttpServletResponse 객체
     * @param newAccessToken JWT Access 토큰
     */
    private void saveAccessTokenInCookie(HttpServletRequest request, HttpServletResponse response, String newAccessToken) {
        
        // 새로 발급 받은 Access Token을 저장한 쿠키를 생성
        ResponseCookie newAccessTokenCookie = ResponseCookie.from(JWT_ACCESS_TOKEN, newAccessToken)
                .sameSite("Strict")                     // CSRF 방지
                .secure(false)                          // HTTPS 설정
                .httpOnly(false)
                .path("/")
                .maxAge(Duration.ofMinutes(ACCESS_TOKEN_COOKIE_EXPIRATION_TIME))
                .build();

        // HTTP 응답 헤더에 쿠키 설정
        response.setHeader("Set-Cookie", newAccessTokenCookie.toString());

        // 기존에 만료된 Access Token을 새로 발급 받은 Access Token으로 교체
        Arrays.stream(request.getCookies()).forEach(cookie -> {
            if (cookie.getName().equals(JWT_ACCESS_TOKEN)) {
                cookie.setValue(newAccessToken);
            }
        });
        
        // 만약, 기존 쿠키에 Access Token이 없는 경우, MethodResolver에서 사용할 수 있도록 request 영역에 저장
        request.setAttribute(JWT_ACCESS_TOKEN, newAccessToken);
    }
}

/**
 * # 스프링 필터 등록
 * - 스프링 부트에서는 필터를 여러 방법으로 구현할 수 있는데, 가장 보편적인 방법은 필터를 상속받아 사용하는 것이다.
 *
 * - 대표적으로 상속하는 객체는 GenericFilterBean 또는 OncePerReqeustFilter 이다.
 *
 * - 이 중에서 GenericFilterBean의 중복 필터 사용 문제를 해결해 나온 것이 OncePerRequestFilter 이므로 OncePerRequestFilter
 *   사용을 권장한다.
 *
 * # JwtAuthenticationFilter
 * - 우리는 사용자 인증/인가 처리를 위해 JWT 토큰을 사용할 것이므로 JWT 토큰 전용 커스텀 필터를 만들어야 한다.
 *
 * - 클라이언트 요청 시, JWT 인증을 하기 위해 해당 필터는 기본적으로 스프링 시큐리티에서 등록하는
 *   UsernamePasswordAuthenticationFilter 이전에 실행되어야 한다.
 *
 * - 즉, JwtAuthenticationFilter를 통과하면 UsernamePasswordAuthenticationFilter 이후의 필터는 거치지 않고 바로 통과한다는 것이다.
 *
 * [참고] 스프링 시큐리티에서 등록하는 필터 목록
 * - https://velog.io/@chullll/Spring-Security-JWT-%ED%95%84%ED%84%B0-%EC%A0%81%EC%9A%A9-%EA%B3%BC%EC%A0%95
 *
 * - 추가적으로 Security Context는 Authentication 객체가 저장되는 보관소이며, 인증 정보가 필요할 때 언제든지 Authentication 객체를
 *   꺼내어 사용하도록 제공되는 클래스이고 이러한 Security Context 객체를 저장하는 객체가 바로 Security Context Holder이다.
 */