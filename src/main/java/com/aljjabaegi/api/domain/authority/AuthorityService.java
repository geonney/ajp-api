package com.aljjabaegi.api.domain.authority;

import com.aljjabaegi.api.common.response.ItemResponse;
import com.aljjabaegi.api.domain.authority.record.AuthorityCreateRequest;
import com.aljjabaegi.api.domain.authority.record.AuthorityCreateResponse;
import com.aljjabaegi.api.domain.member.MemberRepository;
import com.aljjabaegi.api.domain.menu.MenuRepository;
import com.aljjabaegi.api.domain.menuAuthority.MenuAuthorityMapper;
import com.aljjabaegi.api.domain.menuAuthority.MenuAuthorityRepository;
import com.aljjabaegi.api.domain.menuAuthority.record.MenuAuthorityCreateRequest;
import com.aljjabaegi.api.entity.Authority;
import com.aljjabaegi.api.entity.MenuAuthority;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 권한 Service
 *
 * @author GEONLEE
 * @since 2024-04-05
 */
@Service
@RequiredArgsConstructor
public class AuthorityService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AuthorityService.class);
    private final AuthorityRepository authorityRepository;
    private final MenuAuthorityRepository menuAuthorityRepository;
    private final MemberRepository memberRepository;
    private final MenuRepository menuRepository;
    private final AuthorityMapper authorityMapper = AuthorityMapper.INSTANCE;
    private final MenuAuthorityMapper menuAuthorityMapper = MenuAuthorityMapper.INSTANCE;

    @Transactional
    public ResponseEntity<ItemResponse<AuthorityCreateResponse>> createAuthority(AuthorityCreateRequest parameter) {
        if (authorityRepository.existsById(parameter.authorityCode())) {
            throw new EntityExistsException(parameter.authorityCode());
        }
        //insertable, updatable false example
        // 권한 추가
        Authority createRequestEntity = authorityMapper.toEntity(parameter);
        Authority createdEntity = authorityRepository.save(createRequestEntity);

        // 메뉴 권한 추가
        String authorityCode = createdEntity.getAuthorityCode();
        List<String> menuIds = parameter.menuIds();
        List<MenuAuthorityCreateRequest> menuAuthorityCreateRequests = new ArrayList<>();
        for (String menuId : menuIds) {
            menuAuthorityCreateRequests.add(MenuAuthorityCreateRequest.builder()
                    .authorityCode(authorityCode)
                    .menuId(menuId)
                    .build());
        }
        List<MenuAuthority> entityList = menuAuthorityMapper.toEntityList(menuAuthorityCreateRequests); //비영속
        List<MenuAuthority> createdEntityList = menuAuthorityRepository.saveAll(entityList); // 영속
        // MenuAuthority 에 연결
        for (MenuAuthority menuAuthority : createdEntityList) {
            menuRepository.findById(menuAuthority.getKey().getMenuId()).ifPresent(menuAuthority::setMenu); // 메뉴 관계 추가
            authorityRepository.findById(menuAuthority.getKey().getAuthorityCode()).ifPresent(menuAuthority::setAuthority); // 권한 관계 추가
        }
        createdEntity.setMenus(createdEntityList);
        AuthorityCreateResponse response = authorityMapper.toCreateResponse(createdEntity);

        return ResponseEntity.ok()
                .body(ItemResponse.<AuthorityCreateResponse>builder()
                        .status("OK")
                        .message("데이터를 추가하는데 성공하였습니다.")
                        .item(response)
                        .build());
    }

    /**
     * 권한 삭제
     *
     * @author GEONLEE
     * @since 2024-04-05
     */
    @Transactional
    public Long deleteAuthority(String authorityCode) {
        if ("ROLE_ADMIN".equals(authorityCode) || "ROLE_USER".equals(authorityCode)) {
            throw new EntityNotFoundException(authorityCode);
        }
        Authority entity = authorityRepository.findById(authorityCode)
                .orElseThrow(() -> new EntityNotFoundException(authorityCode));
        /*부모 삭제 시 자식도 삭제되길 원한다면
         * 부모 Entity 에 cascade = CascadeType.REMOVE or orphanRemoval = true 설정
         *
         * 부모 삭제 시 자식의 FK를 null 로 초기화 하길 원한다면
         * List<Member> members = entity.getMembers();
         * for (int i = members.size() - 1; i >= 0; i--) {
         *     members.get(i).setAuthority(null);
         * }
         * or Iterator 반복문 활용
         * 기본 반복문 사용 시 관계 해제되면서 size 가 변경되어 문제 발생.
         * java.util.ConcurrentModificationException or FK 관계성 관련 오류
         *
         * 반복문 활용 시, 부모를 FK로 가지고 있는 자식 개수 만큼 반복됨.
         * 이를 해결하기 위해 아래와 같이 벌크 연산 활용
         * */
        int updateSize = memberRepository.updateAuthority(entity.getAuthorityCode());
        LOGGER.info("update member size : {}", updateSize);
        authorityRepository.delete(entity);
        return 1L;
    }
}
