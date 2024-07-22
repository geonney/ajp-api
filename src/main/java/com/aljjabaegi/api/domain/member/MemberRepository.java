package com.aljjabaegi.api.domain.member;

import com.aljjabaegi.api.common.jpa.dynamicSearch.JpaDynamicRepository;
import com.aljjabaegi.api.entity.Member;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Member Repository
 *
 * @author GEONLEE
 * @since 2024-04-01<br />
 * 2024-04-09 GEONLEE - JpaSpecificationExecutor 적용<br />
 */
@Repository
public interface MemberRepository extends JpaDynamicRepository<Member, String>, JpaSpecificationExecutor<Member> {

    @Modifying(clearAutomatically = true)
    @Query(value = "update member set authority_cd = null where authority_cd = :authorityCode", nativeQuery = true)
    int updateAuthority(@Param("authorityCode") String authorityCode);

    @Modifying(clearAutomatically = true)
    @Query(value = "update member set team_id = null where team_id = :teamId", nativeQuery = true)
    int updateTeam(@Param("teamId") Long teamId);

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

    /**
     * 전달된 권한을 가지고 있는 Member 조회
     *
     * @param authorityCode 권한 코드
     * @return member list
     * @author GEONLEE
     * @since 2024-04-04
     */
    List<Member> findByAuthorityAuthorityCode(String authorityCode);

    Optional<Member> findByMemberIdAndAuthorityAuthorityCodeNot(String memberId, String authorityCode);
}
