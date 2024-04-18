package com.aljjabaegi.api.domain.board;

import com.aljjabaegi.api.common.response.ItemResponse;
import com.aljjabaegi.api.common.response.ItemsResponse;
import com.aljjabaegi.api.domain.board.record.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Board Controller
 *
 * @author GEONLEE
 * @since 2024-04-04
 */
@RestController
@Tag(name = "06. Board Management (Using query method)", description = "Responsibility: GEONLEE")
@SecurityRequirement(name = "JWT")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping(value = "/v1/boards")
    @Operation(summary = "Search All boards", operationId = "API-BOARD-01")
    public ResponseEntity<ItemsResponse<BoardSearchResponse>> getBoardList() {
        List<BoardSearchResponse> boardSearchResponseList = boardService.getBoardList();
        long size = boardSearchResponseList.size();
        return ResponseEntity.ok()
                .body(ItemsResponse.<BoardSearchResponse>builder()
                        .status("OK")
                        .message("데이터를 조회하는데 성공하였습니다.")
                        .size(size)
                        .items(boardSearchResponseList).build());
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
    public ResponseEntity<ItemResponse<Long>> deleteBoard(@PathVariable Long boardSequence) {
        Long deleteCount = boardService.deleteBoard(boardSequence);
        return ResponseEntity.ok()
                .body(ItemResponse.<Long>builder()
                        .status("OK")
                        .message("데이터를 삭제하는데 성공하였습니다.")
                        .item(deleteCount).build());
    }

}
