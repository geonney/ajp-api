package com.aljjabaegi.api.domain.board;

import com.aljjabaegi.api.domain.board.record.*;
import com.aljjabaegi.api.entity.Board;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Board Service
 *
 * @author GEONLEE
 * @since 2024-04-04<br />
 */
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper = BoardMapper.INSTANCE;

    /**
     * 전체 Board 조회
     *
     * @author GEONLEE
     * @since 2024-04-04
     */
    public List<BoardSearchResponse> getBoardList() {
        return boardMapper.toSearchResponseList(boardRepository.findAll());
    }

    /**
     * Board 추가
     *
     * @author GEONLEE
     * @since 2024-04-04
     */
    public BoardCreateResponse createBoard(BoardCreateRequest parameter) {
        Board createRequestEntity = boardMapper.toEntity(parameter);
        Board createdEntity = boardRepository.save(createRequestEntity);
        return boardMapper.toCreateResponse(createdEntity);
    }

    /**
     * Board 수정
     *
     * @author GEONLEE
     * @since 2024-04-04
     */
    public BoardModifyResponse modifyBoard(BoardModifyRequest parameter) {
        Board entity = boardRepository.findById(parameter.boardSequence())
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(parameter.boardSequence())));
        Board modifiedEntity = boardMapper.updateFromRequest(parameter, entity);
        modifiedEntity = boardRepository.saveAndFlush(modifiedEntity);
        return boardMapper.toModifyResponse(modifiedEntity);
    }

    /**
     * Board 삭제
     *
     * @author GEONLEE
     * @since 2024-04-04
     */
    public Long deleteBoard(Long boardSequence) {
        Board entity = boardRepository.findById(boardSequence)
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(boardSequence)));
        boardRepository.delete(entity);
        return 1L;
    }
}
