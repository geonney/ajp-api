package com.aljjabaegi.api.domain.authority;

import com.aljjabaegi.api.common.jpa.mapstruct.Converter;
import com.aljjabaegi.api.domain.authority.record.AuthorityCreateRequest;
import com.aljjabaegi.api.domain.authority.record.AuthorityCreateResponse;
import com.aljjabaegi.api.domain.menu.record.MenuSimpleResponse;
import com.aljjabaegi.api.entity.Authority;
import com.aljjabaegi.api.entity.MenuAuthority;
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
public interface AuthorityMapper {
    AuthorityMapper INSTANCE = Mappers.getMapper(AuthorityMapper.class);

//    /**
//     * entity to search response
//     *
//     * @param entity team entity
//     * @return teamSearchResponse
//     * @author GEONLEE
//     * @since 2024-04-08<br />
//     */
//    @Mappings({
//            @Mapping(target = "createDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
//            @Mapping(target = "modifyDate", dateFormat = "yyyy-MM-dd HH:mm:ss"),
//    })
//    MenuSearchResponse toSearchResponse(Menu entity);

//    /**
//     * entity list to search response list
//     *
//     * @param list menu entity list
//     * @return menuSearchResponse list
//     * @author GEONLEE
//     * @since 2024-04-08<br />
//     */
//    List<MenuSearchResponse> toSearchResponseList(List<Menu> list);

    List<MenuSimpleResponse> toCreateMenuResponseList(List<MenuAuthority> menuAuthorities);

    @Mappings({
            @Mapping(target = "menuId", source = "menu.menuId"),
            @Mapping(target = "menuName", source = "menu.menuName")
    })
    MenuSimpleResponse toCreateMenuResponse(MenuAuthority menuAuthority);

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
    AuthorityCreateResponse toCreateResponse(Authority entity);
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
     * @param authorityCreateRequest authority create request
     * @return menu
     * @author GEONLEE
     * @since 2024-04-04<br />
     */
    Authority toEntity(AuthorityCreateRequest authorityCreateRequest);
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
