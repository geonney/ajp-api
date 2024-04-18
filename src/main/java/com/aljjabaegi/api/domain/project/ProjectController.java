package com.aljjabaegi.api.domain.project;

import com.aljjabaegi.api.common.request.DynamicRequest;
import com.aljjabaegi.api.common.response.GridItemsResponse;
import com.aljjabaegi.api.common.response.ItemResponse;
import com.aljjabaegi.api.common.response.ItemsResponse;
import com.aljjabaegi.api.domain.project.record.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Project Controller
 *
 * @author GEONLEE
 * @since 2024-04-04
 */
@RestController
@Tag(name = "05. Project Management [Search using DynamicRepository, Named native query]", description = "Responsibility: GEONLEE")
@SecurityRequirement(name = "JWT")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping(value = "/v1/projects")
    @Operation(summary = "Search All Projects", operationId = "API-PROJECT-01")
    public ResponseEntity<ItemsResponse<ProjectSearchResponse>> getProjectList() {
        List<ProjectSearchResponse> projectSearchResponseList = projectService.getProjectList();
        long size = projectSearchResponseList.size();
        return ResponseEntity.ok()
                .body(ItemsResponse.<ProjectSearchResponse>builder()
                        .status("OK")
                        .message("데이터를 조회하는데 성공하였습니다.")
                        .size(size)
                        .items(projectSearchResponseList).build());
    }

    @GetMapping(value = "/v1/projects/{projectName}")
    @Operation(summary = "Search Project (NamedNativeQuery)", operationId = "API-PROJECT-02")
    public ResponseEntity<ItemsResponse<ProjectSearchResponse>> getProject(@PathVariable String projectName) {
        List<ProjectSearchResponse> projectSearchResponseList = projectService.getProjectByName(projectName);
        return ResponseEntity.ok()
                .body(ItemsResponse.<ProjectSearchResponse>builder()
                        .status("OK")
                        .message("데이터를 조회하는데 성공하였습니다.")
                        .items(projectSearchResponseList)
                        .size((long) projectSearchResponseList.size())
                        .build()
                );
    }

    @PostMapping(value = "/v1/projects-dynamicRepository")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
            examples = {
                    @ExampleObject(name = "filtering and sorting with paging", value = """
                                    {
                                        "pageNo":0,
                                        "pageSize":10,
                                        "filter": [
                                            {
                                                "field":"projectName",
                                                "operator":"contains",
                                                "value":"트1"
                                            }
                                        ],
                                        "sorter": [
                                            {
                                                "field":"createDate",
                                                "sortDirection":"DESC"
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
                                                "field":"projectEndDate",
                                                "operator":"between",
                                                "value":"20240401,20240430"
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
                                                "sortDirection":"DESC"
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
    @Operation(summary = "Search Project (DynamicRepository)", operationId = "API-PROJECT-03")
    public ResponseEntity<GridItemsResponse<ProjectSearchResponse>> getProjectListUsingDynamicRepository(@RequestBody DynamicRequest dynamicRequest) {
        GridItemsResponse<ProjectSearchResponse> gridItemsResponse = projectService.getProjectListUsingDynamicRepository(dynamicRequest);
        return ResponseEntity.ok()
                .body(gridItemsResponse);
    }

    @PostMapping(value = "/v1/project")
    @Operation(summary = "Create Project", operationId = "API-PROJECT-04")
    public ResponseEntity<ItemResponse<ProjectCreateResponse>> createMember(@RequestBody @Valid ProjectCreateRequest parameter) {
        ProjectCreateResponse createdProject = projectService.createProject(parameter);
        return ResponseEntity.ok()
                .body(ItemResponse.<ProjectCreateResponse>builder()
                        .status("OK")
                        .message("데이터를 추가하는데 성공하였습니다.")
                        .item(createdProject).build());
    }

    @PutMapping(value = "/v1/project")
    @Operation(summary = "Modify Project", operationId = "API-PROJECT-05")
    public ResponseEntity<ItemResponse<ProjectModifyResponse>> modifyUser(@RequestBody @Valid ProjectModifyRequest parameter) {
        ProjectModifyResponse modifiedProject = projectService.modifyProject(parameter);
        return ResponseEntity.ok()
                .body(ItemResponse.<ProjectModifyResponse>builder()
                        .status("OK")
                        .message("데이터를 수정하는데 성공하였습니다.")
                        .item(modifiedProject).build());
    }

    @DeleteMapping(value = "/v1/project/{projectId}")
    @Operation(summary = "Delete Project", operationId = "API-PROJECT-06")
    public ResponseEntity<ItemResponse<Long>> deleteMember(@PathVariable String projectId) {
        Long deleteCount = projectService.deleteProject(projectId);
        return ResponseEntity.ok()
                .body(ItemResponse.<Long>builder()
                        .status("OK")
                        .message("데이터를 삭제하는데 성공하였습니다.")
                        .item(deleteCount).build());
    }

}
