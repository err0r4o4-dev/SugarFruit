package com.example.project;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

final class SavedFruitIds {
    private SavedFruitIds() {
    }

    static Set<String> sanitize(Collection<String> ids) {
        Set<String> sanitized = new LinkedHashSet<>();
        if (ids == null) {
            return sanitized;
        }
        for (String id : ids) {
            if (id != null && !id.trim().isEmpty()) {
                sanitized.add(id.trim());
            }
        }
        return sanitized;
    }

    static boolean toggle(Set<String> ids, String fruitId) {
        if (fruitId == null || fruitId.trim().isEmpty()) {
            return false;
        }
        String stableId = fruitId.trim();
        if (ids.remove(stableId)) {
            return false;
        }
        ids.add(stableId);
        return true;
    }
}
