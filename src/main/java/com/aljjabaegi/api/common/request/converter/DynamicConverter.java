package com.aljjabaegi.api.common.request.converter;

import com.aljjabaegi.api.common.exception.code.CommonErrorCode;
import com.aljjabaegi.api.common.exception.custom.ServiceException;
import com.aljjabaegi.api.common.request.DynamicFilter;
import com.aljjabaegi.api.common.request.enumeration.Operator;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author GEONLEE
 * @since 2024-11-08
 * Record 를 List<DynamicFilter> type 으로 변경하는 Converter
 */
public class DynamicConverter<T extends Record> {

    public List<DynamicFilter> toDynamicFilter(Object object) {
        return toDynamicFilter(object, null);
    }

    @SuppressWarnings("unchecked")
    public List<DynamicFilter> toDynamicFilter(Object object, Map<String, Operator> operatorMap) {
        if (!isRecord(object)) {
            throw new ServiceException(CommonErrorCode.LOGIC_ERROR, "Record 타입만 변환이 가능합니다.");
        }
        Class<T> recordClass = (Class<T>) object.getClass();
        List<DynamicFilter> list = new ArrayList<>();
        Arrays.stream(recordClass.getRecordComponents())
                .forEach(recordComponent -> list.add(createDynamicFilter(object, recordComponent, operatorMap)));
        return list;
    }

    private DynamicFilter createDynamicFilter(Object recordObject, RecordComponent recordComponent, Map<String, Operator> operatorMap) {
        try {
            String fieldName = recordComponent.getName();
            Object fieldValue = recordComponent.getAccessor().invoke(recordObject);
            Operator operator = getOperator(operatorMap, fieldName);
            return DynamicFilter.builder()
                    .field(fieldName)
                    .value(String.valueOf(fieldValue))
                    .operator(operator)
                    .build();
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new ServiceException(CommonErrorCode.LOGIC_ERROR, "Record to DynamicFilter 변환 처리에 실패하였습니다.");
        }
    }

    private boolean isRecord(Object object) {
        return object instanceof Record;
    }

    private Operator getOperator(Map<String, Operator> operatorMap, String fieldName) {
        if (ObjectUtils.isEmpty(operatorMap) || ObjectUtils.isEmpty(operatorMap.get(fieldName))) {
            return Operator.EQUAL;
        }
        return operatorMap.get(fieldName);
    }
}
