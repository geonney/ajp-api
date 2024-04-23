package com.aljjabaegi.api.domain.menu;

import com.aljjabaegi.api.common.response.ItemResponse;
import com.aljjabaegi.api.common.response.ItemsResponse;
import com.aljjabaegi.api.domain.menu.record.MenuCreateRequest;
import com.aljjabaegi.api.domain.menu.record.MenuCreateResponse;
import com.aljjabaegi.api.domain.menu.record.MenuSearchResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Team Controller
 *
 * @author GEONLEE
 * @since 2024-04-08
 */
@RestController
@Tag(name = "08. Menu Management", description = "Responsibility: GEONLEE")
@SecurityRequirement(name = "JWT")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping(value = "/v1/menus")
    @Operation(summary = "Search menus by authority", operationId = "API-MENU-01")
    public ResponseEntity<ItemsResponse<MenuSearchResponse>> getMenusByAuthority() {
        return menuService.getMenusByAuthority();
    }

    @PostMapping(value = "/v1/menu")
    @Operation(summary = "Create menu", operationId = "API-MENU-02")
    public ResponseEntity<ItemResponse<MenuCreateResponse>> createTeam(@RequestBody @Valid MenuCreateRequest parameter) {
        return menuService.createMenu(parameter);
    }
//
//    @PutMapping(value = "/v1/team")
//    @Operation(summary = "Modify team", operationId = "API-TEAM-03")
//    public ResponseEntity<ItemResponse<TeamModifyResponse>> modifyTeam(@RequestBody @Valid TeamModifyRequest parameter) {
//        TeamModifyResponse modifiedTeam = teamService.modifyTeam(parameter);
//        return ResponseEntity.ok()
//                .body(ItemResponse.<TeamModifyResponse>builder()
//                        .status("OK")
//                        .message("데이터를 수정하는데 성공하였습니다.")
//                        .item(modifiedTeam).build());
//    }
//
//    @DeleteMapping(value = "/v1/team/{teamId}")
//    @Operation(summary = "Delete board", operationId = "API-TEAM-04")
//    public ResponseEntity<ItemResponse<Long>> deleteTeam(@PathVariable Long teamId) {
//        Long deleteCount = teamService.deleteTeam(teamId);
//        return ResponseEntity.ok()
//                .body(ItemResponse.<Long>builder()
//                        .status("OK")
//                        .message("데이터를 삭제하는데 성공하였습니다.")
//                        .item(deleteCount).build());
//    }

}
