package edu.utsa.cs3443.anw198.foodtracker.model.usda;

public class UsdaFoodPortion {
    private Double gramWeight;
    private Double amount;
    private String modifier;
    private String portionDescription;

    public Double getGramWeight() {
        return gramWeight;
    }

    public void setGramWeight(Double gramWeight) {
        this.gramWeight = gramWeight;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getPortionDescription() {
        return portionDescription;
    }

    public void setPortionDescription(String portionDescription) {
        this.portionDescription = portionDescription;
    }
}
