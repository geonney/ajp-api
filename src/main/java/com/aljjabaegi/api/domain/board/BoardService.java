package com.aljjabaegi.api.domain.board;

import com.aljjabaegi.api.common.jpa.dynamicSearch.querydsl.DynamicBooleanBuilder;
import com.aljjabaegi.api.common.request.DynamicRequest;
import com.aljjabaegi.api.common.response.GridResponse;
import com.aljjabaegi.api.common.response.ItemsResponse;
import com.aljjabaegi.api.domain.board.record.*;
import com.aljjabaegi.api.entity.Board;
import com.aljjabaegi.api.entity.QBoard;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    private final DynamicBooleanBuilder dynamicBooleanBuilder;
    private final JPAQueryFactory query;
    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper = BoardMapper.INSTANCE;

    /**
     * DynamicBooleanBuilder 를 사용한 조회
     *
     * @param dynamicRequest paging, sorting, condition
     * @return Board list
     * @author GEONLEE
     * @since 2024-04-04<br />
     * 2024-04-19 GEONLEE - DynamicBooleanBuilder 사용 방식으로 변경
     */
    public GridResponse<BoardSearchResponse> getBoardListUsingDynamicBooleanBuilder(DynamicRequest dynamicRequest) {
        QBoard board = QBoard.board;
        List<OrderSpecifier<String>> orderSpecifiers = dynamicBooleanBuilder.generateSort(Board.class, dynamicRequest.sorter());
        BooleanBuilder booleanBuilder = dynamicBooleanBuilder.generateConditions(Board.class, dynamicRequest.filter());
        Long totalSize = query.select(board.count())
                .from(board)
                .where(booleanBuilder)
                .fetchOne();
        totalSize = (totalSize == null) ? 0L : totalSize;
        int totalPageSize = (int) Math.ceil((double) totalSize / (double) dynamicRequest.pageSize());
        List<Board> boardList = query.selectFrom(board)
                .where(booleanBuilder)
                .orderBy(orderSpecifiers.toArray(OrderSpecifier[]::new))
                .fetch();
        List<BoardSearchResponse> list = boardMapper.toSearchResponseList(boardList);
        return GridResponse.<BoardSearchResponse>builder()
                .status("OK")
                .message("데이터를 조회하는데 성공하였습니다.")
                .totalSize(totalSize)
                .totalPageSize(totalPageSize)
                .size(list.size())
                .items(list)
                .build();
    }

    /**
     * DynamicDslRepository 를 사용한 조회
     *
     * @param dynamicRequest paging, sorting, condition
     * @return Board list
     * @author GEONLEE
     * @since 2024-04-19
     */
    public GridResponse<BoardSearchResponse> getBoardListUsingDynamicDslRepository(DynamicRequest dynamicRequest) {
        Page<Board> page = boardRepository.findDynamicWithPageable(dynamicRequest);
        List<BoardSearchResponse> list = boardMapper.toSearchResponseList(page.getContent());
        return GridResponse.<BoardSearchResponse>builder()
                .status("OK")
                .message("데이터를 조회하는데 성공하였습니다.")
                .totalSize(page.getTotalElements())
                .totalPageSize(page.getTotalPages())
                .size(page.getNumberOfElements())
                .items(list)
                .build();
    }

    /**
     * Paging 없이 filtering, sorting 만 동작
     *
     * @param dynamicRequest sorting, condition
     * @return Board list
     * @author GEONLEE
     * @since 2024-04-26
     */
    public ItemsResponse<BoardSearchResponse> getBoardListNoPaging(DynamicRequest dynamicRequest) {
        List<Board> entityList = boardRepository.findDynamic(dynamicRequest);
        List<BoardSearchResponse> list = boardMapper.toSearchResponseList(entityList);
        return ItemsResponse.<BoardSearchResponse>builder()
                .status("OK")
                .message("데이터를 조회하는데 성공하였습니다.")
                .totalSize((long) list.size())
                .items(list)
                .build();
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
