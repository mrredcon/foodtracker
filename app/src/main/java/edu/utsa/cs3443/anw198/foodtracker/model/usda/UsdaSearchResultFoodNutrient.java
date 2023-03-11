package edu.utsa.cs3443.anw198.foodtracker.model.usda;

public class UsdaSearchResultFoodNutrient {
    private Integer nutrientId;
    private String nutrientName;
    private Double value;

    public Integer getNutrientId() {
        return nutrientId;
    }

    public void setNutrientId(Integer nutrientId) {
        this.nutrientId = nutrientId;
    }

    public String getNutrientName() {
        return nutrientName;
    }

    public void setNutrientName(String nutrientName) {
        this.nutrientName = nutrientName;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
