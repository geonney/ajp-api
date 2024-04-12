package com.aljjabaegi.api.common.request.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.StringJoiner;

/**
 * @author GEONLEE
 * @since 2024-04-12
 */
public enum SortDirections {
    ASC("ASC"), DESC("DESC");

    private final String type;

    SortDirections(String type) {
        this.type = type;
    }

    /**
     * RequestBody 에서 String to Enum 시 활용
     */
    @JsonCreator
    public static SortDirections fromText(String sortDirectionsText) {
        for (SortDirections sortDirections : SortDirections.values()) {
            if (sortDirections.type().equalsIgnoreCase(sortDirectionsText)) {
                return sortDirections;
            }
        }
        return null;
    }

    /**
     * @return SortDirections type String
     */
    public static String getSorDirections() {
        StringJoiner stringJoiner = new StringJoiner(",");
        for (SortDirections sortDirection : SortDirections.values()) {
            stringJoiner.add(sortDirection.type());
        }
        return stringJoiner.toString();
    }

    public String type() {
        return this.type;
    }
}
