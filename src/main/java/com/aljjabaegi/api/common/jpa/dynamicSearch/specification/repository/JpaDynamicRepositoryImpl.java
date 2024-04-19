package com.aljjabaegi.api.common.jpa.dynamicSearch.specification.repository;

import com.aljjabaegi.api.common.jpa.dynamicSearch.specification.DynamicSpecification;
import com.aljjabaegi.api.common.jpa.dynamicSearch.JpaDynamicRepository;
import com.aljjabaegi.api.common.request.DynamicFilter;
import com.aljjabaegi.api.common.request.DynamicRequest;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * SimpleJpaRepository 의 기능을 사용하면서 메서드를 확장한 JpaRepository 구현 with DynamicSpecification
 *
 * @author GEONLEE
 * @since 2024-04-18
 */
public class JpaDynamicRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements JpaDynamicRepository<T, ID> {

    private final Class<T> entity;

    public JpaDynamicRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entity = entityInformation.getJavaType();
    }

    @Override
    public Long countDynamic(DynamicFilter dynamicFilter) {
        List<DynamicFilter> dynamicFilters = new ArrayList<>() {{
            add(dynamicFilter);
        }};
        return super.count(DynamicSpecification.generateSpecification(dynamicFilters));
    }

    @Override
    public Long countDynamic(List<DynamicFilter> dynamicFilters) {
        return super.count(DynamicSpecification.generateSpecification(dynamicFilters));
    }

    @Override
    public List<T> findDynamic(DynamicFilter dynamicFilter) {
        List<DynamicFilter> dynamicFilters = new ArrayList<>() {{
            add(dynamicFilter);
        }};
        return super.findAll(DynamicSpecification.generateSpecification(dynamicFilters));
    }

    @Override
    public List<T> findDynamic(List<DynamicFilter> dynamicFilters) {
        return super.findAll(DynamicSpecification.generateSpecification(dynamicFilters));
    }

    @Override
    public Page<T> findDynamicWithPageable(DynamicRequest dynamicRequest) {
        Sort sort = DynamicSpecification.generateSort(this.entity, dynamicRequest.sorter());
        Pageable pageable = PageRequest.of(dynamicRequest.pageNo(), dynamicRequest.pageSize(), sort);
        Specification<T> specification = DynamicSpecification.generateSpecification(dynamicRequest.filter());
        return super.findAll(specification, pageable);
    }
}
