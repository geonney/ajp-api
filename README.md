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
  - Entity to record
  - record to Entity
- Key generator
  - @SequenceGenerator (Board Entity)
  - @GenericGenerator (Project Entity)
- Bulk 연산 @Modifying(clearAutomatically = true)
- 연관 관계 (Sample)
  - @JoinColumn (자식-부모 간의 키가 다르거나 명칭이 다를 경우 꼭 referenceColumnName을 명시해야 함)
  - @ManyToOne - @OneToMany 양방향 (Member - Team)
  - @JoincolumnOrFormula (특정 값을 참조 컬럼의 값으로 조인 시 사용, MemberTeam - Code)
  - @OrderBy - 연관관계 설정 시 기본 정렬 조건 추가
- 복합키 관련 (HistoryLogin Entity)
  - @Embeddable
  - @EmbeddedId
  - @MapsId (복합 키에 FK가 있을 경우 PK 로 설정)
  - @IdClass, @Id 보다 @EmbeddedId 가 보다 객체지향적인 방법
- Entity
  - @Enumerated(EnumType.STRING)
    - Enum 속성 사용 시 필수 추가 (타입 변환 관련 에러 발생)
  - @Temporal
    - 날짜 타입 매핑 시 필수 추가 (명시적)
    - 기본 타입은 Timestamp
  - @Formula
    - Entity 조회 시 value 값이 쿼리로 치환 됨
  - @Transient (영속성 대상에서 제외 필드 사용 시)
  - @SQLRestriction (Entity 조회 시 항상 추가되어야 하는 Where 조건 처리 시 사용)
- namedNativeQuery (Project)
- paging and sorting and condition (Login history)
- CriteriaBuilder Specification (Team)
- Dynamic Specification (Member)
  - filtering, sorting, paging 동적 처리
  - Operators enum 사용
    - equal, notEqual, like, between, in, less then equal, greater than equal
  - DynamicFilter 를 사용하여 동적으로 Specification 을 생성 (common/request)
    - Case-insensitive search
    - Reference field search (call getPath method) -> depth 없이 조회할수 있도록 보완 (DynamicSpecification>getSearchFieldPath)
    - Null data search
    - equal, notEqual, in -> LocalDateTime type 을 제외하고 사용 가능
    - like -> String type 만 사용 가능
    - Between -> String type 을 제외하고 사용 가능
  - DynamicSoter 를 사용하여 동적으로 Sort 생성
- DynamicRepository (Custom JpaRepository)
  - JpaRepository 의 기능을 확장하여 서비스 로직 코드를 줄이는 역할
  - SimpleJpaRepository + DynamicRequest 를 사용한 메서드 추가 구현
  - 기존 Repository에서 extends JpaRepository -> extends JpaDynamicRepository 로 변경 하여 확장 메서드 사용
  - DynamicSpecfication 메서드 활용
- Querydsl 적용 (Login history)
  - DynamicBooleanBuilder 
    - DynamicSpecification 과 같이 동적 BooleanBuilder를 생성해주는 클래스
    - BooleanBuilder 사용 시 조회할 값을 객체를 부모부터 참조해 가지 않으면 right join 으로 hibernate 에서 쿼리를 생성하니 주의!
  - DynamicDslRepository (Custom JpaRepository)
    - DynamicRepository 와 같은 기능 (Querydsl 로 동작)

## :heavy_check_mark:Dynamic 관련 클래스 (common/jpa/dynamicSearch) :star2:
- DynamicRequest record 를 사용한 동적 조회 및 정렬 조건, 페이징 처리
- DynamicConditions interface
  - 동적 처리를 위한 interface
  - 구현체 -> DynamicBooleanBuilder, DynamicSpecification
- DynamicRepository interface
  - JpaRepository의 기능 확장용 interface
  - 구현체 -> JpaDynamicRepositoryImpl, JpaDynamicDslRepositoryImpl
  - 각각 Specification 과 querydsl 방식으로 구분
  - BaseRepository 설정으로 사용 방식 설정 가능
- dsl 이 변경될 경우 DynamicConditions, DynamicRepository 를 구현하여 확장 가능
- @DefaultSort
  - Dynamic 을 사용할 때 sort 값이 넘어오지 않았을 경우 entity의 기본 정렬 컬럼을 설정하는 annotation (Apply Board, Project entity)
- @DynamicValid
  - DynamicRequest 를 사용할 경우 필수 값 유효성 체크 (@DynamicValid(essentialFields = "memberId:멤버 ID", "memberName:멤버 명"))
- @NumericSort
  - DB column type 은 varchar 이지만 데이터가 숫자만 있는 경우, 1, 11, 12, 2, 3.. 과 같이 유니코드 정렬을 1, 2, 3, 11, 12 (으)로 정렬해야 할 때 사용

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

## :heavy_check_mark:Logging (resources/logback/logback-spring.xml)
  - folder : base driver/logs/AJP-${active}
  - Info level log 는 시간 단위 TimeBaseRollingPolicy
  - Error level log 는 분 단위 TimeBaseRollingPolicy
  - Request, Response log 는 시간 단위 TimeBaseRollingPolicy (only file)
    - AOP 로 동작
    - 별도의 파일로만 관리

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

#### Password
- 유효성 검증 class (common/util/PasswordUtil)
- Level enum 사용 (PasswordLevel)
  - Level1: 8자리 이상, 하나 이상의 문자, 숫자
  - Level2: 8자리 이상, 하나 이상의 문자, 숫자, 특수문자
  - Level3: 8자리 이상, 하나 이상의 대문자, 소문자, 숫자, 특수문자
- 기본 정규식 사용 (RegExp list)
  - 자릿수, 대문자, 숫자, 특수문자 포함 여부
  - RegExp List 로 원하는 유효성 검증 추가 방식

#### Authentication logic
![image](https://github.com/aljjabaegiProgrammer/ajp_api/assets/148036230/d272debc-aff8-4281-961a-0da4accdbcf9)

#### Excel
- POI 사용
- Excel download
  - record 에 annotation (@ExcelFile, @ExcelColumn)을 추가하는 사용 방식
  - tmeplate file 을 가지고 처리하는 방식
  - 동적 헤더와 동적 데이터로 처리하는 방식


#### ETC
- @JsonFormat
  - 객체가 ResponseBody 로 직렬화 될 때 Json 포멧을 설정
  - Mapper 에서 형변환을 해주는 것과 결과는 같음
  - namedNativeQuery 를 사용하여 sql-result-set-mapping 으로 객체에 매핑할 경우에는 오류 발생 (InstantiationException)
