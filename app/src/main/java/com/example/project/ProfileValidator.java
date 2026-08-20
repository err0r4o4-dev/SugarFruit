package com.example.project;

import java.util.regex.Pattern;

public final class ProfileValidator {
    private static final int MIN_HEIGHT_CM = 30;
    private static final int MAX_HEIGHT_CM = 300;
    private static final int MIN_WEIGHT_KG = 2;
    private static final int MAX_WEIGHT_KG = 500;
    private static final Pattern NAME_PATTERN = Pattern.compile("[\\p{L}\\p{M} .'-]+");

    public enum Result { VALID, MISSING_FIELD, INVALID_NAME, INVALID_HEIGHT, INVALID_WEIGHT }

    private ProfileValidator() {
    }

    public static Result validate(String name, String height, String weight,
                                  String birthDate, String sex, String diabetesLevel) {
        if (isBlank(name) || isBlank(height) || isBlank(weight) || isBlank(birthDate)
                || isBlank(sex) || isBlank(diabetesLevel)) {
            return Result.MISSING_FIELD;
        }
        if (!NAME_PATTERN.matcher(name).matches()) {
            return Result.INVALID_NAME;
        }
        if (!isIntegerInRange(height, MIN_HEIGHT_CM, MAX_HEIGHT_CM)) {
            return Result.INVALID_HEIGHT;
        }
        if (!isIntegerInRange(weight, MIN_WEIGHT_KG, MAX_WEIGHT_KG)) {
            return Result.INVALID_WEIGHT;
        }
        return Result.VALID;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private static boolean isIntegerInRange(String value, int minimum, int maximum) {
        try {
            int parsed = Integer.parseInt(value);
            return parsed >= minimum && parsed <= maximum;
        } catch (NumberFormatException exception) {
            return false;
        }
    }
}
