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
- BaseEntity (@MappedSuperclass)
  - 많은 Entity 에서 공통적으로 사용되는 생성/수정일시 와 같은 필드를 선언하고 상속해서 사용
```java
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseEntity {

    @CreatedDate
    @Column(name = "create_dt", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @SearchableField
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "update_dt")
    @Temporal(TemporalType.TIMESTAMP)
    @SearchableField
    private LocalDateTime modifyDate;
}
```
- @EnableJpaAuditing 적용
  - 영속화 시에 특정 Annotaion (@CreatedDate, @LastModifiedDate, @LastModifiedBy)을 사용하여 값을 자동으로 바인딩하게 설정
  - AuditorAware 구현체 (SpringSecurityAuditorAware.class), @LastModifiedBy 시 Spring Security 에서 UserId 바인딩
  - SpringBoot main class 에 추가, AuditorAware 구현
```java
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    @Nonnull
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.empty();
        }
        return Optional.of(((UserDetails) authentication.getPrincipal()).getUsername());
    }
}
```    
- Mapstruct 적용
  - Entity to record
  - record to Entity
- Key generator
  - @SequenceGenerator (Board Entity)
```java
@SequenceGenerator(
        name = "BOARD_SEQ_GENERATOR"
        , sequenceName = "board_seq"
        , initialValue = 1
        , allocationSize = 1
)
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_SEQ_GENERATOR")
    @Column(name = "board_seq")
    @SearchableField
    private Long boardSequence;
      .
      .
      .
```
  - @GenericGenerator (Project Entity)
    - IdentifierGenerator 구현 (IdGeneratorUtil)
```java
@Id
@GenericGenerator(name = "ProjectIdGenerator", type = IdGeneratorUtil.class
        , parameters = @org.hibernate.annotations.Parameter(name = GENERATOR_PARAM_KEY, value = "project"))
@GeneratedValue(generator = "ProjectIdGenerator")
@Column(name = "project_id")
private String projectId;
```
- Bulk 연산 @Modifying(clearAutomatically = true)
  - Bulk 연산 쿼리 수행 직후 영속성 컨텍스트를 clear 하여 1차 캐시와 실제 데이터간 싱크가 맞지 않는 문제를 보완
```java
@Modifying(clearAutomatically = true)
@Query(value = "update member set authority_cd = null where authority_cd = :authorityCode", nativeQuery = true)
int updateAuthority(@Param("authorityCode") String authorityCode);
```
- 연관 관계 (Sample)
  - @JoinColumn
    - referenceColumnName 을 꼭 명시해야 하는 경우
      - Join 을 하고자 하는 Entity 간의 키가 다르다
      - Join 을 하고자 하는 필드의 명칭이 다르다
        
  - @ManyToOne - @OneToMany 양방향 (Member - Team)
    - 보통 Many 쪽에 FK 가 있음.
    - cascade.REMOVE -> PK 데이터 삭제 시 FK 삭제 (영속성 전이)
    - orphanRemoval = true -> 연관관계 끊기면 FK 삭제

  - @JoincolumnOrFormula (특정 값을 참조 컬럼의 값으로 조인 시 사용, MemberTeam - Code)
```java
@JoinColumnsOrFormulas(value = {
        @JoinColumnOrFormula(column =
                  @JoinColumn(name = "rpbt_cd", referencedColumnName = "code_id", insertable = false, updatable = false)),
        @JoinColumnOrFormula(formula =
                  @JoinFormula(value = "'rpbt_cd'", referencedColumnName = "code_group_id"))
})
```
  - @OrderBy - 연관관계 설정 시 기본 정렬 조건 추가
```java
@OneToMany(mappedBy = "codeGroup")
@OrderBy("key.codeId ASC")
List<Code> codes = new ArrayList<>();
```
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
- N + 1 문제 보완
  - @EntityGraph (1depth)
    ```java
    @EntityGraph(attributePaths = {"authority", "memberTeam"})
  - @NamedEnttiyGraph (2depth more)
    ```java
    @NamedEntityGraph(
          name = "memberGraph",
          attributeNodes = {
                  @NamedAttributeNode(value = "memberTeam", subgraph = "memberTeam"),
                  @NamedAttributeNode(value = "authority"),
          },
          subgraphs = {
                  @NamedSubgraph(
                          name = "memberTeam",
                          attributeNodes = {
                                  @NamedAttributeNode("responsibilityCode"),
                                  @NamedAttributeNode("team")
                          }
                  )
          }
    )
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
  - @FieldValid 를 사용한 동적 필드 유효성 체크
- @NumericSort
  - DB column type 은 varchar 이지만 데이터가 숫자만 있는 경우, 1, 11, 12, 2, 3.. 과 같이 유니코드 정렬을 1, 2, 3, 11, 12 (으)로 정렬해야 할 때 사용
- @SearchableField
  - Entity field 에 적용하여, 해당 field 가 조회되도록 설정
  - 연관관계에 있는 field 조회시에는 columnPath 를 사용하여 객체의 field와 연결. alias 속성을 사용하여 해당 alias 로 조회 및 정렬 가능하게 설정.
    ```java
    @JoinColumn(name = "modify_member_id", referencedColumnName = "member_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY) //ManyToOne 단방향
    @SearchableField(columnPath = "member.memberName", alias = "userName")
    private Member member;
    ```
- DynamicRequest 사용 시 별도의 Field를 or 조건으로 만들어야 할 경우 활용 방식 🆕
  ```java
  //TODO. 각 And 조건으로 묶을 field 를 extractFilterByFields 를 호출하여 추출
  BooleanBuilder memberIdBooleanBuilder = dynamicBooleanBuilder.generateConditions(Member.class
                , parameter.extractFilterByFields(List.of("memberId")));

  BooleanBuilder memberNameBooleanBuilder = dynamicBooleanBuilder.generateConditions(Member.class
          , parameter.extractFilterByFields(List.of("memberName")));

  //TODO. 추출한 And 조건을 or로 묶어 줌
  BooleanBuilder orBooleanBuilder = memberIdBooleanBuilder.or(memberNameBooleanBuilder);
  ```
- QueryCondition 🆕
  - JpaDynamicRepository 에서 Specification 과 BooleanBuilder 를 같이 처리하기 위한 상위 타입 Interface
  - BooleanBuilderCondition 과 SpecificationCondition 구현체가 있음.
  - 사용 예)
    ```java
    Page<Member> page = memberRepository.findDynamicWithPageable(
        new BooleanBuilderCondition(orBooleanBuilder, orderSpecifiers),
        pageable
      );
    ```

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
  - 어떠한 Token 도 response 로 전달하지 않음. AccessToken은 Session Cookie에. Refresh Token은 DB에만 저장. (Refresh Token은 중복 로그인 체크 시 활용)

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
로그인 로직 변경 -> 추후 업로드 예정

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
