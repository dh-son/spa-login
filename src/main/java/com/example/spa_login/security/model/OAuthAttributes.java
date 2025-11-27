package com.example.spa_login.security.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/**
 * 외부 서비스에서 받아온 사용자 정보를 애플리케이션에서 사용할 수 있도록 변환해주는 클래스
 * OAuth2UserService를 구현한 클래스 내부에서 사용
 * 소셜 로그인 과정에서 전달 받은 사용자 정보를 우리 서비스에 받에 변환해서 담아주는 클래스
 * OAuth 인증 후 사용자 정보를 등록하거나 확인할 때 자주 사용
 */
@Getter
public class OAuthAttributes {

    // OAuth2 로그인 시 전달받는 사용자 정보
    private Map<String, Object> attributes; // OAuth2 제공자에서 가져온 모든 사용자 속성
    private String nameAttributeKey; // 사용자 식별에 사용할 키 이름 (ex. sub, id 등)
    private String name; // 사용자 이름
    private String email; // 사용자 이메일
    private String picture; // 프로필 사진 URL
    private String id; // OAuth 사용자 고유 ID (실제 유저 식별 값)

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture, String id) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.id = id;
    }

    // OAuth 제공자 구분에 따라 처리할 메서드
    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
        if (registrationId.equals("naver")) {
            return ofNaver("id", attributes);
        }

        if (registrationId.equals("kakao")) {
            return ofKakao("id", attributes);
        }

        if (registrationId.equals("github")) {
            return ofGitHub("id", attributes);
        }

        return ofGoogle(userNameAttributeName, attributes);
    }

    // GitHub 로그인 사용자 정보를 OAuthAttributes 객체로 변환하는 메서드
    private static OAuthAttributes ofGitHub(String userNameAttributeName,
                                           Map<String, Object> attributes) {
        String username = (String) attributes.get("login");
        Integer id = (Integer) attributes.get("id");
        String nickname = username;
        String profileImageUrl = (String) attributes.get("avatar_url");
        String email = (String) attributes.get("email");

        return OAuthAttributes.builder()
                .name(nickname)
                .email(email)
                .picture(profileImageUrl)
                .id("" + id)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // 카카오 로그인 사용자 정보를 OAuthAttributes 객체로 변환하는 메서드
    private static OAuthAttributes ofKakao(String userNameAttributeName,
                                           Map<String, Object> attributes) {
        Long id = (Long)attributes.get("id");

        // 카카오 계정 정보 가져오기
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        // 프로필 정보 추출
        Map<String , Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        String nickname = (String) profile.get("nickname");
        String profileImageUrl = (String) profile.get("profile_image_url");

        String email = (String) kakaoAccount.get("email");

        return OAuthAttributes.builder()
                .name(nickname)
                .email(email)
                .picture(profileImageUrl)
                .id("" + id)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // 네이버 로그인 사용자 정보를 OAuthAttributes 객체로 변환하는 메서드
    private static OAuthAttributes ofNaver(String userNameAttributeName,
                                           Map<String, Object> attributes) {
        // 네이버 응답의 내부 'response' 필드에서 사용자 정보 추출
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .id((String) response.get(userNameAttributeName))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // Google OAuth 사용자 정보로부터 OAuthAttributes 객체를 생성하는 메서드
    private static OAuthAttributes ofGoogle(String userNameAttributeName,
                                            Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .id((String) attributes.get(userNameAttributeName)) // userNameAttributeName == "sub"
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
}
