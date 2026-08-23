package com.example.project;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Collections;
import java.util.Set;

public final class SavedFruitStore {
    private static final String PREFERENCES_NAME = "sugarfruit:saved-fruits";
    private static final String KEY_SAVED_FRUIT_IDS = "sugarfruit:saved-fruit-ids";
    private static final String KEY_SAVED_AT_PREFIX = "sugarfruit:saved-at:";

    private final SharedPreferences preferences;

    public SavedFruitStore(Context context) {
        preferences = context.getApplicationContext().getSharedPreferences(
                PREFERENCES_NAME,
                Context.MODE_PRIVATE);
    }

    public Set<String> getSavedFruitIds() {
        try {
            return SavedFruitIds.sanitize(
                    preferences.getStringSet(KEY_SAVED_FRUIT_IDS, Collections.emptySet()));
        } catch (ClassCastException exception) {
            preferences.edit().remove(KEY_SAVED_FRUIT_IDS).apply();
            return SavedFruitIds.sanitize(Collections.emptySet());
        }
    }

    public boolean isSaved(String fruitId) {
        return getSavedFruitIds().contains(fruitId);
    }

    public long getSavedAt(String fruitId) {
        if (fruitId == null || fruitId.trim().isEmpty()) {
            return 0L;
        }
        return preferences.getLong(KEY_SAVED_AT_PREFIX + fruitId.trim(), 0L);
    }

    public void save(String fruitId) {
        Set<String> ids = getSavedFruitIds();
        if (fruitId != null && !fruitId.trim().isEmpty() && ids.add(fruitId.trim())) {
            String normalizedId = fruitId.trim();
            preferences.edit()
                    .putStringSet(KEY_SAVED_FRUIT_IDS, ids)
                    .putLong(KEY_SAVED_AT_PREFIX + normalizedId, System.currentTimeMillis())
                    .apply();
        }
    }

    public void remove(String fruitId) {
        Set<String> ids = getSavedFruitIds();
        if (ids.remove(fruitId)) {
            preferences.edit()
                    .putStringSet(KEY_SAVED_FRUIT_IDS, ids)
                    .remove(KEY_SAVED_AT_PREFIX + fruitId)
                    .apply();
        }
    }

    public boolean toggle(String fruitId) {
        Set<String> ids = getSavedFruitIds();
        boolean saved = SavedFruitIds.toggle(ids, fruitId);
        SharedPreferences.Editor editor = preferences.edit()
                .putStringSet(KEY_SAVED_FRUIT_IDS, ids);
        if (saved) {
            editor.putLong(KEY_SAVED_AT_PREFIX + fruitId.trim(), System.currentTimeMillis());
        } else if (fruitId != null) {
            editor.remove(KEY_SAVED_AT_PREFIX + fruitId.trim());
        }
        editor.apply();
        return saved;
    }

    public void clear() {
        SharedPreferences.Editor editor = preferences.edit().remove(KEY_SAVED_FRUIT_IDS);
        for (String fruitId : getSavedFruitIds()) {
            editor.remove(KEY_SAVED_AT_PREFIX + fruitId);
        }
        editor.apply();
    }
}
