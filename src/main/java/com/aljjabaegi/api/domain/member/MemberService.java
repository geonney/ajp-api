package com.aljjabaegi.api.domain.member;

import com.aljjabaegi.api.common.exception.code.CommonErrorCode;
import com.aljjabaegi.api.common.exception.custom.ServiceException;
import com.aljjabaegi.api.common.jpa.dynamicSearch.specification.DynamicSpecification;
import com.aljjabaegi.api.common.request.DynamicFilter;
import com.aljjabaegi.api.common.request.DynamicRequest;
import com.aljjabaegi.api.common.response.GridItemsResponse;
import com.aljjabaegi.api.common.util.password.PasswordUtils;
import com.aljjabaegi.api.config.security.rsa.RsaProvider;
import com.aljjabaegi.api.domain.member.record.*;
import com.aljjabaegi.api.domain.team.TeamRepository;
import com.aljjabaegi.api.entity.Member;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 사용자 Service
 *
 * @author GEONLEE
 * @since 2024-04-01<br />
 * 2024-04-03 GEONLEE user->member 명칭 변경<br />
 */
@Service
@RequiredArgsConstructor
public class MemberService {

    private final DynamicSpecification dynamicSpecification;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final RsaProvider rsaProvider;
    private final MemberMapper memberMapper = MemberMapper.INSTANCE;

    /**
     * 전체 사용자 조회 (Dynamic Filter list 활용)
     *
     * @author GEONLEE
     * @since 2024-04-01<br />
     * 2024-04-04 GEONLEE - 관리자 조회 안되게 수정<br />
     * 2024-04-11 GEONLEE - Apply DynamicSpecification<br />
     */
    @Transactional
    @SuppressWarnings("unchecked")
    public List<MemberSearchResponse> getMemberList(List<DynamicFilter> dynamicFilters) {
        Specification<Member> specification = (Specification<Member>) dynamicSpecification.generateConditions(Member.class, dynamicFilters);
        return memberMapper.toSearchResponseList(memberRepository.findAll(specification));
    }

    /**
     * 전체 사용자 조회 (Dynamic request 활용)
     *
     * @author GEONLEE
     * @since 2024-04-12<br />
     */
    @Transactional
    @SuppressWarnings("unchecked")
    public GridItemsResponse<MemberSearchResponse> getUserListUsingDynamicRequest(DynamicRequest dynamicRequest) {
        Sort sort = dynamicSpecification.generateSort(Member.class, dynamicRequest.sorter());
        Pageable pageable = PageRequest.of(dynamicRequest.pageNo(), dynamicRequest.pageSize(), sort);
        Specification<Member> specification = (Specification<Member>) dynamicSpecification.generateConditions(Member.class, dynamicRequest.filter());

        Page<Member> page = memberRepository.findAll(specification, pageable);

        int totalPage = page.getTotalPages();
        long totalElements = page.getTotalElements();
        List<MemberSearchResponse> memberSearchResponseList = memberMapper.toSearchResponseList(page.getContent());

        return GridItemsResponse.<MemberSearchResponse>builder()
                .status("OK")
                .message("데이터를 조회하는데 성공하였습니다.")
                .totalSize(totalElements)
                .totalPageSize(totalPage)
                .size(page.getNumberOfElements())
                .items(memberSearchResponseList)
                .build();
    }

    /**
     * 사용자 ID로 사용자 조회
     *
     * @author GEONLEE
     * @since 2024-04-01<br />
     * 2024-04-04 GEONLEE - 관리자 조회 안되게 수정<br />
     * 2024-04-16 GEONLEE - @Transactional 추가<br />
     */
    @Transactional
    public MemberSearchResponse getMembers(String memberId) {
        Member entity = memberRepository.findByMemberIdAndAuthorityAuthorityCodeNot(memberId, "ROLE_ADMIN")
                .orElseThrow(() -> new EntityNotFoundException(memberId));
        return memberMapper.toSearchResponse(entity);
    }

    /**
     * 사용자 ID 중복 확인
     *
     * @author GEONLEE
     * @since 2024-04-01
     */
    public boolean checkMemberId(String memberId) {
        return memberRepository.existsById(memberId);
    }

    /**
     * 사용자 추가
     *
     * @author GEONLEE
     * @since 2024-04-01<br />
     * 2024-04-17 GEONLEE - password 유효성 검증 추가<br />
     */
    @Transactional
    public MemberCreateResponse createMember(MemberCreateRequest parameter) {
        if (memberRepository.existsById(parameter.memberId())) {
            throw new EntityExistsException("memberId already existed. memberId: " + parameter.memberId());
        }
        // password 유효성 검증
        boolean isValidPassword = PasswordUtils.validPassword(rsaProvider.decrypt(parameter.password()));
        if (!isValidPassword) {
            throw new ServiceException(CommonErrorCode.INVALID_PARAMETER, "Invalid password.");
        }
        Member createRequestEntity = memberMapper.toEntity(parameter); //비영속
        Member createdEntity = memberRepository.save(createRequestEntity); //영속
        teamRepository.findById(parameter.teamId()).ifPresentOrElse(createdEntity::setTeam, () -> {
            throw new EntityNotFoundException("Team does not exist. teamId: " + parameter.teamId());
        });
        return memberMapper.toCreateResponse(createdEntity);
    }

    /**
     * 사용자 수정
     *
     * @author GEONLEE
     * @since 2024-04-01
     */
    @Transactional
    public MemberModifyResponse modifyMember(MemberModifyRequest parameter) {
        Member entity = memberRepository.findById(parameter.memberId())
                .orElseThrow(() -> new EntityNotFoundException(parameter.memberId()));
        Member modifiedEntity = memberMapper.updateFromRequest(parameter, entity);
        teamRepository.findById(parameter.teamId()).ifPresentOrElse(modifiedEntity::setTeam, () -> {
            throw new EntityNotFoundException("Team does not exist. teamId: " + parameter.teamId());
        });
        modifiedEntity = memberRepository.saveAndFlush(modifiedEntity);
        return memberMapper.toModifyResponse(modifiedEntity);
    }

    /**
     * 사용자 삭제
     *
     * @author GEONLEE
     * @since 2024-04-01
     */
    public Long deleteMember(String memberId) {
        Member entity = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(memberId));
        memberRepository.delete(entity);
        return 1L;
    }
}
