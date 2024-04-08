package com.aljjabaegi.api.domain.historyLogin.record;

import com.aljjabaegi.api.common.jpa.mapstruct.Converter;
import com.aljjabaegi.api.entity.HistoryLogin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * HistoryLogin mapper
 *
 * @author GEONLEE
 * @since 2024-04-01
 */
@Mapper(componentModel = "spring", imports = Converter.class)
public interface HistoryLoginMapper {
    HistoryLoginMapper INSTANCE = Mappers.getMapper(HistoryLoginMapper.class);

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
