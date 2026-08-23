package com.example.project;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import com.google.android.material.card.MaterialCardView;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App_page4 extends BaseActivity {

    private static final Pattern LEADING_METRIC_PATTERN = Pattern.compile(
            "^\\s*([0-9]+(?:[.]\\d+)?(?:\\s*[-–]\\s*[0-9]+(?:[.]\\d+)?)?)\\s*(\\([^)]*\\))?");

    private TextView fruitNameTextView;
    private TextView categoryTextView;
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
    private MaterialCardView safetyContainer;
    private View safetyDot;
    private View impactDot;
    private View recommendationGroup;
    private View generalAdviceGroup;
    private View tip1Row;
    private View tip2Row;
    private View tip3Row;
    private ImageView fruitImageView;
    private ImageButton bookmarkButton;
    private SavedFruitStore savedFruitStore;
    private String fruitId;
    private String fruitName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_page4);

        fruitNameTextView = findViewById(R.id.detail_name);
        categoryTextView = findViewById(R.id.detail_category);
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
        safetyContainer = findViewById(R.id.detailSafetyContainer);
        safetyDot = findViewById(R.id.detailSafetyDot);
        impactDot = findViewById(R.id.detailImpactDot);
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
        bindOptionalText(
                categoryTextView,
                payload == null ? null : payload[FruitDetailPayload.CATEGORY]);
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
        String impactLabel = detailOrUnavailable(
                payload, FruitDetailPayload.IMPACT, intent, AppContracts.EXTRA_FRUIT_IMPACT);
        impactLevelTextView.setText(withoutStatusMarker(impactLabel));
        applyStatusAppearance(null, impactDot, impactLevelTextView, FruitSafety.fromLabel(impactLabel));
        type1AdviceTextView.setText(detailOrUnavailable(
                payload, FruitDetailPayload.TYPE_1, intent, AppContracts.EXTRA_FRUIT_TYPE_1));
        type2AdviceTextView.setText(detailOrUnavailable(
                payload, FruitDetailPayload.TYPE_2, intent, AppContracts.EXTRA_FRUIT_TYPE_2));
        String safetyLabel = detailOrUnavailable(
                payload, FruitDetailPayload.SAFETY, intent, AppContracts.EXTRA_FRUIT_SAFETY);
        safetyTextView.setText(withoutStatusMarker(safetyLabel));
        applyStatusAppearance(
                safetyContainer, safetyDot, safetyTextView, FruitSafety.fromLabel(safetyLabel));
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
        View bookmarkButtonContainer = findViewById(R.id.detailBookmarkContainer);
        bookmarkButtonContainer.setVisibility(hasText(fruitId) ? View.VISIBLE : View.GONE);
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

        findViewById(R.id.detailBackButton).setOnClickListener(view -> finish());
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
        bookmarkButton.setImageResource(
                isSaved ? R.drawable.ic_bookmark_filled : R.drawable.ic_bookmark_outline);
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
        String plainLevel = hasText(level)
                ? level.substring(1, level.length() - 1).trim()
                : null;
        return hasText(plainLevel)
                ? getString(R.string.detail_metric_value_with_note, matcher.group(1), plainLevel)
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

    private String withoutStatusMarker(String value) {
        return value
                .replace("\uD83D\uDFE2", "")
                .replace("\uD83D\uDFE1", "")
                .replace("\uD83D\uDD34", "")
                .trim();
    }

    private void applyStatusAppearance(MaterialCardView container, View dot, TextView label,
            FruitSafety.Level level) {
        int backgroundColor;
        int foregroundColor;
        switch (level) {
            case SAFE:
                backgroundColor = R.color.detail_status_safe_background;
                foregroundColor = R.color.detail_status_safe_foreground;
                break;
            case LIMIT:
                backgroundColor = R.color.detail_status_limit_background;
                foregroundColor = R.color.detail_status_limit_foreground;
                break;
            case AVOID:
                backgroundColor = R.color.detail_status_avoid_background;
                foregroundColor = R.color.detail_status_avoid_foreground;
                break;
            case UNKNOWN:
            default:
                backgroundColor = R.color.detail_status_unknown_background;
                foregroundColor = R.color.detail_status_unknown_foreground;
                break;
        }
        int resolvedForeground = ContextCompat.getColor(this, foregroundColor);
        if (container != null) {
            container.setCardBackgroundColor(ContextCompat.getColor(this, backgroundColor));
        }
        dot.setBackgroundTintList(ColorStateList.valueOf(resolvedForeground));
        label.setTextColor(resolvedForeground);
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
