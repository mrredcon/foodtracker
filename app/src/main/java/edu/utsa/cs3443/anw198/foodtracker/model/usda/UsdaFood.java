package edu.utsa.cs3443.anw198.foodtracker.model.usda;

public class UsdaFood {
    // Common
    private String description;
    private int fdcId;
    private String dataType;
    private UsdaFoodNutrient[] foodNutrients;

    // Branded
    private String gtinUpc;
    private Double servingSize;
    private String servingSizeUnit;
    //private String packageWeight;
    private String householdServingFullText;
    private String brandName;

    // SR Legacy, Survey (FNDDS), Foundation
    private UsdaFoodPortion[] foodPortions;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFdcId() {
        return fdcId;
    }

    public void setFdcId(int fdcId) {
        this.fdcId = fdcId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getGtinUpc() {
        return gtinUpc;
    }

    public void setGtinUpc(String gtinUpc) {
        this.gtinUpc = gtinUpc;
    }

    public Double getServingSize() {
        return servingSize;
    }

    public void setServingSize(Double servingSize) {
        this.servingSize = servingSize;
    }

    public String getServingSizeUnit() {
        return servingSizeUnit;
    }

    public void setServingSizeUnit(String servingSizeUnit) {
        this.servingSizeUnit = servingSizeUnit;
    }

    public String getHouseholdServingFullText() {
        return householdServingFullText;
    }

    public void setHouseholdServingFullText(String householdServingFullText) {
        this.householdServingFullText = householdServingFullText;
    }

    public UsdaFoodNutrient[] getFoodNutrients() {
        return foodNutrients;
    }

    public void setFoodNutrients(UsdaFoodNutrient[] foodNutrients) {
        this.foodNutrients = foodNutrients;
    }

    public UsdaFoodPortion[] getFoodPortions() {
        return foodPortions;
    }

    public void setFoodPortions(UsdaFoodPortion[] foodPortions) {
        this.foodPortions = foodPortions;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
