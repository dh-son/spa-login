package com.example.spa_login.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Spring Security에서 소셜 로그인 처리할 때, Client가 요청한 리디렉션 URL을 쿠키에 저장하는 용도로 사용
 * 소셜 로그인 요청 시 Client가 보낸 redirect_url 파라미터를 쿠키로 저장해 놓고, 로그인 완료 후 해당 URL로 리다이렉트 하기 위한 기반 마련
 * 소셜 로그인 시작 시 Frontend에서 전달된 redirect_url을 쿠키에 저장해주는 필터
 * OncePerRequestFilter: 요청일 들어올 때마다 한번만 실행되도록 보장
 * 동작 흐름:
 *  Get /oauth2/authorization/google?redirect_url=http://localhost:3000/welcome
 *  Set-Cookie: redirect_url=http://localhost:3000/welcome; Max-Age=180; Path=/; HttpOnly
 *  로그인 성공 후 OAuthSuccessHandler에서 다시 꺼내 쓰이게 됨
 *
 */
@Slf4j
@Component
public class RedirectUrlCookieFilter extends OncePerRequestFilter {

    // Client가 전달하는 리디렉션 주소의 파라미터 이름
    public static final String REDIRECT_URI_PARAM = "redirect_url"; // 요청 파라미터 및 쿠키 이름

    private static final int MAX_AGE = 180; // 쿠키 유효 시간: 180초

    // 필터 로직을 구현하는 메서드
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // "/oauth2/authorization" 으로 시작하는 요청이 들어오면 처리
        // 사용자가 소셜 로그인을 시작할 때의 요청
        if (request.getRequestURI().startsWith("/oauth2/authorization")) {
            try {
                log.info("request uri {} ", request.getRequestURI());

                // redirect_url 파라미터 가져오기
                String redirectUrl = request.getParameter(REDIRECT_URI_PARAM);

                // 쿠키 생성 및 설정
                Cookie cookie = new Cookie(REDIRECT_URI_PARAM, redirectUrl); // 쿠키의 이름과 값 설정
                cookie.setPath("/"); // 전체 경로에서 쿠키 사용 가능하도록 설정
                cookie.setHttpOnly(true); // 자바스크립트에서 접근 불가하도록 설정 (보안 강화)
                cookie.setMaxAge(MAX_AGE); // 쿠키 만료 시간 설정 (180초)

                response.addCookie(cookie); // 응답에 쿠키 추가

            } catch (Exception e) {
                // 예외 발생 시 로그 출력
                log.error("Could not set user authentication in security context", e);
                log.info("Unauthorized request");
            }
        }

        // 다음 필터 체인으로 요청 전달
        filterChain.doFilter(request, response);
    }
}
