package kr.co.neighbor21.neighborApi.domain.authority.login.record;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 로그아웃 응답 구조체
 *
 * @author GEONLEE
 * @since 2024-03-29
 */
@Builder
@Schema(description = "로그아웃 응답")
public record LogoutResponse(
        @Schema(description = "응답 코드", example = "NS_OK")
        String status,
        @Schema(description = "응답 메시지", example = "로그아웃 하였습니다.")
        String message) {
}
