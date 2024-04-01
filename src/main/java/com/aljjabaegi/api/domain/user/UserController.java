package com.aljjabaegi.api.domain.user;

import com.aljjabaegi.api.domain.user.record.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 사용자 Controller
 *
 * @author GEONLEE
 * @since 2024-04-01
 */
@RestController
@Tag(name = "사용자 정보 조회 / 편집", description = "담당자: GEONLEE")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/v1/users")
    @Operation(summary = "전체 사용자 조회", operationId = "API-USER-01")
    public ResponseEntity<List<UserSearchResponse>> getUserList() {
        List<UserSearchResponse> userSearchResponseList = userService.getUserList();
        return ResponseEntity.ok()
                .body(userSearchResponseList);
    }

    @GetMapping(value = "/v1/users/{userId}")
    @Operation(summary = "사용자 ID로 조회", operationId = "API-USER-02")
    public ResponseEntity<UserSearchResponse> getUser(@PathVariable String userId) {
        UserSearchResponse userSearchResponse = userService.getUser(userId);
        return ResponseEntity.ok()
                .body(userSearchResponse);
    }

    @PostMapping(value = "/v1/user")
    @Operation(summary = "사용자 정보 추가", operationId = "API-USER-03")
    public ResponseEntity<UserCreateResponse> createUser(@RequestBody @Valid UserCreateRequest parameter) {
        UserCreateResponse createdUser = userService.createUser(parameter);
        return ResponseEntity.ok()
                .body(createdUser);
    }

    @PutMapping(value = "/v1/user")
    @Operation(summary = "사용자 정보 수정", operationId = "API-USER-04")
    public ResponseEntity<UserModifyResponse> modifyUser(@RequestBody @Valid UserModifyRequest parameter) {
        UserModifyResponse modifiedUser = userService.modifyUser(parameter);
        return ResponseEntity.ok()
                .body(modifiedUser);
    }

    @DeleteMapping(value = "/v1/users/{userId}")
    @Operation(summary = "사용자 삭제", operationId = "API-USER-05")
    public ResponseEntity<Long> deleteUser(@PathVariable String userId) {
        System.out.println(userId);
        Long deleteCount = userService.deleteUser(userId);
        return ResponseEntity.ok()
                .body(deleteCount);
    }
}
