package com.project.animal.global.common.provider;

import com.project.animal.global.common.constant.Role;
import com.project.animal.global.common.constant.AuthType;
import com.project.animal.global.common.dto.MemberDto;
import com.project.animal.global.common.provider.inf.TokenProvider;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.InvalidKeyException;
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
import static com.project.animal.global.common.constant.TokenTypeValue.*;

@Slf4j
@Getter
@Component
public class JwtTokenProvider implements TokenProvider {

    private SecretKey key;

    @Value("${jwt.issuer}")
    private String issuer;

    private final RedisServiceProvider redisServiceProvider;

    // JWT 토큰 서명에 사용할 SecretKey를 설정하는 부분으로 토큰 서명에는 대칭키 방식과 비대칭키 방식이 있는데
    // 빠르게 구현하기 위해 대칭키 방식을 채택하였다. (MSA 환경에서는 인증을 편리하게 하기 위해 비대칭키 방식 권장)
    public JwtTokenProvider(@Value("${jwt.secretKey}") String key,
                            RedisServiceProvider redisServiceProvider) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
        this.redisServiceProvider = redisServiceProvider;
    }

    /**
     * JWT 토큰을 발급 하는 메소드이며, 매개변수로 받는 타입에 따라 Access Token으로 생성할 지 Refresh Token으로 생성할 지 결정한다.
     *
     * @version 0.1
     * @author 박성수
     * @param member 토큰에 저장할 클레임 정보
     * @param type 토큰 타입 (Access 또는 Refresh)
     * @return String (JWT 토큰)
     */
    @Override
    public String generateToken(MemberDto member, String type) {
        Date now = new Date();

        // Claim 생성
        Map<String, String> claims = new HashMap<>();
        claims.put(USER_ID, String.valueOf(member.getId()));
        claims.put(USER_NICKNAME, member.getNickname());
        claims.put(USER_ROLE, member.getRole().name());

        // 만료 기간 설정
        Long expirationTime = type.equals(JWT_ACCESS_TOKEN) ?
                ACCESS_TOKEN_EXPIRATION_TIME : REFRESH_TOKEN_EXPIRATION_TIME;
        
        // JWT 토큰 생성
        String token = Jwts.builder()
                .setClaims(claims)                                               // 클레임 설정
                .setHeaderParam("typ", "JWT")                        // 토큰 타입 설정
                .setSubject(member.getEmail())                                   // 이메일로 설정
                .setIssuer(issuer)                                               // 토큰 발급자 설정
                .setIssuedAt(now)                                                // 토큰 발급일자 설정
                .setExpiration(new Date(now.getTime() + expirationTime))         // 토큰 만료기간 설정
                .signWith(key, SignatureAlgorithm.HS256)                         // 서명
                .compact();

        // Refresh 토큰인 경우, Redis에 저장
        if (type.equals(JWT_REFRESH_TOKEN))
            redisServiceProvider.save(AuthType.JWT.name() + ":" + member.getEmail(),
                    token, Duration.ofMillis(REFRESH_TOKEN_EXPIRATION_TIME));

        return token;
    }

    /**
     * Access Token이 만료되어 Refresh Token으로 새로운 Access Token을 재발급하는 메소드이다.
     *
     * @version 0.1
     * @author 박성수
     * @param token JWT Refresh 토큰
     * @return String (JWT Access 토큰)
     */
    public String generateToken(String token) {
        Date now = new Date();

        MemberDto member = parseToken(token);

        // Claim 생성
        Map<String, String> claim = new HashMap<>();
        claim.put(USER_ID, String.valueOf(member.getId()));
        claim.put(USER_NICKNAME, member.getNickname());
        claim.put(USER_ROLE, member.getRole().name());

        return Jwts.builder()
                .setClaims(claim)
                .setHeaderParam("typ", "JWT")
                .setSubject(member.getEmail())
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Redis 서버에 저장된 Refresh 토큰을 삭제하기 위한 메소드이다.
     * 
     * @version 0.1
     * @author 박성수
     * @param member MemberDto 객체
     */
    @Override
    public void removeToken(MemberDto member) {
        redisServiceProvider.remove(AuthType.JWT.name() + ":" + member.getEmail());
    }

    /**
     * 사용자에게서 받은 Refresh 토큰과 Redis 서버에 저장된 Refresh 토큰이 일치하는지 비교하는 메소드이다.
     *
     * @version 0.1
     * @author 박성수
     * @param token JWT Refresh 토큰
     * @return Boolean (토큰이 유효하면 true, 유효하지 않으면 false 리턴)
     */
    @Override
    public boolean matchToken(String token) {
        MemberDto member = parseToken(token);
        Optional<String> findRefreshToken = redisServiceProvider.get(AuthType.JWT.name() + ":" + member.getEmail());

        return token.equals(findRefreshToken.orElse("None"));
    }

    /**
     * JWT 토큰을 파싱하여 MemberDto 객체를 리턴하는 메소드이다.
     * 
     * @version 0.1
     * @author 박성수
     * @param token JWT 토큰
     * @return MemberDto 객체
     * @throws UnsupportedJwtException if the claimsJws argument does not represent an Claims JWS
     * @throws MalformedJwtException if the claimsJws string is not a valid JWS
     * @throws IllegalArgumentException if the claimsJws string is null or empty or only whitespace
     * @throws SignatureException if the claimsJws JWS signature validation fails
     */
    @Override
    public MemberDto parseToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)                             // 위변조 검사 (위변조 시, 예외 발생)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return MemberDto.builder()
                .id(Long.parseLong(((String) claims.get(USER_ID))))
                .email(claims.getSubject())
                .nickname((String) claims.get(USER_NICKNAME))
                .role(Role.valueOf(Role.class, (String) claims.get(USER_ROLE)))
                .build();
    }

    /**
     * HttpServletRequest 객체에 저장된 쿠키에서 JWT 토큰을 가져오는 메소드이다.
     *
     * @version 0.1
     * @author 박성수
     * @param request HttpServletRequest 객체
     * @param type 토큰 타입 (Access 또는 Refresh)
     * @return String (JWT 토큰) 또는 null
     */
    @Override
    public String resolveToken(HttpServletRequest request, String type) {
        Cookie[] cookieArr = request.getCookies();

        // 쿠키가 없는 경우
        if (cookieArr == null)
            return null;

        // 쿠키에 저장된 JWT 토큰 조회 --> 없으면 null 리턴
        Cookie cookie = Arrays.stream(cookieArr)
                .filter(c -> c.getName().equals(type))
                .findFirst().orElse(null);

        return cookie != null ? cookie.getValue() : null;
    }

    /**
     * JWT 토큰을 검증하는 메소드 (위변조 및 만료 검사)
     *
     * @version 0.1
     * @author 박성수
     * @param token JWT 토큰
     * @return Boolean (토큰이 유효하면 true, 유효하지 않으면 false 리턴)
     * @throws UnsupportedJwtException if the claimsJws argument does not represent an Claims JWS
     * @throws MalformedJwtException if the claimsJws string is not a valid JWS
     * @throws IllegalArgumentException if the claimsJws string is null or empty or only whitespace
     * @throws SignatureException if the claimsJws JWS signature validation fails
     */
    @Override
    public boolean validateToken(String token) {

        try {
            // token이 null인 경우
            if (token == null) {
                return false;
            }

            // token이 유효한 경우
            return !Jwts.parserBuilder()
                    .setSigningKey(key)                     // 위변조 검사 (위변조 시, 예외 발생)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration()
                    .before(new Date());                    // 만료일 체크

        } catch (ExpiredJwtException expiredJwtException) {
            // token이 만료된 경우
            return false;
        }
    }

    /**
     * JWT 토큰에 저장된 클레임 정보를 이용하여 Authentication 객체를 생성하는 메소드이다.
     *
     * @version 0.1
     * @author 박성수
     * @param token JWT Access 토큰
     * @return Authentication 객체
     */
    public Authentication getAuthentication(String token) {
        MemberDto member = parseToken(token);

        return new UsernamePasswordAuthenticationToken(member.getEmail(), null,
                List.of(new SimpleGrantedAuthority(member.getRole().name())));
    }
}
