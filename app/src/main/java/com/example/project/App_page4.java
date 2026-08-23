package com.example.project;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App_page4 extends BaseActivity {

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
    private MaterialButton bookmarkButton;
    private SavedFruitStore savedFruitStore;
    private String fruitId;
    private String fruitName;

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
        String[] payload = payloadForCurrentLocale(intent);
        fruitName = detailOrUnavailable(
                payload, FruitDetailPayload.NAME, intent, AppContracts.EXTRA_FRUIT_NAME);
        fruitNameTextView.setText(fruitName);
        glycemicIndexTextView.setText(formatGlycemicIndex(
                detailOrUnavailable(
                        payload, FruitDetailPayload.INDEX,
                        intent, AppContracts.EXTRA_FRUIT_INDEX)));
        carbohydrateContentTextView.setText(formatNutrientMetric(
                detailOrUnavailable(
                        payload, FruitDetailPayload.CARBOHYDRATE,
                        intent, AppContracts.EXTRA_FRUIT_CARBOHYDRATE)));
        fiberContentTextView.setText(formatNutrientMetric(
                detailOrUnavailable(
                        payload, FruitDetailPayload.FIBER,
                        intent, AppContracts.EXTRA_FRUIT_FIBER)));
        impactLevelTextView.setText(detailOrUnavailable(
                payload, FruitDetailPayload.IMPACT, intent, AppContracts.EXTRA_FRUIT_IMPACT));
        type1AdviceTextView.setText(detailOrUnavailable(
                payload, FruitDetailPayload.TYPE_1, intent, AppContracts.EXTRA_FRUIT_TYPE_1));
        type2AdviceTextView.setText(detailOrUnavailable(
                payload, FruitDetailPayload.TYPE_2, intent, AppContracts.EXTRA_FRUIT_TYPE_2));
        safetyTextView.setText(detailOrUnavailable(
                payload, FruitDetailPayload.SAFETY, intent, AppContracts.EXTRA_FRUIT_SAFETY));
        bindOptionalText(
                introductionTextView,
                optionalDetail(
                        payload, FruitDetailPayload.INTRODUCTION,
                        intent, AppContracts.EXTRA_FRUIT_INTRODUCTION));

        boolean hasRecommendedAmount = bindOptionalText(
                recommendedAmountTextView,
                optionalDetail(
                        payload, FruitDetailPayload.RECOMMENDED_AMOUNT,
                        intent, AppContracts.EXTRA_FRUIT_RECOMMENDED_AMOUNT));
        boolean hasRecommendedEquivalent = bindOptionalText(
                recommendedEquivalentTextView,
                optionalDetail(
                        payload, FruitDetailPayload.RECOMMENDED_EQUIVALENT,
                        intent, AppContracts.EXTRA_FRUIT_RECOMMENDED_EQUIVALENT));
        recommendationGroup.setVisibility(
                hasRecommendedAmount || hasRecommendedEquivalent ? View.VISIBLE : View.GONE);

        String firstTip = optionalDetail(
                payload, FruitDetailPayload.TIP_1, intent, AppContracts.EXTRA_FRUIT_TIP_1);
        if (!hasText(firstTip)) {
            firstTip = optionalDetail(
                    payload, FruitDetailPayload.GENERAL_ADVICE,
                    intent, AppContracts.EXTRA_FRUIT_END);
        }
        boolean hasTip1 = bindTip(generalAdviceTextView, tip1Row, firstTip);
        boolean hasTip2 = bindTip(
                tip2TextView,
                tip2Row,
                optionalDetail(
                        payload, FruitDetailPayload.TIP_2,
                        intent, AppContracts.EXTRA_FRUIT_TIP_2));
        boolean hasTip3 = bindTip(
                tip3TextView,
                tip3Row,
                optionalDetail(
                        payload, FruitDetailPayload.TIP_3,
                        intent, AppContracts.EXTRA_FRUIT_TIP_3));
        generalAdviceGroup.setVisibility(
                hasTip1 || hasTip2 || hasTip3 ? View.VISIBLE : View.GONE);

        int imageResource = intent.getIntExtra(AppContracts.EXTRA_FRUIT_IMAGE, 0);
        fruitImageView.setImageResource(isDrawableResource(imageResource) ? imageResource : R.drawable.logo);
        fruitImageView.setContentDescription(getString(R.string.fruit_image_description, fruitName));

        fruitId = intent.getStringExtra(AppContracts.EXTRA_FRUIT_ID);
        if (!hasText(fruitId) && isDrawableResource(imageResource)) {
            fruitId = getResources().getResourceEntryName(imageResource);
        }
        savedFruitStore = new SavedFruitStore(this);
        bookmarkButton = findViewById(R.id.buttonBookmarkDetail);
        bookmarkButton.setVisibility(hasText(fruitId) ? View.VISIBLE : View.GONE);
        if (hasText(fruitId)) {
            bindBookmarkButton();
            bookmarkButton.setOnClickListener(view -> {
                boolean isSaved = savedFruitStore.toggle(fruitId);
                bindBookmarkButton();
                Toast.makeText(
                        this,
                        getString(
                                isSaved ? R.string.fruit_saved_message : R.string.fruit_removed_message,
                                fruitName),
                        Toast.LENGTH_SHORT).show();
            });
        }

        findViewById(R.id.button_Next).setOnClickListener(view -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bookmarkButton != null && hasText(fruitId)) {
            bindBookmarkButton();
        }
    }

    private void bindBookmarkButton() {
        boolean isSaved = savedFruitStore.isSaved(fruitId);
        bookmarkButton.setSelected(isSaved);
        bookmarkButton.setIconResource(
                isSaved ? R.drawable.ic_bookmark_filled : R.drawable.ic_bookmark_outline);
        bookmarkButton.setText(isSaved ? R.string.saved_fruit : R.string.save_fruit);
        bookmarkButton.setContentDescription(getString(
                isSaved
                        ? R.string.remove_fruit_from_saved_description
                        : R.string.save_fruit_description,
                fruitName));
    }

    private String[] payloadForCurrentLocale(Intent intent) {
        boolean isEnglish = Locale.ENGLISH.getLanguage().equals(
                getResources().getConfiguration().getLocales().get(0).getLanguage());
        String key = isEnglish
                ? AppContracts.EXTRA_FRUIT_DETAILS_EN
                : AppContracts.EXTRA_FRUIT_DETAILS_TH;
        String[] payload = intent.getStringArrayExtra(key);
        return payload != null && payload.length == FruitDetailPayload.SIZE ? payload : null;
    }

    private String detailOrUnavailable(String[] payload, int index, Intent intent, String oldKey) {
        String value = optionalDetail(payload, index, intent, oldKey);
        return hasText(value) ? value : getString(R.string.detail_unavailable);
    }

    private String optionalDetail(String[] payload, int index, Intent intent, String oldKey) {
        return payload == null ? intent.getStringExtra(oldKey) : payload[index];
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
