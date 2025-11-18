package com.example.spa_login.user;

import com.example.spa_login.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * UserEntity를 대상으로 DB 접근을 수행하는 JPA 리포지토리 인터페이스
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // username으로 사용자 정보 조회
    UserEntity findByUsername(String username);

    // username이 존재하는지 여부를 boolean으로 반환
    Boolean existsByUsername(String username);

    // username과 password가 일치하는 사용자 조회 (로그인 검증에 사용 가능)
    UserEntity findByUsernameAndPassword(String username, String password);
}
