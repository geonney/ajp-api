package com.aljjabaegi.api.domain.board;

import com.aljjabaegi.api.common.request.DynamicRequest;
import com.aljjabaegi.api.common.response.GridResponse;
import com.aljjabaegi.api.common.response.ItemResponse;
import com.aljjabaegi.api.domain.board.record.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Board Controller
 *
 * @author GEONLEE
 * @since 2024-04-04
 */
@RestController
@Tag(name = "06. Board Management [Search using DynamicBooleanBuilder]", description = "Responsibility: GEONLEE")
@SecurityRequirement(name = "JWT")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping(value = "/v1/boards-dynamicDslRepository")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
            examples = {
                    @ExampleObject(name = "filtering and sorting with paging", value = """
                                    {
                                        "pageNo":0,
                                        "pageSize":10,
                                        "filter": [
                                            {
                                                "field":"memberId",
                                                "operator":"eq",
                                                "value":"honggildong123"
                                            },
                                            {
                                                "field":"createDate",
                                                "operator":"between",
                                                "value":"20240408000000,20240408170000"
                                            }
                                        ],
                                        "sorter": [
                                            {
                                                "field":"createDate",
                                                "direction":"DESC"
                                            }
                                        ]
                                    }
                            """),
                    @ExampleObject(name = "filtering with paging", value = """
                                    {
                                        "pageNo":0,
                                        "pageSize":10,
                                        "filter": [
                                            {
                                                "field":"memberId",
                                                "operator":"eq",
                                                "value":"honggildong123"
                                            }
                                        ]
                                    }
                            """),
                    @ExampleObject(name = "sorting with paging", value = """
                                    {
                                        "pageNo":0,
                                        "pageSize":10,
                                        "sorter": [
                                            {
                                                "field":"createDate",
                                                "direction":"DESC"
                                            }
                                        ]
                                    }
                            """),
                    @ExampleObject(name = "Empty parameter", value = """
                                    {

                                    }
                            """),
            }
    ))
    @Operation(summary = "Search boards (DynamicDslRepository)", operationId = "API-BOARD-02")
    public ResponseEntity<GridResponse<BoardSearchResponse>> getBoardListUsingDynamicDslRepository(@RequestBody DynamicRequest dynamicRequest) {
        GridResponse<BoardSearchResponse> gridItemsResponse = boardService.getBoardListUsingDynamicDslRepository(dynamicRequest);
        return ResponseEntity.ok()
                .body(gridItemsResponse);
    }

    @PostMapping(value = "/v1/boards")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
            examples = {
                    @ExampleObject(name = "filtering and sorting with paging", value = """
                                    {
                                        "pageNo":0,
                                        "pageSize":10,
                                        "filter": [
                                            {
                                                "field":"memberId",
                                                "operator":"eq",
                                                "value":"honggildong123"
                                            },
                                            {
                                                "field":"createDate",
                                                "operator":"between",
                                                "value":"20240408000000,20240408170000"
                                            }
                                        ],
                                        "sorter": [
                                            {
                                                "field":"createDate",
                                                "direction":"DESC"
                                            }
                                        ]
                                    }
                            """),
                    @ExampleObject(name = "filtering with paging", value = """
                                    {
                                        "pageNo":0,
                                        "pageSize":10,
                                        "filter": [
                                            {
                                                "field":"memberId",
                                                "operator":"eq",
                                                "value":"honggildong123"
                                            }
                                        ]
                                    }
                            """),
                    @ExampleObject(name = "sorting with paging", value = """
                                    {
                                        "pageNo":0,
                                        "pageSize":10,
                                        "sorter": [
                                            {
                                                "field":"createDate",
                                                "direction":"DESC"
                                            }
                                        ]
                                    }
                            """),
                    @ExampleObject(name = "Empty parameter", value = """
                                    {

                                    }
                            """),
            }
    ))
    @Operation(summary = "Search boards (DynamicBooleanBuilder)", operationId = "API-BOARD-01")
    public ResponseEntity<GridResponse<BoardSearchResponse>> getBoardList(@RequestBody DynamicRequest dynamicRequest) {
        GridResponse<BoardSearchResponse> gridItemsResponse = boardService.getBoardListUsingDynamicBooleanBuilder(dynamicRequest);
        return ResponseEntity.ok()
                .body(gridItemsResponse);
    }

    @PostMapping(value = "/v1/board")
    @Operation(summary = "Create board", operationId = "API-BOARD-02")
    public ResponseEntity<ItemResponse<BoardCreateResponse>> createBoard(@RequestBody @Valid BoardCreateRequest parameter) {
        BoardCreateResponse createdBoard = boardService.createBoard(parameter);
        return ResponseEntity.ok()
                .body(ItemResponse.<BoardCreateResponse>builder()
                        .status("OK")
                        .message("데이터를 추가하는데 성공하였습니다.")
                        .item(createdBoard).build());
    }

    @PutMapping(value = "/v1/board")
    @Operation(summary = "Modify board", operationId = "API-board-03")
    public ResponseEntity<ItemResponse<BoardModifyResponse>> modifyBoard(@RequestBody @Valid BoardModifyRequest parameter) {
        BoardModifyResponse modifiedBoard = boardService.modifyBoard(parameter);
        return ResponseEntity.ok()
                .body(ItemResponse.<BoardModifyResponse>builder()
                        .status("OK")
                        .message("데이터를 수정하는데 성공하였습니다.")
                        .item(modifiedBoard).build());
    }

    @DeleteMapping(value = "/v1/board/{boardSequence}")
    @Operation(summary = "Delete board", operationId = "API-board-04")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<ItemResponse<Long>> deleteBoard(@PathVariable Long boardSequence) {
        Long deleteCount = boardService.deleteBoard(boardSequence);
        return ResponseEntity.ok()
                .body(ItemResponse.<Long>builder()
                        .status("OK")
                        .message("데이터를 삭제하는데 성공하였습니다.")
                        .item(deleteCount).build());
    }
}
