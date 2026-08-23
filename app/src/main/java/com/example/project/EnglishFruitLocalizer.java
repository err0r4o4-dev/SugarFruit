package com.example.project;

import android.content.Context;
import android.content.res.Configuration;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class EnglishFruitLocalizer {
    private static final Pattern LEADING_NUMBER_PATTERN = Pattern.compile(
            "^\\s*([0-9]+(?:[.]\\d+)?(?:\\s*[-–]\\s*[0-9]+(?:[.]\\d+)?)?)");
    private static final Pattern FIRST_NUMBER_PATTERN = Pattern.compile(
            "([0-9]+(?:[.]\\d+)?(?:\\s*[-–]\\s*[0-9]+(?:[.]\\d+)?)?)");

    private EnglishFruitLocalizer() {
    }

    public static void prepare(Context context, List<Fruit> fruits) {
        Context englishContext = localizedContext(context, Locale.ENGLISH);
        String[] names = englishContext.getResources().getStringArray(R.array.fruit_names_en);
        String[] type1Advice = englishContext.getResources().getStringArray(R.array.fruit_type1_advice_en);
        String[] type2Advice = englishContext.getResources().getStringArray(R.array.fruit_type2_advice_en);
        String[] generalAdvice = englishContext.getResources().getStringArray(R.array.fruit_general_advice_en);
        requireMatchingSize(fruits, names, type1Advice, type2Advice, generalAdvice);

        for (int index = 0; index < fruits.size(); index++) {
            Fruit fruit = fruits.get(index);
            fruit.attachEnglishText(
                    names[index],
                    englishContext.getString(
                            R.string.fruit_sugar_summary_en,
                            firstNumber(fruit.getSugar())),
                    sugarDetail(englishContext, fruit.getSugar_()),
                    englishContext.getString(
                            R.string.fruit_gi_summary_en,
                            firstNumber(fruit.getIndex())),
                    glycemicIndexDetail(englishContext, fruit.getIndex_()),
                    nutrientDetail(englishContext, fruit.getCarbohydrate_(), true),
                    nutrientDetail(englishContext, fruit.getFiber_(), false),
                    index == 0
                            ? englishContext.getString(R.string.mango_detail_impact)
                            : impactLabel(englishContext, fruit.getImpact_()),
                    type1Advice[index],
                    type2Advice[index],
                    generalAdvice[index]);
        }
        attachEnglishDetailGuides(englishContext, fruits);
        if (Locale.ENGLISH.getLanguage().equals(
                context.getResources().getConfiguration().getLocales().get(0).getLanguage())) {
            for (Fruit fruit : fruits) {
                fruit.applyEnglishText();
            }
        }
    }

    private static void requireMatchingSize(List<Fruit> fruits, String[]... arrays) {
        for (String[] array : arrays) {
            if (array.length != fruits.size()) {
                throw new IllegalStateException(
                        "English fruit resources must contain " + fruits.size() + " items");
            }
        }
    }

    private static void attachEnglishDetailGuides(Context context, List<Fruit> fruits) {
        fruits.get(0).withEnglishDetailGuide(
                context.getString(R.string.mango_detail_introduction),
                context.getString(R.string.mango_detail_recommended_amount),
                context.getString(R.string.mango_detail_recommended_equivalent),
                context.getString(R.string.mango_detail_tip_1),
                context.getString(R.string.mango_detail_tip_2),
                context.getString(R.string.mango_detail_tip_3));

        attachGuide(context, fruits.get(1), R.string.banana_detail_evidence,
                R.string.banana_recommended_amount, R.string.banana_detail_note);
        attachGuide(context, fruits.get(2), R.string.watermelon_detail_evidence,
                R.string.watermelon_recommended_amount, 0);
        attachGuide(context, fruits.get(3), R.string.papaya_detail_evidence,
                R.string.papaya_recommended_amount, R.string.papaya_detail_note);
        attachGuide(context, fruits.get(5), R.string.lychee_detail_evidence,
                R.string.lychee_recommended_amount, R.string.lychee_detail_note);
        attachGuide(context, fruits.get(6), R.string.pineapple_detail_evidence,
                R.string.pineapple_recommended_amount, R.string.pineapple_detail_note);
        attachGuide(context, fruits.get(8), R.string.guava_detail_evidence,
                R.string.guava_recommended_amount, 0);
        attachGuide(context, fruits.get(10), R.string.sweet_tamarind_detail_evidence,
                R.string.sweet_tamarind_recommended_amount, R.string.sweet_tamarind_detail_note);
        attachGuide(context, fruits.get(14), R.string.jujube_detail_evidence,
                R.string.jujube_recommended_amount, R.string.jujube_detail_note);
        attachGuide(context, fruits.get(16), R.string.durian_detail_evidence,
                R.string.durian_recommended_amount, R.string.durian_detail_note);
        attachGuide(context, fruits.get(17), R.string.mangosteen_detail_evidence,
                R.string.mangosteen_recommended_amount, 0);
        attachGuide(context, fruits.get(18), R.string.rambutan_detail_evidence,
                R.string.rambutan_recommended_amount, R.string.rambutan_detail_note);
        attachGuide(context, fruits.get(19), R.string.longan_detail_evidence,
                R.string.longan_recommended_amount, R.string.longan_detail_note);
        attachGuide(context, fruits.get(20), R.string.longkong_detail_evidence,
                R.string.longkong_recommended_amount, 0);
        attachGuide(context, fruits.get(27), R.string.pomelo_detail_evidence,
                R.string.pomelo_recommended_amount, 0);
        attachGuide(context, fruits.get(35), R.string.apple_detail_evidence,
                R.string.apple_recommended_amount, 0);
    }

    private static void attachGuide(Context context, Fruit fruit, int evidenceResource,
            int amountResource, int noteResource) {
        fruit.withEnglishDetailGuide(
                context.getString(evidenceResource),
                context.getString(amountResource),
                context.getString(R.string.fruit_serving_carbohydrate_equivalent),
                noteResource == 0 ? null : context.getString(noteResource),
                null,
                null);
    }

    private static Context localizedContext(Context context, Locale locale) {
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }

    private static String sugarDetail(Context context, String original) {
        int template;
        if (original.contains("ไม่มีน้ำตาล")) {
            template = R.string.fruit_sugar_detail_none_en;
        } else if (original.contains("สูงมาก")) {
            template = R.string.fruit_sugar_detail_very_high_en;
        } else if (original.contains("สูง")) {
            template = R.string.fruit_sugar_detail_high_en;
        } else if (original.contains("ปานกลาง")) {
            template = R.string.fruit_sugar_detail_moderate_en;
        } else {
            template = R.string.fruit_sugar_detail_low_en;
        }
        return context.getString(template, firstNumber(original));
    }

    private static String glycemicIndexDetail(Context context, String original) {
        int template;
        if (original.contains("สูง")) {
            template = R.string.fruit_gi_detail_high_en;
        } else if (original.contains("ปานกลาง")) {
            template = R.string.fruit_gi_detail_moderate_en;
        } else {
            template = R.string.fruit_gi_detail_low_en;
        }
        return context.getString(template, firstNumber(original));
    }

    private static String nutrientDetail(Context context, String original, boolean carbohydrate) {
        String value = leadingNumber(original);
        boolean perHundredGrams = original.contains("/100") || original.contains("ต่อ 100");
        int template;
        if (original.contains("สูงมาก")) {
            template = carbohydrate
                    ? R.string.fruit_carbohydrate_very_high_en
                    : R.string.fruit_fiber_high_en;
        } else if (original.contains("สูง")) {
            template = carbohydrate
                    ? R.string.fruit_carbohydrate_high_en
                    : R.string.fruit_fiber_high_en;
        } else if (original.contains("ปานกลาง")) {
            template = carbohydrate
                    ? R.string.fruit_carbohydrate_moderate_en
                    : R.string.fruit_fiber_moderate_en;
        } else if (original.contains("ต่ำ")) {
            template = carbohydrate
                    ? R.string.fruit_carbohydrate_low_en
                    : R.string.fruit_fiber_low_en;
        } else {
            template = R.string.fruit_metric_plain_en;
        }
        String unit = perHundredGrams
                ? context.getString(R.string.fruit_metric_per_100_grams_en)
                : context.getString(R.string.fruit_metric_grams_en);
        return context.getString(template, value, unit);
    }

    private static String impactLabel(Context context, String original) {
        if (original.contains("🟢") || original.contains("ต่ำ")) {
            return context.getString(R.string.fruit_impact_low_en);
        }
        if (original.contains("🔴") || original.contains("สูง")) {
            return context.getString(R.string.fruit_impact_high_en);
        }
        return context.getString(R.string.fruit_impact_moderate_en);
    }

    private static String leadingNumber(String value) {
        Matcher matcher = LEADING_NUMBER_PATTERN.matcher(value);
        return matcher.find() ? matcher.group(1) : firstNumber(value);
    }

    private static String firstNumber(String value) {
        Matcher matcher = FIRST_NUMBER_PATTERN.matcher(value);
        return matcher.find() ? matcher.group(1) : value;
    }
}
