package com.aljjabaegi.api.common.jpa.dynamicSearch.querydsl.repository;

import com.aljjabaegi.api.common.contextHolder.ApplicationContextHolder;
import com.aljjabaegi.api.common.jpa.dynamicSearch.JpaDynamicRepository;
import com.aljjabaegi.api.common.jpa.dynamicSearch.querydsl.DynamicBooleanBuilder;
import com.aljjabaegi.api.common.request.DynamicFilter;
import com.aljjabaegi.api.common.request.DynamicRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * SimpleJpaRepository 의 기능을 사용하면서 메서드를 확장한 JpaRepository 구현 with DynamicBooleanBuilder<br />
 * querydsl 사용 방식<br />
 *
 * @author GEONLEE
 * @since 2024-04-19
 */
public class JpaDynamicDslRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements JpaDynamicRepository<T, ID> {

    private final DynamicBooleanBuilder dynamicBooleanBuilder;
    private final JPAQueryFactory queryFactory;
    private final Class<T> entity;
    private final PathBuilder<T> pathBuilder;

    public JpaDynamicDslRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.entity = entityInformation.getJavaType();
        this.pathBuilder = new PathBuilderFactory().create(this.entity);
        this.dynamicBooleanBuilder = ApplicationContextHolder.getContext().getBean(DynamicBooleanBuilder.class);
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
        List<DynamicFilter> dynamicFilters = new ArrayList<>() {{
            add(dynamicFilter);
        }};
        BooleanBuilder booleanBuilder = dynamicBooleanBuilder.generateConditions(this.entity, dynamicFilters);
        return this.queryFactory
                .select(this.pathBuilder.count())
                .from(this.pathBuilder)
                .where(booleanBuilder)
                .fetchOne();
    }

    @Override
    public Long countDynamic(List<DynamicFilter> dynamicFilters) {
        BooleanBuilder booleanBuilder = dynamicBooleanBuilder.generateConditions(this.entity, dynamicFilters);
        return this.queryFactory
                .select(this.pathBuilder.count())
                .from(this.pathBuilder)
                .where(booleanBuilder)
                .fetchOne();
    }

    @Override
    public List<T> findDynamic(DynamicFilter dynamicFilter) {
        List<DynamicFilter> dynamicFilters = new ArrayList<>() {{
            add(dynamicFilter);
        }};
        BooleanBuilder booleanBuilder = dynamicBooleanBuilder.generateConditions(this.entity, dynamicFilters);
        return this.queryFactory
                .selectFrom(this.pathBuilder)
                .where(booleanBuilder)
                .fetch();
    }

    @Override
    public List<T> findDynamic(List<DynamicFilter> dynamicFilters) {
        BooleanBuilder booleanBuilder = dynamicBooleanBuilder.generateConditions(this.entity, dynamicFilters);
        return this.queryFactory
                .selectFrom(this.pathBuilder)
                .where(booleanBuilder)
                .fetch();
    }

    @Override
    public Page<T> findDynamicWithPageable(DynamicRequest dynamicRequest) {
        BooleanBuilder booleanBuilder = dynamicBooleanBuilder.generateConditions(this.entity, dynamicRequest.filter());
        List<OrderSpecifier<String>> orderSpecifiers = dynamicBooleanBuilder.generateSort(this.entity, dynamicRequest.sorter());
        Long totalSize = countDynamic(dynamicRequest.filter());
        totalSize = (totalSize == null) ? 0L : totalSize;
        Pageable pageable = PageRequest.of(dynamicRequest.pageNo(), dynamicRequest.pageSize());
        List<T> list = this.queryFactory
                .selectFrom(this.pathBuilder)
                .where(booleanBuilder)
                .orderBy(orderSpecifiers.toArray(OrderSpecifier[]::new))
                .fetch();
        return new PageImpl<>(list, pageable, totalSize);
    }
}
