package com.aljjabaegi.api.domain.historyLogin;

import com.aljjabaegi.api.common.jpa.dynamicSearch.converter.DateConverter;
import com.aljjabaegi.api.common.jpa.dynamicSearch.converter.enumeration.DateType;
import com.aljjabaegi.api.domain.historyLogin.record.HistoryLoginCreateRequest;
import com.aljjabaegi.api.domain.historyLogin.record.HistoryLoginSearchResponse;
import com.aljjabaegi.api.entity.HistoryLogin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * HistoryLogin mapper
 *
 * @author GEONLEE
 * @since 2024-04-01
 */
@Mapper(componentModel = "spring", imports = {DateConverter.class, DateType.class})
public interface HistoryLoginMapper {
    HistoryLoginMapper INSTANCE = Mappers.getMapper(HistoryLoginMapper.class);

    /**
     * entity to search response
     *
     * @param entity history login entity
     * @return historyLoginSearchResponse
     * @author GEONLEE
     * @since 2024-04-09<br />
     */
    @Mappings({
            @Mapping(target = "createDate", expression = "java(DateConverter.toString(entity.getKey().getCreateDate(), DateType.YMDHMS_F))"),
            @Mapping(target = "memberId", source = "key.memberId"),
    })
    HistoryLoginSearchResponse toSearchResponse(HistoryLogin entity);

    /**
     * entity list to search response list
     *
     * @param list history login entity list
     * @return historyLoginSearchResponse list
     * @author GEONLEE
     * @since 2024-04-09<br />
     */
    List<HistoryLoginSearchResponse> toSearchResponseList(List<HistoryLogin> list);

    /**
     * createRequest to entity key
     *
     * @param historyLoginRequest history login create request
     * @return History entity key
     * @author GEONLEE
     * @since 2024-04-05<br />
     */
    @Mappings({
            @Mapping(target = "key.memberId", source = "memberId"),
            @Mapping(target = "key.createDate", source = "createDate")
    })
    HistoryLogin toEntity(HistoryLoginCreateRequest historyLoginRequest);
}
