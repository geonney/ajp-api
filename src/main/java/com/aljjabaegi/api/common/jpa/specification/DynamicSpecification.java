package com.aljjabaegi.api.common.jpa.specification;

import com.aljjabaegi.api.common.exception.code.CommonErrorCode;
import com.aljjabaegi.api.common.exception.custom.ServiceException;
import com.aljjabaegi.api.common.jpa.annotation.SearchableField;
import com.aljjabaegi.api.common.jpa.base.BaseEntity;
import com.aljjabaegi.api.common.jpa.mapstruct.Converter;
import com.aljjabaegi.api.common.request.DynamicFilter;
import com.aljjabaegi.api.common.request.DynamicSorter;
import com.aljjabaegi.api.common.request.enumeration.Operators;
import com.aljjabaegi.api.common.request.enumeration.SortDirections;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.hibernate.query.sqm.PathElementException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.lang.annotation.Annotation;
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
 * - getSearchFieldPath, BASE_ENTITY_FIELD 추가 -> BaseEntity field 처리 방식 변경
 */
public class DynamicSpecification {

    private static final List<String> BASE_ENTITY_FIELD = Arrays.stream(BaseEntity.class.getDeclaredFields()).map(Field::getName).toList();

    public static Sort generateSort(Class<?> entity, List<DynamicSorter> dynamicSorters) {
        if (dynamicSorters == null) {
            return Sort.unsorted();
        }
        checkSortableField(entity, dynamicSorters);
        List<Sort.Order> orderList = new ArrayList<>();
        for (DynamicSorter dynamicSorter : dynamicSorters) {
            if (dynamicSorter.sortDirection() == null) {
                throw new ServiceException(CommonErrorCode.INVALID_PARAMETER
                        , "Invalid sort direction. Possible sort directions -> " + SortDirections.getSorDirections());
            }
            switch (dynamicSorter.sortDirection()) {
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

    /**
     * @param <T>            entity
     * @param dynamicFilters search condition
     * @return Specification
     */
    public static <T> Specification<T> generateSpecification(List<DynamicFilter> dynamicFilters) {
        return (root, query, criteriaBuilder) -> {
            if (dynamicFilters == null || dynamicFilters.size() == 0) {
                return criteriaBuilder.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();
            for (DynamicFilter dynamicFilter : dynamicFilters) {
                if (dynamicFilter.operator() == null) {
                    throw new ServiceException(CommonErrorCode.INVALID_PARAMETER, "Invalid operator. Possible Operators -> " + Operators.getOperators());
                }
                String fieldPath = getSearchFieldPath(root, dynamicFilter.field());
                Path<String> path = getPath(root, fieldPath);
//                checkSearchableField(path.getParentPath(), dynamicFilter.field());
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
     * 클래스에 조회 가능한 field path string 을 리턴<br />
     * Searchable annotation 이 있는 경우에만 조회 가능<br />
     * 참조 객체의 경우 Searchable annotation 의 column path 로 설정<br />
     *
     * @param path            root field path
     * @param searchFieldName search field name
     * @return field path string
     */
    private static String getSearchFieldPath(Path<?> path, String searchFieldName) {
        String fieldPath = null;
        Class<?> entity = path.getJavaType();
        // Check Base entity field
        if (BASE_ENTITY_FIELD.contains(searchFieldName)) {
            entity = path.getJavaType().getSuperclass();
        }
        Field[] fields = entity.getDeclaredFields();
        for (Field field : fields) {
            SearchableField searchableField = field.getAnnotation(SearchableField.class);
            // Check @SearchableField annotation
            if (searchableField == null) continue;

            // If the field names are the same, return the columnPath
            if (field.getName().equals(searchFieldName)) {
                fieldPath = field.getName();
                break;
            }

            // If the field name is different, determine it as a reference object and return the columnPath
            String[] paths = searchableField.columnPath();
            for (String searchableFieldPath : paths) {
                String lastFieldName = searchableFieldPath.substring(searchableFieldPath.lastIndexOf(".") + 1);
                if (lastFieldName.equals(searchFieldName)) {
                    fieldPath = searchableFieldPath;
                    break;
                }
            }
        }
        // If the field path is different, throw exception
        if (fieldPath == null) {
            throw new ServiceException(CommonErrorCode.INVALID_PARAMETER, "'" + searchFieldName + "' field that cannot be searched.");
        }
        return fieldPath;
    }

    /**
     * Possible search to Referenced entity attributes (recursion)
     *
     * @throws ServiceException Entity 에서 field 가 없을 경우 PathElementException 발생
     * @author GEONLEE
     * @since 2024-04-11<br />
     * 2024-04-12 GEONLEE - PathElementException 발생 시 INVALID_PARAMETER 전달<br />
     */
    private static Path<String> getPath(Path<?> path, String fieldName) {
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
     * SearchableField annotation 을 확인하여 조회할 수 있는 컬럼인지 체크<br />
     * BaseEntity field 인 경우 superclass 로 치환<br />
     *
     * @author GEONLEE
     * @since 2024-04-11<br />
     * 2024-04-18 GEONLEE - Deprecated, getSearchFieldPath 에서 처리하도록 변경
     */
    @Deprecated
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

    /**
     * Check sort request field validity<br / >
     * base entity 를 extends 받았다면 컬럼 명 추가
     *
     * @throws ServiceException 유효하지 않은 정보 throw
     * @author GEONLEE
     * @since 2024-04-12
     */
    private static void checkSortableField(Class<?> entity, List<DynamicSorter> dynamicSorters) {
        Set<String> fieldNames = new HashSet<>(Arrays.stream(entity.getDeclaredFields()).map(Field::getName).toList());
        if (entity.getSuperclass() != null) {
            List<String> baseEntityFieldNames = Arrays.stream(entity.getSuperclass().getDeclaredFields()).map(Field::getName).toList();
            fieldNames.addAll(baseEntityFieldNames);
        }
        for (DynamicSorter dynamicSorter : dynamicSorters) {
            if (!fieldNames.contains(dynamicSorter.field())) {
                throw new ServiceException(CommonErrorCode.INVALID_PARAMETER
                        , "'" + dynamicSorter.field() + "' does not exist in the '" + entity.getSimpleName() + "' entity.");
            }
        }
    }
}
