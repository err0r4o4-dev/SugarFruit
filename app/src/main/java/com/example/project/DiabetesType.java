package com.example.project;

public enum DiabetesType {
    TYPE_1("type_1"),
    TYPE_2("type_2"),
    GESTATIONAL("gestational"),
    UNKNOWN("");

    private final String code;

    DiabetesType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static DiabetesType fromCode(String code) {
        for (DiabetesType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public static DiabetesType fromPosition(int position) {
        switch (position) {
            case 0:
                return TYPE_1;
            case 1:
                return TYPE_2;
            case 2:
                return GESTATIONAL;
            default:
                return UNKNOWN;
        }
    }
}
