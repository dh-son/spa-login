package com.example.spa_login.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * HTTP 요청에서 JWT 토큰을 추출하고 인증 정보를 설정하는 필터 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Component // 스프링 컴포넌트로 등록 (필터로 사용됨): 스프링이 자동으로 빈으로 등록하고 관리
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    // OPTIONS 요청은 필터를 건너 뛰도록 설정 (CORS 사전 요청 등 무시)
    // CORS Preflight 요청을 필터에서 제외
    // 인증과 관련 없는 OPTIONS 요청에 대해서는 불필요한 필터 처리 생략 -> 성능, 안정성 Up
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        if (request.getMethod().equals("OPTIONS")) {
            return true; // OPTIONS 메서드는 필터 대상 아님
        }

        return false;
    }

    // 필터 내부 로직: 요청에서 JWT 파싱 -> 검증 -> 인증 객체 생성 -> SecurityContext 설정
    // 1. 요청 헤더에서 JWT 토큰을 추출
    // 2. 추출한 토큰이 유효한지 확인
    // 3. 유효한 경우 토큰에 담긴 사용자 아이디를 바탕으로 Spring Security의 인증 객체 생성
    // 4. 이 인증정보를 SecurityContext에 설정
    // 5. 다음 필터로 요청을 넘김
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = parseBearerToken(request); // Authorization 헤더에서 JWT 토큰 파싱
            log.info("doFilterInternal");

            // 토큰이 존재하고 "null"이 아닌 경우
            if (token != null && !token.equalsIgnoreCase("null")) {
                String userId = tokenProvider.validateAndGetUserId(token); // 토큰 검증 및 사용자 ID 추출
                log.info("Authenticated user Id: " + userId);

                // 사용자 ID를 기반으로 인증 객체 생성 (권한 없음)
                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId, null, AuthorityUtils.NO_AUTHORITIES);

                // 요청 정보 추가 (IP, 세션 등): 감사 로그 또는 세션 추적에 사용
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 새로운 SecurityContext 생성 및 인증 객체 설정
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);

                // 현재 스레드에 SecurityContext 등록
                // 컨트롤러, 서비스 계층: @AuthenticationPrincipal or SecurityContextHolder.getContext()를 통해 현재 로그인한 사용자 정보에 접근 가능
                SecurityContextHolder.setContext(securityContext);
            }
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex); // 예외 발생 시 로그 출력
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    // Authorization 헤더에서 Bearer 토큰만 추출하는 메서드
    private String parseBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization"); // Authorization 헤더 가져오기
        /*
         * 예시: Authorization: Bearer sdlksdklfkldjflr..
         */

        // 헤더 값이 있고 "Bearer "로 시작하면 토큰만 추출하여 반환
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 이후 문자열 추출
        }

        return null; // 유효하지 않은 경우 null 반환
    }
}
