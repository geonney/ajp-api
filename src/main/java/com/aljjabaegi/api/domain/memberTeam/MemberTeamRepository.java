package com.aljjabaegi.api.domain.memberTeam;

import com.aljjabaegi.api.entity.Member;
import com.aljjabaegi.api.entity.MemberTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Member Repository
 *
 * @author GEONLEE
 * @since 2024-04-01<br />
 * 2024-04-09 GEONLEE - JpaSpecificationExecutor 적용<br />
 */
@Repository
public interface MemberTeamRepository extends JpaRepository<MemberTeam, Member> {

}
