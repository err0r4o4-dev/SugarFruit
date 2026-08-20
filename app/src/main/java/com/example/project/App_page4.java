package com.example.project;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
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
    private TextView safetyTextView;
    private ImageView fruitImageView;

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
        safetyTextView = findViewById(R.id.detail_safety);
        fruitImageView = findViewById(R.id.detail_image);

        Intent intent = getIntent();
        String fruitName = extraOrUnavailable(intent, AppContracts.EXTRA_FRUIT_NAME);
        fruitNameTextView.setText(fruitName);
        glycemicIndexTextView.setText(extraOrUnavailable(intent, AppContracts.EXTRA_FRUIT_INDEX));
        sugarContentTextView.setText(extraOrUnavailable(intent, AppContracts.EXTRA_FRUIT_SUGAR));
        carbohydrateContentTextView.setText(
                extraOrUnavailable(intent, AppContracts.EXTRA_FRUIT_CARBOHYDRATE));
        fiberContentTextView.setText(extraOrUnavailable(intent, AppContracts.EXTRA_FRUIT_FIBER));
        impactLevelTextView.setText(extraOrUnavailable(intent, AppContracts.EXTRA_FRUIT_IMPACT));
        type1AdviceTextView.setText(extraOrUnavailable(intent, AppContracts.EXTRA_FRUIT_TYPE_1));
        type2AdviceTextView.setText(extraOrUnavailable(intent, AppContracts.EXTRA_FRUIT_TYPE_2));
        generalAdviceTextView.setText(extraOrUnavailable(intent, AppContracts.EXTRA_FRUIT_END));
        safetyTextView.setText(extraOrUnavailable(intent, AppContracts.EXTRA_FRUIT_SAFETY));

        int imageResource = intent.getIntExtra(AppContracts.EXTRA_FRUIT_IMAGE, 0);
        fruitImageView.setImageResource(isDrawableResource(imageResource) ? imageResource : R.drawable.logo);
        fruitImageView.setContentDescription(getString(R.string.fruit_image_description, fruitName));

        findViewById(R.id.button_Next).setOnClickListener(view -> finish());
    }

    private String extraOrUnavailable(Intent intent, String key) {
        String value = intent.getStringExtra(key);
        return value == null || value.trim().isEmpty() ? getString(R.string.detail_unavailable) : value;
    }

    private boolean isDrawableResource(int resourceId) {
        if (resourceId == 0) {
            return false;
        }
        try {
            String type = getResources().getResourceTypeName(resourceId);
            return "drawable".equals(type) || "mipmap".equals(type);
        } catch (Resources.NotFoundException exception) {
            return false;
        }
    }
}
