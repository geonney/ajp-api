# API Spec
![Static Badge](https://img.shields.io/badge/java-v17-blue) ![Static Badge](https://img.shields.io/badge/springboot-v3.2.4-blue) ![Static Badge](https://img.shields.io/badge/mapstruct-v1.5.5.Final-blue) ![Static Badge](https://img.shields.io/badge/lombok-v1.18.30-blue)
![Static Badge](https://img.shields.io/badge/JJWT-v0.12.5-blue) ![Static Badge](https://img.shields.io/badge/Jasypt-v3.0.5-blue)

![Static Badge](https://img.shields.io/badge/API-v1.0.0-green) [![Static Badge](https://img.shields.io/badge/welcome-aljjabaegi.tistory.com-hotpink)](http://aljjabaegi.tistory.com)

### Swagger (config/swagger)

- Information
- grouping
- Operation sorting
- tag sorting
- display-resquest-duration
- filter

## JPA (common/jpa)
- BaseEntity (@MappedSuperclass)
- Mapstruct

## Exception (common/exception)
- GlobalExceptionHandler
- Custom Exception (ServiceException)

## Standardization
- response (item, items, error)

## Spring Security (config/security/spring/SecurityConfig)
- CSRF
- CORS
- Filter
- DelegatingPasswordEncoder (bcrypt, sha-256 with salt)

## JWT (config/security/jwt)
- JwtFilter
- TokenProvider
- JwtAccessDeniedHandler
- JwtAuthenticationEntryPoint

## Jasypt (config/security/jasypt)
- application setting file encryption (ENC)

## RSA (config/security/rsa)
- RsaProvider
- Encrypt password with public key
- Decrypt password with private key
