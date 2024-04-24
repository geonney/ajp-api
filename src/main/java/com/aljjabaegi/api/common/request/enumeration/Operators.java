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
 * @since 2024-04-09
 */
public enum Operators {
    EQUAL("eq"), NOT_EQUAL("neq"), LIKE("contains"), BETWEEN("between"), IN("in"), LTE("lte"), GTE("gte");

    private static final Map<String, Operators> OPERATOR_MAP = Stream.of(values())
            .collect(Collectors.toMap(Operators::type, e -> e));
    private final String type;

    Operators(String type) {
        this.type = type;
    }

    public static Optional<Operators> value(String operator) {
        return Optional.ofNullable(OPERATOR_MAP.get(operator));
    }

    /**
     * RequestBody 에서 String to Enum 시 활용
     */
    @JsonCreator
    public static Operators fromText(String operatorText) {
        for (Operators operators : Operators.values()) {
            if (operators.type().equals(operatorText)) {
                return operators;
            }
        }
        return null;
    }

    /**
     * @return Operators type String
     */
    public static String getOperators() {
        StringJoiner stringJoiner = new StringJoiner(",");
        for (Operators operators : Operators.values()) {
            stringJoiner.add(operators.type());
        }
        return stringJoiner.toString();
    }

    public String type() {
        return this.type;
    }

}
