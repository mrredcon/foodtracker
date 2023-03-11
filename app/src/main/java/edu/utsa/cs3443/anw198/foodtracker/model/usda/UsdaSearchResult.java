package edu.utsa.cs3443.anw198.foodtracker.model.usda;

public class UsdaSearchResult {
    private Integer totalHits;
    private UsdaSearchResultFood[] foods;

    public Integer getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(Integer totalHits) {
        this.totalHits = totalHits;
    }

    public UsdaSearchResultFood[] getFoods() {
        return foods;
    }

    public void setFoods(UsdaSearchResultFood[] foods) {
        this.foods = foods;
    }
}
