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
- 스프링 시큐리티 설정
  - WebSecurityConfig: 애플리케이션의 웹 보안을 구성하는 설정 클래스
    - Stateless, JWT 필터 추가, 403 처리는 핵심 설정
- 컨트롤러 클래스 정의
  - TodoController: 사용자 할 일(Todo) 목록을 CRUD 처리하는 REST 컨트롤러
  - UserController: 사용자 회원 가입 및 로그인 처리를 담당하는 컨트롤러

## 2.일반 로그인 기능
- 회원 가입
  - 사용자가 회원가입 페이지에 접근
  - 회원가입 폼 작성 및 제출
  - API 요청 발생
  - Spring Controller에서 회원 가입 처리
  - DB 저장 (중복 체크 포함)
  - 실제 DB 테이블 구조
  - 응답 생성 및 전달
  - React에서 로그인 페이지로 이동
- 일반 로그인
  - 로그인 폼 제출
  - 로그인 API 호출
  - 로그인 요청 처리
  - 비밀번호 검증
  - JWT 토큰 발급 및 Client에 전달
  - React에서 JWT 저장
  - API 요청 시 토큰 포함
  - JWT 검증 및 사용자 인증
  - 인증된 사용자 정보 사용
- 할 일 추가
  - 사용자가 할 일을 입력하고 버튼 클릭
  - 부모(App)에서 API 요청 실행
  - 실제 API 요청 전송
  - Spring Boot Controller에서 요청 받기
  - DB에 저장 처리
  - DB 저장 후 응답 데이터
  - React에서 응답 받아 상태 업데이트
  - 전체 목록 제렌더링
- 할 일 목록 조회
  - React 앱 실핼 시 목록 요청
  - API 요청 처리 (JWT 토큰 포함)
  - Spring Security에서 JWT 인증
  - Controller에서 목록 처리
  - Service에서 목록 조회
  - Repository에서 실제 DB 조회
  - Client에 응답 후 렌더링
- 할 일 수정
  - 사용자가 할 일 텍스트 수정
  - API 호출로 수정 요청 전송
  - HTTP 요청 내부 처리
  - JWT 인증 필터에서 사용자 검증
  - Controller에서 수정 처리
  - Service에서 DB 수정
  - DB에서 자동 저장
  - Client에서 목록 갱신
- 할 일 삭제
  - 사용자가 삭제 버튼 클릭
  - Backend에 삭제 요청 보내기
  - API 요청 처리
  - JWT 인증 처리
  - Controller에서 삭제 처리
  - Service에서 DB 삭제 로직 수행
  - Repository에서 실제 삭제 처리
  - React에서 응답 받은 목록 렌더링
- 로그아웃 (Client에서만 처리)
  - 사용자가 로그아웃 버튼 클릭
  - 토큰 삭제 및 로그인 페이지로 이동
  - 로그아웃 후 인증된 페이지에 접근
  - 인증 실패 시 자동 리다이렉트 (403 error)

## 3.소셜 로그인
- 소셜 로그인 후 사용자 정보 매핑 클래스
  - OAuthAttributes: 
    - 소셜 로그인 과정에서 전달 받은 사용자 정보를 우리 서비스에 받에 변환해서 담아주는 클래스
    - OAuth2UserService를 구현한 클래스 내부에서 사용
    - OAuth 인증 후 사용자 정보를 등록하거나 확인할 때 자주 사용
- 소셜 로그인 사용자 정보 클래스
  - CustomUser:
    - OAuth2 로그인 사용자 정보를 담는 CustomUser 클래스
    - Spring Security의 DefaultOAuth2User를 확장하여 사용자 ID, 이메일, 사용자명을 추가로 보관
    - 소셜 로그인 이후 인증된 사용자 정보를 우리 서비스의 요구사항에 맞게 가공 또는 저장
    - DefaultOAuth2User: Spring Security가 소셜 로그인을 처리할 때 사용자 정보를 담기 위해 기본으로 사용하는 클래스
- 소셜 로그인 사용자 정보와 회원 가입 처리 클래스
  - CustomOAuth2UserService:
    - Spring Security OAuth2 로그인 시 사용자를 처리하는 핵심 서비스 클래스
    - 소셜 로그인 성공 후 자동으로 호출되어, 사용자 정보를 가져오고 가공해서 CustomUser 객체로 반환
    - OAuth2UserService: Spring Security가 소셜 로그인 처리 중, OAuth2 서버로부터 사용자 정보를 받아왔을때 호출하는 서비스 역활
- 리디렉션 URL을 쿠키에 저장하는 클래스
  - RedirectUrlCookieFilter:
    - Spring Security에서 소셜 로그인 처리할 때, Client가 요청한 리디렉션 URL을 쿠키에 저장하는 용도로 사용
    - 소셜 로그인 요청 시 Client가 보낸 redirect_url 파라미터를 쿠키로 저장해 놓고, 로그인 완료 후 해당 URL로 리다이렉트 하기 위한 기반 마련
    - 소셜 로그인 시작 시 Frontend에서 전달된 redirect_url을 쿠키에 저장해주는 필터
    - OncePerRequestFilter: 요청일 들어올 때마다 한번만 실행되도록 보장
- 사용자 정의 성공 핸들러
  - OAuthSuccessHandler:
    - 소셜 로그인 성공 후 동작하는 커스텀 성공 핸들러 클래스
    - 로그인 성공 -> JWT 토큰 생성 -> Client가 요청한 리디렉션 URL로 토큰을 포함하여 리다이렉트
    - SimpleUrlAuthenticationSuccessHandler: OAuth2 로그인 성공 시 처리 로직 담당 클래스
- 스프링 시큐리티 설정
  - WebSecurityConfig: OAuth2 로그인 설정
    - CustomOAuth2UserService: 사용자 정보를 로드할 사용자 정의 OAuth2 서비스
    - OAuthSuccessHandler: OAuth2 로그인 성공 시 동작할 핸들러
    - RedirectUrlCookieFilter: 리다이렉트 URI 정보를 쿠키에 저장하는 필터
- 소셜 로그인 기능 흐름
  1. 프론트에서 로그인 화면 진입: Login.jsx
    - /Login 경로에 진입, 구글로 로그인하기 버튼 클릭, handleSocialLogin("google") 함수 호출
    - socialLogin 함수로 연결
    - 백엔드의 '/oauth2/authorization/google' 주소로 이동 및 현재 프론트의 주소(로그인 완료 후 다시 돌아올 위치)도 같이 전송
  2. 백엔드에서 리디렉션 주소 저장: RedirectUrlCookieFilter.java
    - 백엔드에 요청이 도달하면 RedirectUrlCookieFilter.java에서 redirect_url 값을 읽어서 쿠키에 저장
      (로그인 처리 종료 후 어디로 다시 리디렉션해야 할지 기억)
  3. Spring Security를 통한 OAuth 로그인 처리: WebSecurityConfig.java/oauth2Login()
    - CustomOAuth2UserService: 사용자 정보를 받아옴
    - OAuthSuccessHandler: 로그인 성공 이후의 처리를 하게 됨
  4. 사용자 정보 처리 및 저장: CustomOAuth2UserService.java
    - 구글로부터 받은 사용자 정보를 바탕으로 사용자 정보를 파싱하고 DB에 저장하거나 기존 사용자를 조회
    - 처음 로그인한 유저는 자동으로 회원가입, 이후에는 기존 정보를 재사용
  5. JWT 토큰 생성 및 프론트로 리디렉션: OAuthSuccessHandler.java
    - 로그인 성공 시 JWT 토큰 생성
    - 토큰은 쿠키에 저장했던 주소로 리디렉션하면서 URL 파라미터로 전달
  6. 프론트에서 토큰 저장 및 이동
    - /sociallogin 경로로 이동하면서 URL에 토큰이 포함
    - SocialLogin.jsx: 이 토큰을 읽어 localStorate에 저장, 홈 화면 '/'로 자동 이동
  7. API 요청 시 토큰 포함
    - 사용자가 서버에 API 요청을 보낼 때마다, 이 토큰이 자동으로 포함되도록 ApiService.js에서 설정
  8. 백엔드에서 토큰 인증: JwtAuthenticationFilter.java
    - JwtAuthenticationFilter.java에서 토큰을 파싱하고 검증
    - Controller에서 '@AuthenticationPrincipal String userId' 와 같이 주입받아서 사용자의 정보를 활용
