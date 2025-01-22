package com.aljjabaegi.api.domain.member;

import com.aljjabaegi.api.common.exception.code.CommonErrorCode;
import com.aljjabaegi.api.common.exception.custom.ServiceException;
import com.aljjabaegi.api.common.jpa.dynamicSearch.querydsl.DynamicBooleanBuilder;
import com.aljjabaegi.api.common.jpa.dynamicSearch.specification.DynamicSpecification;
import com.aljjabaegi.api.common.request.DynamicFilter;
import com.aljjabaegi.api.common.request.DynamicRequest;
import com.aljjabaegi.api.common.request.enumeration.Operator;
import com.aljjabaegi.api.common.response.GridResponse;
import com.aljjabaegi.api.common.util.password.PasswordUtils;
import com.aljjabaegi.api.config.security.jwt.TokenProvider;
import com.aljjabaegi.api.config.security.jwt.record.TokenResponse;
import com.aljjabaegi.api.config.security.rsa.RsaProvider;
import com.aljjabaegi.api.domain.member.record.*;
import com.aljjabaegi.api.domain.memberTeam.MemberTeamRepository;
import com.aljjabaegi.api.domain.team.TeamRepository;
import com.aljjabaegi.api.entity.Member;
import com.aljjabaegi.api.entity.MemberTeam;
import com.aljjabaegi.api.entity.QMember;
import com.aljjabaegi.api.entity.Team;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final DynamicBooleanBuilder dynamicBooleanBuilder;
    private final JPAQueryFactory query;
    private final DynamicSpecification dynamicSpecification;
    private final MemberRepository memberRepository;
    private final MemberTeamRepository memberTeamRepository;
    private final TeamRepository teamRepository;
    private final RsaProvider rsaProvider;
    private final TokenProvider tokenProvider;
    private final MemberMapper memberMapper = MemberMapper.INSTANCE;


    /**
     * dynamicSpecification 을 이용한 동적 Filtering
     *
     * @param parameter dynamicFilter list
     * @return Member list
     * @author GEONLEE
     * @since 2024-07-22
     */
    @Transactional
    @SuppressWarnings("unchecked")
    public List<MemberSearchResponse> getMemberListBySpecification(List<DynamicFilter> parameter) {
        Specification<Member> specification = (Specification<Member>) dynamicSpecification.generateConditions(Member.class, parameter);
        return memberMapper.toSearchResponseList(memberRepository.findAll(specification));
    }

    /**
     * findDynamic 을 이용한 동적 Filtering
     *
     * @param parameter dynamicFilter list
     * @return Member list
     * @author GEONLEE
     * @since 2024-07-22
     */
    @Transactional
    public List<MemberSearchResponse> getMemberListByFindDynamic(List<DynamicFilter> parameter) {
        List<Member> memberList = memberRepository.findDynamic(parameter);
        return memberMapper.toSearchResponseList(memberList);
    }

    /**
     * findDynamic 을 이용한 동적 Filtering 및 paging
     *
     * @param parameter dynamicRequest
     * @return Member list
     * @author GEONLEE
     * @since 2024-07-22
     */
    @Transactional
    public GridResponse<MemberSearchResponse> getMemberListWithPagingByFindDynamic(DynamicRequest parameter) {
        Page<Member> page = memberRepository.findDynamicWithPageable(parameter);
        if (page.getContent().size() == 0) throw new ServiceException(CommonErrorCode.NO_DATA);
        return GridResponse.<MemberSearchResponse>builder()
                .status("OK")
                .message("데이터를 조회하는데 성공하였습니다.")
                .totalSize(page.getTotalElements())
                .totalPageSize(page.getTotalPages())
                .size(page.getNumberOfElements())
                .items(memberMapper.toSearchResponseList(page.getContent()))
                .build();
    }

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
    public List<MemberSearchResponse> getMemberList(DynamicRequest dynamicRequest) {
        //specification 반식
//        Specification<Member> specification = (Specification<Member>) dynamicSpecification.generateConditions(Member.class, dynamicFilters);
//        return memberMapper.toSearchResponseList(memberRepository.findAll(specification));
        //booleanBuilder 방식
        BooleanBuilder booleanBuilder = dynamicBooleanBuilder.generateConditions(Member.class, dynamicRequest.filter());
        List<Member> memberListQEntity = query.selectFrom(QMember.member)
                .where(booleanBuilder)
                .fetch();
        //dynamic repository 방식
        List<Member> memberList = memberRepository.findDynamic(dynamicRequest);
        return memberMapper.toSearchResponseList(memberList);
        //dynamic repository paging
//        List<DynamicSorter> sorters = new ArrayList<>();
//        DynamicSorter dynamicSorter = new DynamicSorter("useYn", SortDirection.ASC);
//        sorters.add(dynamicSorter);
//
//        DynamicRequest dynamicRequest = new DynamicRequest(0, 10, dynamicFilters, sorters);
//        Page<Member> page = memberRepository.findDynamicWithPageable(dynamicRequest);

//        return memberMapper.toSearchResponseList(page.getContent());
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

    @Transactional
    public List<MemberSearchResponse> getMembersOrCondition(DynamicRequest parameter) {

        BooleanBuilder memberIdBooleanBuilder = dynamicBooleanBuilder.generateConditions(Member.class
                , List.of(DynamicFilter.builder()
                        .field("memberId")
                        .value(parameter.getFieldValue("memberId"))
                        .operator(Operator.LIKE)
                        .build()));

        BooleanBuilder memberNameBooleanBuilder = dynamicBooleanBuilder.generateConditions(Member.class
                , List.of(DynamicFilter.builder()
                        .field("memberName")
                        .value(parameter.getFieldValue("memberName"))
                        .operator(Operator.LIKE)
                        .build()));

        BooleanBuilder orBooleanBuilder = memberIdBooleanBuilder.or(memberNameBooleanBuilder);
        List<OrderSpecifier<?>> orderSpecifiers = dynamicBooleanBuilder.generateSort(Member.class, parameter.sorter());
        QMember member = new QMember("member");
        Long totalSize = query.select(member.count())
                .from(member)
                .where(orBooleanBuilder)
                .fetchOne();
        totalSize = (totalSize == null) ? 0L : totalSize;
        Pageable pageable = PageRequest.of(parameter.pageNo(), parameter.pageSize());

        List<Member> members = query.selectFrom(member)
                .where(orBooleanBuilder)
                .orderBy(orderSpecifiers.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Page<Member> page = new PageImpl<>(members, pageable, totalSize);
        return memberMapper.toSearchResponseList(page.getContent());
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
            memberTeamEntity.setResponsibilityCodeId("cd00");
            MemberTeam createdMemberTeam = memberTeamRepository.saveAndFlush(memberTeamEntity); //영속
            createdEntity.setMemberTeam(createdMemberTeam); //영속된 팀을 연결
        }, () -> {
            throw new EntityNotFoundException("Team does not exist. teamId: " + parameter.teamId());
        });
        return memberMapper.toCreateResponse(createdEntity);
    }

    /**
     * 사용자 수정
     *
     * @author GEONLEE
     * @since 2024-04-01<br />
     * 2024-05-24 GEONLEE - Token 내 포함된 정보 변경 시 Token 갱신 처리 추가<br />
     */
    @Transactional
    public MemberModifyResponse modifyMember(MemberModifyRequest parameter, HttpServletResponse httpServletResponse) {
        Member entity = memberRepository.findById(parameter.memberId())
                .orElseThrow(() -> new EntityNotFoundException("Member does not exist, memberId: " + parameter.memberId()));
        Team teamEntity = teamRepository.findById(parameter.teamId())
                .orElseThrow(() -> new EntityNotFoundException("Team does not exist. teamId: " + parameter.teamId()));
        if (!parameter.memberName().equals(entity.getMemberName())) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            TokenResponse tokenResponse = tokenProvider.generateTokenResponse(authentication, entity, false);
            entity.setAccessToken(tokenResponse.token());
            entity.setRefreshToken(tokenResponse.refreshToken());
            tokenProvider.renewalAccessTokenInCookie(httpServletResponse, tokenResponse.token());
        }
        Member modifiedEntity = memberMapper.updateFromRequest(parameter, entity);
        MemberTeam memberTeam = modifiedEntity.getMemberTeam();
        if (memberTeam == null) {
            MemberTeam newMemberTeam = new MemberTeam();
            newMemberTeam.setMemberId(entity.getMemberId());
            newMemberTeam.setTeam(teamEntity);
            newMemberTeam = memberTeamRepository.save(newMemberTeam);
            modifiedEntity.setMemberTeam(newMemberTeam);
        } else {
            modifiedEntity.getMemberTeam().setTeam(teamEntity);
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
