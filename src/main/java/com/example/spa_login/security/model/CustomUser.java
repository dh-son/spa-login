package com.example.spa_login.security.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * OAuth2 로그인 사용자 정보를 담는 CustomUser 클래스
 * Spirng Security의 DefaultOAuth2User를 확장하여 사용자 ID, 이메일, 사용자명을 추가로 보관
 * 소셜 로그인 이후 인증된 사용자 정보를 우리 서비스의 요구사항에 맞게 가공 또는 저장
 * DefaultOAuth2User: Spring Security가 소셜 로그인을 처리할 때 사용자 정보를 담기 위해 기본으로 사용하는 클래스
 *
 */
public class CustomUser extends DefaultOAuth2User {

    private static final long serialVersionUID = 1L; // 직렬화 버전 UID

    // 추가 필드
    private Long id; // DB에 저장된 사용자 고유 ID
    private String email;
    private String username;

    // OAuth2 인증 정보를 기반으로 CustomUser 객체 생성
    public CustomUser(Long id,
                      String email,
                      String username,
                      Collection<? extends GrantedAuthority> authorities,
                      OAuthAttributes attributes // OAuth 사용자 정보
                      ) {
        // 부모 클래스인 DefaultOAuth2User의 생성자 호출
        // attributes.getAttributes(): 속성 맵
        // attributes.getNameAttributeKey(): 이름 식별 키(고유 식별 키)
        super(authorities, attributes.getAttributes(), attributes.getNameAttributeKey());
        this.id = id;
        this.email = email;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    // OAuth2User 인터페이스 구현: 사용자의 고유 이름(ID)을 문자열로 반환
    // 반드시 구현해야 하는 필수 메서드 (AuthenticatedPrincipal)
    // Spring Security에서 인증된 사용자를 식별할 때 꼭 필요한 메서드
    @Override
    public String getName() {
        return "" + this.id; // ID를 문자열로 반환하여 변환
    }
}
