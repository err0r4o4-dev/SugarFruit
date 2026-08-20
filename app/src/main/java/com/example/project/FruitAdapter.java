package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.DiffUtil;
import java.util.ArrayList;
import java.util.List;
import android.content.Intent;

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.FruitViewHolder> {

    List<Fruit> fruitList;
    String level;

    public FruitAdapter(List<Fruit> fruits, String level) {
        this.fruitList = new ArrayList<>(fruits);
        this.level = level;
    }

    public static class FruitViewHolder extends RecyclerView.ViewHolder {
        TextView fruitName, fruitIndex, fruiTrue, fruitSugar;
        ImageView imageView;
        Button buttonNext;
        public FruitViewHolder(View itemView) {
            super(itemView);
            fruitName = itemView.findViewById(R.id.fruitName);
            fruitIndex = itemView.findViewById(R.id.fruitIndex);
            fruitSugar = itemView.findViewById(R.id.fruitSugar);
            fruiTrue = itemView.findViewById(R.id.fruiTrue);
            imageView = itemView.findViewById(R.id.imageView4);
            buttonNext = itemView.findViewById(R.id.button_Next);

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
        String safety = FruitSafety.forDiabetesLevel(f, level);
        holder.fruiTrue.setText(safety);
        holder.imageView.setImageResource(f.getImageResId());
        holder.imageView.setContentDescription(
                holder.itemView.getContext().getString(R.string.fruit_image_description, f.getName()));
        holder.buttonNext.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), App_page4.class);
            intent.putExtra(AppContracts.EXTRA_FRUIT_NAME, f.getName());
            intent.putExtra(AppContracts.EXTRA_FRUIT_INDEX, f.getIndex_());
            intent.putExtra(AppContracts.EXTRA_FRUIT_SUGAR, f.getSugar_());
            intent.putExtra(AppContracts.EXTRA_FRUIT_CARBOHYDRATE, f.getCarbohydrate_());
            intent.putExtra(AppContracts.EXTRA_FRUIT_FIBER, f.getFiber_());
            intent.putExtra(AppContracts.EXTRA_FRUIT_IMPACT, f.getImpact_());
            intent.putExtra(AppContracts.EXTRA_FRUIT_TYPE_1, f.getType1_());
            intent.putExtra(AppContracts.EXTRA_FRUIT_TYPE_2, f.getType2_());
            intent.putExtra(AppContracts.EXTRA_FRUIT_END, f.getEnd_());
            intent.putExtra(AppContracts.EXTRA_FRUIT_IMAGE, f.getImageResId());
            intent.putExtra(AppContracts.EXTRA_FRUIT_SAFETY, safety);
            intent.putExtra(AppContracts.EXTRA_LEVEL, level);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return fruitList.size();
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

}
