package com.example.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class SavedFruitIdsTest {
    @Test
    public void sanitize_removesBlankAndDuplicateIds() {
        Set<String> ids = SavedFruitIds.sanitize(
                Arrays.asList("q1", " q1 ", "", "  ", null, "q2"));

        assertEquals(new LinkedHashSet<>(Arrays.asList("q1", "q2")), ids);
    }

    @Test
    public void toggle_addsThenRemovesOneStableId() {
        Set<String> ids = new LinkedHashSet<>();

        assertTrue(SavedFruitIds.toggle(ids, "q5"));
        assertTrue(ids.contains("q5"));
        assertFalse(SavedFruitIds.toggle(ids, "q5"));
        assertFalse(ids.contains("q5"));
    }

    @Test
    public void toggle_rejectsMissingId() {
        Set<String> ids = new LinkedHashSet<>();

        assertFalse(SavedFruitIds.toggle(ids, "  "));
        assertTrue(ids.isEmpty());
    }
}
