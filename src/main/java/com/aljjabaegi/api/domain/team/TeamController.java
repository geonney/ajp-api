package com.aljjabaegi.api.domain.team;

import com.aljjabaegi.api.common.response.ItemResponse;
import com.aljjabaegi.api.common.response.ItemsResponse;
import com.aljjabaegi.api.domain.team.record.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Team Controller
 *
 * @author GEONLEE
 * @since 2024-04-08
 */
@RestController
@Tag(name = "04. Team Management", description = "Responsibility: GEONLEE")
@SecurityRequirement(name = "JWT")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping(value = "/v1/teams")
    @Operation(summary = "Search teams by conditions with specification", operationId = "API-TEAM-01")
    public ResponseEntity<ItemsResponse<TeamSearchResponse>> getTeamList(
            @RequestParam(value = "teamName", required = false) String teamName,
            @RequestParam(value = "startDate", defaultValue = "19000101000000") String startDate,
            @RequestParam(value = "endDate", defaultValue = "21000430235959") String endDate) {

        List<TeamSearchResponse> teamSearchResponseList = teamService.getTeamListBySpecification(teamName, startDate, endDate);
        long size = teamSearchResponseList.size();
        return ResponseEntity.ok()
                .body(ItemsResponse.<TeamSearchResponse>builder()
                        .status("OK")
                        .message("데이터를 조회하는데 성공하였습니다.")
                        .size(size)
                        .items(teamSearchResponseList).build());
    }

    @PostMapping(value = "/v1/team")
    @Operation(summary = "Create team", operationId = "API-TEAM-02")
    public ResponseEntity<ItemResponse<TeamCreateResponse>> createTeam(@RequestBody @Valid TeamCreateRequest parameter) {
        TeamCreateResponse createdTeam = teamService.createTeam(parameter);
        return ResponseEntity.ok()
                .body(ItemResponse.<TeamCreateResponse>builder()
                        .status("OK")
                        .message("데이터를 추가하는데 성공하였습니다.")
                        .item(createdTeam).build());
    }

    @PutMapping(value = "/v1/team")
    @Operation(summary = "Modify team", operationId = "API-TEAM-03")
    public ResponseEntity<ItemResponse<TeamModifyResponse>> modifyTeam(@RequestBody @Valid TeamModifyRequest parameter) {
        TeamModifyResponse modifiedTeam = teamService.modifyTeam(parameter);
        return ResponseEntity.ok()
                .body(ItemResponse.<TeamModifyResponse>builder()
                        .status("OK")
                        .message("데이터를 수정하는데 성공하였습니다.")
                        .item(modifiedTeam).build());
    }

    @DeleteMapping(value = "/v1/team/{teamId}")
    @Operation(summary = "Delete board", operationId = "API-TEAM-04")
    public ResponseEntity<ItemResponse<Long>> deleteTeam(@PathVariable Long teamId) {
        Long deleteCount = teamService.deleteTeam(teamId);
        return ResponseEntity.ok()
                .body(ItemResponse.<Long>builder()
                        .status("OK")
                        .message("데이터를 삭제하는데 성공하였습니다.")
                        .item(deleteCount).build());
    }

}
