package com.aljjabaegi.api.domain.member;

import com.aljjabaegi.api.common.converter.Converter;
import com.aljjabaegi.api.domain.member.record.*;
import com.aljjabaegi.api.entity.Member;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Member mapper
 *
 * @author GEONLEE
 * @since 2024-04-01<br />
 * 2024-04-07 GEONLEE - @Enumerated(EnumType.STRING) UseYn 사용 테스트 추가<br />
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
            @Mapping(target = "teamName", source = "memberTeam.team.teamName"),
            @Mapping(target = "responsibilityCodeName", source = "memberTeam.responsibilityCode.codeName"),
//            @Mapping(target = "birthDate", dateFormat = "yyyy-MM-dd"),
//            @Mapping(target = "createDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
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
            @Mapping(target = "isUse", expression = "java(Converter.useYnToBoolean(entity.getUseYn()))"),
            @Mapping(target = "teamName", source = "memberTeam.team.teamName"),
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
            @Mapping(target = "isUse", expression = "java(Converter.useYnToBoolean(entity.getUseYn()))"),
            @Mapping(target = "teamName", source = "memberTeam.team.teamName"),
            @Mapping(target = "createDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(target = "modifyDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    })
    MemberModifyResponse toModifyResponse(Member entity);

    /**
     * createRequest to entity
     *
     * @param memberCreateRequest member create request
     * @return member
     * @author GEONLEE
     * @since 2024-04-01<br />
     * 2024-04-17 GEONLEE - passwordUpdateDate 추가<br />
     */
    @Mappings({
//            @Mapping(target = "useYn", expression = "java(Converter.booleanToUseYn(memberCreateRequest.isUse()))"),
            @Mapping(target = "password", expression = "java(Converter.encodePassword(memberCreateRequest.password()))"),
            @Mapping(target = "authority.authorityCode", source = "authorityCode", defaultValue = "ROLE_TEST"),
//            @Mapping(target = "passwordUpdateDate", expression = "java(Converter.getToday())")
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
//    @Mapping(target = "useYn", expression = "java(Converter.booleanToUseYn(memberModifyRequest.isUse()))")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    Member updateFromRequest(MemberModifyRequest memberModifyRequest, @MappingTarget Member entity);

}
