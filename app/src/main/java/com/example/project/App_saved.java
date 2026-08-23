package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class App_saved extends BaseActivity {
    private static final int SORT_DEFAULT = 0;
    private static final int SORT_NEWEST = 1;
    private static final int SORT_OLDEST = 2;

    private SavedFruitStore savedFruitStore;
    private List<Fruit> allFruits;
    private FruitAdapter adapter;
    private RecyclerView recyclerView;
    private View emptyState;
    private TextView resultCountText;
    private TextView sortLabel;
    private int sortMode = SORT_DEFAULT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_saved);

        savedFruitStore = new SavedFruitStore(this);
        allFruits = App_page3.createFruitData(this);
        recyclerView = findViewById(R.id.savedRecyclerView);
        emptyState = findViewById(R.id.savedEmptyState);
        resultCountText = findViewById(R.id.savedResultCount);
        sortLabel = findViewById(R.id.savedSortLabel);
        sortLabel.setOnClickListener(view -> showSortDialog());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FruitAdapter(
                new ArrayList<>(),
                AppSettings.getDiabetesType(this).getCode(),
                savedFruitStore,
                (fruit, isSaved) -> {
                    Toast.makeText(
                            this,
                            getString(
                                    isSaved
                                            ? R.string.fruit_saved_message
                                            : R.string.fruit_removed_message,
                                    fruit.getName()),
                            Toast.LENGTH_SHORT).show();
                    refreshSavedFruits();
                });
        recyclerView.setAdapter(adapter);

        findViewById(R.id.browseFruitsButton).setOnClickListener(view -> openHome());
        BottomNavigationCoordinator.bind(
                this,
                (BottomNavigationView) findViewById(R.id.bottomNavigation),
                BottomNavigationCoordinator.Destination.SAVED);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationCoordinator.selectCurrent(
                (BottomNavigationView) findViewById(R.id.bottomNavigation),
                BottomNavigationCoordinator.Destination.SAVED);
        if (adapter != null) {
            adapter.setDiabetesLevel(AppSettings.getDiabetesType(this).getCode());
            refreshSavedFruits();
        }
    }

    private void refreshSavedFruits() {
        Set<String> savedIds = savedFruitStore.getSavedFruitIds();
        List<Fruit> savedFruits = new ArrayList<>();
        for (Fruit fruit : allFruits) {
            if (savedIds.contains(fruit.getStableId(this))) {
                savedFruits.add(fruit);
            }
        }

        if (sortMode != SORT_DEFAULT) {
            savedFruits.sort((first, second) -> {
                long firstSavedAt = savedFruitStore.getSavedAt(first.getStableId(this));
                long secondSavedAt = savedFruitStore.getSavedAt(second.getStableId(this));
                return sortMode == SORT_NEWEST
                        ? Long.compare(secondSavedAt, firstSavedAt)
                        : Long.compare(firstSavedAt, secondSavedAt);
            });
        }

        adapter.updateFruits(savedFruits);
        int count = savedFruits.size();
        boolean hasSavedFruits = count > 0;
        recyclerView.setVisibility(hasSavedFruits ? View.VISIBLE : View.GONE);
        resultCountText.setVisibility(hasSavedFruits ? View.VISIBLE : View.GONE);
        sortLabel.setVisibility(hasSavedFruits ? View.VISIBLE : View.GONE);
        emptyState.setVisibility(hasSavedFruits ? View.GONE : View.VISIBLE);
        resultCountText.setText(getResources().getQuantityString(
                R.plurals.saved_fruit_count,
                count,
                count));
    }

    private void showSortDialog() {
        CharSequence[] options = {
                getString(R.string.saved_sort_option_default),
                getString(R.string.saved_sort_option_newest),
                getString(R.string.saved_sort_option_oldest)
        };
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.saved_sort_title)
                .setSingleChoiceItems(options, sortMode, (dialog, which) -> {
                    sortMode = which;
                    updateSortLabel();
                    refreshSavedFruits();
                    dialog.dismiss();
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void updateSortLabel() {
        int labelResId;
        if (sortMode == SORT_NEWEST) {
            labelResId = R.string.saved_sort_newest;
        } else if (sortMode == SORT_OLDEST) {
            labelResId = R.string.saved_sort_oldest;
        } else {
            labelResId = R.string.saved_sort_default;
        }
        sortLabel.setText(labelResId);
        sortLabel.setContentDescription(getString(labelResId));
    }

    private void openHome() {
        Intent intent = new Intent(this, App_page3.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra(
                AppContracts.EXTRA_LEVEL,
                AppSettings.getDiabetesType(this).getCode());
        startActivity(intent);
    }
}
