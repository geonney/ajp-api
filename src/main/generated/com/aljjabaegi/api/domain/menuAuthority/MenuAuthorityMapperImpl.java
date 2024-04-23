package com.aljjabaegi.api.domain.menuAuthority;

import com.aljjabaegi.api.common.jpa.mapstruct.Converter;
import com.aljjabaegi.api.domain.menu.record.MenuSearchResponse;
import com.aljjabaegi.api.domain.menuAuthority.record.MenuAuthorityCreateRequest;
import com.aljjabaegi.api.entity.Menu;
import com.aljjabaegi.api.entity.MenuAuthority;
import com.aljjabaegi.api.entity.key.MenuAuthorityKey;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-23T09:32:47+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.7.jar, environment: Java 17 (Oracle Corporation)"
)
@Component
public class MenuAuthorityMapperImpl implements MenuAuthorityMapper {

    private final DateTimeFormatter dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168 = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" );

    @Override
    public MenuSearchResponse toSearchResponse(MenuAuthority entity) {
        if ( entity == null ) {
            return null;
        }

        String menuId = null;
        String menuName = null;
        String upperMenuId = null;
        String menuPath = null;
        String createDate = null;
        String modifyDate = null;

        menuId = entityMenuMenuId( entity );
        menuName = entityMenuMenuName( entity );
        upperMenuId = entityMenuUpperMenuId( entity );
        menuPath = entityMenuMenuPath( entity );
        if ( entity.getCreateDate() != null ) {
            createDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getCreateDate() );
        }
        if ( entity.getModifyDate() != null ) {
            modifyDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getModifyDate() );
        }

        MenuSearchResponse menuSearchResponse = new MenuSearchResponse( menuId, menuName, upperMenuId, menuPath, createDate, modifyDate );

        return menuSearchResponse;
    }

    @Override
    public List<MenuSearchResponse> toSearchResponseList(List<MenuAuthority> list) {
        if ( list == null ) {
            return null;
        }

        List<MenuSearchResponse> list1 = new ArrayList<MenuSearchResponse>( list.size() );
        for ( MenuAuthority menuAuthority : list ) {
            list1.add( toSearchResponse( menuAuthority ) );
        }

        return list1;
    }

    @Override
    public MenuAuthority toEntity(MenuAuthorityCreateRequest menuAuthorityCreateRequest) {
        if ( menuAuthorityCreateRequest == null ) {
            return null;
        }

        MenuAuthority menuAuthority = new MenuAuthority();

        menuAuthority.setKey( menuAuthorityCreateRequestToMenuAuthorityKey( menuAuthorityCreateRequest ) );

        return menuAuthority;
    }

    @Override
    public List<MenuAuthority> toEntityList(List<MenuAuthorityCreateRequest> menuAuthorityCreateRequest) {
        if ( menuAuthorityCreateRequest == null ) {
            return null;
        }

        List<MenuAuthority> list = new ArrayList<MenuAuthority>( menuAuthorityCreateRequest.size() );
        for ( MenuAuthorityCreateRequest menuAuthorityCreateRequest1 : menuAuthorityCreateRequest ) {
            list.add( toEntity( menuAuthorityCreateRequest1 ) );
        }

        return list;
    }

    private String entityMenuMenuId(MenuAuthority menuAuthority) {
        if ( menuAuthority == null ) {
            return null;
        }
        Menu menu = menuAuthority.getMenu();
        if ( menu == null ) {
            return null;
        }
        String menuId = menu.getMenuId();
        if ( menuId == null ) {
            return null;
        }
        return menuId;
    }

    private String entityMenuMenuName(MenuAuthority menuAuthority) {
        if ( menuAuthority == null ) {
            return null;
        }
        Menu menu = menuAuthority.getMenu();
        if ( menu == null ) {
            return null;
        }
        String menuName = menu.getMenuName();
        if ( menuName == null ) {
            return null;
        }
        return menuName;
    }

    private String entityMenuUpperMenuId(MenuAuthority menuAuthority) {
        if ( menuAuthority == null ) {
            return null;
        }
        Menu menu = menuAuthority.getMenu();
        if ( menu == null ) {
            return null;
        }
        String upperMenuId = menu.getUpperMenuId();
        if ( upperMenuId == null ) {
            return null;
        }
        return upperMenuId;
    }

    private String entityMenuMenuPath(MenuAuthority menuAuthority) {
        if ( menuAuthority == null ) {
            return null;
        }
        Menu menu = menuAuthority.getMenu();
        if ( menu == null ) {
            return null;
        }
        String menuPath = menu.getMenuPath();
        if ( menuPath == null ) {
            return null;
        }
        return menuPath;
    }

    protected MenuAuthorityKey menuAuthorityCreateRequestToMenuAuthorityKey(MenuAuthorityCreateRequest menuAuthorityCreateRequest) {
        if ( menuAuthorityCreateRequest == null ) {
            return null;
        }

        MenuAuthorityKey menuAuthorityKey = new MenuAuthorityKey();

        menuAuthorityKey.setMenuId( menuAuthorityCreateRequest.menuId() );
        menuAuthorityKey.setAuthorityCode( menuAuthorityCreateRequest.authorityCode() );

        return menuAuthorityKey;
    }
}
