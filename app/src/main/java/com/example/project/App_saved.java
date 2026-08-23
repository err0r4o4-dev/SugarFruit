package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class App_saved extends BaseActivity {
    private SavedFruitStore savedFruitStore;
    private List<Fruit> allFruits;
    private FruitAdapter adapter;
    private RecyclerView recyclerView;
    private View emptyState;
    private TextView resultCountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_saved);

        savedFruitStore = new SavedFruitStore(this);
        allFruits = App_page3.createFruitData(this);
        recyclerView = findViewById(R.id.savedRecyclerView);
        emptyState = findViewById(R.id.savedEmptyState);
        resultCountText = findViewById(R.id.savedResultCount);

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

        adapter.updateFruits(savedFruits);
        int count = savedFruits.size();
        boolean hasSavedFruits = count > 0;
        recyclerView.setVisibility(hasSavedFruits ? View.VISIBLE : View.GONE);
        resultCountText.setVisibility(hasSavedFruits ? View.VISIBLE : View.GONE);
        emptyState.setVisibility(hasSavedFruits ? View.GONE : View.VISIBLE);
        resultCountText.setText(getResources().getQuantityString(
                R.plurals.saved_fruit_count,
                count,
                count));
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
