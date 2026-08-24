package com.example.project;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatDelegate;

public final class AppSettings {
    private static final String PREFERENCES_NAME = "sugarfruit:preferences";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_BIRTH_DATE = "birth_date";
    private static final String KEY_SEX_POSITION = "sex_position";
    private static final String KEY_DIABETES_TYPE = "diabetes_type";
    private static final String KEY_DARK_MODE = "dark_mode";
    private static final String KEY_FONT_SCALE = "font_scale";

    public static final float FONT_SCALE_SMALL = 0.90f;
    public static final float FONT_SCALE_STANDARD = 1.00f;
    public static final float FONT_SCALE_LARGE = 1.15f;

    private AppSettings() {
    }

    public static void saveProfile(Context context, String userName, String height,
            String weight, String birthDate, int sexPosition, DiabetesType diabetesType) {
        preferences(context).edit()
                .putString(KEY_USER_NAME, userName)
                .putString(KEY_HEIGHT, height)
                .putString(KEY_WEIGHT, weight)
                .putString(KEY_BIRTH_DATE, birthDate)
                .putInt(KEY_SEX_POSITION, sexPosition)
                .putString(KEY_DIABETES_TYPE, diabetesType.getCode())
                .apply();
    }

    public static boolean hasProfile(Context context) {
        return !getUserName(context).trim().isEmpty();
    }

    public static String getUserName(Context context) {
        return preferences(context).getString(KEY_USER_NAME, "");
    }

    public static String getHeight(Context context) {
        return preferences(context).getString(KEY_HEIGHT, "");
    }

    public static String getWeight(Context context) {
        return preferences(context).getString(KEY_WEIGHT, "");
    }

    public static String getBirthDate(Context context) {
        return preferences(context).getString(KEY_BIRTH_DATE, "");
    }

    public static int getSexPosition(Context context) {
        return preferences(context).getInt(KEY_SEX_POSITION, -1);
    }

    public static DiabetesType getDiabetesType(Context context) {
        return DiabetesType.fromCode(
                preferences(context).getString(
                        KEY_DIABETES_TYPE,
                        DiabetesType.UNKNOWN.getCode()));
    }

    public static void setDiabetesType(Context context, DiabetesType diabetesType) {
        preferences(context).edit()
                .putString(KEY_DIABETES_TYPE, diabetesType.getCode())
                .apply();
    }

    public static void clearProfile(Context context) {
        preferences(context).edit()
                .remove(KEY_USER_NAME)
                .remove(KEY_HEIGHT)
                .remove(KEY_WEIGHT)
                .remove(KEY_BIRTH_DATE)
                .remove(KEY_SEX_POSITION)
                .remove(KEY_DIABETES_TYPE)
                .apply();
    }

    public static int getNightMode(Context context) {
        SharedPreferences preferences = preferences(context);
        if (!preferences.contains(KEY_DARK_MODE)) {
            return AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        }
        return preferences.getBoolean(KEY_DARK_MODE, false)
                ? AppCompatDelegate.MODE_NIGHT_YES
                : AppCompatDelegate.MODE_NIGHT_NO;
    }

    public static boolean isDarkModeEnabled(Context context) {
        int nightMode = getNightMode(context);
        if (nightMode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
            int currentNightMode = context.getResources().getConfiguration().uiMode
                    & Configuration.UI_MODE_NIGHT_MASK;
            return currentNightMode == Configuration.UI_MODE_NIGHT_YES;
        }
        return nightMode == AppCompatDelegate.MODE_NIGHT_YES;
    }

    public static void setDarkModeEnabled(Context context, boolean enabled) {
        preferences(context).edit().putBoolean(KEY_DARK_MODE, enabled).apply();
    }

    public static float getFontScale(Context context) {
        float scale = preferences(context).getFloat(KEY_FONT_SCALE, FONT_SCALE_STANDARD);
        if (scale == FONT_SCALE_SMALL || scale == FONT_SCALE_LARGE) {
            return scale;
        }
        return FONT_SCALE_STANDARD;
    }

    public static void setFontScale(Context context, float scale) {
        float safeScale = scale == FONT_SCALE_SMALL || scale == FONT_SCALE_LARGE
                ? scale
                : FONT_SCALE_STANDARD;
        preferences(context).edit().putFloat(KEY_FONT_SCALE, safeScale).apply();
    }

    private static SharedPreferences preferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }
}
