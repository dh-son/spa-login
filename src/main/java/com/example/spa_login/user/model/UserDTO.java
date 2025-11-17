package com.example.spa_login.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

/**
 * 사용자 정보 전달을 위한 DTO (Data Transfer Object)
 * - 로그인 요청할 때 사용
 * - 로그인 응답할 때 사용
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {
    private String token; // 인증 토큰 (JWT 등), 로그인 후 클라이언트에 전달
    private String username;
    @JsonIgnore
    private String password;
    private Long id;
}

/**
### 첫째, 로그인 요청 시
 {
 "username":"user123",
 "password":"secret"
 }

### 둘째, 로그인 응답 시
 {
 "id":1,
 "username":"user123",
 "token":"jskfdksjfl..."
 }
 */