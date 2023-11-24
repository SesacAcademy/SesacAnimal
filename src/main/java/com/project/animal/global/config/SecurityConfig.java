package com.project.animal.global.config;

import com.project.animal.global.common.filter.JwtAuthenticationFilter;
import com.project.animal.global.common.filter.JwtExceptionFilter;
import com.project.animal.global.common.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity       // 동일 주소, 다른 메소드 매핑할 떄
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    private final HandlerExceptionResolver handlerExceptionResolver;

    private final String[] authenticationArr = {
            "/v1/adoption/edit/**",
            "/v1/missing/new"
    };
    private final String[] authorizationArr = {};

    // JWT를 사용하기 위해서는 기본적으로 패스워드 인코딩이 필요하기에 패스워드 인코딩 전용 빈을 등록한다. (Bcrypt encoder)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 1. 스프링 Security에서 기본으로 제공하는 로그인 페이지 비활성화
        http.formLogin().disable();

        // 2. Session 기반의 인증 방식을 사용하지 않는 설정
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 3. CSRF 공격 방지 기능 비활성화 (일반적으로 Restful API에서 사용되지 않음)
        http.csrf().disable();

        // 4. HTTP Basic Authentication Filter 비활성화
        // - HTTP Basic Authentication은 사용자 이름과 비밀번호를 Base64로 인코딩하여 HTTP 요청 헤더의 Authorization 필드에
        //   포함시키는 인증 방식
        http.httpBasic().disable();

        http.authorizeRequests()
                //.antMatchers("/static/**").permitAll()                      // 5. 정적 리소스 허용
                //.antMatchers("/error/**").permitAll()                       // ignoring으로 대체 (자세한 이유는 위의 참고글)
            .and()
            /*.authorizeRequests()                                              // 6. 인증 필요 없는 페이지
                .antMatchers("/v1/auth/**").permitAll()
                .antMatchers("/v1/api/auth/**").permitAll()
            .and()*/
            .authorizeRequests()                                              // 7. 인증이 필요한 페이지
                .antMatchers("/v1/adoption/edit/**").authenticated()
                .antMatchers(HttpMethod.POST, "/v1/adoption/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/v1/adoption/**").authenticated()
                .antMatchers("/v1/missing/new", "/v1/missing/edit/**", "/v1/missing/delete/**",
                             "/v1/missing/like", "/v1/missing/comment/**").authenticated()
                .antMatchers("/v1/auth/logout", "/v1/member/mypage/**", "/v1/api/member/**").authenticated()
                .antMatchers("/review/write/**", "/review/edit/**", "/review/update/**", "/review/delete/**",
                             "/review/comment/**", "/review/like/**").authenticated()
            .and()
            .authorizeRequests()                                              // 8. 인가가 필요한 페이지
                .antMatchers("/test2").hasRole("ADMIN")
            .and()
            .authorizeRequests()
                .anyRequest().permitAll()                                     // 9. 그 외의 페이지는 모두 허용
            .and()
            .exceptionHandling()
                .authenticationEntryPoint(CustomAuthenticationEntryPoint())
                .accessDeniedHandler(CustomAccessDeniedHandler());            // 10. 401 또는 403 코드 발생 시, 리다이렉트

        // 11. Jwt 인증 필터 및 예외 핉터 추가 (순서 꼭 지켜야함)
        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring()
                .requestMatchers(PathRequest
                        .toStaticResources()
                        .atCommonLocations()
                );
    }

    @Bean
    public AuthenticationEntryPoint CustomAuthenticationEntryPoint() {
        return new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                response.setCharacterEncoding("UTF-8");
                response.sendError(HttpStatus.UNAUTHORIZED.value());
            }
        };
    }

    @Bean
    public AccessDeniedHandler CustomAccessDeniedHandler() {
        return new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                response.sendError(HttpStatus.FORBIDDEN.value());
            }
        };
    }
}

/**
 *  [참고] Security Config 설정
 *  - 과거에는 Spring Security 설정을 WebSecurityConfigurerAdapter 클래스를 상속받아서 구현했지만 Spring Boot 버전이 올라가면서
 *    해당 방식은 Deprecated 되었다.
 *
 *  - 따라서, 이제는 빈 등록을 통해 Spring Security를 설정한다.
 *
 *  [참고] 인증, 인가 처리를 위한 권한 설정 및 표현식
 *  - https://velog.io/@dailylifecoding/spring-security-authorize-api-basic
 *
 *  [참고] ignore와 PermitAll의 차이점
 *  - https://codejava.net/frameworks/spring-boot/fix-websecurityconfigureradapter-deprecated
 *  - https://ojt90902.tistory.com/843
 *  - https://velog.io/@jeongm/SpringSecurity-staticcssjs...-%ED%8C%8C%EC%9D%BC-%EC%84%A4%EC%A0%95
 */