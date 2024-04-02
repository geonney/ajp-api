package com.aljjabaegi.api.domain.member;

import com.aljjabaegi.api.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findOneByMemberIdAndRefreshToken(String memberId, String refreshToken);

    Optional<Member> findOneByMemberIdAndAccessToken(String memberId, String accessToken);
}
