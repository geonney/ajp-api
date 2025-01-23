package com.aljjabaegi.api.common.jpa.dynamicSearch.conditions;

/**
 * @author GEONLEE
 * @since 2025-01-22
 * JpaDynamicRepository 에서 findDynamicWithPageable 의 매개변수로 Specification 과 booleanBuilder 를 받아 공통으로 처리하기 위한 Interface
 * 형변환 오류 발생 가능성이 있고, 굳이 필요할까 하지만 캡슐화가 가능
 */
public interface QueryCondition {

    Object getCondition();

    Object getSorter();

}
