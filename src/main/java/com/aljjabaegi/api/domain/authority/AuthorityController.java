package com.aljjabaegi.api.domain.authority;

import com.aljjabaegi.api.common.response.ItemResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 권한 Controller
 *
 * @author GEONLEE
 * @since 2024-04-05
 */
@RestController
@Tag(name = "03. Authority Management", description = "Responsibility: GEONLEE")
@SecurityRequirement(name = "JWT")
@RequiredArgsConstructor
public class AuthorityController {
    private final AuthorityService authorityService;

    @DeleteMapping(value = "/v1/authority/{authorityCode}")
    @Operation(summary = "Delete Authority", operationId = "API-AUTHORITY-06")
    public ResponseEntity<ItemResponse<Long>> deleteMember(@PathVariable String authorityCode) {
        Long deleteCount = authorityService.deleteAuthority(authorityCode);
        return ResponseEntity.ok()
                .body(ItemResponse.<Long>builder()
                        .status("OK")
                        .message("데이터를 삭제하는데 성공하였습니다.")
                        .item(deleteCount).build());
    }
}
