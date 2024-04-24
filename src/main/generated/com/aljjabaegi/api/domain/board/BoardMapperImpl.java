package com.aljjabaegi.api.domain.board;

import com.aljjabaegi.api.common.jpa.mapstruct.Converter;
import com.aljjabaegi.api.domain.board.record.BoardCreateRequest;
import com.aljjabaegi.api.domain.board.record.BoardCreateResponse;
import com.aljjabaegi.api.domain.board.record.BoardModifyRequest;
import com.aljjabaegi.api.domain.board.record.BoardModifyResponse;
import com.aljjabaegi.api.domain.board.record.BoardSearchResponse;
import com.aljjabaegi.api.entity.Board;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-24T13:08:27+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.7.jar, environment: Java 17 (Oracle Corporation)"
)
@Component
public class BoardMapperImpl implements BoardMapper {

    private final DateTimeFormatter dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168 = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" );

    @Override
    public BoardSearchResponse toSearchResponse(Board entity) {
        if ( entity == null ) {
            return null;
        }

        String createDate = null;
        String modifyDate = null;
        Long boardSequence = null;
        String boardTitle = null;
        String boardContent = null;

        if ( entity.getCreateDate() != null ) {
            createDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getCreateDate() );
        }
        if ( entity.getModifyDate() != null ) {
            modifyDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getModifyDate() );
        }
        boardSequence = entity.getBoardSequence();
        boardTitle = entity.getBoardTitle();
        boardContent = entity.getBoardContent();

        BoardSearchResponse boardSearchResponse = new BoardSearchResponse( boardSequence, boardTitle, boardContent, createDate, modifyDate );

        return boardSearchResponse;
    }

    @Override
    public List<BoardSearchResponse> toSearchResponseList(List<Board> list) {
        if ( list == null ) {
            return null;
        }

        List<BoardSearchResponse> list1 = new ArrayList<BoardSearchResponse>( list.size() );
        for ( Board board : list ) {
            list1.add( toSearchResponse( board ) );
        }

        return list1;
    }

    @Override
    public BoardCreateResponse toCreateResponse(Board entity) {
        if ( entity == null ) {
            return null;
        }

        String createDate = null;
        String modifyDate = null;
        Long boardSequence = null;
        String boardTitle = null;
        String boardContent = null;

        if ( entity.getCreateDate() != null ) {
            createDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getCreateDate() );
        }
        if ( entity.getModifyDate() != null ) {
            modifyDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getModifyDate() );
        }
        boardSequence = entity.getBoardSequence();
        boardTitle = entity.getBoardTitle();
        boardContent = entity.getBoardContent();

        BoardCreateResponse boardCreateResponse = new BoardCreateResponse( boardSequence, boardTitle, boardContent, createDate, modifyDate );

        return boardCreateResponse;
    }

    @Override
    public BoardModifyResponse toModifyResponse(Board entity) {
        if ( entity == null ) {
            return null;
        }

        String createDate = null;
        String modifyDate = null;
        Long boardSequence = null;
        String boardTitle = null;
        String boardContent = null;

        if ( entity.getCreateDate() != null ) {
            createDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getCreateDate() );
        }
        if ( entity.getModifyDate() != null ) {
            modifyDate = dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( entity.getModifyDate() );
        }
        boardSequence = entity.getBoardSequence();
        boardTitle = entity.getBoardTitle();
        boardContent = entity.getBoardContent();

        BoardModifyResponse boardModifyResponse = new BoardModifyResponse( boardSequence, boardTitle, boardContent, createDate, modifyDate );

        return boardModifyResponse;
    }

    @Override
    public Board toEntity(BoardCreateRequest boardCreateRequest) {
        if ( boardCreateRequest == null ) {
            return null;
        }

        Board board = new Board();

        board.setBoardTitle( boardCreateRequest.boardTitle() );
        board.setBoardContent( boardCreateRequest.boardContent() );

        return board;
    }

    @Override
    public Board updateFromRequest(BoardModifyRequest boardModifyRequest, Board entity) {
        if ( boardModifyRequest == null ) {
            return entity;
        }

        entity.setBoardSequence( boardModifyRequest.boardSequence() );
        entity.setBoardTitle( boardModifyRequest.boardTitle() );
        entity.setBoardContent( boardModifyRequest.boardContent() );

        return entity;
    }
}
