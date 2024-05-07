package com.aljjabaegi.api.domain.menu;

import com.aljjabaegi.api.common.jpa.mapstruct.Converter;
import com.aljjabaegi.api.domain.menu.record.MenuCreateRequest;
import com.aljjabaegi.api.domain.menu.record.MenuCreateResponse;
import com.aljjabaegi.api.domain.menu.record.MenuSearchResponse;
import com.aljjabaegi.api.entity.Menu;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-07T18:54:05+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.7.jar, environment: Java 17 (Oracle Corporation)"
)
@Component
public class MenuMapperImpl implements MenuMapper {

    private final DateTimeFormatter dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168 = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" );

    @Override
    public MenuSearchResponse toSearchResponse(Menu entity) {
        if ( entity == null ) {
            return null;
        }

        String createDate = null;
        String modifyDate = null;
        String menuId = null;
        String menuName = null;
        String upperMenuId = null;
        String menuPath = null;
        List<MenuSearchResponse> subMenus = null;

        if ( entity.getCreateDate() != null ) {
            createDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getCreateDate() );
        }
        if ( entity.getModifyDate() != null ) {
            modifyDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getModifyDate() );
        }
        menuId = entity.getMenuId();
        menuName = entity.getMenuName();
        upperMenuId = entity.getUpperMenuId();
        menuPath = entity.getMenuPath();
        subMenus = toSearchResponseList( entity.getSubMenus() );

        MenuSearchResponse menuSearchResponse = new MenuSearchResponse( menuId, menuName, upperMenuId, menuPath, createDate, modifyDate, subMenus );

        return menuSearchResponse;
    }

    @Override
    public List<MenuSearchResponse> toSearchResponseList(List<Menu> list) {
        if ( list == null ) {
            return null;
        }

        List<MenuSearchResponse> list1 = new ArrayList<MenuSearchResponse>( list.size() );
        for ( Menu menu : list ) {
            list1.add( toSearchResponse( menu ) );
        }

        return list1;
    }

    @Override
    public MenuCreateResponse toCreateResponse(Menu entity) {
        if ( entity == null ) {
            return null;
        }

        String createDate = null;
        String modifyDate = null;
        String menuId = null;
        String menuName = null;
        String upperMenuId = null;
        String menuPath = null;

        if ( entity.getCreateDate() != null ) {
            createDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getCreateDate() );
        }
        if ( entity.getModifyDate() != null ) {
            modifyDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getModifyDate() );
        }
        menuId = entity.getMenuId();
        menuName = entity.getMenuName();
        upperMenuId = entity.getUpperMenuId();
        menuPath = entity.getMenuPath();

        MenuCreateResponse menuCreateResponse = new MenuCreateResponse( menuId, menuName, upperMenuId, menuPath, createDate, modifyDate );

        return menuCreateResponse;
    }

    @Override
    public Menu toEntity(MenuCreateRequest menuCreateRequest) {
        if ( menuCreateRequest == null ) {
            return null;
        }

        Menu menu = new Menu();

        menu.setMenuName( menuCreateRequest.menuName() );
        menu.setUpperMenuId( menuCreateRequest.upperMenuId() );
        menu.setMenuPath( menuCreateRequest.menuPath() );

        return menu;
    }
}
