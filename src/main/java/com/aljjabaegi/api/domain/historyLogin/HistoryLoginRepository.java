package com.aljjabaegi.api.domain.historyLogin;

import com.aljjabaegi.api.entity.HistoryLogin;
import com.aljjabaegi.api.entity.key.HistoryLoginKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * History login Repository
 *
 * @author GEONLEE
 * @since 2024-04-05
 */
@Repository
public interface HistoryLoginRepository extends JpaRepository<HistoryLogin, HistoryLoginKey> {

}
