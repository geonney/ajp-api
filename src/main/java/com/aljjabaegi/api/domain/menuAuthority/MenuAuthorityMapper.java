package com.aljjabaegi.api.domain.menuAuthority;

import com.aljjabaegi.api.common.converter.Converter;
import com.aljjabaegi.api.domain.menu.record.MenuSearchResponse;
import com.aljjabaegi.api.domain.menuAuthority.record.MenuAuthorityCreateRequest;
import com.aljjabaegi.api.entity.Menu;
import com.aljjabaegi.api.entity.MenuAuthority;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Menu Authority mapper
 *
 * @author GEONLEE
 * @since 2024-04-22
 */
@Mapper(componentModel = "spring", imports = Converter.class)
public interface MenuAuthorityMapper {
    MenuAuthorityMapper INSTANCE = Mappers.getMapper(MenuAuthorityMapper.class);



    List<MenuSearchResponse> toSearchSubMenusResponse(List<Menu> menus);
    /**
     * entity to search response
     *
     * @param entity menu authority entity
     * @return teamSearchResponse
     * @author GEONLEE
     * @since 2024-04-08<br />
     */
    @Mappings({
            @Mapping(target = "menuId", source = "menu.menuId"),
            @Mapping(target = "menuName", source = "menu.menuName"),
            @Mapping(target = "upperMenuId", source = "menu.upperMenuId"),
            @Mapping(target = "menuPath", source = "menu.menuPath"),
            @Mapping(target = "subMenus", source = "menu.subMenus"),
            @Mapping(target = "createDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(target = "modifyDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
    })
    MenuSearchResponse toSearchResponse(MenuAuthority entity);

    /**
     * entity list to search response list
     *
     * @param list menu authority entity list
     * @return menuSearchResponse list
     * @author GEONLEE
     * @since 2024-04-08<br />
     */
    List<MenuSearchResponse> toSearchResponseList(List<MenuAuthority> list);

//    /**
//     * entity to create response
//     *
//     * @param entity menu entity
//     * @return menuCreateResponse
//     * @author GEONLEE
//     * @since 2024-04-08<br />
//     */
//    @Mappings({
//            @Mapping(target = "createDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
//            @Mapping(target = "modifyDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
//    })
//    MenuCreateResponse toCreateResponse(Menu entity);
//
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
     * @param menuAuthorityCreateRequest menu create request
     * @return menu
     * @author GEONLEE
     * @since 2024-04-04<br />
     */
    @Mappings({
            @Mapping(target = "key.menuId", source = "menuId"),
            @Mapping(target = "key.authorityCode", source = "authorityCode"),
    })
    MenuAuthority toEntity(MenuAuthorityCreateRequest menuAuthorityCreateRequest);

    List<MenuAuthority> toEntityList(List<MenuAuthorityCreateRequest> menuAuthorityCreateRequest);
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
