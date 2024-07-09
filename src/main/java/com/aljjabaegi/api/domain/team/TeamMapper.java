package com.aljjabaegi.api.domain.team;

import com.aljjabaegi.api.common.converter.Converter;
import com.aljjabaegi.api.domain.team.record.*;
import com.aljjabaegi.api.entity.Team;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Team mapper
 *
 * @author GEONLEE
 * @since 2024-04-08
 */
@Mapper(componentModel = "spring", imports = Converter.class)
public interface TeamMapper {
    TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);

    /**
     * entity to search response
     *
     * @param entity team entity
     * @return teamSearchResponse
     * @author GEONLEE
     * @since 2024-04-08<br />
     */
    @Mappings({
            @Mapping(target = "createDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(target = "modifyDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
    })
    TeamSearchResponse toSearchResponse(Team entity);

    /**
     * entity list to search response list
     *
     * @param list team entity list
     * @return teamSearchResponse list
     * @author GEONLEE
     * @since 2024-04-08<br />
     */
    List<TeamSearchResponse> toSearchResponseList(List<Team> list);

    /**
     * entity to create response
     *
     * @param entity team entity
     * @return teamCreateResponse
     * @author GEONLEE
     * @since 2024-04-08<br />
     */
    @Mappings({
            @Mapping(target = "createDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(target = "modifyDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    })
    TeamCreateResponse toCreateResponse(Team entity);

    /**
     * entity to modify response
     *
     * @param entity team entity
     * @return teamModifyResponse
     * @author GEONLEE
     * @since 2024-04-08<br />
     */
    @Mappings({
            @Mapping(target = "createDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(target = "modifyDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    })
    TeamModifyResponse toModifyResponse(Team entity);


    /**
     * createRequest to entity
     *
     * @param teamCreateRequest team create request
     * @return team
     * @author GEONLEE
     * @since 2024-04-04<br />
     */
    Team toEntity(TeamCreateRequest teamCreateRequest);

    /**
     * update from record
     *
     * @param teamModifyRequest update request record
     * @param entity            update request entity
     * @return team
     * @author GEONLEE
     * @since 2024-04-08<br />
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    Team updateFromRequest(TeamModifyRequest teamModifyRequest, @MappingTarget Team entity);
}
