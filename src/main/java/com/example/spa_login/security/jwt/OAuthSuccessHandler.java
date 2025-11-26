package com.example.spa_login.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static com.example.spa_login.security.jwt.RedirectUrlCookieFilter.REDIRECT_URI_PARAM;

/**
 * 소셜 로그인 성공 후 동작하는 커스텀 성공 핸들러 클래스
 * 로그인 성공 -> JWT 토큰 생성 -> Client가 요청한 리디렉션 URL로 토큰을 포함하여 리다이렉트
 * SimpleUrlAuthenticationSuccessHandler: OAuth2 로그인 성공 시 처리 로직 담당 클래스
 */
@Slf4j
@AllArgsConstructor
@Component
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    // 소셜 로그인 성공 후 사용자를 돌려보낼 기본 페이지 주소
    private static final String LOCAL_REDIRECT_URL = "http://localhost:3000"; // 기본 리다이렉트 주소

    // 로그인 인증이 성공했을 때 호출되는 메서드 (후처리 로직)
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        TokenProvider tokenProvider = new TokenProvider(); // JWT 토큰 발급을 위한 객체 생성
        String token = tokenProvider.create(authentication); // 인증 정보를 기반으로 JWT 토큰 생성

        log.info("token: {}", token);

        // 요청에 포함된 쿠키 중 redirect_url 이름의 쿠키를 찾아 Optional로 래핑
        Optional<Cookie> oCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(REDIRECT_URI_PARAM))
                .findFirst();

        // 쿠키가 존재하면 값 추출, 없으면 Optional.empty
        Optional<String> redirectUri = oCookie.map(Cookie::getValue);

        log.info("redirectUri {}", redirectUri);

        // 쿠키 값이 존재하면 해당 값 사용, 없으면 기본 주소 사용 후 JWT 토큰 쿼리 파라미터로 추가
        // ex. http://localhost:3000/sociallogin?token=sdjflsjfl...
        String targetUrl = redirectUri.orElseGet(() -> LOCAL_REDIRECT_URL) + "/sociallogin?token=" + token;

        log.info("targetUrl {}", targetUrl);

        response.sendRedirect(targetUrl); // 사용자를 최종 URL로 리다이렉트
    }
}
