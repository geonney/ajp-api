package com.aljjabaegi.api.domain.team;

import com.aljjabaegi.api.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Team Repository
 *
 * @author GEONLEE
 * @since 2024-04-08
 */
@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

}
