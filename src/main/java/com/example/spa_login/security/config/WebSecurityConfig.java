package com.example.spa_login.security.config;

import com.example.spa_login.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * 애플리케이션의 웹 보안을 구성하는 설정 클래스
 */
@Configuration
@EnableWebSecurity // Spring Security 활성화
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter; // JWT 인증 필터 의존성 주입

    public WebSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // 보안 필터 체인 정의: 인증, 인가, 세션, 예외 처리, JWT 필터 설정
    // 기본 보안 정책 비활성화하고 JWT 기반 인증 방식의 최적화된 구조로 필터 체인 구성
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> {}) // CORS 설정 활성화
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (REST API 서버에서 주로 사용
                .httpBasic(httpBasic -> httpBasic.disable()) // 기본 인증 방식 비활성화
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // // 세션 사용 안함 (JWT 기반 인증)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/auth/**").permitAll() // 루트 및 /auth/** 경로는 인증 없이 허용
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                )
                // JWT 필터를 UsernamePasswordAuthenticationFilter 이후에 실행되도록 추가
                .addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        // 인증 실패 시 403 Forbidden 반환
                        .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
                );

        return http.build(); // 필터 체인 객체 반환
    }

    // CORS 설정을 정의하는 Bean
    // 브라우저 콘솔에서 CORS 관련 에러 없이 프론트와 백엔드가 원활하게 통신 가능
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true); // 자격 증명 포함 허용 (예: 쿠키, Authorization 헤더)
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // 허용할 프로트엔드 도메인
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")); // 허용 메서드
        configuration.setAllowedHeaders(List.of("*")); // 모든 요청 헤더 허용
        configuration.setExposedHeaders(List.of("*")); // 응답 헤더 노출

        // 위의 CORS 설정을 모든 경로에 적용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 요청에 대해 설정 적용
        return source;
    }
}
