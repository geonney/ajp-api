package com.aljjabaegi.api.domain.member;

import com.aljjabaegi.api.common.exception.code.CommonErrorCode;
import com.aljjabaegi.api.common.exception.custom.ServiceException;
import com.aljjabaegi.api.common.jpa.dynamicSearch.querydsl.DynamicBooleanBuilder;
import com.aljjabaegi.api.common.jpa.dynamicSearch.specification.DynamicSpecification;
import com.aljjabaegi.api.common.request.DynamicFilter;
import com.aljjabaegi.api.common.request.DynamicRequest;
import com.aljjabaegi.api.common.request.DynamicSorter;
import com.aljjabaegi.api.common.request.enumeration.SortDirection;
import com.aljjabaegi.api.common.response.GridResponse;
import com.aljjabaegi.api.common.util.password.PasswordUtils;
import com.aljjabaegi.api.config.security.rsa.RsaProvider;
import com.aljjabaegi.api.domain.member.record.*;
import com.aljjabaegi.api.domain.memberTeam.MemberTeamRepository;
import com.aljjabaegi.api.domain.team.TeamRepository;
import com.aljjabaegi.api.entity.Member;
import com.aljjabaegi.api.entity.MemberTeam;
import com.aljjabaegi.api.entity.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
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

import java.util.ArrayList;
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

    private final DynamicBooleanBuilder dynamicBooleanBuilder;
    private final JPAQueryFactory query;
    private final DynamicSpecification dynamicSpecification;
    private final MemberRepository memberRepository;
    private final MemberTeamRepository memberTeamRepository;
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
     * 2024-04-29 GEONLEE - DynamicDslRepository 방식으로 변경<br />
     */
    @Transactional
    @SuppressWarnings("unchecked")
    public List<MemberSearchResponse> getMemberList(List<DynamicFilter> dynamicFilters) {
        //specification 반식
//        Specification<Member> specification = (Specification<Member>) dynamicSpecification.generateConditions(Member.class, dynamicFilters);
//        return memberMapper.toSearchResponseList(memberRepository.findAll(specification));
        //booleanBuilder 방식
//        BooleanBuilder booleanBuilder = dynamicBooleanBuilder.generateConditions(Board.class, dynamicFilters);
//        List<Member> memberList = query.selectFrom(QMember.member)
//                .where(booleanBuilder)
//                .fetch();
        //dynamic repository 방식
//        List<Member> memberList = memberRepository.findDynamic(dynamicFilters);
        //dynamic repository paging

        List<DynamicSorter> sorters = new ArrayList<>();
        DynamicSorter dynamicSorter = new DynamicSorter("useYn", SortDirection.ASC);
        sorters.add(dynamicSorter);

        DynamicRequest dynamicRequest = new DynamicRequest(0, 10, dynamicFilters, sorters);
        Page<Member> page = memberRepository.findDynamicWithPageable(dynamicRequest);

        return memberMapper.toSearchResponseList(page.getContent());
    }

    /**
     * 전체 사용자 조회 (Dynamic request 활용)
     *
     * @author GEONLEE
     * @since 2024-04-12<br />
     */
    @Transactional
    @SuppressWarnings("unchecked")
    public GridResponse<MemberSearchResponse> getUserListUsingDynamicRequest(DynamicRequest dynamicRequest) {
        Sort sort = dynamicSpecification.generateSort(Member.class, dynamicRequest.sorter());
        Pageable pageable = PageRequest.of(dynamicRequest.pageNo(), dynamicRequest.pageSize(), sort);
        Specification<Member> specification = (Specification<Member>) dynamicSpecification.generateConditions(Member.class, dynamicRequest.filter());

        Page<Member> page = memberRepository.findAll(specification, pageable);

        int totalPage = page.getTotalPages();
        long totalElements = page.getTotalElements();
        List<MemberSearchResponse> memberSearchResponseList = memberMapper.toSearchResponseList(page.getContent());

        return GridResponse.<MemberSearchResponse>builder()
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

        teamRepository.findById(parameter.teamId()).ifPresentOrElse(team -> {
            MemberTeam memberTeamEntity = new MemberTeam(); // 비영속
            memberTeamEntity.setMemberId(createdEntity.getMemberId());
            memberTeamEntity.setTeam(team);
            memberTeamEntity.setResponsibilitiesCode("01");
            MemberTeam createdMemberTeam = memberTeamRepository.saveAndFlush(memberTeamEntity); //영속
            createdEntity.setTeam(createdMemberTeam); //영속된 팀을 연결
        }, () -> {
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
                .orElseThrow(() -> new EntityNotFoundException("Member does not exist, memberId: " + parameter.memberId()));
        Team teamEntity = teamRepository.findById(parameter.teamId())
                .orElseThrow(() -> new EntityNotFoundException("Team does not exist. teamId: " + parameter.teamId()));
        Member modifiedEntity = memberMapper.updateFromRequest(parameter, entity);
        MemberTeam memberTeam = modifiedEntity.getTeam();
        if (memberTeam == null) {
            MemberTeam newMemberTeam = new MemberTeam();
            newMemberTeam.setMemberId(entity.getMemberId());
            newMemberTeam.setTeam(teamEntity);
            newMemberTeam = memberTeamRepository.save(newMemberTeam);
            modifiedEntity.setTeam(newMemberTeam);
        } else {
            modifiedEntity.getTeam().setTeam(teamEntity);
        }

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
