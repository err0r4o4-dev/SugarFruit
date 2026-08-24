package com.example.project;

public enum FruitSeason {
    SUMMER,
    RAINY,
    WINTER;

    public static FruitSeason fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (FruitSeason season : values()) {
            if (season.name().equalsIgnoreCase(code)) {
                return season;
            }
        }
        return null;
    }
}
