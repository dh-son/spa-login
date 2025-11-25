package com.example.spa_login.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 사용자 정보를 저장사는 JPA 엔터티 클래스
 * - 로그인, 회원가입, 권한 관리 등 사용자와 관련된 모든 핵심정보 포함
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(
        uniqueConstraints = { @UniqueConstraint(columnNames = "username")} // username 컬럼은 유일해야 함
)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 사용자 고유 ID

    @Column(nullable = false) // username은 null이 될 수 없음
    private String username; // 사용자명 (email 등으로 사용 가능)

    private String password; // 사용자 비밀번호 (소셜 로그인 시 null 가능)

    private String role; // 사용자 역활 (예: ROLE_USER, ROLE_ADMIN)

    private String authProvider; // OAuth 제공자 정보

}
