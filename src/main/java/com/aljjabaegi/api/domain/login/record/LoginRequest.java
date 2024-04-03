package com.aljjabaegi.api.domain.login.record;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 로그인 요청 record
 *
 * @author GEONLEE
 * @since 2024-04-02<br />
 */
@Schema(description = "로그인 요청 record")
public record LoginRequest(
        @Schema(description = "사용자 ID", example = "honggildong123")
        @NotNull
        String id,
        @Schema(description = "패스워드", example = "fNh5dG78YnGU2Ydlhsm97Wwobp30fYvaC+bzWryYcPSOPUHfrz2lxVoNhVUig0vQP996uLN5lp4hIOiMDFgXerCgB/DTVY96LdBIQhjZGI/bO79z4Q6OJSvV/2hrbRO/gOJnzB6YWFy5v9+sqD/tzV4repmvSZTZS2ta0+uf9vjJ3W6nOlRRVaaiH/p8F9i2bf8rascTkvUyPV59aWN0dlQT0VUr9uR/u0RCsCXQdR4pBmtrIOi1JJekRa12Xto4VntHoWi07cJiI8cMZSN3vAAaXQYxhXe+GXdQ9wB6+goOQOwhT6IvlNtvmTB+0kbqpeAp+aRAbJiepjgRn53jSw==")
        @NotNull
        String password
) {

}
