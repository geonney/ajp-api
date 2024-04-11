package com.aljjabaegi.api.common.jpa.specification;

import com.aljjabaegi.api.common.exception.code.CommonErrorCode;
import com.aljjabaegi.api.common.exception.custom.ServiceException;
import com.aljjabaegi.api.common.jpa.annotation.SearchableField;
import com.aljjabaegi.api.common.jpa.mapstruct.Converter;
import com.aljjabaegi.api.common.request.DynamicFilter;
import com.aljjabaegi.api.common.request.enumeration.Operators;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
 */
public class DynamicSpecification {

    /**
     * @param <T>            entity
     * @param dynamicFilters search condition
     * @return Specification
     */
    public static <T> Specification<T> getSpecification(List<DynamicFilter> dynamicFilters) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (dynamicFilters.size() == 0) {
                    return criteriaBuilder.conjunction();
                }

                List<Predicate> predicates = new ArrayList<>();
                for (DynamicFilter dynamicFilter : dynamicFilters) {
                    if (dynamicFilter.operator() == null) {
                        throw new ServiceException(CommonErrorCode.INVALID_PARAMETER, "Wrong operator. Possible Operators -> " + Operators.getOperators());
                    }
                    //Possible search to Referenced entity attributes
                    Path<String> path = getPath(root, dynamicFilter.field());
                    checkSearchableField(path.getParentPath(), dynamicFilter.field().substring(dynamicFilter.field().indexOf(".") + 1));
                    String fieldType = path.getModel().getBindableJavaType().getSimpleName();
                    //Possible search to without case sensitivity
                    String value = (dynamicFilter.value() == null) ? null : dynamicFilter.value().toLowerCase();
                    // Possible search to null data
                    if (value == null) {
                        predicates.add(criteriaBuilder.isNull(path));
                        continue;
                    }
                    switch (dynamicFilter.operator()) {
                        case EQUAL -> {
                            checkPossibleFieldType(dynamicFilter.operator(), fieldType);
                            predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(path), value));
                        }
                        case NOT_EQUAL -> {
                            checkPossibleFieldType(dynamicFilter.operator(), fieldType);
                            predicates.add(criteriaBuilder.notEqual(criteriaBuilder.lower(path), value));
                        }
                        case LIKE -> {
                            checkPossibleFieldType(dynamicFilter.operator(), fieldType);
                            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(path), "%" + value + "%"));
                        }
                        case BETWEEN -> {
                            checkPossibleFieldType(dynamicFilter.operator(), fieldType);
                            if ("LocalDate".equals(fieldType)) {
                                List<LocalDate> list = Arrays.stream(value.split(",")).map(Converter::dateStringToLocalDate).toList();
                                predicates.add(criteriaBuilder.between(path.as(LocalDate.class), list.get(0), list.get(1)));
                            } else if ("LocalDateTime".equals(fieldType)) {
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
                            checkPossibleFieldType(dynamicFilter.operator(), fieldType);
                            if ("LocalDate".equals(fieldType)) {
                                Path<LocalDate> localDatePath = root.get(dynamicFilter.field());
                                List<LocalDate> list = Arrays.stream(value.split(",")).map(Converter::dateStringToLocalDate).toList();
                                predicates.add(localDatePath.in(list));
                            } else {
                                List<String> list = Arrays.asList(value.split(","));
                                predicates.add(path.in(list));
                            }
                        }
                    }
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }

    /**
     * Check field types that can be searched by operation
     *
     * @param fieldType entity field type
     * @param operators Operators enum class
     * @throws ServiceException 조회 할 수 있는 type 이 아니면 throw ServiceException(CommonErrorCode.INVALID_PARAMETER
     * @author GEONLEE
     * @since 2024-04-11
     */
    private static void checkPossibleFieldType(Operators operators, String fieldType) {
        switch (operators) {
            case EQUAL, NOT_EQUAL, IN -> {
                //Possible field type -> String, Integer, Double, LocalDate, Enum
                if ("LocalDateTime".equals(fieldType)) {
                    throw new ServiceException(CommonErrorCode.INVALID_PARAMETER
                            , "For LocalDateTime type, use 'between' operator.");
                }
            }
            case LIKE -> {
                //Possible field type -> String
                if (!"String".equals(fieldType)) {
                    throw new ServiceException(CommonErrorCode.INVALID_PARAMETER
                            , "The 'like' operator can only use 'String' types.");
                }
            }
            case BETWEEN -> {
                //Possible field type -> LocalDate, LocalDateType, Integer, Double
                if ("String".equals(fieldType)) {
                    throw new ServiceException(CommonErrorCode.INVALID_PARAMETER
                            , "The 'between' operator can only use 'LocalDate' or 'LocalDateTime' types.");
                }
            }
        }
    }

    /**
     * Possible search to Referenced entity attributes (recursion)
     *
     * @author GEONLEE
     * @since 2024-04-11
     */
    private static Path<String> getPath(Path<?> path, String fieldName) {
        if (fieldName.contains(".")) {
            String[] entityField = fieldName.split("\\.");
            return getPath(path.get(entityField[0]), fieldName.substring(fieldName.indexOf(".") + 1));
        }
        return path.get(fieldName);
    }

    /**
     * SearchableField annotation 을 확인하여 조회할 수 있는 컬럼인지 체크<br />
     * BaseEntity field 인 경우 superclass 로 치환<br />
     *
     * @author GEONLEE
     * @since 2024-04-11
     */
    private static void checkSearchableField(Path<?> path, String fieldName) {
        Class<?> entity = path.getJavaType();
        //BaseEntity field 조회 할 경우 상속받은 BaseEntity 로 변경
        if ("createDate".equals(fieldName) || "updateDate".equals(fieldName)) {
            entity = path.getJavaType().getSuperclass();
        }
        boolean searchableField = false;
        try {
            Field field = entity.getDeclaredField(fieldName);
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof SearchableField) {
                    searchableField = true;
                    break;
                }
            }
        } catch (NoSuchFieldException e) {
            throw new ServiceException(CommonErrorCode.INVALID_PARAMETER
                    , "'" + fieldName + "' does not exist in the '" + entity.getSimpleName() + "' entity.");
        }
        if (!searchableField) {
            throw new ServiceException(CommonErrorCode.INVALID_PARAMETER, "'" + fieldName + "' field that cannot be searched.");
        }
    }
}
