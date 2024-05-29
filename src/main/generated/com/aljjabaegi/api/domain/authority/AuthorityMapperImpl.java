package com.aljjabaegi.api.domain.authority;

import com.aljjabaegi.api.common.jpa.mapstruct.Converter;
import com.aljjabaegi.api.domain.authority.record.AuthorityCreateRequest;
import com.aljjabaegi.api.domain.authority.record.AuthorityCreateResponse;
import com.aljjabaegi.api.domain.menu.record.MenuSimpleResponse;
import com.aljjabaegi.api.entity.Authority;
import com.aljjabaegi.api.entity.Menu;
import com.aljjabaegi.api.entity.MenuAuthority;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-29T18:28:00+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.7.jar, environment: Java 17 (Oracle Corporation)"
)
@Component
public class AuthorityMapperImpl implements AuthorityMapper {

    private final DateTimeFormatter dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168 = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" );

    @Override
    public List<MenuSimpleResponse> toCreateMenuResponseList(List<MenuAuthority> menuAuthorities) {
        if ( menuAuthorities == null ) {
            return null;
        }

        List<MenuSimpleResponse> list = new ArrayList<MenuSimpleResponse>( menuAuthorities.size() );
        for ( MenuAuthority menuAuthority : menuAuthorities ) {
            list.add( toCreateMenuResponse( menuAuthority ) );
        }

        return list;
    }

    @Override
    public MenuSimpleResponse toCreateMenuResponse(MenuAuthority menuAuthority) {
        if ( menuAuthority == null ) {
            return null;
        }

        String menuId = null;
        String menuName = null;

        menuId = menuAuthorityMenuMenuId( menuAuthority );
        menuName = menuAuthorityMenuMenuName( menuAuthority );

        MenuSimpleResponse menuSimpleResponse = new MenuSimpleResponse( menuId, menuName );

        return menuSimpleResponse;
    }

    @Override
    public AuthorityCreateResponse toCreateResponse(Authority entity) {
        if ( entity == null ) {
            return null;
        }

        String createDate = null;
        String modifyDate = null;
        String authorityCode = null;
        String authorityName = null;
        List<MenuSimpleResponse> menus = null;

        if ( entity.getCreateDate() != null ) {
            createDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getCreateDate() );
        }
        if ( entity.getModifyDate() != null ) {
            modifyDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getModifyDate() );
        }
        authorityCode = entity.getAuthorityCode();
        authorityName = entity.getAuthorityName();
        menus = toCreateMenuResponseList( entity.getMenus() );

        AuthorityCreateResponse authorityCreateResponse = new AuthorityCreateResponse( authorityCode, authorityName, menus, createDate, modifyDate );

        return authorityCreateResponse;
    }

    @Override
    public Authority toEntity(AuthorityCreateRequest authorityCreateRequest) {
        if ( authorityCreateRequest == null ) {
            return null;
        }

        Authority authority = new Authority();

        authority.setAuthorityCode( authorityCreateRequest.authorityCode() );
        authority.setAuthorityName( authorityCreateRequest.authorityName() );

        return authority;
    }

    private String menuAuthorityMenuMenuId(MenuAuthority menuAuthority) {
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

    private String menuAuthorityMenuMenuName(MenuAuthority menuAuthority) {
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
}
