package com.aljjabaegi.api.domain.project;

import com.aljjabaegi.api.common.request.DynamicRequest;
import com.aljjabaegi.api.common.response.GridItemsResponse;
import com.aljjabaegi.api.domain.member.record.MemberSearchResponse;
import com.aljjabaegi.api.domain.project.record.*;
import com.aljjabaegi.api.entity.Project;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Project Service
 *
 * @author GEONLEE
 * @since 2024-04-04<br />
 */
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper = ProjectMapper.INSTANCE;

    /**
     * 전체 Project 조회
     *
     * @author GEONLEE
     * @since 2024-04-04
     */
    public List<ProjectSearchResponse> getProjectList() {
        return projectMapper.toSearchResponseList(projectRepository.findAll());
    }

    /**
     * 전체 Project 조회 DynamicRepository 사용
     *
     * @author GEONLEE
     * @since 2024-04-18
     */
    public GridItemsResponse<ProjectSearchResponse> getProjectListUsingDynamicRepository(DynamicRequest dynamicRequest) {
        Page<Project> page = projectRepository.findDynamicWithPageable(dynamicRequest);
        int totalPage = page.getTotalPages();
        long totalElements = page.getTotalElements();
        List<ProjectSearchResponse> projectSearchResponseList = projectMapper.toSearchResponseList(page.getContent());

        return GridItemsResponse.<ProjectSearchResponse>builder()
                .status("OK")
                .message("데이터를 조회하는데 성공하였습니다.")
                .totalSize(totalElements)
                .totalPageSize(totalPage)
                .size(page.getNumberOfElements())
                .items(projectSearchResponseList)
                .build();
    }

    /**
     * ProjectName 으로 Project 조회 (named native query)
     *
     * @author GEONLEE
     * @since 2024-04-08
     */
    public List<ProjectSearchResponse> getProjectByName(String projectName) {
        return projectRepository.findByProjectName(projectName);
    }

    /**
     * Project 추가
     *
     * @author GEONLEE
     * @since 2024-04-04
     */
    public ProjectCreateResponse createProject(ProjectCreateRequest parameter) {
        Project createRequestEntity = projectMapper.toEntity(parameter);
        Project createdEntity = projectRepository.save(createRequestEntity);
        return projectMapper.toCreateResponse(createdEntity);
    }

    /**
     * Project 수정
     *
     * @author GEONLEE
     * @since 2024-04-04
     */
    public ProjectModifyResponse modifyProject(ProjectModifyRequest parameter) {
        Project entity = projectRepository.findById(parameter.projectId())
                .orElseThrow(() -> new EntityNotFoundException(parameter.projectId()));
        Project modifiedEntity = projectMapper.updateFromRequest(parameter, entity);
        modifiedEntity = projectRepository.saveAndFlush(modifiedEntity);
        return projectMapper.toModifyResponse(modifiedEntity);
    }

    /**
     * Project 삭제
     *
     * @author GEONLEE
     * @since 2024-04-04
     */
    public Long deleteProject(String projectId) {
        Project entity = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException(projectId));
        projectRepository.delete(entity);
        return 1L;
    }
}
