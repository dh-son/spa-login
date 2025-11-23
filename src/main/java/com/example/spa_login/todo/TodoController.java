package com.example.spa_login.todo;

import com.example.spa_login.common.model.ResponseDTO;
import com.example.spa_login.todo.model.TodoDTO;
import com.example.spa_login.todo.model.TodoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 사용자 할 일(Todo) 목록을 CRUD 처리하는 REST 컨트롤러
 */
@RequiredArgsConstructor
@RestController // REST 컨트롤러 선언: 자동으로 JSON 형식의 응답으로 변환되어 클라이언트에게 전달 (@Controller + @ResponseBody)
@RequestMapping("todo") // "/todo" 경로로 요청 매핑
public class TodoController {

    private final TodoService todoService;

    // 할 일 생성 API
    @PostMapping
    public ResponseEntity<?> createTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO todoDto) {
        try {
            TodoEntity todoEntity = TodoDTO.toEntity(todoDto); // DTO -> Entity 변환
            todoEntity.setId(null); // 새 엔터티 이므로 ID는 null 처리
            todoEntity.setUserId(Long.parseLong(userId)); // 인증된 사용자 ID 설정

            // 서비스 호출
            List<TodoEntity> todoEntities = todoService.create(todoEntity);

            // 엔터티 리스트 -> DTO 리스트로 변환
            List<TodoDTO> todoDtos = todoEntities.stream()
                    .map(TodoDTO::new)
                    .collect(Collectors.toList());

            // 정상 응답 객체 생성
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder()
                    .data(todoDtos).build();

            return ResponseEntity.ok().body(response); // 200 OK 응답
        } catch (Exception e) {
            String error = e.getMessage(); // 예외 메시지 추출
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder() // 에러 응답 객체 생성
                    .error(error).build();
            return ResponseEntity.badRequest().body(response); // 400 Bad Request 응답
        }
    }

    // 할 일 목록 조회 API
    @GetMapping
    public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userId) {
        // 사용자 ID로 할 일 목록 조회
        List<TodoEntity> todoEntities = todoService.retrieve(Long.parseLong(userId));

        // Entity -> DTO 변환
        List<TodoDTO> todoDtos = todoEntities.stream()
                .map(TodoDTO::new)
                .collect(Collectors.toList());

        // 응답 생성
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder()
                .data(todoDtos).build();

        return ResponseEntity.ok().body(response); // 200 OK 응답
    }

    // 할 일 수정 API
    @PutMapping
    public ResponseEntity<?> updateTodo(@AuthenticationPrincipal String userId,
                                         @RequestBody TodoDTO todoDTO) {
        TodoEntity entity = TodoDTO.toEntity(todoDTO); // DTO -> Entity 변환
        entity.setUserId(Long.parseLong(userId)); // 사용자 ID 설정

        // 업데이트 요청
        List<TodoEntity> entities = todoService.update(entity);

        // 결과 반환
        List<TodoDTO> dtos = entities.stream()
                .map(TodoDTO::new)
                .collect(Collectors.toList());

        // 응답 객체 생성
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder()
                .data(dtos).build();

        return ResponseEntity.ok().body(response); // 200 OK 응답
    }

    // 할 일 삭제 API
    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@AuthenticationPrincipal String userId,
                                        @RequestBody TodoDTO todoDTO) {
        try {
            TodoEntity entity = TodoDTO.toEntity(todoDTO); // DTO -> Entity 변환
            entity.setUserId(Long.parseLong(userId)); // 사용자 ID 설정

            // 삭제 요청
            List<TodoEntity> entities = todoService.delete(entity);

            // 결과 반환
            List<TodoDTO> dtos = entities.stream()
                    .map(TodoDTO::new)
                    .collect(Collectors.toList());

            // 응답 객체 생성
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder()
                    .data(dtos).build();

            return ResponseEntity.ok().body(response); // 200 OK 응답
        } catch (Exception e) {
            String error = e.getMessage(); // 예외 메시지 추출
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder() // 에러 응답 객체 생성
                    .error(error).build();
            return ResponseEntity.badRequest().body(response); // 400 Bad Request 응답
        }
    }
}
