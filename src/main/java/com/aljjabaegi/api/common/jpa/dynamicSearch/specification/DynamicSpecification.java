package com.aljjabaegi.api.common.jpa.dynamicSearch.specification;

import com.aljjabaegi.api.common.exception.code.CommonErrorCode;
import com.aljjabaegi.api.common.exception.custom.ServiceException;
import com.aljjabaegi.api.common.jpa.annotation.DefaultSort;
import com.aljjabaegi.api.common.jpa.base.BaseEntity;
import com.aljjabaegi.api.common.jpa.dynamicSearch.DynamicConditions;
import com.aljjabaegi.api.common.jpa.mapstruct.Converter;
import com.aljjabaegi.api.common.request.DynamicFilter;
import com.aljjabaegi.api.common.request.DynamicSorter;
import com.aljjabaegi.api.common.request.enumeration.Operator;
import com.aljjabaegi.api.common.request.enumeration.SortDirection;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.hibernate.query.sqm.PathElementException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Generate specification using dynamic filter<br />
 * - 대소문자 구분 없이 조회 가능<br />
 * - Null 인 데이터 조회 가능<br />
 * - 참조 객체 조회 가능<br />
 * - EQUAL, NOT_EQUAL, IN -> LocalDateTime 제외하고 조회 가능<br />
 * - LIKE -> String 만 가능<br />
 * - BETWEEN -> String 제외하고 가능<br />
 *
 * @author GEONLEE
 * @since 2024-04-09<br />
 * 2024-04-18 GEONLEE - checkSearchableField Deprecated, getSearchFieldPath 에서 해당 기능 포함<br />
 * - getSearchFieldPath, BASE_ENTITY_FIELD 추가 -> BaseEntity field 처리 방식 변경<br />
 * - implements DynamicConditions 추가<br />
 * 2024-04-24 GEONLEE - LTE, GTE 조건 추가<br />
 * - 기존 generateSort 메서드에서 Sort 생성 부분을 parseSort 메서드로 분리, implements generateDefaultSort method<br />
 * 2024-04-29 GEONLEE - LIKE 검색의 경우에만 대소문자 구분 안되도록 수정, checkAvailableFieldTypes parameter 변경에 따른 수정<br />
 */
@Component
public class DynamicSpecification implements DynamicConditions {

    /**
     * Possible search to Referenced entity attributes (recursion)
     *
     * @throws ServiceException Entity 에서 field 가 없을 경우 PathElementException 발생
     * @author GEONLEE
     * @since 2024-04-11<br />
     * 2024-04-12 GEONLEE - PathElementException 발생 시 INVALID_PARAMETER 전달<br />
     */
    private Path<String> getPath(Path<?> path, String fieldName) {
        try {
            if (fieldName.contains(".")) {
                String[] entityField = fieldName.split("\\.");
                return getPath(path.get(entityField[0]), fieldName.substring(fieldName.indexOf(".") + 1));
            }
            return path.get(fieldName);
        } catch (PathElementException e) {
            throw new ServiceException(CommonErrorCode.INVALID_PARAMETER, e);
        }
    }

    /**
     * Check sort request field validity<br / >
     * base entity 를 extends 받았다면 컬럼 명 추가
     *
     * @throws ServiceException 유효하지 않은 정보 throw
     * @author GEONLEE
     * @since 2024-04-12
     */
    private void checkSortableField(Class<?> entity, List<DynamicSorter> dynamicSorters) {
        Set<String> fieldNames = new HashSet<>(Arrays.stream(entity.getDeclaredFields()).map(Field::getName).toList());
        if (entity.getSuperclass() == BaseEntity.class) {
            fieldNames.addAll(BASE_ENTITY_FIELDS);
        }
        for (DynamicSorter dynamicSorter : dynamicSorters) {
            if (!fieldNames.contains(dynamicSorter.field())) {
                throw new ServiceException(CommonErrorCode.INVALID_PARAMETER
                        , "'" + dynamicSorter.field() + "' does not exist in the '" + entity.getSimpleName() + "' entity.");
            }
        }
    }

    @Override
    public Sort generateSort(Class<?> entity, List<DynamicSorter> dynamicSorters) {
        if (dynamicSorters == null) {
            return generateDefaultSort(entity);
        }
        checkSortableField(entity, dynamicSorters);
        return parseSort(dynamicSorters);
    }

    /**
     * DynamicSort list 로 sort 를 생성하여 리턴
     *
     * @param dynamicSorters DynamicSorter list
     * @return Sort 정렬 조건
     */
    private Sort parseSort(List<DynamicSorter> dynamicSorters) {
        List<Sort.Order> orderList = new ArrayList<>();
        for (DynamicSorter dynamicSorter : dynamicSorters) {
            if (dynamicSorter.direction() == null) {
                throw new ServiceException(CommonErrorCode.INVALID_PARAMETER
                        , "Invalid sort direction. Possible sort directions -> " + SortDirection.getSorDirectionString());
            }
            switch (dynamicSorter.direction()) {
                case ASC -> {
                    orderList.add(Sort.Order.asc(dynamicSorter.field()));
                }
                case DESC -> {
                    orderList.add(Sort.Order.desc(dynamicSorter.field()));
                }
            }
        }
        return Sort.by(orderList);
    }

    @Override
    public Sort generateDefaultSort(Class<?> entity) {
        DefaultSort defaultSort = entity.getAnnotation(DefaultSort.class);
        if (defaultSort == null) {
            return Sort.unsorted();
        }
        String[] columnNames = defaultSort.columnName();
        SortDirection[] sortDirections = defaultSort.direction();
        if (columnNames.length != sortDirections.length) {
            throw new ServiceException(CommonErrorCode.SERVICE_ERROR,
                    "Check '" + entity.getSimpleName() + "' entity @DefaultSort settings. different lengths. (columnName-direction)");
        }
        List<DynamicSorter> dynamicSorters = new ArrayList<>();
        for (int i = 0, n = columnNames.length; i < n; i++) {
            DynamicSorter dynamicSorter = new DynamicSorter(columnNames[i], sortDirections[i]);
            dynamicSorters.add(dynamicSorter);
        }
        checkSortableField(entity, dynamicSorters);
        return parseSort(dynamicSorters);
    }

    @Override
    public Specification<?> generateConditions(Class<?> entity, List<DynamicFilter> dynamicFilters) {
        return (root, query, criteriaBuilder) -> {
            if (dynamicFilters == null || dynamicFilters.size() == 0) {
                return criteriaBuilder.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();
            for (DynamicFilter dynamicFilter : dynamicFilters) {
                if (dynamicFilter.operator() == null) {
                    throw new ServiceException(CommonErrorCode.INVALID_PARAMETER, "Invalid operator. Possible Operators -> " + Operator.getOperatorString());
                }
                String fieldPath = getSearchFieldPath(entity, dynamicFilter.field()); // root.getJavaType();
                Path<String> path = getPath(root, fieldPath);
                Class<?> fieldType = path.getModel().getBindableJavaType();
                String value = dynamicFilter.value();
                // Possible search to null data
                if (value == null) {
                    predicates.add(criteriaBuilder.isNull(path));
                    continue;
                }
                switch (dynamicFilter.operator()) {
                    case EQUAL -> {
                        checkAvailableFieldTypes(dynamicFilter.operator(), fieldType);
                        predicates.add(criteriaBuilder.equal(path, value));
                    }
                    case NOT_EQUAL -> {
                        checkAvailableFieldTypes(dynamicFilter.operator(), fieldType);
                        predicates.add(criteriaBuilder.notEqual(path, value));
                    }
                    case LIKE -> {
                        checkAvailableFieldTypes(dynamicFilter.operator(), fieldType);
                        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(path), "%" + value.toLowerCase() + "%"));
                    }
                    case BETWEEN -> {
                        checkAvailableFieldTypes(dynamicFilter.operator(), fieldType);
                        if (fieldType == LocalDate.class) {
                            try {
                                List<LocalDate> list = Arrays.stream(value.split(",")).map(Converter::dateStringToLocalDate).toList();
                                predicates.add(criteriaBuilder.between(path.as(LocalDate.class), list.get(0), list.get(1)));
                            } catch (DateTimeParseException e) {
                                throw new ServiceException(CommonErrorCode.INVALID_PARAMETER, e);
                            }
                        } else if (fieldType == LocalDateTime.class) {
                            try {
                                List<LocalDateTime> list = Arrays.stream(value.split(","))
                                        .map(Converter::dateTimeStringToLocalDateTime).toList();
                                predicates.add(criteriaBuilder.between(path.as(LocalDateTime.class), list.get(0), list.get(1)));
                            } catch (DateTimeParseException e) {
                                throw new ServiceException(CommonErrorCode.INVALID_PARAMETER, e);
                            }
                        } else {
                            List<String> list = Arrays.asList(value.split(","));
                            predicates.add(criteriaBuilder.between(path, list.get(0), list.get(1)));
                        }
                    }
                    case IN -> {
                        checkAvailableFieldTypes(dynamicFilter.operator(), fieldType);
                        if (fieldType == LocalDate.class) {
                            Path<LocalDate> localDatePath = root.get(dynamicFilter.field());
                            List<LocalDate> list = Arrays.stream(value.split(",")).map(Converter::dateStringToLocalDate).toList();
                            predicates.add(localDatePath.in(list));
                        } else {
                            List<String> list = Arrays.asList(value.split(","));
                            predicates.add(path.in(list));
                        }
                    }
                    case LTE -> {
                        checkAvailableFieldTypes(dynamicFilter.operator(), fieldType);
                        if (fieldType == LocalDate.class) {
                            try {
                                LocalDate localDate = Converter.dateStringToLocalDate(value);
                                predicates.add(criteriaBuilder.lessThanOrEqualTo(path.as(LocalDate.class), localDate));
                            } catch (DateTimeParseException e) {
                                throw new ServiceException(CommonErrorCode.INVALID_PARAMETER, e);
                            }
                        } else if (fieldType == LocalDateTime.class) {
                            try {
                                LocalDateTime localDateTime = Converter.dateTimeStringToLocalDateTime(value);
                                predicates.add(criteriaBuilder.lessThanOrEqualTo(path.as(LocalDateTime.class), localDateTime));
                            } catch (DateTimeParseException e) {
                                throw new ServiceException(CommonErrorCode.INVALID_PARAMETER, e);
                            }
                        } else {
                            predicates.add(criteriaBuilder.lessThanOrEqualTo(path, value));
                        }
                    }
                    case GTE -> {
                        checkAvailableFieldTypes(dynamicFilter.operator(), fieldType);
                        if (fieldType == LocalDate.class) {
                            try {
                                LocalDate localDate = Converter.dateStringToLocalDate(value);
                                predicates.add(criteriaBuilder.greaterThanOrEqualTo(path.as(LocalDate.class), localDate));
                            } catch (DateTimeParseException e) {
                                throw new ServiceException(CommonErrorCode.INVALID_PARAMETER, e);
                            }
                        } else if (fieldType == LocalDateTime.class) {
                            try {
                                LocalDateTime localDateTime = Converter.dateTimeStringToLocalDateTime(value);
                                predicates.add(criteriaBuilder.greaterThanOrEqualTo(path.as(LocalDateTime.class), localDateTime));
                            } catch (DateTimeParseException e) {
                                throw new ServiceException(CommonErrorCode.INVALID_PARAMETER, e);
                            }
                        } else {
                            predicates.add(criteriaBuilder.greaterThanOrEqualTo(path, value));
                        }
                    }
                }
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
