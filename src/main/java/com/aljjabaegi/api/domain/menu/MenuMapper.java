package com.aljjabaegi.api.domain.menu;

import com.aljjabaegi.api.common.converter.Converter;
import com.aljjabaegi.api.domain.menu.record.MenuCreateRequest;
import com.aljjabaegi.api.domain.menu.record.MenuCreateResponse;
import com.aljjabaegi.api.domain.menu.record.MenuSearchResponse;
import com.aljjabaegi.api.entity.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Team mapper
 *
 * @author GEONLEE
 * @since 2024-04-08
 */
@Mapper(componentModel = "spring", imports = Converter.class)
public interface MenuMapper {
    MenuMapper INSTANCE = Mappers.getMapper(MenuMapper.class);

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
    MenuSearchResponse toSearchResponse(Menu entity);

    /**
     * entity list to search response list
     *
     * @param list menu entity list
     * @return menuSearchResponse list
     * @author GEONLEE
     * @since 2024-04-08<br />
     */
    List<MenuSearchResponse> toSearchResponseList(List<Menu> list);

    /**
     * entity to create response
     *
     * @param entity menu entity
     * @return menuCreateResponse
     * @author GEONLEE
     * @since 2024-04-08<br />
     */
    @Mappings({
            @Mapping(target = "createDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(target = "modifyDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    })
    MenuCreateResponse toCreateResponse(Menu entity);
//    /**
//     * entity to modify response
//     *
//     * @param entity menu entity
//     * @return menuModifyResponse
//     * @author GEONLEE
//     * @since 2024-04-08<br />
//     */
//    @Mappings({
//            @Mapping(target = "createDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
//            @Mapping(target = "modifyDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
//    })
//    MenuModifyResponse toModifyResponse(Menu entity);
//
//

    /**
     * createRequest to entity
     *
     * @param menuCreateRequest menu create request
     * @return menu
     * @author GEONLEE
     * @since 2024-04-04<br />
     */
    Menu toEntity(MenuCreateRequest menuCreateRequest);
//
//    /**
//     * update from record
//     *
//     * @param menuModifyRequest update request record
//     * @param entity            update request entity
//     * @return menu
//     * @author GEONLEE
//     * @since 2024-04-08<br />
//     */
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
//    Team updateFromRequest(MenuModifyRequest menuModifyRequest, @MappingTarget Menu entity);
}
