package com.korit.security_study.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private final Key KEY;

    public JwtUtils(@Value("${jwt.secret}") String secret) {
        KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateAccessToken(String id) {
        return Jwts.builder()
                .subject("AccessToken") // 토큰 용도 식별자
                .id(id) // 토큰의 고유한 식별자 부여, 추후 토큰 무효화나 사용자 조회 때 사용
                .expiration(new Date(new Date().getTime() + (1000L * 60L * 60L * 24L * 30L))) // 만료기간, 30일
                .signWith(KEY) // jW
                .compact();
    }

    // Claims: JWT의 Payload 영역, 사용자 정보, 만료일자 등등 담겨있음
    // JwtException: 토큰이 잘못 되었을 경우 발생하는 예외
    public Claims getClaims(String token) throws JwtException {
        JwtParserBuilder jwtParserBuilder = Jwts.parser();

        // 비밀키
        jwtParserBuilder.setSigningKey(KEY);
        JwtParser jwtParser = jwtParserBuilder.build();
        return jwtParser.parseClaimsJws(token).getBody(); // 순수 Claims JWT를 파싱

        /*
        * return Jwts.parser()
        .verifyWith(KEY)
        .build()
        .parseSignedClaims(token)
        .getPayload();
        * */
    }

    public boolean isBearer(String token) {
        if (token == null) {
            return false;
        }

        if (!token.startsWith("Bearer ")) {
            return false;
        }
        return true;
    }

    public String removeBearer(String bearerToken) {
        return bearerToken.replaceFirst("Bearer ", "");
    }

}
