package com.aljjabaegi.api.domain.board;

import com.aljjabaegi.api.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Board Repository
 *
 * @author GEONLEE
 * @since 2024-04-04
 */
@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

}
