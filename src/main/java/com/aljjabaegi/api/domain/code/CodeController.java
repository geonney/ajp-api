package com.aljjabaegi.api.domain.code;

import com.aljjabaegi.api.common.request.DynamicRequest;
import com.aljjabaegi.api.common.response.ItemsResponse;
import com.aljjabaegi.api.domain.code.record.CodeSearchResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 코드 관리 Controller
 *
 * @author GEONLEE
 * @since 2024-05-29
 */
@RestController
@Tag(name = "09. Code Management", description = "Responsibility: GEONLEE")
@SecurityRequirement(name = "JWT")
@RequiredArgsConstructor
public class CodeController {

    private final CodeService codeService;

    @PostMapping(value = "/v1/code/search", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Search Code", operationId = "API-CODE-01")
    public ResponseEntity<ItemsResponse<CodeSearchResponse>> getCodeList(@RequestBody DynamicRequest dynamicRequest) {
        ItemsResponse<CodeSearchResponse> gridItemsResponse = codeService.getCodeList(dynamicRequest);
        return ResponseEntity.ok()
                .body(gridItemsResponse);
    }
}
