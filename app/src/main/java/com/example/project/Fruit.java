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
    public int getImageResId() { return imageResId; }

}
