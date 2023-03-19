package edu.utsa.cs3443.anw198.foodtracker.model.usda;

public class UsdaFoodNutrient {
    private Double amount;
    private UsdaNutrient nutrient;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public UsdaNutrient getNutrient() {
        return nutrient;
    }

    public void setNutrient(UsdaNutrient nutrient) {
        this.nutrient = nutrient;
    }
}
