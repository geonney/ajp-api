# API Spec
![Static Badge](https://img.shields.io/badge/java-v17-blue) ![Static Badge](https://img.shields.io/badge/springboot-v3.2.4-blue) ![Static Badge](https://img.shields.io/badge/mapstruct-v1.5.5.Final-blue) ![Static Badge](https://img.shields.io/badge/lombok-v1.18.30-blue)
![Static Badge](https://img.shields.io/badge/JJWT-v0.12.5-blue) ![Static Badge](https://img.shields.io/badge/Jasypt-v3.0.5-blue)

![Static Badge](https://img.shields.io/badge/API-v1.0.0-green) [![Static Badge](https://img.shields.io/badge/welcome-aljjabaegi.tistory.com-hotpink)](http://aljjabaegi.tistory.com)

## :heavy_check_mark:Swagger (config/swagger)

- 기본 API Information 설정
- JWT 인증 버튼 추가
- API Group 설정
- OperationCustomzier 적용, Operation 공통 response 추가
- tag, operation sorting
- display-request-duration 설정
- display-operation-id 설정
- filter 추가

## :heavy_check_mark:JPA (common/jpa)
- BaseEntity (@MappedSuperclass) 적용
- @EnableJpaAuditing 적용
  - 영속화 시 값 자동 바인딩
  - AuditorAware 구현체 (SpringSecurityAuditorAware.class) - @LastModifiedBy 시 Spring Security 에서 UserId 바인딩
  - @CreatedDate, @LastModifiedDate, @LastModifiedBy
- Mapstruct 적용
- @SequenceGenerator (Board Entity)
- @GenericGenerator (Project Entity)
- Bulk 연산 @Modifying(clearAutomatically = true)
- 연관 관계
  - @ManyToOne - @OneToMany 양방향
- 복합키 관련 (HistoryLogin Entity)
  - @Embeddable
  - @EmbeddedId
- Entity
  - @Enumerated(EnumType.STRING)
  - @Temporal
  - @Id
  - @EmbeddedId, @Embeddable
- namedNativeQuery (Project)
- paging and sorting and condition (Login history)
- CriteriaBuilder Specification (Team)
- Dynamic Specification (Member) :star2:
  - filtering, sorting, paging 동적 처리
  - Operators enum 사용
    - equal, notEqual, like, between, in
  - DynamicFilter 를 사용하여 동적으로 Specification 을 생성 (common/request)
    - Case-insensitive search
    - Reference field search (call getPath method)
    - Null data search
    - equal, notEqual, in -> LocalDateTime type 을 제외하고 사용 가능
    - like -> String type 만 사용 가능
    - Between -> String type 을 제외하고 사용 가능
  - DynamicSoter 를 사용하여 동적으로 Sort 생성

## :heavy_check_mark:Exception (common/exception)
- 전역 Exception Handler 적용, GlobalExceptionHandler
- Custom Exception 활용, checked Exception 처리, ServiceException

## :heavy_check_mark:Standardization (common/response)
- 공통 에러 코드 enum 정의, CommonErrorCode
- 동적 request 처리를 위한 DynamicFilter -> DynamicSpecification 에서 처리
  - Dynamic Request (pageNo, pageSize, DynamicFilter list, DynamicSorter list)
- 공통 response 구조체 정의
  - 단수 객체 ItemResponse
  - 복수 객체 ItemsResponse
  - 그리드 복수 객체 GridItemsResponse (with Page information)
  - 오류 객체 ErrorResponse

## :closed_lock_with_key:Security (config/security)

#### Spring Security
- CSRF
- CORS
- SecurityFilter
- JwtFilter
- RequestFilter
  - request.getReader() 를 복수 호출 할 수 있도록 HttpServletRequestWrapper Filter 추가
  - Invalid json format exception 발생 시 해당 json 로깅
- 401 Handler, JwtAuthenticationEntiryPoint
- 403 Handler, JwtAccessDeniedHandler
- 단방향 암호화 DelegatingPasswordEncoder 적용 (bcrypt, sha-256 with salt)

#### JWT (Java Web Token)
- Token 유효성 검증 로직, 중복로그인 검증, JwtFilter
- Token 생성, 추출, Token Claim 추출, Token 만료, Token 유효성 체크, TokenProvider
- 401 Handler, JwtAuthenticationEntryPoint
- 403 Handler, JwtAccessDeniedHandler
- 로그인 한 브라우져 간 인증 유지 -> Cookie 에 Access token 을 담아 전달
- Refresh token -> 로그인 시 전달. Access token 만료 시 Header Bearer 에 refresh token을 전달하면 자동으로 AccessToken 갱신.

#### Jasypt
- application 설정파일 Text 암호화

#### RSA
- 비대칭 암호화 (private, public key)
- Public key 전달 -> 패스워드 암호화
- public key로 암호화된 패스워드 전달 -> private key 복호화


#### Authentication logic
![image](https://github.com/aljjabaegiProgrammer/ajp_api/assets/148036230/d272debc-aff8-4281-961a-0da4accdbcf9)
