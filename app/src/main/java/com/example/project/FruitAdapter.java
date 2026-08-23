package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.DiffUtil;
import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.ColorStateList;
import androidx.core.content.ContextCompat;
import java.util.Locale;

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.FruitViewHolder> {

    List<Fruit> fruitList;
    String level;
    private final SavedFruitStore savedFruitStore;
    private final OnSavedStateChangedListener savedStateChangedListener;

    public interface OnSavedStateChangedListener {
        void onSavedStateChanged(Fruit fruit, boolean isSaved);
    }

    public FruitAdapter(List<Fruit> fruits, String level) {
        this(fruits, level, null, null);
    }

    public FruitAdapter(List<Fruit> fruits, String level, SavedFruitStore savedFruitStore,
            OnSavedStateChangedListener savedStateChangedListener) {
        this.fruitList = new ArrayList<>(fruits);
        this.level = level;
        this.savedFruitStore = savedFruitStore;
        this.savedStateChangedListener = savedStateChangedListener;
    }

    public static class FruitViewHolder extends RecyclerView.ViewHolder {
        TextView fruitName, fruitIndex, fruiTrue, fruitSugar;
        ImageView imageView;
        View statusDot;
        ImageButton bookmarkButton;
        Button buttonNext;
        public FruitViewHolder(View itemView) {
            super(itemView);
            fruitName = itemView.findViewById(R.id.fruitName);
            fruitIndex = itemView.findViewById(R.id.fruitIndex);
            fruitSugar = itemView.findViewById(R.id.fruitSugar);
            fruiTrue = itemView.findViewById(R.id.fruiTrue);
            imageView = itemView.findViewById(R.id.imageView4);
            statusDot = itemView.findViewById(R.id.fruitStatusDot);
            buttonNext = itemView.findViewById(R.id.button_Next);
            bookmarkButton = itemView.findViewById(R.id.buttonBookmark);

        }
    }

    @Override
    public FruitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fruit, parent, false);
        return new FruitViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FruitViewHolder holder, int position) {
        Fruit f = fruitList.get(position);
        holder.fruitName.setText(f.getName());
        holder.fruitIndex.setText(f.getIndex());
        holder.fruitSugar.setText(f.getSugar());
        FruitSafety.Level safetyLevel = FruitSafety.forDiabetesLevel(f, level);
        String safetyLabel = FruitSafety.plainLocalizedLabel(holder.itemView.getContext(), safetyLevel);
        holder.fruiTrue.setText(safetyLabel);
        holder.statusDot.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(
                holder.itemView.getContext(), statusColor(safetyLevel))));
        holder.imageView.setImageResource(f.getImageResId());
        holder.imageView.setContentDescription(
                holder.itemView.getContext().getString(R.string.fruit_image_description, f.getName()));
        String fruitId = f.getStableId(holder.itemView.getContext());
        if (savedFruitStore == null) {
            holder.bookmarkButton.setVisibility(View.GONE);
        } else {
            holder.bookmarkButton.setVisibility(View.VISIBLE);
            bindBookmarkButton(holder, f, fruitId);
            holder.bookmarkButton.setOnClickListener(view -> {
                boolean isSaved = savedFruitStore.toggle(fruitId);
                bindBookmarkButton(holder, f, fruitId);
                if (savedStateChangedListener != null) {
                    savedStateChangedListener.onSavedStateChanged(f, isSaved);
                }
            });
        }
        holder.buttonNext.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), App_page4.class);
            Context thaiContext = localizedContext(v.getContext(), new Locale("th", "TH"));
            Context englishContext = localizedContext(v.getContext(), Locale.ENGLISH);
            intent.putExtra(
                    AppContracts.EXTRA_FRUIT_DETAILS_TH,
                    f.createDetailPayload(
                            false,
                            FruitSafety.localizedLabel(thaiContext, safetyLevel)));
            intent.putExtra(
                    AppContracts.EXTRA_FRUIT_DETAILS_EN,
                    f.createDetailPayload(
                            true,
                            FruitSafety.localizedLabel(englishContext, safetyLevel)));
            intent.putExtra(AppContracts.EXTRA_FRUIT_IMAGE, f.getImageResId());
            intent.putExtra(AppContracts.EXTRA_FRUIT_ID, fruitId);
            intent.putExtra(AppContracts.EXTRA_LEVEL, level);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return fruitList.size();
    }

    private void bindBookmarkButton(FruitViewHolder holder, Fruit fruit, String fruitId) {
        boolean isSaved = savedFruitStore.isSaved(fruitId);
        holder.bookmarkButton.setSelected(isSaved);
        holder.bookmarkButton.setImageResource(
                isSaved ? R.drawable.ic_bookmark_filled : R.drawable.ic_bookmark_outline);
        holder.bookmarkButton.setContentDescription(holder.itemView.getContext().getString(
                isSaved
                        ? R.string.remove_fruit_from_saved_description
                        : R.string.save_fruit_description,
                fruit.getName()));
    }

    private int statusColor(FruitSafety.Level safetyLevel) {
        switch (safetyLevel) {
            case SAFE:
                return R.color.health_safe;
            case LIMIT:
                return R.color.detail_status_limit_foreground;
            case AVOID:
                return R.color.detail_status_avoid_foreground;
            case UNKNOWN:
            default:
                return R.color.detail_status_unknown_foreground;
        }
    }

    private Context localizedContext(Context context, Locale locale) {
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }

    public void updateFruits(List<Fruit> newFruits) {
        List<Fruit> oldFruits = new ArrayList<>(fruitList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldFruits.size();
            }

            @Override
            public int getNewListSize() {
                return newFruits.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return oldFruits.get(oldItemPosition).getImageResId()
                        == newFruits.get(newItemPosition).getImageResId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return oldFruits.get(oldItemPosition) == newFruits.get(newItemPosition);
            }
        });
        fruitList.clear();
        fruitList.addAll(newFruits);
        result.dispatchUpdatesTo(this);
    }

    public void setDiabetesLevel(String newLevel) {
        level = newLevel == null ? DiabetesType.UNKNOWN.getCode() : newLevel;
        if (getItemCount() > 0) {
            notifyItemRangeChanged(0, getItemCount());
        }
    }

    public void refreshSavedState() {
        if (getItemCount() > 0) {
            notifyItemRangeChanged(0, getItemCount());
        }
    }

}
