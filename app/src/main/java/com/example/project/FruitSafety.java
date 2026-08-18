package com.example.project;

public final class FruitSafety {
    public static final String TYPE_1 = "เบาหวานชนิดที่ 1";
    public static final String TYPE_2 = "เบาหวานชนิดที่ 2";
    public static final String GESTATIONAL = "เบาหวานขณะตั้งครรภ์";

    private FruitSafety() {
    }

    public static String forDiabetesLevel(Fruit fruit, String diabetesLevel) {
        if (TYPE_1.equals(diabetesLevel)) {
            return fruit.getLevel1();
        }
        if (TYPE_2.equals(diabetesLevel)) {
            return fruit.getLevel2();
        }
        if (GESTATIONAL.equals(diabetesLevel)) {
            return fruit.getLevel3();
        }
        return fruit.getTrue();
    }

    public static boolean matchesFilter(Fruit fruit, String diabetesLevel,
                                        String selectedSafetyLevel, String allSafetyLevels) {
        return allSafetyLevels.equals(selectedSafetyLevel)
                || forDiabetesLevel(fruit, diabetesLevel).contains(selectedSafetyLevel);
    }
}
