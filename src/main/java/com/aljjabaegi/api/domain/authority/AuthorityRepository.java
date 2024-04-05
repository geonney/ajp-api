package com.aljjabaegi.api.domain.authority;

import com.aljjabaegi.api.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Authority Repository
 *
 * @author GEONLEE
 * @since 2024-04-05
 */
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
