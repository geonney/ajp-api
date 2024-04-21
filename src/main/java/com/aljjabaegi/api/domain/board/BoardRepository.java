package com.aljjabaegi.api.domain.board;

import com.aljjabaegi.api.common.jpa.dynamicSearch.JpaDynamicRepository;
import com.aljjabaegi.api.entity.Board;
import org.springframework.stereotype.Repository;

/**
 * Board Repository
 *
 * @author GEONLEE
 * @since 2024-04-04
 */
@Repository
public interface BoardRepository extends JpaDynamicRepository<Board, Long> {

}
