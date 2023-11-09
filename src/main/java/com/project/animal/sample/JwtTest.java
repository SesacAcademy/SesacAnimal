package com.project.animal.sample;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {

    public static void main(String[] args) throws InterruptedException {
        long TOKEN_EXPIRATION_TIME = 1000 * 60 * 1L;      // 1분

        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode("secretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKey"));

        Date now = new Date();

        // Claim 생성
        Map<String, String> claims = new HashMap<>();
        claims.put("1", "");

        // JWT 토큰 생성
        String token = Jwts.builder()                                                      // 클레임 메소드는 맨 위에 있어야함!!!
                .setClaims(claims)                                                         // 클레임 설정
                .setSubject("hi")
                .setHeaderParam("typ", "JWT")
                .setIssuer("Issuer Test")                                                  // 토큰 발급자 설정
                .setIssuedAt(now)                                                          // 토큰 발급일자 설정
                .setExpiration(new Date(now.getTime() + TOKEN_EXPIRATION_TIME))            // 토큰 만료기간 설정
                .signWith(SignatureAlgorithm.HS256, key)                                   // 서명
                .compact();

        // 토큰 출력
        System.out.println(token);

        Claims claim = Jwts.parser()
                .setSigningKey(key) // 서명 키 설정
                .parseClaimsJws(token) // JWT 토큰 디코딩
                .getBody();

        // 만료 시간 (expiration) 추출
        Date expiration = claim.getExpiration();

        // 발급 일자 (issued at) 추출
        Date issuedAt = claim.getIssuedAt();

        System.out.println("만료 시간 (exp): " + expiration);
        System.out.println("발급 일자 (iat): " + issuedAt);


        // 토큰 위변조 검사
        try {
            Claims claims2 = Jwts.parser()
                    .setSigningKey(key) // 서버에서만 알고 있는 비밀 키
                    .parseClaimsJws(token + "1")
                    .getBody();

            // 서명이 일치하는 경우
            System.out.println("JWT 토큰 서명이 유효합니다 : " + claims2.getSubject() );

        } catch (SignatureException e) {
            // 서명이 일치하지 않는 경우
            System.out.println("JWT 토큰이 위변조되었습니다.");
        }

        Thread.sleep(10000);

        boolean before = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(now);



        System.out.println(before);

    }
}
