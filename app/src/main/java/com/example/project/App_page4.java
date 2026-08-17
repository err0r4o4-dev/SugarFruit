package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class App_page4 extends AppCompatActivity {

    private TextView fruitNameTextView;
    private TextView glycemicIndexTextView;
    private TextView sugarContentTextView;
    private TextView carbohydrateContentTextView;
    private TextView fiberContentTextView;
    private TextView impactLevelTextView;
    private TextView type1AdviceTextView;
    private TextView type2AdviceTextView;
    private TextView generalAdviceTextView;
    private ImageView fruitImageView;
    private String level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_page4);

        fruitNameTextView = findViewById(R.id.detail_name);
        glycemicIndexTextView = findViewById(R.id.detail_index_);
        sugarContentTextView = findViewById(R.id.detail_sugar_);
        carbohydrateContentTextView = findViewById(R.id.detail_carbohydrate_);
        fiberContentTextView = findViewById(R.id.detail_fiber_);
        impactLevelTextView = findViewById(R.id.detail_impact_);
        type1AdviceTextView = findViewById(R.id.detail_type1_);
        type2AdviceTextView = findViewById(R.id.detail_type2_);
        generalAdviceTextView = findViewById(R.id.detail_end_);
        fruitImageView = findViewById(R.id.detail_image);

        Intent intent = getIntent();
        level = intent.getStringExtra("level");
        fruitNameTextView.setText(intent.getStringExtra("fruitName"));
        glycemicIndexTextView.setText(intent.getStringExtra("fruitIndex_"));
        sugarContentTextView.setText(intent.getStringExtra("fruitSugar_"));
        carbohydrateContentTextView.setText(intent.getStringExtra("fruitCarbohydrate_"));
        fiberContentTextView.setText(intent.getStringExtra("fruitFiber_"));
        impactLevelTextView.setText(intent.getStringExtra("fruitImpact_"));
        type1AdviceTextView.setText(intent.getStringExtra("fruitType1_"));
        type2AdviceTextView.setText(intent.getStringExtra("fruitType2_"));
        generalAdviceTextView.setText(intent.getStringExtra("fruitEnd_"));
        int defaultImageResource = 0;
        fruitImageView.setImageResource(intent.getIntExtra("fruitImage", defaultImageResource));

        Button button_Next = findViewById(R.id.button_Next);
        button_Next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(App_page4.this, App_page3.class);
                intent.putExtra("level", level);
                startActivity(intent);
            }
        });
    }

}