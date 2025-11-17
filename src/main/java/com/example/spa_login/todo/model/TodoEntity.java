package com.example.spa_login.todo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Todo 정보를 저장하는 데이터베이스 테이블과 매핑되는 JPA 엔터티 클래스
 */
@Builder // 객체 생성 시 빌더 패턴으로 사용할 수 있도록 함
@NoArgsConstructor // 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 포함한 생성자 자동 생성
@Data // @Getter, @Setter, @ToString, @EqualsAndHashCode 등을 자동 생성
@Entity // 클래스가 JPA 엔터티임을 명시
@Table(name = "Todo") // 엔터티가 매핑될 테이블 이름 지정
public class TodoEntity {

    @Id //기본 키(PK) 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 전략 사용 (MySQL AUTO_INCREMENT 등)
    private Long id; // 할 일을 고유 ID

    private Long userId; // 할 일을 작성한 사용자 ID

    private String title; // 할 일 제목 또는 내용

    private boolean done; // 완료 여부 (true: 완료, false: 미완료)
}
