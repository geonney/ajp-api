package com.aljjabaegi.api.domain.project;

import com.aljjabaegi.api.common.jpa.mapstruct.Converter;
import com.aljjabaegi.api.domain.project.record.ProjectCreateRequest;
import com.aljjabaegi.api.domain.project.record.ProjectCreateResponse;
import com.aljjabaegi.api.domain.project.record.ProjectModifyRequest;
import com.aljjabaegi.api.domain.project.record.ProjectModifyResponse;
import com.aljjabaegi.api.domain.project.record.ProjectSearchResponse;
import com.aljjabaegi.api.entity.Project;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-22T18:22:26+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.7.jar, environment: Java 17 (Oracle Corporation)"
)
@Component
public class ProjectMapperImpl implements ProjectMapper {

    private final DateTimeFormatter dateTimeFormatter_yyyy_MM_dd_0159776256 = DateTimeFormatter.ofPattern( "yyyy-MM-dd" );
    private final DateTimeFormatter dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168 = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" );

    @Override
    public ProjectSearchResponse toSearchResponse(Project entity) {
        if ( entity == null ) {
            return null;
        }

        String projectStartDate = null;
        String projectEndDate = null;
        String createDate = null;
        String modifyDate = null;
        String projectId = null;
        String projectName = null;

        if ( entity.getProjectStartDate() != null ) {
            projectStartDate = dateTimeFormatter_yyyy_MM_dd_0159776256.format( entity.getProjectStartDate() );
        }
        if ( entity.getProjectEndDate() != null ) {
            projectEndDate = dateTimeFormatter_yyyy_MM_dd_0159776256.format( entity.getProjectEndDate() );
        }
        if ( entity.getCreateDate() != null ) {
            createDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getCreateDate() );
        }
        if ( entity.getModifyDate() != null ) {
            modifyDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getModifyDate() );
        }
        projectId = entity.getProjectId();
        projectName = entity.getProjectName();

        ProjectSearchResponse projectSearchResponse = new ProjectSearchResponse( projectId, projectName, projectStartDate, projectEndDate, createDate, modifyDate );

        return projectSearchResponse;
    }

    @Override
    public List<ProjectSearchResponse> toSearchResponseList(List<Project> list) {
        if ( list == null ) {
            return null;
        }

        List<ProjectSearchResponse> list1 = new ArrayList<ProjectSearchResponse>( list.size() );
        for ( Project project : list ) {
            list1.add( toSearchResponse( project ) );
        }

        return list1;
    }

    @Override
    public ProjectCreateResponse toCreateResponse(Project entity) {
        if ( entity == null ) {
            return null;
        }

        String projectStartDate = null;
        String projectEndDate = null;
        String createDate = null;
        String modifyDate = null;
        String projectId = null;
        String projectName = null;

        if ( entity.getProjectStartDate() != null ) {
            projectStartDate = dateTimeFormatter_yyyy_MM_dd_0159776256.format( entity.getProjectStartDate() );
        }
        if ( entity.getProjectEndDate() != null ) {
            projectEndDate = dateTimeFormatter_yyyy_MM_dd_0159776256.format( entity.getProjectEndDate() );
        }
        if ( entity.getCreateDate() != null ) {
            createDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getCreateDate() );
        }
        if ( entity.getModifyDate() != null ) {
            modifyDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getModifyDate() );
        }
        projectId = entity.getProjectId();
        projectName = entity.getProjectName();

        ProjectCreateResponse projectCreateResponse = new ProjectCreateResponse( projectId, projectName, projectStartDate, projectEndDate, createDate, modifyDate );

        return projectCreateResponse;
    }

    @Override
    public ProjectModifyResponse toModifyResponse(Project entity) {
        if ( entity == null ) {
            return null;
        }

        String projectStartDate = null;
        String projectEndDate = null;
        String createDate = null;
        String modifyDate = null;
        String projectId = null;
        String projectName = null;

        if ( entity.getProjectStartDate() != null ) {
            projectStartDate = dateTimeFormatter_yyyy_MM_dd_0159776256.format( entity.getProjectStartDate() );
        }
        if ( entity.getProjectEndDate() != null ) {
            projectEndDate = dateTimeFormatter_yyyy_MM_dd_0159776256.format( entity.getProjectEndDate() );
        }
        if ( entity.getCreateDate() != null ) {
            createDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getCreateDate() );
        }
        if ( entity.getModifyDate() != null ) {
            modifyDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getModifyDate() );
        }
        projectId = entity.getProjectId();
        projectName = entity.getProjectName();

        ProjectModifyResponse projectModifyResponse = new ProjectModifyResponse( projectId, projectName, projectStartDate, projectEndDate, createDate, modifyDate );

        return projectModifyResponse;
    }

    @Override
    public Project toEntity(ProjectCreateRequest projectCreateRequest) {
        if ( projectCreateRequest == null ) {
            return null;
        }

        Project project = new Project();

        project.setProjectName( projectCreateRequest.projectName() );

        project.setProjectStartDate( Converter.dateStringToLocalDate(projectCreateRequest.projectStartDate()) );
        project.setProjectEndDate( Converter.dateStringToLocalDate(projectCreateRequest.projectEndDate()) );

        return project;
    }

    @Override
    public Project updateFromRequest(ProjectModifyRequest projectModifyRequest, Project entity) {
        if ( projectModifyRequest == null ) {
            return entity;
        }

        entity.setProjectId( projectModifyRequest.projectId() );
        entity.setProjectName( projectModifyRequest.projectName() );

        entity.setProjectStartDate( Converter.dateStringToLocalDate(projectModifyRequest.projectStartDate()) );
        entity.setProjectEndDate( Converter.dateStringToLocalDate(projectModifyRequest.projectEndDate()) );

        return entity;
    }
}
