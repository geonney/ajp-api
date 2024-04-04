package com.aljjabaegi.api.domain.project;

import com.aljjabaegi.api.common.jpa.mapstruct.Converter;
import com.aljjabaegi.api.domain.project.record.*;
import com.aljjabaegi.api.entity.Project;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Project mapper
 *
 * @author GEONLEE
 * @since 2024-04-04
 */
@Mapper(componentModel = "spring", imports = Converter.class)
public interface ProjectMapper {
    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    /**
     * entity to search response
     *
     * @param entity project entity
     * @return projectSearchResponse
     * @author GEONLEE
     * @since 2024-04-04<br />
     */
    @Mappings({
            @Mapping(target = "projectStartDate", dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "projectEndDate", dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "createDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(target = "modifyDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
    })
    ProjectSearchResponse toSearchResponse(Project entity);

    /**
     * entity list to search response list
     *
     * @param list project entity list
     * @return projectSearchResponse list
     * @author GEONLEE
     * @since 2024-04-04<br />
     */
    List<ProjectSearchResponse> toSearchResponseList(List<Project> list);

    /**
     * entity to create response
     *
     * @param entity project entity
     * @return projectCreateResponse
     * @author GEONLEE
     * @since 2024-04-01<br />
     */
    @Mappings({
            @Mapping(target = "projectStartDate", dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "projectEndDate", dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "createDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(target = "modifyDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    })
    ProjectCreateResponse toCreateResponse(Project entity);

    /**
     * entity to modify response
     *
     * @param entity project entity
     * @return projectModifyResponse
     * @author GEONLEE
     * @since 2024-04-04<br />
     */
    @Mappings({
            @Mapping(target = "projectStartDate", dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "projectEndDate", dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "createDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(target = "modifyDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    })
    ProjectModifyResponse toModifyResponse(Project entity);

    /**
     * createRequest to entity
     *
     * @param projectCreateRequest project create request
     * @return project
     * @author GEONLEE
     * @since 2024-04-04<br />
     */
    @Mappings({
            @Mapping(target = "projectStartDate", expression = "java(Converter.dateStringToLocalDate(projectCreateRequest.projectStartDate()))"),
            @Mapping(target = "projectEndDate", expression = "java(Converter.dateStringToLocalDate(projectCreateRequest.projectEndDate()))"),
    })
    Project toEntity(ProjectCreateRequest projectCreateRequest);

    /**
     * update from record
     *
     * @param projectModifyRequest update request record
     * @param entity               update request entity
     * @return project
     * @author GEONLEE
     * @since 2024-04-01<br />
     */
    @Mappings({
            @Mapping(target = "projectStartDate", expression = "java(Converter.dateStringToLocalDate(projectModifyRequest.projectStartDate()))"),
            @Mapping(target = "projectEndDate", expression = "java(Converter.dateStringToLocalDate(projectModifyRequest.projectEndDate()))"),
    })
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    Project updateFromRequest(ProjectModifyRequest projectModifyRequest, @MappingTarget Project entity);
}
