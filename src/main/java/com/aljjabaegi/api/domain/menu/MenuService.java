package com.aljjabaegi.api.domain.menu;

import com.aljjabaegi.api.common.request.DynamicFilter;
import com.aljjabaegi.api.common.request.enumeration.Operator;
import com.aljjabaegi.api.common.response.ItemResponse;
import com.aljjabaegi.api.common.response.ItemsResponse;
import com.aljjabaegi.api.domain.menu.record.MenuCreateRequest;
import com.aljjabaegi.api.domain.menu.record.MenuCreateResponse;
import com.aljjabaegi.api.domain.menu.record.MenuSearchResponse;
import com.aljjabaegi.api.domain.menuAuthority.MenuAuthorityMapper;
import com.aljjabaegi.api.domain.menuAuthority.MenuAuthorityRepository;
import com.aljjabaegi.api.entity.Menu;
import com.aljjabaegi.api.entity.MenuAuthority;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Team Service
 *
 * @author GEONLEE
 * @since 2024-04-08<br />
 */
@Service
@RequiredArgsConstructor
public class MenuService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuService.class);

    private final MenuAuthorityRepository menuAuthorityRepository;
    private final MenuRepository menuRepository;
    private final MenuAuthorityMapper menuAuthorityMapper = MenuAuthorityMapper.INSTANCE;
    private final MenuMapper menuMapper = MenuMapper.INSTANCE;

    /**
     * 전체 Team 조회
     *
     * @author GEONLEE
     * @since 2024-04-08<br />
     * 2024-04-09 GEONLEE - Apply Specifications<br />
     */
    @Transactional
    public ResponseEntity<ItemsResponse<MenuSearchResponse>> getMenusByAuthority() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<MenuSearchResponse> menuSearchResponses = new ArrayList<>();
        if (authentication != null && authentication.isAuthenticated()) {
            StringJoiner stringJoiner = new StringJoiner(",");
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                String authorityName = authority.getAuthority();
                stringJoiner.add(authorityName);
            }
            List<MenuAuthority> menuAuthorities = menuAuthorityRepository.findDynamic(DynamicFilter.builder()
                    .field("authorityCode")
                    .operator(Operator.IN)
                    .value(stringJoiner.toString())
                    .build());
            menuSearchResponses = menuAuthorityMapper.toSearchResponseList(menuAuthorities);
        }
        return ResponseEntity.ok()
                .body(ItemsResponse.<MenuSearchResponse>builder()
                        .status("OK")
                        .message("데이터를 조회하는데 성공하였습니다.")
                        .size((long) menuSearchResponses.size())
                        .items(menuSearchResponses)
                        .build());
    }

    /**
     * Manu 추가
     *
     * @author GEONLEE
     * @since 2024-04-08
     */
    public ResponseEntity<ItemResponse<MenuCreateResponse>> createMenu(MenuCreateRequest parameter) {
        Menu createRequestEntity = menuMapper.toEntity(parameter);
        Menu createdEntity = menuRepository.save(createRequestEntity);
        return ResponseEntity.ok()
                .body(ItemResponse.<MenuCreateResponse>builder()
                        .status("OK")
                        .message("데이터를 추가하는데 성공하였습니다.")
                        .item(menuMapper.toCreateResponse(createdEntity)).build());
    }
//
//    /**
//     * Team 수정
//     *
//     * @author GEONLEE
//     * @since 2024-04-08
//     */
//    public MenuModifyResponse modifyMenu(MenuModifyRequest parameter) {
//        Menu entity = menuRepository.findById(parameter.menuId())
//                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(parameter.menuId())));
//        Menu modifiedEntity = menuMapper.updateFromRequest(parameter, entity);
//        modifiedEntity = menuRepository.saveAndFlush(modifiedEntity);
//        return menuMapper.toModifyResponse(modifiedEntity);
//    }
//
//    /**
//     * Team 삭제
//     *
//     * @author GEONLEE
//     * @since 2024-04-04
//     */
//    @Transactional
//    public Long deleteTeam(String menuId) {
//        Menu entity = menuRepository.findById(menuId)
//                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(menuId)));
//        menuRepository.delete(entity);
//        return 1L;
//    }
}
