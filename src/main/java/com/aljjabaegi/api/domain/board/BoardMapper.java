package com.aljjabaegi.api.domain.board;

import com.aljjabaegi.api.common.converter.Converter;
import com.aljjabaegi.api.domain.board.record.*;
import com.aljjabaegi.api.entity.Board;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Board mapper
 *
 * @author GEONLEE
 * @since 2024-04-04
 */
@Mapper(componentModel = "spring", imports = Converter.class)
public interface BoardMapper {
    BoardMapper INSTANCE = Mappers.getMapper(BoardMapper.class);

    /**
     * entity to search response
     *
     * @param entity board entity
     * @return boardSearchResponse
     * @author GEONLEE
     * @since 2024-04-04<br />
     */
    @Mappings({
            @Mapping(target = "createDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(target = "modifyDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
    })
    BoardSearchResponse toSearchResponse(Board entity);

    /**
     * entity list to search response list
     *
     * @param list board entity list
     * @return boardSearchResponse list
     * @author GEONLEE
     * @since 2024-04-04<br />
     */
    List<BoardSearchResponse> toSearchResponseList(List<Board> list);

    /**
     * entity to create response
     *
     * @param entity board entity
     * @return boardCreateResponse
     * @author GEONLEE
     * @since 2024-04-01<br />
     */
    @Mappings({
            @Mapping(target = "createDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(target = "modifyDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    })
    BoardCreateResponse toCreateResponse(Board entity);

    /**
     * entity to modify response
     *
     * @param entity board entity
     * @return boardModifyResponse
     * @author GEONLEE
     * @since 2024-04-04<br />
     */
    @Mappings({
            @Mapping(target = "createDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(target = "modifyDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    })
    BoardModifyResponse toModifyResponse(Board entity);

    /**
     * createRequest to entity
     *
     * @param boardCreateRequest board create request
     * @return board
     * @author GEONLEE
     * @since 2024-04-04<br />
     */
    Board toEntity(BoardCreateRequest boardCreateRequest);

    /**
     * update from record
     *
     * @param boardModifyRequest update request record
     * @param entity             update request entity
     * @return board
     * @author GEONLEE
     * @since 2024-04-01<br />
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    Board updateFromRequest(BoardModifyRequest boardModifyRequest, @MappingTarget Board entity);
}
