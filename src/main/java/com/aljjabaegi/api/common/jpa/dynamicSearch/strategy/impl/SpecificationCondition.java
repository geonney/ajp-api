package com.aljjabaegi.api.common.jpa.dynamicSearch.strategy.impl;

import com.aljjabaegi.api.common.jpa.dynamicSearch.strategy.QueryCondition;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author GEONLEE
 * @since 2025-01-22
 */
public class SpecificationCondition implements QueryCondition {
    private final Specification<T> specification;
    private final Sort sort;

    public SpecificationCondition(Specification<T> specification, Sort sort) {
        this.specification = specification;
        this.sort = sort;
    }

    @Override
    public Specification<T> getCondition() {
        return specification;
    }

    public Sort getSorter() {
        return this.sort;
    }
}
