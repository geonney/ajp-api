package com.aljjabaegi.api.domain.member;

import com.aljjabaegi.api.common.jpa.mapstruct.Converter;
import com.aljjabaegi.api.domain.member.record.*;
import com.aljjabaegi.api.entity.Member;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Member mapper
 *
 * @author GEONLEE
 * @since 2024-04-01
 */
@Mapper(componentModel = "spring", imports = Converter.class)
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    /**
     * entity to search response
     *
     * @param entity member entity
     * @return memberSearchResponse
     * @author GEONLEE
     * @since 2024-04-01<br />
     */
    @Mappings({
            @Mapping(target = "createDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(target = "modifyDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
    })
    MemberSearchResponse toSearchResponse(Member entity);

    /**
     * entity list to search response list
     *
     * @param list member entity list
     * @return memberSearchResponse list
     * @author GEONLEE
     * @since 2024-04-01<br />
     */
    List<MemberSearchResponse> toSearchResponseList(List<Member> list);

    /**
     * entity to create response
     *
     * @param entity member entity
     * @return memberCreateResponse
     * @author GEONLEE
     * @since 2024-04-01<br />
     */
    @Mappings({
            @Mapping(target = "isUse", expression = "java(Converter.stringToBoolean(entity.getUseYn()))"),
            @Mapping(target = "createDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(target = "modifyDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
    })
    MemberCreateResponse toCreateResponse(Member entity);

    /**
     * entity to modify response
     *
     * @param entity member entity
     * @return memberModifyResponse
     * @author GEONLEE
     * @since 2024-04-01<br />
     */
    @Mappings({
            @Mapping(target = "isUse", expression = "java(Converter.stringToBoolean(entity.getUseYn()))"),
            @Mapping(target = "createDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(target = "modifyDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
    })
    MemberModifyResponse toModifyResponse(Member entity);

    /**
     * createRequest to entity
     *
     * @param memberCreateRequest member create request
     * @return member
     * @author GEONLEE
     * @since 2024-04-01<br />
     */
    @Mappings({
            @Mapping(target = "useYn", expression = "java(Converter.booleanToString(memberCreateRequest.isUse()))"),
    })
    Member toEntity(MemberCreateRequest memberCreateRequest);

    /**
     * update from record
     *
     * @param memberModifyRequest update request record
     * @param entity              update request entity
     * @return member
     * @author GEONLEE
     * @since 2024-04-01<br />
     */
    @Mapping(target = "useYn", expression = "java(Converter.booleanToString(memberModifyRequest.isUse()))")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    Member updateFromRequest(MemberModifyRequest memberModifyRequest, @MappingTarget Member entity);

}
