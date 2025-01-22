package com.aljjabaegi.api.common.jpa.dynamicSearch;

import com.aljjabaegi.api.common.jpa.dynamicSearch.strategy.QueryCondition;
import com.aljjabaegi.api.common.request.DynamicFilter;
import com.aljjabaegi.api.common.request.DynamicRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * JpaRepository 를 커스텀 하여 추가적인 조회 메서드 추가 Repository implementation
 * 표준화 한 요청 구조체 사용 (DynamicRequest, DynamicFilter
 *
 * @author GEONLEE
 * @since 2024-04-18<br />
 * 2024-04-26 GEONLEE - 페이징 없이 filtering, sorting 만 가능한 findDynamic 추가<br />
 */
@NoRepositoryBean
public interface JpaDynamicRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    /**
     * DynamicFilter 를 사용한 count 조회
     */
    Long countDynamic(DynamicFilter dynamicFilter);

    /**
     * DynamicFilter list 를 사용한 복수조건 count 조회
     */
    Long countDynamic(List<DynamicFilter> dynamicFilterList);

    /**
     * DynamicFilter 를 사용한 List 조회
     */
    List<T> findDynamic(DynamicFilter dynamicFilter);

    /**
     * DynamicFilter list 를 사용한 복수조건 List 조회
     */
    List<T> findDynamic(List<DynamicFilter> dynamicFilter);

    /**
     * DynamicRequest 를 사용한 복수조건, 복수 정렬 List 조회, No paging
     */
    List<T> findDynamic(DynamicRequest dynamicRequest);

    /**
     * DynamicRequest 를 활용한 List 조회, with paging
     */
    Page<T> findDynamicWithPageable(DynamicRequest dynamicRequest);

    Page<T> findDynamicWithPageable(QueryCondition queryCondition, Pageable pageable);

}
