package com.aljjabaegi.api.common.jpa.dynamicSearch.querydsl.repository;

import com.aljjabaegi.api.common.jpa.dynamicSearch.JpaDynamicRepository;
import com.aljjabaegi.api.common.request.DynamicFilter;
import com.aljjabaegi.api.common.request.DynamicRequest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.io.Serializable;
import java.util.List;

/**
 * SimpleJpaRepository 의 기능을 사용하면서 메서드를 확장한 JpaRepository 구현 with DynamicSpecification
 *
 * @author GEONLEE
 * @since 2024-04-19
 */
public class JpaDynamicDslRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements JpaDynamicRepository<T, ID> {

    private final JPAQueryFactory queryFactory;

    public JpaDynamicDslRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * Querydsl 을 사용하기 위한 queryFactory return
     *
     * @return query factory
     */
    public JPAQueryFactory getQueryFactory() {
        return this.queryFactory;
    }

    @Override
    public Long countDynamic(DynamicFilter dynamicFilter) {
        return null;
    }

    @Override
    public Long countDynamic(List<DynamicFilter> dynamicFilterList) {
        return null;
    }

    @Override
    public List<T> findDynamic(DynamicFilter dynamicFilter) {
        return null;
    }

    @Override
    public List<T> findDynamic(List<DynamicFilter> dynamicFilter) {
        return null;
    }

    @Override
    public Page<T> findDynamicWithPageable(DynamicRequest dynamicRequest) {
        return null;
    }
}
