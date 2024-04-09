package com.aljjabaegi.api.domain.historyLogin;

import com.aljjabaegi.api.common.response.GridItemsResponse;
import com.aljjabaegi.api.domain.historyLogin.record.HistoryLoginSearchResponse;
import com.aljjabaegi.api.entity.HistoryLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 로그인 이력 Service
 *
 * @author GEONLEE
 * @since 2024-04-09<br />
 */
@Service
@RequiredArgsConstructor
public class HistoryLoginService {

    private final HistoryLoginRepository historyLoginRepository;
    private final HistoryLoginMapper historyLoginMapper = HistoryLoginMapper.INSTANCE;

    /**
     * 전체 로그인 이력 조회, 최근 정보 조회
     *
     * @author GEONLEE
     * @since 2024-04-09<br />
     */
    public GridItemsResponse<HistoryLoginSearchResponse> getHistoryLoginList(String sortDirection, String sortColumn, int pageNo, int pageSize) {
        // default paging and sorting
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortColumn).ascending() : Sort.by(sortColumn).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<HistoryLogin> page = historyLoginRepository.findAll(pageable);

        int totalPage = page.getTotalPages();
        long totalElements = page.getTotalElements();
        List<HistoryLoginSearchResponse> historyLoginSearchResponseList = historyLoginMapper.toSearchResponseList(page.getContent());

        return GridItemsResponse.<HistoryLoginSearchResponse>builder()
                .status("OK")
                .message("데이터를 조회하는데 성공하였습니다.")
                .totalSize(totalElements)
                .totalPageSize(totalPage)
                .size(page.getNumberOfElements())
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

        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortColumn).ascending() : Sort.by(sortColumn).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<HistoryLogin> page = historyLoginRepository.findByKeyMemberId(memberId, pageable);
        int totalPage = page.getTotalPages();
        long totalElements = page.getTotalElements();
        List<HistoryLoginSearchResponse> historyLoginSearchResponseList = historyLoginMapper.toSearchResponseList(page.getContent());

        return GridItemsResponse.<HistoryLoginSearchResponse>builder()
                .status("OK")
                .message("데이터를 조회하는데 성공하였습니다.")
                .totalSize(totalElements)
                .totalPageSize(totalPage)
                .size(page.getNumberOfElements())
                .items(historyLoginSearchResponseList)
                .build();
    }

}
