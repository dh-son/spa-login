# SPA-로그인

## 1.일반 로그인 코드
- 엔터티 클래스 정의
  - TodoEntity: Todo 정보를 저장하는 데이터베이스 테이블과 매핑되는 JPA 엔터티 클래스
  - UserEntity: 사용자 정보를 저장사는 JPA 엔터티 클래스
- DTO 클래스 정의
  - TodoDTO: 클라이언트와의 데이터 전송을 위한 Todo DTO 클래스
  - UserDTO: 사용자 정보 전달을 위한 DTO (Data Transfer Object)
  - ResponseDTO: 공통 API 응답 포맷을 제공하는 제네릭 DTO 클래스
- 리포지토리 인터페이스 정의
  - TodoRepository: TodoEntity를 관리하는 JPA 리포지토리 인터페이스
  - UserRepository: UserEntity를 대상으로 DB 접근을 수행하는 JPA 리포지토리 인터페이스