package com.aljjabaegi.api.domain.member;

import com.aljjabaegi.api.domain.member.record.*;
import com.aljjabaegi.api.domain.team.TeamRepository;
import com.aljjabaegi.api.entity.Member;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final MemberMapper memberMapper = MemberMapper.INSTANCE;

    /**
     * 전체 사용자 조회
     *
     * @author GEONLEE
     * @since 2024-04-01<br />
     * 2024-04-04 GEONLEE - 관리자 조회 안되게 수정<br />
     */
    @Transactional
    public List<MemberSearchResponse> getMemberList() {
        return memberMapper.toSearchResponseList(memberRepository.findByAuthorityAuthorityCodeNot("ROLE_ADMIN"));
    }

    /**
     * 사용자 ID로 사용자 조회
     *
     * @author GEONLEE
     * @since 2024-04-01<br />
     * 2024-04-04 GEONLEE - 관리자 조회 안되게 수정<br />
     */
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
     * @since 2024-04-01
     */
    @Transactional
    public MemberCreateResponse createMember(MemberCreateRequest parameter) {
        if (memberRepository.existsById(parameter.memberId())) {
            throw new EntityExistsException("memberId already existed. memberId: " + parameter.memberId());
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
