package com.aljjabaegi.api.domain.historyLogin;

import com.aljjabaegi.api.common.request.enumeration.SortDirections;
import com.aljjabaegi.api.common.response.GridItemsResponse;
import com.aljjabaegi.api.domain.historyLogin.record.HistoryLoginSearchResponse;
import com.aljjabaegi.api.entity.HistoryLogin;
import com.aljjabaegi.api.entity.QHistoryLogin;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 로그인 이력 Service
 *
 * @author GEONLEE
 * @since 2024-04-09<br />
 */
@Service
@RequiredArgsConstructor
public class HistoryLoginService {
    private final JPAQueryFactory query;
    private final HistoryLoginMapper historyLoginMapper = HistoryLoginMapper.INSTANCE;

    /**
     * 전체 로그인 이력 조회, 최근 정보 조회
     *
     * @author GEONLEE
     * @since 2024-04-09<br />
     * 2024-04-18 GEONLEE - querydsl 로 변경<br />
     */
    @Transactional
    public GridItemsResponse<HistoryLoginSearchResponse> getHistoryLoginList(String sortDirection, String sortColumn, int pageNo, int pageSize) {
        QHistoryLogin historyLogin = QHistoryLogin.historyLogin;

        PathBuilder<HistoryLogin> pathBuilder = new PathBuilder<>(HistoryLogin.class, "historyLogin");
        OrderSpecifier<String> orderSpecifier = null;
        switch (Objects.requireNonNull(SortDirections.fromText(sortDirection))) {
            case ASC -> {
                orderSpecifier = pathBuilder.getString(sortColumn).asc().nullsLast();
            }
            case DESC -> {
                orderSpecifier = pathBuilder.getString(sortColumn).desc().nullsLast();
            }
        }
        Long totalSize = query.select(historyLogin.count()).from(historyLogin).fetchOne();
        totalSize = (totalSize == null) ? 0L : totalSize;
        List<HistoryLogin> list = query.selectFrom(historyLogin)
                .offset((long) pageNo * pageSize)
                .limit(pageSize)
                .orderBy(orderSpecifier)
                .fetch();
        int totalPageSize = (int) Math.ceil((double) totalSize / (double) pageSize);

        List<HistoryLoginSearchResponse> historyLoginSearchResponseList = historyLoginMapper.toSearchResponseList(list);

        return GridItemsResponse.<HistoryLoginSearchResponse>builder()
                .status("OK")
                .message("데이터를 조회하는데 성공하였습니다.")
                .totalSize(totalSize)
                .totalPageSize(totalPageSize)
                .size(list.size())
                .items(historyLoginSearchResponseList)
                .build();
    }

    /**
     * 사용자별 로그인 이력 조회
     *
     * @author GEONLEE
     * @since 2024-04-09<br />
     */
    public GridItemsResponse<HistoryLoginSearchResponse> getHistoryLoginListByMemberId(
            String memberId, String sortDirection, String sortColumn, int pageNo, int pageSize) {

        QHistoryLogin historyLogin = QHistoryLogin.historyLogin;

        PathBuilder<HistoryLogin> pathBuilder = new PathBuilder<>(HistoryLogin.class, "historyLogin");
        OrderSpecifier<String> orderSpecifier = null;
        switch (Objects.requireNonNull(SortDirections.fromText(sortDirection))) {
            case ASC -> {
                orderSpecifier = pathBuilder.getString(sortColumn).asc().nullsLast();
            }
            case DESC -> {
                orderSpecifier = pathBuilder.getString(sortColumn).desc().nullsLast();
            }
        }
        Long totalSize = query.select(historyLogin.count())
                .from(historyLogin)
                .where(historyLogin.key.memberId.eq(memberId))
                .fetchOne();
        totalSize = (totalSize == null) ? 0L : totalSize;
        List<HistoryLogin> list = query.selectFrom(historyLogin)
                .where(historyLogin.key.memberId.eq(memberId))
                .offset((long) pageNo * pageSize)
                .limit(pageSize)
                .orderBy(orderSpecifier)
                .fetch();
        int totalPageSize = (int) Math.ceil((double) totalSize / (double) pageSize);

        List<HistoryLoginSearchResponse> historyLoginSearchResponseList = historyLoginMapper.toSearchResponseList(list);


//        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())
//                ? Sort.by(sortColumn).ascending() : Sort.by(sortColumn).descending();
//        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
//        Page<HistoryLogin> page = historyLoginRepository.findByKeyMemberId(memberId, pageable);
//        int totalPage = page.getTotalPages();
//        long totalElements = page.getTotalElements();
//        List<HistoryLoginSearchResponse> historyLoginSearchResponseList = historyLoginMapper.toSearchResponseList(page.getContent());

        return GridItemsResponse.<HistoryLoginSearchResponse>builder()
                .status("OK")
                .message("데이터를 조회하는데 성공하였습니다.")
                .totalSize(totalSize)
                .totalPageSize(totalPageSize)
                .size(list.size())
                .items(historyLoginSearchResponseList)
                .build();
    }

}
