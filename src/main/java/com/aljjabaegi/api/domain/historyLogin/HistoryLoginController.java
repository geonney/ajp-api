package com.aljjabaegi.api.domain.historyLogin;

import com.aljjabaegi.api.common.response.GridItemsResponse;
import com.aljjabaegi.api.domain.historyLogin.record.HistoryLoginSearchResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 로그인 이력 Controller
 *
 * @author GEONLEE
 * @since 2024-04-08<br />
 */
@RestController
@Tag(name = "07. Login history [Search using Querydsl]", description = "Responsibility: GEONLEE")
@SecurityRequirement(name = "JWT")
@RequiredArgsConstructor
public class HistoryLoginController {
    private final HistoryLoginService historyLoginService;

    @GetMapping(value = "/v1/login-history")
    @Operation(summary = "Search members login history (Querydsl, paging and sorting)", operationId = "API-LOGIN_HISTORY-01")
    public ResponseEntity<GridItemsResponse<HistoryLoginSearchResponse>> getHistoryLoginList(
            @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection,
            @RequestParam(value = "sortColumn", defaultValue = "key.createDate") String sortColumn,
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        GridItemsResponse<HistoryLoginSearchResponse> gridItemsResponse =
                historyLoginService.getHistoryLoginList(sortDirection, sortColumn, pageNo, pageSize);

        return ResponseEntity.ok()
                .body(gridItemsResponse);
    }

    @GetMapping(value = "/v1/login-history/{memberId}")
    @Operation(summary = "Search members login history by memberId (Querydsl, paging, sorting, condition)", operationId = "API-LOGIN_HISTORY-02")
    public ResponseEntity<GridItemsResponse<HistoryLoginSearchResponse>> getHistoryLoginListByMemberId(
            @PathVariable String memberId,
            @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection,
            @RequestParam(value = "sortColumn", defaultValue = "key.createDate") String sortColumn,
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        GridItemsResponse<HistoryLoginSearchResponse> gridItemsResponse =
                historyLoginService.getHistoryLoginListByMemberId(
                        memberId, sortDirection, sortColumn, pageNo, pageSize);

        return ResponseEntity.ok()
                .body(gridItemsResponse);
    }
}
