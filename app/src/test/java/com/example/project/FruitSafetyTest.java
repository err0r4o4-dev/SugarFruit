package com.example.project;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FruitSafetyTest {
    private final Fruit fruit = new Fruit(
            0, "ผลไม้ทดสอบ", "sugar", "sugar detail", "index", "index detail",
            "🟢 ปลอดภัย", "Summer", "carbohydrate", "fiber", "impact",
            "type 1", "type 2", "end", "🟡 ควรจำกัด", "🔴 ควรหลีกเลี่ยง", "🟢 ปลอดภัย");

    @Test
    public void safetyLabel_usesSelectedDiabetesLevel() {
        assertEquals(FruitSafety.Level.LIMIT,
                FruitSafety.forDiabetesLevel(fruit, DiabetesType.TYPE_1.getCode()));
        assertEquals(FruitSafety.Level.AVOID,
                FruitSafety.forDiabetesLevel(fruit, DiabetesType.TYPE_2.getCode()));
        assertEquals(FruitSafety.Level.SAFE,
                FruitSafety.forDiabetesLevel(fruit, DiabetesType.GESTATIONAL.getCode()));
    }

    @Test
    public void filter_usesStableSafetyLevel() {
        assertTrue(FruitSafety.matchesFilter(
                fruit, DiabetesType.TYPE_2.getCode(), FruitSafety.Level.AVOID));
        assertFalse(FruitSafety.matchesFilter(
                fruit, DiabetesType.TYPE_2.getCode(), FruitSafety.Level.SAFE));
        assertTrue(FruitSafety.matchesFilter(
                fruit, DiabetesType.TYPE_2.getCode(), null));
    }

    @Test
    public void season_usesStableCodeInsteadOfDisplayedText() {
        assertEquals(FruitSeason.SUMMER, fruit.getSeasonValue());
    }
}
