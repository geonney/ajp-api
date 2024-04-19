package com.aljjabaegi.api.common.jpa.dynamicSearch.querydsl;

import com.aljjabaegi.api.common.jpa.base.BaseEntity;
import com.aljjabaegi.api.common.request.DynamicSorter;
import com.querydsl.core.types.OrderSpecifier;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author GEONLEE
 * @since 2024-04-19
 */
public class DynamicBooleanBuilder {
    private static final List<String> BASE_ENTITY_FIELD = Arrays.stream(BaseEntity.class.getDeclaredFields()).map(Field::getName).toList();

    public List<OrderSpecifier<String>> getOrderSpecifier(List<DynamicSorter> dynamicSorters) {
        List<OrderSpecifier<String>> orderSpecifierList = new ArrayList<>();
        if (dynamicSorters != null) {

        }
        return orderSpecifierList;
    }
}
