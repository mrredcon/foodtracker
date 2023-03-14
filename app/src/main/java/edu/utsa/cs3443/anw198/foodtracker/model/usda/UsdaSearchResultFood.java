package edu.utsa.cs3443.anw198.foodtracker.model.usda;

public class UsdaSearchResultFood {
    private String description;
    private String dataType;
    private UsdaSearchResultFoodNutrient[] foodNutrients;

    private long fdcId;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public UsdaSearchResultFoodNutrient[] getFoodNutrients() {
        return foodNutrients;
    }

    public void setFoodNutrients(UsdaSearchResultFoodNutrient[] foodNutrients) {
        this.foodNutrients = foodNutrients;
    }

    public long getFdcId() {
        return fdcId;
    }

    public void setFdcId(long fdcId) {
        this.fdcId = fdcId;
    }
}
