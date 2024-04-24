package com.aljjabaegi.api.common.request.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Conditions used in DynamicSpecification
 *
 * @author GEONLEE
 * @since 2024-04-09<br />
 * 2024-04-24 Operator 로 명칭 변경, getOperatorString 으로 메서드 명칭 변경<br />
 */
public enum Operator {
    EQUAL("eq"), NOT_EQUAL("neq"), LIKE("contains"), BETWEEN("between"), IN("in"), LTE("lte"), GTE("gte");

    private static final Map<String, Operator> OPERATOR_MAP = Stream.of(values())
            .collect(Collectors.toMap(Operator::type, e -> e));
    private final String type;

    Operator(String type) {
        this.type = type;
    }

    public static Optional<Operator> value(String operator) {
        return Optional.ofNullable(OPERATOR_MAP.get(operator));
    }

    /**
     * RequestBody 에서 String to Enum 시 활용
     */
    @JsonCreator
    public static Operator fromText(String operatorText) {
        for (Operator operators : Operator.values()) {
            if (operators.type().equals(operatorText)) {
                return operators;
            }
        }
        return null;
    }

    /**
     * @return Operators type String
     */
    public static String getOperatorString() {
        StringJoiner stringJoiner = new StringJoiner(",");
        for (Operator operators : Operator.values()) {
            stringJoiner.add(operators.type());
        }
        return stringJoiner.toString();
    }

    public String type() {
        return this.type;
    }

}
