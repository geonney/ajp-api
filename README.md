**#API**
====
API 구현 기능
----
```
Swagger (config/swagger/SwaggerConfig)
- Information setting
- grouping
- Operation sorting
- tag sorting
- display-resquest-duration
- filter

JPA (common/jpa)
- BaseEntity (@MappedSuperclass)
- Mapstruct

Exception (common/exception)
- GlobalExceptionHandler
- Custom Exception (ServiceException)

Standardization
- response (item, items, error)

Spring Security (config/security/spring/SecurityConfig)
- CSRF
- CORS
- Filter
- DelegatingPasswordEncoder (bcrypt, sha-256 with salt)

JWT (config/security/jwt)
- JwtFilter
- TokenProvider
- JwtAccessDeniedHandler
- JwtAuthenticationEntryPoint

Jasypt (config/security/jasypt)
- application setting file encryption (ENC)

RSA (config/security/rsa)
- RsaProvider
- Encrypt password with public key
- Decrypt password with private key

```
**#JWT Authentication Process**
```
[![image]()](https://github.com/aljjabaegiProgrammer/ajp_api/assets/148036230/4e79d85a-a8db-4699-b110-5254f964d675)
```

**#Specification**
==============
----
```
java 17
springboot 3.2.4

springdoc-openapi-starter-webmvc-ui 2.2.0
JPA 3.2.4
mapstruct 1.5.5.Final
lombok 1.18.30
validation 3.2.4
Spring security 3.2.4
JJWT 0.12.5
jasypt 3.0.5
```
