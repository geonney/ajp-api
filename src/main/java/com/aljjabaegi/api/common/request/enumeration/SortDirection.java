package com.aljjabaegi.api.common.request.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.StringJoiner;

/**
 * @author GEONLEE
 * @since 2024-04-12<br />
 * 2024-04-24 GEONLEE - SortDirection 으로 명칭 변경, getSorDirectionString 으로 메서드 명 변경<br />
 */
public enum SortDirection {
    ASC("ASC"), DESC("DESC");

    private final String type;

    SortDirection(String type) {
        this.type = type;
    }

    /**
     * RequestBody 에서 String to Enum 시 활용
     */
    @JsonCreator
    public static SortDirection fromText(String sortDirectionsText) {
        for (SortDirection sortDirections : SortDirection.values()) {
            if (sortDirections.type().equalsIgnoreCase(sortDirectionsText)) {
                return sortDirections;
            }
        }
        return null;
    }

    /**
     * @return SortDirections type String
     */
    public static String getSorDirectionString() {
        StringJoiner stringJoiner = new StringJoiner(",");
        for (SortDirection sortDirection : SortDirection.values()) {
            stringJoiner.add(sortDirection.type());
        }
        return stringJoiner.toString();
    }

    public String type() {
        return this.type;
    }
}
