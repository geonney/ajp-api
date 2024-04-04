package com.aljjabaegi.api.domain.member;

import com.aljjabaegi.api.domain.member.record.*;
import com.aljjabaegi.api.entity.Member;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
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
    private final MemberMapper memberMapper = MemberMapper.INSTANCE;

    /**
     * 전체 사용자 조회
     *
     * @author GEONLEE
     * @since 2024-04-01<br />
     * 2024-04-04 GEONLEE - 관리자 조회 안되게 수정<br />
     */
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
    public MemberCreateResponse createMember(MemberCreateRequest parameter) {
        if (memberRepository.existsById(parameter.memberId())) {
            throw new EntityExistsException(parameter.memberId());
        }
        Member createRequestEntity = memberMapper.toEntity(parameter);
        Member createdEntity = memberRepository.save(createRequestEntity);
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
