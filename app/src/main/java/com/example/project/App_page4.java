package com.example.project;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App_page4 extends AppCompatActivity {

    private static final Pattern LEADING_METRIC_PATTERN = Pattern.compile(
            "^\\s*([0-9]+(?:[.]\\d+)?(?:\\s*[-–]\\s*[0-9]+(?:[.]\\d+)?)?)\\s*(\\([^)]*\\))?");

    private TextView fruitNameTextView;
    private TextView glycemicIndexTextView;
    private TextView carbohydrateContentTextView;
    private TextView fiberContentTextView;
    private TextView impactLevelTextView;
    private TextView type1AdviceTextView;
    private TextView type2AdviceTextView;
    private TextView generalAdviceTextView;
    private TextView introductionTextView;
    private TextView recommendedAmountTextView;
    private TextView recommendedEquivalentTextView;
    private TextView tip2TextView;
    private TextView tip3TextView;
    private TextView safetyTextView;
    private View recommendationGroup;
    private View generalAdviceGroup;
    private View tip1Row;
    private View tip2Row;
    private View tip3Row;
    private ImageView fruitImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_page4);

        fruitNameTextView = findViewById(R.id.detail_name);
        glycemicIndexTextView = findViewById(R.id.detail_index_);
        carbohydrateContentTextView = findViewById(R.id.detail_carbohydrate_);
        fiberContentTextView = findViewById(R.id.detail_fiber_);
        impactLevelTextView = findViewById(R.id.detail_impact_);
        type1AdviceTextView = findViewById(R.id.detail_type1_);
        type2AdviceTextView = findViewById(R.id.detail_type2_);
        generalAdviceTextView = findViewById(R.id.detail_end_);
        introductionTextView = findViewById(R.id.detail_introduction);
        recommendedAmountTextView = findViewById(R.id.detail_recommended_amount);
        recommendedEquivalentTextView = findViewById(R.id.detail_recommended_equivalent);
        tip2TextView = findViewById(R.id.detail_tip_2);
        tip3TextView = findViewById(R.id.detail_tip_3);
        safetyTextView = findViewById(R.id.detail_safety);
        recommendationGroup = findViewById(R.id.detail_recommendation_group);
        generalAdviceGroup = findViewById(R.id.detail_general_advice_group);
        tip1Row = findViewById(R.id.detail_tip_1_row);
        tip2Row = findViewById(R.id.detail_tip_2_row);
        tip3Row = findViewById(R.id.detail_tip_3_row);
        fruitImageView = findViewById(R.id.detail_image);

        Intent intent = getIntent();
        String fruitName = extraOrUnavailable(intent, AppContracts.EXTRA_FRUIT_NAME);
        fruitNameTextView.setText(fruitName);
        glycemicIndexTextView.setText(formatGlycemicIndex(
                extraOrUnavailable(intent, AppContracts.EXTRA_FRUIT_INDEX)));
        carbohydrateContentTextView.setText(formatNutrientMetric(
                extraOrUnavailable(intent, AppContracts.EXTRA_FRUIT_CARBOHYDRATE)));
        fiberContentTextView.setText(formatNutrientMetric(
                extraOrUnavailable(intent, AppContracts.EXTRA_FRUIT_FIBER)));
        impactLevelTextView.setText(extraOrUnavailable(intent, AppContracts.EXTRA_FRUIT_IMPACT));
        type1AdviceTextView.setText(extraOrUnavailable(intent, AppContracts.EXTRA_FRUIT_TYPE_1));
        type2AdviceTextView.setText(extraOrUnavailable(intent, AppContracts.EXTRA_FRUIT_TYPE_2));
        safetyTextView.setText(extraOrUnavailable(intent, AppContracts.EXTRA_FRUIT_SAFETY));
        bindOptionalText(
                introductionTextView,
                intent.getStringExtra(AppContracts.EXTRA_FRUIT_INTRODUCTION));

        boolean hasRecommendedAmount = bindOptionalText(
                recommendedAmountTextView,
                intent.getStringExtra(AppContracts.EXTRA_FRUIT_RECOMMENDED_AMOUNT));
        boolean hasRecommendedEquivalent = bindOptionalText(
                recommendedEquivalentTextView,
                intent.getStringExtra(AppContracts.EXTRA_FRUIT_RECOMMENDED_EQUIVALENT));
        recommendationGroup.setVisibility(
                hasRecommendedAmount || hasRecommendedEquivalent ? View.VISIBLE : View.GONE);

        String firstTip = intent.getStringExtra(AppContracts.EXTRA_FRUIT_TIP_1);
        if (!hasText(firstTip)) {
            firstTip = intent.getStringExtra(AppContracts.EXTRA_FRUIT_END);
        }
        boolean hasTip1 = bindTip(generalAdviceTextView, tip1Row, firstTip);
        boolean hasTip2 = bindTip(
                tip2TextView,
                tip2Row,
                intent.getStringExtra(AppContracts.EXTRA_FRUIT_TIP_2));
        boolean hasTip3 = bindTip(
                tip3TextView,
                tip3Row,
                intent.getStringExtra(AppContracts.EXTRA_FRUIT_TIP_3));
        generalAdviceGroup.setVisibility(
                hasTip1 || hasTip2 || hasTip3 ? View.VISIBLE : View.GONE);

        int imageResource = intent.getIntExtra(AppContracts.EXTRA_FRUIT_IMAGE, 0);
        fruitImageView.setImageResource(isDrawableResource(imageResource) ? imageResource : R.drawable.logo);
        fruitImageView.setContentDescription(getString(R.string.fruit_image_description, fruitName));

        findViewById(R.id.button_Next).setOnClickListener(view -> finish());
    }

    private String extraOrUnavailable(Intent intent, String key) {
        String value = intent.getStringExtra(key);
        return hasText(value) ? value : getString(R.string.detail_unavailable);
    }

    private boolean bindOptionalText(TextView textView, String value) {
        boolean isAvailable = hasText(value);
        textView.setVisibility(isAvailable ? View.VISIBLE : View.GONE);
        if (isAvailable) {
            textView.setText(value);
        }
        return isAvailable;
    }

    private boolean bindTip(TextView textView, View row, String value) {
        boolean isAvailable = hasText(value);
        row.setVisibility(isAvailable ? View.VISIBLE : View.GONE);
        if (isAvailable) {
            textView.setText(value);
        }
        return isAvailable;
    }

    private String formatGlycemicIndex(String value) {
        Matcher matcher = LEADING_METRIC_PATTERN.matcher(value);
        if (!matcher.find()) {
            return value;
        }
        String level = matcher.group(2);
        return hasText(level)
                ? getString(R.string.detail_metric_value_with_note, matcher.group(1), level)
                : matcher.group(1);
    }

    private String formatNutrientMetric(String value) {
        Matcher matcher = LEADING_METRIC_PATTERN.matcher(value);
        if (!matcher.find()) {
            return value;
        }
        String grams = getString(R.string.detail_metric_grams, matcher.group(1));
        boolean isPerHundredGrams = value.contains("/100") || value.contains("ต่อ 100");
        return isPerHundredGrams
                ? getString(
                        R.string.detail_metric_value_with_note,
                        grams,
                        getString(R.string.detail_metric_per_100_grams))
                : grams;
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
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
