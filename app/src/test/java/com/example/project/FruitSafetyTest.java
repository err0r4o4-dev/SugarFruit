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
        assertEquals("🟡 ควรจำกัด", FruitSafety.forDiabetesLevel(fruit, FruitSafety.TYPE_1));
        assertEquals("🔴 ควรหลีกเลี่ยง", FruitSafety.forDiabetesLevel(fruit, FruitSafety.TYPE_2));
        assertEquals("🟢 ปลอดภัย", FruitSafety.forDiabetesLevel(fruit, FruitSafety.GESTATIONAL));
    }

    @Test
    public void filter_usesSameLabelAsListRow() {
        assertTrue(FruitSafety.matchesFilter(
                fruit, FruitSafety.TYPE_2, "ควรหลีกเลี่ยง", "ทั้งหมด"));
        assertFalse(FruitSafety.matchesFilter(
                fruit, FruitSafety.TYPE_2, "ปลอดภัย", "ทั้งหมด"));
        assertTrue(FruitSafety.matchesFilter(
                fruit, FruitSafety.TYPE_2, "ทั้งหมด", "ทั้งหมด"));
    }
}
