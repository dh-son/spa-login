package com.example.spa_login.todo;

import com.example.spa_login.todo.model.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TodoEntity를 관리하는 JPA 리포지토리 인터페이스
 */
@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, Long> {

    // 특정 사용자(userId)에 속한 모든  Todo 목록을 조회하는 메서드
    List<TodoEntity> findByUserId(Long userId);

    // JPQL을 사용하여 UserId로 단일 TodoEntity를 조회하는 커스텀 쿼리
    @Query("SELECT t FROM TodoEntity t WHERE t.userId =?1 userId")
    TodoEntity findByUserIdQuery(Long userId);
}
