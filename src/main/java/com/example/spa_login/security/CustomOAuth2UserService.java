package com.example.spa_login.security;

import com.example.spa_login.security.model.CustomUser;
import com.example.spa_login.security.model.OAuthAttributes;
import com.example.spa_login.user.UserRepository;
import com.example.spa_login.user.model.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring Security OAuth2 로그인 시 사용자를 처리하는 핵심 서비스 클래스
 * 소셜 로그인 성공 후, 사용자 정보를 가져오고 가공해서 CustomUser 객체로 반환
 * OAuth2UserService: Spring Security가 소셜 로그인 처리 중,
 *                    OAuth2 서버로부터 사용자 정보를 받아왔을때 호출하는 서비스 역활
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    // OAuth2 로그인에 성공했을 때 자동으로 호출
    // OAuth2 로그인 사용자의 정보를 가져와서 CustomUser 객체로 변환하는 메서드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("loadUser");

        // 기본 OAuth2 사용자 정보 제공 서비스 생성
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest); // 사용자 정보 조회

        // OAuth2 공급자 이름 (ex. google, naver, kakao, github 등)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 사용자 식별을 위한 키 이름 (ex. id, sub 등)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        // 로그 출력
        log.info("loadUser registrationId = " + registrationId);
        log.info("loadUser userNameAttributeName = " + userNameAttributeName);

        // 공급자로부터 받은 사용자 정보를 OAuthAttributes DTO로 매핑
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // 사용자 정보 추출
        String nameAttributeKey = attributes.getNameAttributeKey();
        String name = attributes.getName();
        String email = attributes.getEmail();
        String picture = attributes.getPicture();
        String id = attributes.getId();
        String socialType = registrationId;

        // 로그 출력
        log.info("loadUser nameAttributeKey = " + nameAttributeKey);
        log.info("loadUser id = " + id);
        log.info("loadUser socialType = " + socialType);
        log.info("loadUser name = " + name);
        log.info("loadUser email = " + email);
        log.info("loadUser picture = " + picture);
        log.info("loadUser attributes =" + attributes);

        // null 방지 처리
        if (name == null) name = "";
        if (email == null) email = "";

        // 권한 목록 생성 (기본 권한 ROLE_USER 부여)
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        authorities.add(authority);

        String username = email;
        String authProvider = socialType;

        // 사용자 정보가 DB에 없으면 새로 저장
        UserEntity userEntity = getUserEntity(username, authProvider);
        log.info("Successfully pulled user info username {} authProvider {}", username, authProvider);

        // 사용자 정보를 CustomUser 객체로 반환
        return new CustomUser(userEntity.getId(), email, name, authorities, attributes);
    }

    // 사용자 정보가 DB에 없으면 새로 저장
    private UserEntity getUserEntity(String username, String authProvider) {
        if (!userRepository.existsByUsername(username)) {
            UserEntity userEntity = UserEntity.builder()
                    .username(username)
                    .authProvider(authProvider)
                    .build();
            return userRepository.save(userEntity); // DB에 저장
        }

        return userRepository.findByUsername(username); // 기존 사용자 조회
    }
}
