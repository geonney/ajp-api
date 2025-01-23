package com.aljjabaegi.api.common.jpa.dynamicSearch.conditions.impl;

import com.aljjabaegi.api.common.jpa.dynamicSearch.conditions.QueryCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;

import java.util.List;

/**
 * @author GEONLEE
 * @since 2025-01-22
 */
public class BooleanBuilderCondition implements QueryCondition {
    private final BooleanBuilder booleanBuilder;
    private final List<OrderSpecifier<?>> orderSpecifiers;

    public BooleanBuilderCondition(BooleanBuilder booleanBuilder, List<OrderSpecifier<?>> orderSpecifiers) {
        this.booleanBuilder = booleanBuilder;
        this.orderSpecifiers = orderSpecifiers;
    }

    @Override
    public BooleanBuilder getCondition() {
        return this.booleanBuilder;
    }

    @Override
    public List<OrderSpecifier<?>> getSorter() {
        return this.orderSpecifiers;
    }
}
