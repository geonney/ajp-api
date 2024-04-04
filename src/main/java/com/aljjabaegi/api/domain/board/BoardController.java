package com.aljjabaegi.api.domain.project;

import com.aljjabaegi.api.common.response.ItemResponse;
import com.aljjabaegi.api.common.response.ItemsResponse;
import com.aljjabaegi.api.domain.project.record.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author GEONLEE
 * @since 2024-04-04
 */
@RestController
@Tag(name = "Project Management", description = "Responsibility: GEONLEE")
@SecurityRequirement(name = "JWT")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping(value = "/v1/projects")
    @Operation(summary = "Search All Projects", operationId = "API-PROJECT-01")
    public ResponseEntity<ItemsResponse<ProjectSearchResponse>> getUserList() {
        List<ProjectSearchResponse> projectSearchResponseList = projectService.getProjectList();
        long size = projectSearchResponseList.size();
        return ResponseEntity.ok()
                .body(ItemsResponse.<ProjectSearchResponse>builder()
                        .status("OK")
                        .message("데이터를 조회하는데 성공하였습니다.")
                        .size(size)
                        .items(projectSearchResponseList).build());
    }

    @PostMapping(value = "/v1/project")
    @Operation(summary = "Create Project", operationId = "API-PROJECT-02")
    public ResponseEntity<ItemResponse<ProjectCreateResponse>> createMember(@RequestBody @Valid ProjectCreateRequest parameter) {
        ProjectCreateResponse createdProject = projectService.createProject(parameter);
        return ResponseEntity.ok()
                .body(ItemResponse.<ProjectCreateResponse>builder()
                        .status("OK")
                        .message("데이터를 추가하는데 성공하였습니다.")
                        .item(createdProject).build());
    }

    @PutMapping(value = "/v1/project")
    @Operation(summary = "Modify Project", operationId = "API-PROJECT-03")
    public ResponseEntity<ItemResponse<ProjectModifyResponse>> modifyUser(@RequestBody @Valid ProjectModifyRequest parameter) {
        ProjectModifyResponse modifiedProject = projectService.modifyProject(parameter);
        return ResponseEntity.ok()
                .body(ItemResponse.<ProjectModifyResponse>builder()
                        .status("OK")
                        .message("데이터를 수정하는데 성공하였습니다.")
                        .item(modifiedProject).build());
    }

    @DeleteMapping(value = "/v1/project/{projectId}")
    @Operation(summary = "Delete Project", operationId = "API-PROJECT-04")
    public ResponseEntity<ItemResponse<Long>> deleteMember(@PathVariable String projectId) {
        Long deleteCount = projectService.deleteProject(projectId);
        return ResponseEntity.ok()
                .body(ItemResponse.<Long>builder()
                        .status("OK")
                        .message("데이터를 삭제하는데 성공하였습니다.")
                        .item(deleteCount).build());
    }

}
