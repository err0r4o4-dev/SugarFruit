package com.example.project;

public class Fruit {
    public String name;
    public String index;
    public String index_;
    public String sugar;
    public String sugar_;
    public String carbohydrate_;
    public String fiber_;
    public String impact_;
    public String type1_;
    public String type2_;
    public String end_;
    public String True_;
    public String Season;
    public String level1;
    public String level2;
    public String level3;
    private String detailIntroduction;
    private String recommendedAmount;
    private String recommendedEquivalent;
    private String detailTip1;
    private String detailTip2;
    private String detailTip3;
    private final String thaiName;
    private final String thaiIndexDetail;
    private final String thaiSugarDetail;
    private final String thaiCarbohydrate;
    private final String thaiFiber;
    private final String thaiImpact;
    private final String thaiType1;
    private final String thaiType2;
    private final String thaiGeneralAdvice;
    private String thaiDetailIntroduction;
    private String thaiRecommendedAmount;
    private String thaiRecommendedEquivalent;
    private String thaiDetailTip1;
    private String thaiDetailTip2;
    private String thaiDetailTip3;
    private String englishName;
    private String englishSugar;
    private String englishSugarDetail;
    private String englishIndex;
    private String englishIndexDetail;
    private String englishCarbohydrate;
    private String englishFiber;
    private String englishImpact;
    private String englishType1;
    private String englishType2;
    private String englishGeneralAdvice;
    private String englishDetailIntroduction;
    private String englishRecommendedAmount;
    private String englishRecommendedEquivalent;
    private String englishDetailTip1;
    private String englishDetailTip2;
    private String englishDetailTip3;
    public int imageResId;
    public Fruit(int imageResId, String name, String sugar, String sugar_, String index, String index_, String True_, String Season
            , String carbohydrate_, String fiber_, String impact_, String type1_, String type2_, String end_, String level1, String level2, String level3) {
        this.name = name;
        this.index = index;
        this.index_ = index_;
        this.carbohydrate_ = carbohydrate_;
        this.fiber_ = fiber_;
        this.impact_ = impact_;
        this.type1_ = type1_;
        this.type2_ = type2_;
        this.end_ = end_;
        this.sugar = sugar;
        this.sugar_ = sugar_;
        this.True_ = True_;
        this.Season = Season;
        this.imageResId = imageResId;
        this.level1 = level1;
        this.level2 = level2;
        this.level3 = level3;
        thaiName = name;
        thaiIndexDetail = index_;
        thaiSugarDetail = sugar_;
        thaiCarbohydrate = carbohydrate_;
        thaiFiber = fiber_;
        thaiImpact = impact_;
        thaiType1 = type1_;
        thaiType2 = type2_;
        thaiGeneralAdvice = end_;
    }

    public String getName() { return name; }
    public String getIndex() { return index; }
    public String getIndex_() { return index_; }
    public String getSugar() { return sugar; }
    public String getSugar_() { return sugar_; }
    public String getCarbohydrate_() { return carbohydrate_; }
    public String getFiber_() { return fiber_; }
    public String getImpact_() { return impact_; }
    public String getType1_() { return type1_; }
    public String getType2_() { return type2_; }
    public String getEnd_() { return end_; }
    public String getTrue() { return True_; }
    public String getLevel1() { return level1; }
    public String getLevel2() { return level2; }
    public String getLevel3() { return level3; }
    public String getSeason() { return Season; }
    public FruitSeason getSeasonValue() { return FruitSeason.fromCode(Season); }
    public int getImageResId() { return imageResId; }

    public void attachEnglishText(String localizedName, String localizedSugar,
            String localizedSugarDetail, String localizedIndex, String localizedIndexDetail,
            String localizedCarbohydrate, String localizedFiber, String localizedImpact,
            String localizedType1, String localizedType2, String localizedGeneralAdvice) {
        englishName = localizedName;
        englishSugar = localizedSugar;
        englishSugarDetail = localizedSugarDetail;
        englishIndex = localizedIndex;
        englishIndexDetail = localizedIndexDetail;
        englishCarbohydrate = localizedCarbohydrate;
        englishFiber = localizedFiber;
        englishImpact = localizedImpact;
        englishType1 = localizedType1;
        englishType2 = localizedType2;
        englishGeneralAdvice = localizedGeneralAdvice;
    }

    public void applyEnglishText() {
        name = englishName;
        sugar = englishSugar;
        sugar_ = englishSugarDetail;
        index = englishIndex;
        index_ = englishIndexDetail;
        carbohydrate_ = englishCarbohydrate;
        fiber_ = englishFiber;
        impact_ = englishImpact;
        type1_ = englishType1;
        type2_ = englishType2;
        end_ = englishGeneralAdvice;
        detailIntroduction = englishDetailIntroduction;
        recommendedAmount = englishRecommendedAmount;
        recommendedEquivalent = englishRecommendedEquivalent;
        detailTip1 = englishDetailTip1;
        detailTip2 = englishDetailTip2;
        detailTip3 = englishDetailTip3;
    }

    public Fruit withDetailGuide(String detailIntroduction, String recommendedAmount,
            String recommendedEquivalent, String detailTip1, String detailTip2,
            String detailTip3) {
        this.detailIntroduction = detailIntroduction;
        this.recommendedAmount = recommendedAmount;
        this.recommendedEquivalent = recommendedEquivalent;
        this.detailTip1 = detailTip1;
        this.detailTip2 = detailTip2;
        this.detailTip3 = detailTip3;
        thaiDetailIntroduction = detailIntroduction;
        thaiRecommendedAmount = recommendedAmount;
        thaiRecommendedEquivalent = recommendedEquivalent;
        thaiDetailTip1 = detailTip1;
        thaiDetailTip2 = detailTip2;
        thaiDetailTip3 = detailTip3;
        return this;
    }

    public Fruit withEnglishDetailGuide(String detailIntroduction, String recommendedAmount,
            String recommendedEquivalent, String detailTip1, String detailTip2,
            String detailTip3) {
        englishDetailIntroduction = detailIntroduction;
        englishRecommendedAmount = recommendedAmount;
        englishRecommendedEquivalent = recommendedEquivalent;
        englishDetailTip1 = detailTip1;
        englishDetailTip2 = detailTip2;
        englishDetailTip3 = detailTip3;
        return this;
    }

    public String[] createDetailPayload(boolean english, String localizedSafetyLabel) {
        return new String[]{
                english ? englishName : thaiName,
                english ? englishIndexDetail : thaiIndexDetail,
                english ? englishSugarDetail : thaiSugarDetail,
                english ? englishCarbohydrate : thaiCarbohydrate,
                english ? englishFiber : thaiFiber,
                english ? englishImpact : thaiImpact,
                english ? englishType1 : thaiType1,
                english ? englishType2 : thaiType2,
                english ? englishGeneralAdvice : thaiGeneralAdvice,
                localizedSafetyLabel,
                english ? englishDetailIntroduction : thaiDetailIntroduction,
                english ? englishRecommendedAmount : thaiRecommendedAmount,
                english ? englishRecommendedEquivalent : thaiRecommendedEquivalent,
                english ? englishDetailTip1 : thaiDetailTip1,
                english ? englishDetailTip2 : thaiDetailTip2,
                english ? englishDetailTip3 : thaiDetailTip3
        };
    }

    public String getDetailIntroduction() { return detailIntroduction; }
    public String getRecommendedAmount() { return recommendedAmount; }
    public String getRecommendedEquivalent() { return recommendedEquivalent; }
    public String getDetailTip1() { return detailTip1; }
    public String getDetailTip2() { return detailTip2; }
    public String getDetailTip3() { return detailTip3; }

}
