package com.aljjabaegi.api.domain.authority;

import com.aljjabaegi.api.common.response.ItemResponse;
import com.aljjabaegi.api.domain.authority.record.AuthorityCreateRequest;
import com.aljjabaegi.api.domain.authority.record.AuthorityCreateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = "/v1/authority")
    @Operation(summary = "Create authority", operationId = "API-AUTHORITY-02")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
            examples = {
                    @ExampleObject(name = "create authority", value = """
                                    {
                                        "authorityCode":"ROLE_MANAGER",
                                        "authorityName":"매니저 권한",
                                        "menuIds": [
                                            "eb664ee0-b365-47d4-8bcb-9b8bfeeb201f","0c7dd3eb-f644-4d6f-9d8e-8d1b7b8e89bd"
                                        ]
                                    }
                            """)
            }
    ))
    public ResponseEntity<ItemResponse<AuthorityCreateResponse>> createTeam(@RequestBody @Valid AuthorityCreateRequest parameter) {
        return authorityService.createAuthority(parameter);
    }

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
