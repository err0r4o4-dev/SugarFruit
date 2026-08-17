package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.content.Intent;

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.FruitViewHolder> {

    List<Fruit> fruitList;
    String level;

    public FruitAdapter(List<Fruit> fruits, String level) {
        this.fruitList = fruits;
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
        Fruit f = fruitList.get(holder.getAdapterPosition());
        holder.fruitName.setText(f.getName());
        holder.fruitIndex.setText(f.getIndex());
        holder.fruitSugar.setText(f.getSugar());
        if ("เบาหวานชนิดที่ 1".equals(level)) {
            holder.fruiTrue.setText(f.getLevel1());
        }else if ("เบาหวานชนิดที่ 2".equals(level)) {
            holder.fruiTrue.setText(f.getLevel2());
        }else if ("เบาหวานขณะตั้งครรภ์".equals(level)) {
            holder.fruiTrue.setText(f.getLevel3());
        }else {
            holder.fruiTrue.setText(f.getTrue());
        }
        holder.imageView.setImageResource(f.getImageResId());
        holder.buttonNext.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), App_page4.class);
            intent.putExtra("fruitName", f.getName());
            intent.putExtra("fruitIndex_", f.getIndex_());
            intent.putExtra("fruitSugar_", f.getSugar_());
            intent.putExtra("fruitCarbohydrate_", f.getCarbohydrate_());
            intent.putExtra("fruitFiber_", f.getFiber_());
            intent.putExtra("fruitImpact_", f.getImpact_());
            intent.putExtra("fruitType1_", f.getType1_());
            intent.putExtra("fruitType2_", f.getType2_());
            intent.putExtra("fruitEnd_", f.getEnd_());
            intent.putExtra("fruitImage", f.getImageResId());
            intent.putExtra("level", level);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return fruitList.size();
    }

}
