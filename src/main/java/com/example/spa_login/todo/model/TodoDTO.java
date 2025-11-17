package com.example.spa_login.todo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 클라이언트와의 데이터 전송을 위한 Todo DTO 클래스
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TodoDTO {

    private Long id;
    private String title;
    private boolean done;

    // 엔터티 객체를 기반으로 DTO를 생성하는 생성자
    public TodoDTO(final TodoEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.done = entity.isDone();
    }

    // DTO 객체를 엔터티 객채로 변환하는 정적 메서드
    public static TodoEntity toEntity(final TodoDTO dto) {
        return TodoEntity.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .done(dto.isDone())
                .build();
    }
}
