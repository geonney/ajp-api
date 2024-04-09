package com.aljjabaegi.api.domain.historyLogin;

import com.aljjabaegi.api.entity.HistoryLogin;
import com.aljjabaegi.api.entity.key.HistoryLoginKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * History login Repository
 *
 * @author GEONLEE
 * @since 2024-04-05
 */
@Repository
public interface HistoryLoginRepository extends JpaRepository<HistoryLogin, HistoryLoginKey> {

    Page<HistoryLogin> findByKeyMemberId(String memberId, Pageable pageable);

}
