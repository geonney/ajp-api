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
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NamedEntityGraph;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
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
 * @since 2024-04-19<br />
 * 2024-07-22 GEONLEE - N + 1 문제 보완용 namedEntityGraph 있을 때 hint 추가하도록 개선<br />
 */
public class JpaDynamicDslRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements JpaDynamicRepository<T, ID> {
    private final DynamicBooleanBuilder dynamicBooleanBuilder;
    private final JPAQueryFactory queryFactory;
    private final Class<T> entity;
    private final PathBuilder<T> pathBuilder;
    private final EntityManager entityManager;
    private final String HINT_NAME = "jakarta.persistence.loadgraph";

    public JpaDynamicDslRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.entity = entityInformation.getJavaType();
        this.pathBuilder = new PathBuilderFactory().create(this.entity);
        this.dynamicBooleanBuilder = ApplicationContextHolder.getContext().getBean(DynamicBooleanBuilder.class);
        this.entityManager = entityManager;
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
        JPAQuery<T> query = this.queryFactory
                .selectFrom(this.pathBuilder)
                .where(booleanBuilder);
        checkNamedEntityGraph(query);
        return query.fetch();
    }

    @Override
    public List<T> findDynamic(List<DynamicFilter> dynamicFilters) {
        BooleanBuilder booleanBuilder = dynamicBooleanBuilder.generateConditions(this.entity, dynamicFilters);
        JPAQuery<T> query = this.queryFactory
                .selectFrom(this.pathBuilder)
                .where(booleanBuilder);
        checkNamedEntityGraph(query);
        return query.fetch();
    }

    @Override
    public List<T> findDynamic(DynamicRequest dynamicRequest) {
        BooleanBuilder booleanBuilder = dynamicBooleanBuilder.generateConditions(this.entity, dynamicRequest.filter());
        List<OrderSpecifier<?>> orderSpecifiers = dynamicBooleanBuilder.generateSort(this.entity, dynamicRequest.sorter());
        JPAQuery<T> query = this.queryFactory
                .selectFrom(this.pathBuilder)
                .where(booleanBuilder)
                .orderBy(orderSpecifiers.toArray(OrderSpecifier[]::new));
        checkNamedEntityGraph(query);
        return query.fetch();
    }

    @Override
    public Page<T> findDynamicWithPageable(DynamicRequest dynamicRequest) {
        BooleanBuilder booleanBuilder = dynamicBooleanBuilder.generateConditions(this.entity, dynamicRequest.filter());
        List<OrderSpecifier<?>> orderSpecifiers = dynamicBooleanBuilder.generateSort(this.entity, dynamicRequest.sorter());
        Long totalSize = countDynamic(dynamicRequest.filter());
        totalSize = (totalSize == null) ? 0L : totalSize;
        Pageable pageable = PageRequest.of(dynamicRequest.pageNo(), dynamicRequest.pageSize());
        JPAQuery<T> query = this.queryFactory
                .selectFrom(this.pathBuilder)
                .where(booleanBuilder)
                .orderBy(orderSpecifiers.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        checkNamedEntityGraph(query);
        List<T> list = query.fetch();
        return new PageImpl<>(list, pageable, totalSize);
    }

    /**
     * Entity 의 namedEntityGraph annotation 이 있다면 명을 추출하여 리턴한다.
     *
     * @return NamedEntityGraph name
     * @author GEONLEE
     * @since 2024-07-22
     */
    private String getNamedEntityGraph() {
        NamedEntityGraph namedEntityGraph = this.entity.getAnnotation(NamedEntityGraph.class);
        if (ObjectUtils.isEmpty(namedEntityGraph)) {
            return null;
        }
        return namedEntityGraph.name();
    }

    /**
     * Entity 에 namedEntityGraph 를 체크하고 있다면 query 에 hint 를 추가한다.
     *
     * @author GEONLEE
     * @since 2024-07-23
     */
    private void checkNamedEntityGraph(JPAQuery<T> query) {
        String namedEntityGraphName = getNamedEntityGraph();
        if (StringUtils.isNotEmpty(namedEntityGraphName)) {
            query.setHint(this.HINT_NAME, this.entityManager.getEntityGraph(namedEntityGraphName));
        }
    }
}
