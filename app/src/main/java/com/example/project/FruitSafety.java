package com.example.project;

import android.content.Context;

public final class FruitSafety {
    public enum Level {
        SAFE,
        LIMIT,
        AVOID,
        UNKNOWN
    }

    private FruitSafety() {
    }

    public static Level forDiabetesLevel(Fruit fruit, String diabetesTypeCode) {
        DiabetesType diabetesType = DiabetesType.fromCode(diabetesTypeCode);
        if (diabetesType == DiabetesType.TYPE_1) {
            return fromLabel(fruit.getLevel1());
        }
        if (diabetesType == DiabetesType.TYPE_2) {
            return fromLabel(fruit.getLevel2());
        }
        if (diabetesType == DiabetesType.GESTATIONAL) {
            return fromLabel(fruit.getLevel3());
        }
        return fromLabel(fruit.getTrue());
    }

    public static boolean matchesFilter(Fruit fruit, String diabetesTypeCode, Level selectedLevel) {
        return selectedLevel == null || forDiabetesLevel(fruit, diabetesTypeCode) == selectedLevel;
    }

    public static String localizedLabel(Context context, Level level) {
        switch (level) {
            case SAFE:
                return context.getString(R.string.safety_value_safe);
            case LIMIT:
                return context.getString(R.string.safety_value_limit);
            case AVOID:
                return context.getString(R.string.safety_value_avoid);
            case UNKNOWN:
            default:
                return context.getString(R.string.detail_unavailable);
        }
    }

    public static int rank(Level level) {
        switch (level) {
            case SAFE:
                return 0;
            case LIMIT:
                return 1;
            case AVOID:
                return 2;
            case UNKNOWN:
            default:
                return 3;
        }
    }

    public static Level fromLabel(String label) {
        if (label == null) {
            return Level.UNKNOWN;
        }
        if (label.contains("\uD83D\uDFE2")) {
            return Level.SAFE;
        }
        if (label.contains("\uD83D\uDFE1")) {
            return Level.LIMIT;
        }
        if (label.contains("\uD83D\uDD34")) {
            return Level.AVOID;
        }
        return Level.UNKNOWN;
    }
}
