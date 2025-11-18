package com.example.spa_login.user;

import com.example.spa_login.user.model.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 사용자 등록 및 인증을 처리하는 서비스 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    // 새 사용자를 등록하는 메서드
    public UserEntity create(final UserEntity userEntity) {
        // 사용자 정보가 null이거나 username이 없으면 예외 발생
        if (userEntity == null || userEntity.getUsername() == null) {
            throw new RuntimeException("Invalid arguments");
        }

        final String username = userEntity.getUsername(); // 사용자명 추출

        // 이미 같은 사용자 명이 존재하는지 확인
        if (userRepository.existsByUsername(username)) {
            log.warn("Username already exists {}", username); // 중목 경고 로그 출력
            throw new RuntimeException("Username already exists"); // 중복 사용자 예외
        }

        return userRepository.save(userEntity);
    }

    // 사용자 인증 메서드: username과 password를 비교하여 사용자 반환
    public UserEntity getByCredentials(final String username,
                                       final String password,
                                       final PasswordEncoder passwordEncoder) {
        final UserEntity originalUser = userRepository.findByUsername(username); // 사용자명으로 사용자 조회

        // 사용자 존재 및 비밀번호 일치 여부 확인
        if (originalUser != null && passwordEncoder.matches(password, originalUser.getPassword())) {
            return originalUser; // 인증 성공 시 사용자 객체 반환
        }

        return null; // 인증 실패 시 null 반환
    }
}
