package com.project.animal.global.common.provider;

import com.project.animal.global.common.constant.Role;
import com.project.animal.global.common.constant.TokenType;
import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.member.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.*;
import static com.project.animal.global.common.constant.ExpirationTime.ACCESS_TOKEN_EXPIRATION_TIME;
import static com.project.animal.global.common.constant.ExpirationTime.REFRESH_TOKEN_EXPIRATION_TIME;

/**
 * [참고] JWT 토큰 구조
 * - JWT는 하나의 문자열로 구성되어 있으며 크게 "HEADER.PAYLOAD.SIGNATURE"로 구성되어 있다. (구분자 Dot)
 *
 * - 헤더에는 보통 해시 알고리즘과 토큰의 타입이 들어간다.
 *
 * ex)
 * {
 *     "alg" : "HS256",
 *     "typ" : "JWT"
 * }
 *
 * - 페이로드에는 저장하고자 하는 데이터를 담는데, 각각의 Key를 Claim 이라고 부른다.
 *
 * - claim은 사용자가 원하는 key와 value로 구성할 수 있으며 Claim에는 3가지 종류가 있다.
 *
 * - registerd Claim (등록된 클레임)
 * 컴팩트하게 3글자로 정의하며, 필수는 아니나 사용이 권장된다.
 *
 * ex)
 * {
 *     "sub"    : "test",
 *     "iss"    : "Sesac Animal"
 *     "name"   : "sHu",
 *     "iat"    : "1516239022"
 * }
 *
 * - public Claim (공개 클레임)
 * 사용자가 자유롭게 정의할 수 있다.
 *
 * - private Claim (비공개 클레임)
 * 이는 등록된 또는 공개 클레임이 아닌 클레임이며, 정보를 공유하기 위해 만들어진 커스터마이징된 클레임이다.
 */

@Slf4j
@Getter
@Component
public class JwtTokenProvider {

    private SecretKey key;                                                  // 대칭키

    @Value("${jwt.issuer}")
    private String issuer;                                                  // 발급자

    private final RedisServiceProvider redisServiceProvider;

    // JWT 토큰 서명에 사용할 SecretKey를 설정하는 부분으로 토큰 서명에는 대칭키 방식과 비대칭키 방식이 있는데
    // 빠르게 구현하기 위해 대칭키 방식을 채택하였다.
    public JwtTokenProvider(@Value("${jwt.secretKey}") String key,
                            RedisServiceProvider redisServiceProvider) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
        this.redisServiceProvider = redisServiceProvider;
    }

    /**
     * Access 토큰 생성
     * @return
     */
    public String createAccessToken(Member member) {
        Date now = new Date();

        // Claim 생성
        Map<String, String> claims = new HashMap<>();
        claims.put("uid", String.valueOf(member.getId()));
        claims.put("role", member.getRole().name());

        // Access 토큰 생성
        String accessToken = Jwts.builder()
                .setClaims(claims)                                                                  // 클레임 설정
                .setHeaderParam("typ", "JWT")                                           // 토큰 타입 설정
                .setSubject(member.getEmail())                                                      // 이메일로 설정
                .setIssuer(issuer)                                                                  // 토큰 발급자 설정
                .setIssuedAt(now)                                                                   // 토큰 발급일자 설정
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_TIME))              // 토큰 만료기간 설정
                .signWith(key, SignatureAlgorithm.HS256)                                            // 서명
                .compact();

        return accessToken;
    }

    /**
     * Refresh 토큰 생성
     * @return
     */
    public String createRefreshToken(Member member) {
        Date now = new Date();

        // Claim 생성
        Map<String, String> claims = new HashMap<>();
        claims.put("uid", String.valueOf(member.getId()));
        claims.put("role", member.getRole().name());

        String refreshToken = Jwts.builder()
                .setClaims(claims)                                                                  // 클레임 설정
                .setHeaderParam("typ", "JWT")                                           // 토큰 타입 설정
                .setSubject(member.getEmail())                                                      // 이메일로 설정
                .setIssuer(issuer)                                                                  // 토큰 발급자 설정
                .setIssuedAt(now)                                                                   // 토큰 발급일자 설정
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_TIME))             // 토큰 만료기간 설정
                .signWith(key, SignatureAlgorithm.HS256)                                            // 서명
                .compact();

        // Redis에 저장
        saveRefreshToken(member.getEmail(), refreshToken);

        return refreshToken;
    }

    /**
     * JWT 토큰 파싱
     * @param request
     * @param type
     * @return
     */
    public String resolveToken(HttpServletRequest request, String type) {
        Cookie[] cookieArr = request.getCookies();

        // 쿠키가 없는 경우
        if (cookieArr == null)
            return null;

        // 쿠키에 저장된 Access 토큰 조회 --> 없으면 null 리턴
        Cookie cookie = Arrays.stream(cookieArr)
                .filter(c -> c.getName().equals(type))
                .findFirst().orElse(null);

        return cookie != null ? cookie.getValue() : null;
    }

    /**
     * JWT 토큰 검증
     * @return
     */
    public boolean validateToken(String token) {
        return !Jwts.parserBuilder()
                .setSigningKey(key)                         // 위변조 검사 (위변조 시, 예외 발생)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());                        // 만료일 체크
    }

    /**
     * AceessToken 재발급
     * @param accessToken
     * @return
     */
    public String newAccessToken(String accessToken) {

        Date now = new Date();

        MemberDto member = new MemberDto();

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();

        member.setEmail(claims.getSubject());
        member.setId(Long.parseLong(((String) claims.get("uid"))));
        member.setRole(Role.valueOf(Role.class, (String) claims.get("role")));

        // Claim 생성
        Map<String, String> claim = new HashMap<>();
        claim.put("uid", String.valueOf(member.getId()));
        claim.put("role", member.getRole().name());

        String newAccessToken = Jwts.builder()
                .setClaims(claim)                                                                   // 클레임 설정
                .setHeaderParam("typ", "JWT")                                           // 토큰 타입 설정
                .setSubject(member.getEmail())                                                      // 이메일로 설정
                .setIssuer(issuer)                                                                  // 토큰 발급자 설정
                .setIssuedAt(now)                                                                   // 토큰 발급일자 설정
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_TIME))              // 토큰 만료기간 설정
                .signWith(key, SignatureAlgorithm.HS256)                                            // 서명
                .compact();

        return newAccessToken;
    }

    public Authentication getAuthentication(String token) {
        Claims claim = Jwts.parserBuilder()
                .setSigningKey(key)                         // 위변조 검사 (위변조 시, 예외 발생)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return new UsernamePasswordAuthenticationToken(claim.getSubject(), null,
                List.of(new SimpleGrantedAuthority((String) claim.get("role"))));
    }

    /**
     * Refresh 토큰 비교
     */
    public boolean matchRefreshToken(String refreshToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();

        String email = claims.getSubject();

        Optional<String> findRefreshToken = redisServiceProvider.get(TokenType.JWT.name() + ":" + email);

        return refreshToken.equals(findRefreshToken.orElse("None"));
    }

    /**
     * 리프레시 토큰을 Redis에 저장함
     * @param email
     * @param token
     */
    private void saveRefreshToken(String email, String token) {
        redisServiceProvider.save(TokenType.JWT.name() + ":" + email, token, Duration.ofMillis(REFRESH_TOKEN_EXPIRATION_TIME));
    }
}
