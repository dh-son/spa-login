package com.example.spa_login.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 공통 API 응답 포맷을 제공하는 제네릭 DTO 클래스
 * - 모든 API 응답을 동일한 형식으로 제공
 * - 뛰어난 재사용성: Generic Type
 * - 성공과 실패를 한눈에 구분
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseDTO<T> {
    private String error; // 에러 메시지 (예: "Unauthorized", "Validation failed")
    private List<T> data; // 실제 응답 데이터 리스트 (TodoDTO, UserDTO 등 다양한 타입 가능)
}

/**
 ResponseDTO<TodoDTO>
 ResponseDTO<UserDTO>
 */

/**
 ## 사용 예시

 ### 성공 응답
 {
 "data":[
 {
 "id":1
 "title":"할 일 1",
 "done":false
 }
 {
 "id":2
 "title":"할 일 2",
 "done":true
 }
 "error":null
 }

 ### 실패 응답
 {
 "data":null
 "error":"사용자를 찾을 수 없습니다."
 }
 */
