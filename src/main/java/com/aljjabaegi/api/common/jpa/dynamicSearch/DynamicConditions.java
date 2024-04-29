package com.aljjabaegi.api.common.jpa.dynamicSearch;

import com.aljjabaegi.api.common.exception.code.CommonErrorCode;
import com.aljjabaegi.api.common.exception.custom.ServiceException;
import com.aljjabaegi.api.common.jpa.annotation.SearchableField;
import com.aljjabaegi.api.common.jpa.base.BaseEntity;
import com.aljjabaegi.api.common.request.DynamicFilter;
import com.aljjabaegi.api.common.request.DynamicSorter;
import com.aljjabaegi.api.common.request.enumeration.Operator;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Dynamic request 관련 동적 조회, 정렬 조건 생성 interface
 *
 * @author GEONLEE
 * @since 2024-04-19<br />
 * 2024-04-24 GEONLEE - Add generateDefaultSort method<br />
 */
public interface DynamicConditions {
    List<String> BASE_ENTITY_FIELDS = Arrays.stream(BaseEntity.class.getDeclaredFields()).map(Field::getName).toList();

    /**
     * 정렬 조건 생성
     */
    Object generateSort(Class<?> entity, List<DynamicSorter> dynamicSorters);

    /**
     * 키 값으로 기본 정렬 조건 생성
     */
    Object generateDefaultSort(Class<?> entity);

    /**
     * 조회 조건 생성
     */
    Object generateConditions(Class<?> entity, List<DynamicFilter> dynamicFilters);

    /**
     * 클래스에 조회 가능한 field path string 을 리턴<br />
     * Searchable annotation 이 있는 경우에만 조회 가능<br />
     * 일반 속성의 경우 속성명으로 column path 설정<br />
     * 참조 객체의 경우 Searchable annotation 의 column path 로 설정<br />
     *
     * @param entity          entity class
     * @param searchFieldName search field name
     * @return field path string
     * @author GEONLEE
     * @since 2024-04-18
     * 2024-04-19 GEONLEE - super class 가 Object 가 아닌 조건 추가
     */
    default String getSearchFieldPath(Class<?> entity, String searchFieldName) {
        String fieldPath = null;
        // Check Base entity field
        if (entity.getSuperclass() == BaseEntity.class && BASE_ENTITY_FIELDS.contains(searchFieldName)) {
            entity = BaseEntity.class;
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
     * Check available field types that can be searched by operation
     *
     * @param fieldType entity field type
     * @param operators Operators enum class
     * @throws ServiceException 조회 할 수 있는 type 이 아니면 throw ServiceException(CommonErrorCode.INVALID_PARAMETER
     * @author GEONLEE
     * @since 2024-04-11<br />
     * 2024-04-29 GEONLEE - fieldType String -> Class 로 변경, BETWEEN, LTE, GTE 조건에 enum 추가<br />
     */
    default void checkAvailableFieldTypes(Operator operators, Class<?> fieldType) {
        switch (operators) {
            case EQUAL, NOT_EQUAL, IN -> {
                //Possible field type -> String, Integer, Double, LocalDate, Enum
                if (fieldType == LocalDateTime.class) {
                    throw new ServiceException(CommonErrorCode.INVALID_PARAMETER
                            , "For LocalDateTime type, use 'between' operator.");
                }
            }
            case LIKE -> {
                //Possible field type -> String
                if (fieldType != String.class) {
                    throw new ServiceException(CommonErrorCode.INVALID_PARAMETER
                            , "The 'like' operator can only use 'String' types.");
                }
            }
            case BETWEEN, LTE, GTE -> {
                //Possible field type -> LocalDate, LocalDateType, Number type
                if (fieldType == String.class || fieldType.isEnum()) {
                    throw new ServiceException(CommonErrorCode.INVALID_PARAMETER
                            , "The 'between' operator can only use 'LocalDate','LocalDateTime' or 'Number' types.");
                }
            }
        }
    }
}
