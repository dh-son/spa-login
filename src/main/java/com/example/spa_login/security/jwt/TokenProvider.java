package com.example.spa_login.security.jwt;

import com.example.spa_login.security.model.CustomUser;
import com.example.spa_login.user.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;


/**
 * JWT 토큰을 생성하고 검증하는 역활을 수행하는 클래스
 */
@Service
public class TokenProvider {

    // JWT 서명에 사용할 비밀 키 (512비트 이상 추천)
    private static final String SECRET_KEY = "iWSjSekWzSxK9Ou43E8W2zaRB6BP4F7AYLut2N19c3MIGPbg8glSab0Mw3r9ZCAFqQKlZ2FuIgDYtrabc3EK1g";

    // 비밀 키로부터 HMAC_SHA 키 객체 생성 (서명용 Key)
    private static final SecretKey SIGNING_KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    // 사용자 정보를 기반으로 JWT 토큰 생성
    public String create(UserEntity userEntity) {
        // 토큰 만료 시간을 현재 시각으로부터 1일 뒤로 설정
        Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

        // JWT 생성 및 반환
        return Jwts.builder()
                .signWith(SIGNING_KEY, Jwts.SIG.HS512) // 서명 알고리즘과 키 설정
                .subject(String.valueOf(userEntity.getId())) // 사용자 ID를 subject로 설정
                .issuer("spa-login") // 토큰 발급자 정보 설정
                .issuedAt(new Date()) // 토큰 발급 시간 설정
                .expiration(expiryDate) // 만료 시간 설정
                .compact(); // 토큰 생성 완료
    }

    // 토큰을 검증하고, 포함된 사용자 ID(subject)를 반환
    public String validateAndGetUserId(String token) {
        // 토큰 파싱 및 검증 (서명이 유효한지 확인)
        Claims claims = Jwts.parser()
                .verifyWith(SIGNING_KEY) // 서명키 설정
                .build()
                .parseSignedClaims(token)// 토큰 파싱
                .getPayload(); // Payload(Claims) 추출

        return claims.getSubject(); // 사용자 ID(subject) 반환
    }

    // 소셜 로그인 인증 정보를 기반으로 JWT 토큰 생성
    public String create(final Authentication authentication) {
        CustomUser userPrincipal = (CustomUser) authentication.getPrincipal();

        Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

        return Jwts.builder()
                .subject(userPrincipal.getName())
                .issuedAt(new Date())
                .expiration(expiryDate)
                .signWith(SIGNING_KEY, Jwts.SIG.HS512)
                .compact();
    }

    // 사용자 ID만을 기반으로 토큰 생성 (예: OAuth 사용자 등): 로그인 이후 사용자 정보를 간단히 전달
    public String crateByUserId(final Long userId) {
        // 만료일 1일 후 설정
        Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

        // JWT 생성 및 반환
        return Jwts.builder()
                .signWith(SIGNING_KEY, Jwts.SIG.HS512) // 서명 알고리즘과 키 설정
                .subject(String.valueOf(String.valueOf(userId))) // 사용자 ID를 subject로 설정
                .issuer("spa-login") // 토큰 발급자 정보 설정
                .issuedAt(new Date()) // 토큰 발급 시간 설정
                .expiration(expiryDate) // 만료 시간 설정
                .compact(); // 토큰 생성 완료
    }
}
