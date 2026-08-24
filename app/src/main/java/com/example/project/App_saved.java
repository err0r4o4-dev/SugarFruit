package com.example.project;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

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
        sortLabel.setOnClickListener(view -> showSortBottomSheet());

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

    private void showSortBottomSheet() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View contentView = getLayoutInflater().inflate(R.layout.bottom_sheet_fruit_sort, null);
        RadioGroup sortOptions = contentView.findViewById(R.id.sortOptions);

        ((TextView) contentView.findViewById(R.id.sortSheetTitle))
                .setText(R.string.saved_sort_title);
        ((TextView) contentView.findViewById(R.id.sortSheetDescription))
                .setText(R.string.saved_sort_description);
        ((TextView) contentView.findViewById(R.id.sortOptionPrimary))
                .setText(R.string.saved_sort_option_default);
        ((TextView) contentView.findViewById(R.id.sortOptionSecondary))
                .setText(R.string.saved_sort_option_newest);
        ((TextView) contentView.findViewById(R.id.sortOptionTertiary))
                .setText(R.string.saved_sort_option_oldest);

        if (sortMode == SORT_NEWEST) {
            sortOptions.check(R.id.sortOptionSecondary);
        } else if (sortMode == SORT_OLDEST) {
            sortOptions.check(R.id.sortOptionTertiary);
        } else {
            sortOptions.check(R.id.sortOptionPrimary);
        }

        sortOptions.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.sortOptionSecondary) {
                sortMode = SORT_NEWEST;
            } else if (checkedId == R.id.sortOptionTertiary) {
                sortMode = SORT_OLDEST;
            } else {
                sortMode = SORT_DEFAULT;
            }
            updateSortLabel();
            refreshSavedFruits();
            dialog.dismiss();
        });

        dialog.setContentView(contentView);
        dialog.setDismissWithAnimation(true);
        dialog.show();
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
        BottomNavigationCoordinator.navigate(
                this,
                BottomNavigationCoordinator.Destination.HOME);
    }
}
