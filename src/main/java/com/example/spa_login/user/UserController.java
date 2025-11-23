package com.example.spa_login.user;

import com.example.spa_login.common.model.ResponseDTO;
import com.example.spa_login.security.jwt.TokenProvider;
import com.example.spa_login.user.model.UserDTO;
import com.example.spa_login.user.model.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자 회원가입 및 로그인 처리를 담당하는 컨트롤러
 */
@Slf4j
@RequiredArgsConstructor
@RestController // @Controller + @ResponseBody
@RequestMapping("/auth") // "/auth" 경로로 진입하는 요청 처리
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //회원가입 처리 메서드
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
            // 비밀번호가 없으면 예외 발생
            if (userDTO == null || userDTO.getPassword() == null) {
                throw new RuntimeException("Invalid Password value");
            }

            // 사용자 엔터티 생성 및 비밀번호 암호화
            UserEntity user = UserEntity.builder()
                    .username(userDTO.getUsername())
                    .password(passwordEncoder.encode(userDTO.getPassword()))
                    .build();

            // 사용자 등록
            UserEntity registeredUser = userService.create(user);

            // 응답 객체 생성 (비밀번호 제외)
            UserDTO responseUserDTO = UserDTO.builder()
                    .id(registeredUser.getId())
                    .username(registeredUser.getUsername())
                    .build();

            return ResponseEntity.ok().body(responseUserDTO); // 200 OK 응답

        } catch (Exception e) {
            // 예외 발생 시 에러 메시지 포함 응답 반환
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity.badRequest().body(responseDTO); // 400 Bad Request
        }
    }

    // 로그인 처리 메서드
    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
        // 입력받은 사용자 정보로 인증 시도
        UserEntity user = userService.getByCredentials(userDTO.getUsername(),
                userDTO.getPassword(), passwordEncoder);

        if (user != null) {
            // 인증 성공 시 JWT 토큰 발급
            final String token = tokenProvider.create(user);

            // 응답 객체에 사용자 정보 및 토큰 포함
            final UserDTO responseUserDTO = UserDTO.builder()
                    .username(user.getUsername())
                    .id(user.getId())
                    .token(token)
                    .build();

            return ResponseEntity.ok().body(responseUserDTO); // 200 OK 응답
        } else {
            // 인증 실패 시 에러 응답
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Login failed")
                    .build();

            return ResponseEntity.badRequest().body(responseDTO); // 400 Bae Request
        }
    }
}
