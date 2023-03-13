package edu.utsa.cs3443.anw198.foodtracker.providers;

import edu.utsa.cs3443.anw198.foodtracker.model.FoodSearchResult;

public interface FoodSearchProvider {
    FoodSearchResult[] searchFoods(String query);
    void cancelSearch();
}
