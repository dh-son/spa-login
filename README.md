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
- 서비스 클래스 정의
  - TodoService: 할 일(TodoEntity)에 대한 비즈니스 로직을 처리하는 서비스 클래스
  - UserService: 사용자 등록 및 인증을 처리하는 서비스 클래스
- JWT 관리 클래스 정의
  - TokenProvider: JWT 토큰을 생성하고 검증, 사용자 ID 추출하는 역활을 수행하는 클래스
- JWT 기반 인증 처리 필터 클래스 정의
  - JwtAuthenticationFilter: HTTP 요청에서 JWT 토큰을 추출하고 인증 정보를 설정하는 필터 클래스
    - OncePerRequestFilter: HTTP 요청당 한 번만 실행되는 필터를 만들 수 있음