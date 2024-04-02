package com.aljjabaegi.api.config.security.jwt.record;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Jwt token valid process 에서 사용하는 dto
 *
 * @author GEONLEE
 * @since 2024-04-04<br />
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class JwtValidDto {
    private boolean valid;
    private String userId;
    private String accessToken;
}
