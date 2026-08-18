package com.example.project;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProfileValidatorTest {
    @Test
    public void validProfile_isAccepted() {
        assertEquals(ProfileValidator.Result.VALID,
                ProfileValidator.validate("สมชาย ใจดี", "170", "65", "1 มกราคม 2543",
                        "ชาย", FruitSafety.TYPE_2));
    }

    @Test
    public void missingField_isRejected() {
        assertEquals(ProfileValidator.Result.MISSING_FIELD,
                ProfileValidator.validate("", "170", "65", "1 มกราคม 2543",
                        "ชาย", FruitSafety.TYPE_2));
    }

    @Test
    public void nameWithNumber_isRejected() {
        assertEquals(ProfileValidator.Result.INVALID_NAME,
                ProfileValidator.validate("สมชาย123", "170", "65", "1 มกราคม 2543",
                        "ชาย", FruitSafety.TYPE_2));
    }

    @Test
    public void outOfRangeHeight_isRejected() {
        assertEquals(ProfileValidator.Result.INVALID_HEIGHT,
                ProfileValidator.validate("สมชาย", "0", "65", "1 มกราคม 2543",
                        "ชาย", FruitSafety.TYPE_2));
    }

    @Test
    public void outOfRangeWeight_isRejected() {
        assertEquals(ProfileValidator.Result.INVALID_WEIGHT,
                ProfileValidator.validate("สมชาย", "170", "999", "1 มกราคม 2543",
                        "ชาย", FruitSafety.TYPE_2));
    }
}
