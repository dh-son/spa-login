package com.example.spa_login.todo;

import com.example.spa_login.todo.model.TodoEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 할 일(TodoEntity)에 대한 비즈니스 로직을 처리하는 서비스 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TodoService {

    private final TodoRepository todoRepository;

    // 새로운 Todo 항목을 생성하고, 사용자 ID의 모든 Todo 목록 반환
    public List<TodoEntity> create(final TodoEntity entity) {
        validate(entity); // 유효성 검사

        todoRepository.save(entity); // DB 저장

        log.info("Entity Id: {} is saved", entity.getId()); // 저장 완료 로그 출력

        // 사용자 ID로 할 일 목록 조회 및 반환
        return todoRepository.findByUserId(entity.getUserId());
    }

    // Todo 엔터티에 대한 유효성 검사 메서드
    private void validate(final TodoEntity entity) {
        if (entity == null) {
            log.warn("Entity cannot be null"); // null 체크
            throw new RuntimeException("Entity cannot be null");
        }

        if (entity.getUserId() == null) {
            log.warn("Unknown user"); // userId가 없으면 예외 처리
            throw new RuntimeException("Unknown user");
        }
    }

    // 특정 사용자의 모든 Todo 항목을 조회
    public List<TodoEntity> retrieve(final Long userId) {
        return todoRepository.findByUserId(userId); // userId로 할일 목록 조회
    }

    // 기존 Todo 항목을 수정하고, 수정 후 해당 사용자 ID의 Todo 목록 반환
    public List<TodoEntity> update(final TodoEntity entity) {
        validate(entity); // 유효성 검사

        // DB에서 기존 엔터티 조회
        final Optional<TodoEntity> original = todoRepository.findById(entity.getId());

        // 존재할 경우 제목과 완료 여부만 수정
        original.ifPresent(todo -> {
            todo.setTitle(entity.getTitle()); // 제목 수정
            todo.setDone(entity.isDone());  // 완료 여부 수정

            todoRepository.save(todo); // 수정된 엔터티 저장
        });

        return retrieve(entity.getUserId()); // 사용자 ID로 전체 목록 반환
    }

    // Todo 항목을 삭제하고, 삭제 후 해당 사용자의 ID의 Todo 목록 반환
    public List<TodoEntity> delete(final TodoEntity entity) {
        validate(entity); // 유효성 검사

        try {
            todoRepository.delete(entity); // 삭제 수행
        } catch (Exception e) {
            log.error("error deleting entity" + entity.getId(), e); // 삭제 중 에러 로그 출력

            // 삭제 실패 시 예외 발생
            throw new RuntimeException("error deleting entity" + entity.getId());
        }

        return retrieve(entity.getUserId()); // 사용자 ID로 전체 목록 반환
    }
}
