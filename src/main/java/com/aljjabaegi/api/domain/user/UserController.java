package com.aljjabaegi.api.domain.user;

import com.aljjabaegi.api.common.response.ItemResponse;
import com.aljjabaegi.api.common.response.ItemsResponse;
import com.aljjabaegi.api.domain.user.record.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 사용자 Controller
 *
 * @author GEONLEE
 * @since 2024-04-01<br />
 * 2024-04-02 GEONLEE - 응답 객체 Builder Pattern 적용
 */
@RestController
@Tag(name = "사용자 정보 조회 / 편집", description = "담당자: GEONLEE")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/v1/users")
    @Operation(summary = "전체 사용자 조회", operationId = "API-USER-01")
    public ResponseEntity<ItemsResponse<UserSearchResponse>> getUserList() {
        List<UserSearchResponse> userSearchResponseList = userService.getUserList();
        long size = userSearchResponseList.size();
        return ResponseEntity.ok()
                .body(ItemsResponse.<UserSearchResponse>builder()
                        .status("OK")
                        .message("데이터를 조회하는데 성공하였습니다.")
                        .size(size)
                        .items(userSearchResponseList).build());
    }

    @GetMapping(value = "/v1/users/{userId}")
    @Operation(summary = "사용자 ID로 조회", operationId = "API-USER-02")
    public ResponseEntity<ItemResponse<UserSearchResponse>> getUser(@PathVariable String userId) {
        UserSearchResponse userSearchResponse = userService.getUser(userId);

        return ResponseEntity.ok()
                .body(ItemResponse.<UserSearchResponse>builder()
                        .status("OK")
                        .message("데이터를 조회하는데 성공하였습니다.")
                        .item(userSearchResponse).build());
    }

    @GetMapping(value = "/v1/check/user-id/{userId}")
    @Operation(summary = "사용자 ID 중복 여부 조회", operationId = "API-USER-03")
    public ResponseEntity<ItemResponse<Boolean>> checkUserId(@PathVariable @NotNull String userId) {
        boolean isDuplication = userService.checkUserId(userId);
        String message = (isDuplication) ? "중복된 ID가 존재합니다." : "사용 가능한 ID 입니다.";
        return ResponseEntity.ok()
                .body(ItemResponse.<Boolean>builder()
                        .status("OK")
                        .message(message)
                        .item(isDuplication).build());
    }

    @PostMapping(value = "/v1/user")
    @Operation(summary = "사용자 정보 추가", operationId = "API-USER-04")
    public ResponseEntity<ItemResponse<UserCreateResponse>> createUser(@RequestBody @Valid UserCreateRequest parameter) {
        UserCreateResponse createdUser = userService.createUser(parameter);
        return ResponseEntity.ok()
                .body(ItemResponse.<UserCreateResponse>builder()
                        .status("OK")
                        .message("데이터를 추가하는데 성공하였습니다.")
                        .item(createdUser).build());
    }

    @PutMapping(value = "/v1/user")
    @Operation(summary = "사용자 정보 수정", operationId = "API-USER-05")
    public ResponseEntity<ItemResponse<UserModifyResponse>> modifyUser(@RequestBody @Valid UserModifyRequest parameter) {
        UserModifyResponse modifiedUser = userService.modifyUser(parameter);
        return ResponseEntity.ok()
                .body(ItemResponse.<UserModifyResponse>builder()
                        .status("OK")
                        .message("데이터를 수정하는데 성공하였습니다.")
                        .item(modifiedUser).build());
    }

    @DeleteMapping(value = "/v1/users/{userId}")
    @Operation(summary = "사용자 삭제", operationId = "API-USER-06")
    public ResponseEntity<ItemResponse<Long>> deleteUser(@PathVariable String userId) {
        System.out.println(userId);
        Long deleteCount = userService.deleteUser(userId);
        return ResponseEntity.ok()
                .body(ItemResponse.<Long>builder()
                        .status("OK")
                        .message("데이터를 삭제하는데 성공하였습니다.")
                        .item(deleteCount).build());
    }
}
