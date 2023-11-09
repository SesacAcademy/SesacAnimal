package com.project.animal.global.common.filter;

import com.project.animal.global.common.provider.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.BadCredentialsException;
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
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Access 토큰 및 Refresh 토큰 가져오기
        String accessToken = jwtTokenProvider.resolveToken(request, JWT_ACCESS_TOKEN);
        String refreshToken = jwtTokenProvider.resolveToken(request, JWT_REFRESH_TOKEN);

        log.info("JWT 필터 적용 전");
        log.info("Access Token : {}", accessToken);
        log.info("Refresh Token : {}", refreshToken);

        // Authentication 객체 선언
        Authentication authentication = null;

        try {
            // Access 토큰 검증 (데이터 위변조 및 토큰 만료 검사)
            if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
                log.info("Access 토큰 검증 완료되어 Authentication 인증 객체 저장");

                authentication = jwtTokenProvider.getAuthentication(accessToken);
            }

            // Access 토큰이 클라이언트 쿠키에서 삭제되어 Refresh 토큰만 있는 경우, Refresh 토큰 검증
            else if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken) && jwtTokenProvider.matchRefreshToken(refreshToken)) {

                // Access 토큰 재발급
                String newAccessToken = jwtTokenProvider.newAccessToken(refreshToken);
                authentication = jwtTokenProvider.getAuthentication(newAccessToken);

                log.info("Access 토큰이 클라이언트 쿠키에서 삭제되어 Access 토큰 재발급 프로세스 진행! \n" +
                         "Access 토큰 재발급 완료 : {}", newAccessToken);

                // 재발급한 Access 토큰 HTTP 헤더에 추가 (쿠키)
                saveAccessTokenInCookie(request, response, newAccessToken);
            }
            // SecurityContext에 Authentication 객체 저장 (인증 정보)
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (MalformedJwtException   | IllegalArgumentException | UnsupportedJwtException |
                 BadCredentialsException | SignatureException invalidTokenException) {
            
            // JWT 토큰이 위변조된 경우 (잘못된 서명, 지원되지 않는 포맷, JWT 토큰 손상)
            log.error("JWT 토큰이 위변조되거나 검증에 실패하였습니다.");
            throw invalidTokenException;

        } catch (ExpiredJwtException expiredJwtException) {

            // Access 토큰이 클라이언트 쿠키에서 삭제되어 Refresh 토큰 검증 과정에서 Refresh 토큰이 만료된 경우, 그대로 예외 전달
            if (accessToken == null) {
                log.info("Access 토큰이 클라이언트 쿠키에서 삭제되어 Refresh 토큰 검증하였으나 Refresh 토큰 만료됨");
                throw expiredJwtException;
            }

            // Access 토큰이 만료되어 ExpiredJwtException이 발생한 경우, Refresh 토큰 검증
            else if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken) && jwtTokenProvider.matchRefreshToken(refreshToken)) {
                // Access 토큰 재발급
                String newAccessToken = jwtTokenProvider.newAccessToken(refreshToken);
                authentication = jwtTokenProvider.getAuthentication(newAccessToken);

                log.info("Access 토큰이 만료되어 Access 토큰 재발급 프로세스 진행! \n" +
                         "Access 토큰 재발급 완료 : {}", newAccessToken);

                // 재발급한 Access 토큰 HTTP 응답 헤더에 추가 (쿠키)
                saveAccessTokenInCookie(request, response, newAccessToken);

                // SecurityContext에 Authentication 객체 저장 (인증 정보)
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * HTTP 헤더에 새로 발급받은 Access Token을 저장
     * @param response
     * @param newAccessToken
     */
    private void saveAccessTokenInCookie(HttpServletRequest request, HttpServletResponse response, String newAccessToken) {
        ResponseCookie newAccessTokenCookie = ResponseCookie.from(JWT_ACCESS_TOKEN, newAccessToken)
                .sameSite("Strict")                     // CSRF 방지
                .secure(false)                          // HTTPS 설정
                .httpOnly(false)
                .path("/")
                .maxAge(Duration.ofMinutes(ACCESS_TOKEN_COOKIE_EXPIRATION_TIME))
                .build();

        // 기존 쿠키에 저장되어 있는 만료된 AccessToken을 새로 발급받은 Token으로 교체
        Arrays.stream(request.getCookies()).forEach(x -> {
            if (x.getName().equals(newAccessTokenCookie.getName())) {
                log.info("새로 발급한 Access Token을 Request 객체에 있는 쿠키와 바꿔치기!");
                x.setValue(newAccessToken);
            }
        });
        
        // 만약, 기존에 쿠키에 AccessToken이 없는 경우, MethodResolver에서 사용할 수 있도록 request 영역에 저장
        request.setAttribute(JWT_ACCESS_TOKEN, newAccessToken);

        // HTTP 응답 헤더에 새로 발급받은 Access 토큰 저장 (쿠키)
        response.setHeader("Set-Cookie", newAccessTokenCookie.toString());
    }
}