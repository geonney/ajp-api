package com.aljjabaegi.api.domain.member;

import com.aljjabaegi.api.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Member Repository
 *
 * @author GEONLEE
 * @since 2024-04-01
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    /**
     * Refresh Token 으로 Member 조회
     *
     * @author GEONLEE
     * @since 2024-04-02
     */
    Optional<Member> findOneByMemberIdAndRefreshToken(String memberId, String refreshToken);

    /**
     * Access Token 으로 Member 조회
     *
     * @author GEONLEE
     * @since 2024-04-02
     */
    Optional<Member> findOneByMemberIdAndAccessToken(String memberId, String accessToken);

    /**
     * 관리자 제외 전체 사용자 조회
     *
     * @author GEONLEE
     * @since 2024-04-04
     */
    List<Member> findByAuthorityAuthorityCodeNot(String authorityCode);

    Optional<Member> findByMemberIdAndAuthorityAuthorityCodeNot(String memberId, String authorityCode);
}
