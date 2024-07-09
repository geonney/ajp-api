package com.aljjabaegi.api.domain.code;

import com.aljjabaegi.api.common.converter.Converter;
import com.aljjabaegi.api.domain.code.record.CodeSearchResponse;
import com.aljjabaegi.api.entity.Code;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author GEONLEE
 * @since 2024-05-29
 */
@Mapper(componentModel = "spring", imports = Converter.class)
public interface CodeMapper {
    CodeMapper INSTANCE = Mappers.getMapper(CodeMapper.class);

    /**
     * entity to search response
     *
     * @param entity board entity
     * @return boardSearchResponse
     * @author GEONLEE
     * @since 2024-04-04<br />
     */
    @Mappings({
            @Mapping(target = "codeGroupId", source = "key.codeGroupId"),
            @Mapping(target = "codeId", source = "key.codeId"),
            @Mapping(target = "createDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(target = "modifyDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
    })
    CodeSearchResponse toSearchResponse(Code entity);

    List<CodeSearchResponse> toSearchListResponse(List<Code> entityList);
}
