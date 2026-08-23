package com.example.project;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Collections;
import java.util.Set;

public final class SavedFruitStore {
    private static final String PREFERENCES_NAME = "sugarfruit:saved-fruits";
    private static final String KEY_SAVED_FRUIT_IDS = "sugarfruit:saved-fruit-ids";

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

    public void save(String fruitId) {
        Set<String> ids = getSavedFruitIds();
        if (fruitId != null && !fruitId.trim().isEmpty() && ids.add(fruitId.trim())) {
            persist(ids);
        }
    }

    public void remove(String fruitId) {
        Set<String> ids = getSavedFruitIds();
        if (ids.remove(fruitId)) {
            persist(ids);
        }
    }

    public boolean toggle(String fruitId) {
        Set<String> ids = getSavedFruitIds();
        boolean saved = SavedFruitIds.toggle(ids, fruitId);
        persist(ids);
        return saved;
    }

    public void clear() {
        preferences.edit().remove(KEY_SAVED_FRUIT_IDS).apply();
    }

    private void persist(Set<String> ids) {
        preferences.edit().putStringSet(KEY_SAVED_FRUIT_IDS, ids).apply();
    }
}
