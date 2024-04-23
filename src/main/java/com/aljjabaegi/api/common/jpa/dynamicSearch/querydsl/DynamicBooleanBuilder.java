package com.aljjabaegi.api.common.jpa.dynamicSearch.querydsl;

import com.aljjabaegi.api.common.exception.code.CommonErrorCode;
import com.aljjabaegi.api.common.exception.custom.ServiceException;
import com.aljjabaegi.api.common.jpa.base.BaseEntity;
import com.aljjabaegi.api.common.jpa.dynamicSearch.DynamicConditions;
import com.aljjabaegi.api.common.jpa.mapstruct.Converter;
import com.aljjabaegi.api.common.request.DynamicFilter;
import com.aljjabaegi.api.common.request.DynamicSorter;
import com.aljjabaegi.api.common.request.enumeration.Operators;
import com.aljjabaegi.api.common.request.enumeration.SortDirections;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import org.hibernate.query.sqm.PathElementException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Querydsl 에서 조회조건에 활용하는 BooleanBuilder, OrderSpecifier 를 동적으로 생성
 *
 * @author GEONLEE
 * @since 2024-04-19
 */
@Component
public class DynamicBooleanBuilder implements DynamicConditions {

    @Override
    public List<OrderSpecifier<String>> generateSort(Class<?> entity, List<DynamicSorter> dynamicSorters) {
        List<OrderSpecifier<String>> orderSpecifierList = new ArrayList<>();
        if (dynamicSorters != null) {
            PathBuilder<Object> root = new PathBuilder<>(entity, lowerCaseFirst(entity.getSimpleName()));
            for (DynamicSorter dynamicSorter : dynamicSorters) {
                String fieldPath = getSearchFieldPath(entity, dynamicSorter.field());
                PathBuilder<Object> rootPath = getParentPath(root, fieldPath);
                if (dynamicSorter.sortDirection() == null) {
                    throw new ServiceException(CommonErrorCode.INVALID_PARAMETER
                            , "Invalid sort direction. Possible sort directions -> " + SortDirections.getSorDirections());
                }
                switch (dynamicSorter.sortDirection()) {
                    case ASC -> {
                        orderSpecifierList.add(rootPath.getString(dynamicSorter.field()).asc().nullsLast());
                    }
                    case DESC -> {
                        orderSpecifierList.add(rootPath.getString(dynamicSorter.field()).desc().nullsLast());
                    }
                }
            }
        }
        return orderSpecifierList;
    }

    @Override
    public BooleanBuilder generateConditions(Class<?> entity, List<DynamicFilter> dynamicFilters) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (dynamicFilters == null || dynamicFilters.size() == 0) {
            return booleanBuilder;
        }
        for (DynamicFilter dynamicFilter : dynamicFilters) {
            if (dynamicFilter.operator() == null) {
                throw new ServiceException(CommonErrorCode.INVALID_PARAMETER, "Invalid operator. Possible Operators -> " + Operators.getOperators());
            }
            PathBuilder<Object> root = new PathBuilder<>(entity, lowerCaseFirst(entity.getSimpleName()));
            String fieldPath = getSearchFieldPath(entity, dynamicFilter.field());
            PathBuilder<Object> rootPath = getParentPath(root, fieldPath);
            String fieldType = getType(entity, fieldPath);

            //Possible search to without case sensitivity
            String value = (dynamicFilter.value() == null) ? null : dynamicFilter.value();
            // Possible search to null data
            if (value == null) {
                booleanBuilder.and(rootPath.get(dynamicFilter.field()).isNull());
                continue;
            }
            switch (dynamicFilter.operator()) {
                case EQUAL -> {
                    checkAvailableFieldTypes(dynamicFilter.operator(), fieldType);
                    booleanBuilder.and(rootPath.get(dynamicFilter.field()).eq(value));
                }
                case NOT_EQUAL -> {
                    checkAvailableFieldTypes(dynamicFilter.operator(), fieldType);
                    booleanBuilder.and(rootPath.get(dynamicFilter.field()).ne(value));
                }
                case LIKE -> {
                    checkAvailableFieldTypes(dynamicFilter.operator(), fieldType);
                    booleanBuilder.and(rootPath.getString(dynamicFilter.field()).likeIgnoreCase("%" + value + "%"));
                }
                case BETWEEN -> {
                    checkAvailableFieldTypes(dynamicFilter.operator(), fieldType);
                    if ("LocalDate".equals(fieldType)) {
                        List<LocalDate> list = Arrays.stream(value.split(",")).map(Converter::dateStringToLocalDate).toList();
                        booleanBuilder.and(rootPath.getDate(dynamicFilter.field(), LocalDate.class).between(list.get(0), list.get(1)));
                    } else if ("LocalDateTime".equals(fieldType)) {
                        try {
                            List<LocalDateTime> list = Arrays.stream(value.split(","))
                                    .map(Converter::dateTimeStringToLocalDateTime).toList();
                            booleanBuilder.and(rootPath.getDateTime(dynamicFilter.field(), LocalDateTime.class).between(list.get(0), list.get(1)));
                        } catch (DateTimeParseException e) {
                            throw new ServiceException(CommonErrorCode.INVALID_PARAMETER, e);
                        }
                    } else {
                        List<String> list = Arrays.asList(value.split(","));
                        booleanBuilder.and(rootPath.getNumber(dynamicFilter.field(), Double.class)
                                .between(Double.valueOf(list.get(0)), Double.valueOf(list.get(1))));
                    }
                }
                case IN -> {
                    checkAvailableFieldTypes(dynamicFilter.operator(), fieldType);
                    if ("LocalDate".equals(fieldType)) {
                        List<LocalDate> list = Arrays.stream(value.split(",")).map(Converter::dateStringToLocalDate).toList();
                        booleanBuilder.and(rootPath.getDateTime(dynamicFilter.field(), LocalDate.class).in(list));
                    } else {
                        List<String> list = Arrays.asList(value.split(","));
                        booleanBuilder.and(rootPath.get(dynamicFilter.field()).in(list));
                    }
                }
            }
        }
        return booleanBuilder;
    }

    /**
     * Entity 속성의 타입을 리턴 (recursion)
     *
     * @param entity    entity class
     * @param fieldName field name
     * @return class attribute simple type name
     * @author GEONLEE
     * @since 2024-04-19
     */
    private String getType(Class<?> entity, String fieldName) {
        try {
            if (entity.getSuperclass() == BaseEntity.class && BASE_ENTITY_FIELDS.contains(fieldName)) {
                entity = entity.getSuperclass();
            }
            if (fieldName.contains(".")) {
                String[] entityField = fieldName.split("\\.");
                return getType(entity.getDeclaredField(entityField[0]).getType(), fieldName.substring(fieldName.indexOf(".") + 1));
            }
            return entity.getDeclaredField(fieldName).getType().getSimpleName();
        } catch (PathElementException e) {
            throw new ServiceException(CommonErrorCode.INVALID_PARAMETER, e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 조회하고자 하는 속성의 상위 Path 리턴<br />
     * 해당 PathBuilder 에서 get 으로 실제 속성 path 에 접근
     *
     * @param root      root path
     * @param fieldPath field path
     * @return 조회하고자 하는 속성의 상위 path
     * @author GEONLEE
     * @since 2024-04-19
     */
    private PathBuilder<Object> getParentPath(PathBuilder<Object> root, String fieldPath) {
        if (fieldPath.contains(".")) {
            return root.get(fieldPath.substring(0, fieldPath.lastIndexOf(".")));
        } else {
            return root;
        }
    }

    /**
     * 첫글자 소문자료 변경하여 리턴
     *
     * @param text plain text
     * @return 첫글자가 소문자로 변경된 text
     * @author GEONLEE
     * @since 2024-04-19
     */
    private String lowerCaseFirst(String text) {
        char[] chars = text.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }
}
