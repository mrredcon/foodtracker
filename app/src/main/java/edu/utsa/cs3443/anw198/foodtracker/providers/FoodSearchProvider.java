package edu.utsa.cs3443.anw198.foodtracker.providers;

import edu.utsa.cs3443.anw198.foodtracker.APIListener;
import edu.utsa.cs3443.anw198.foodtracker.model.FoodSearchResult;

public interface FoodSearchProvider {
    void searchFoods(String query, APIListener<FoodSearchResult[]> listener);
    void cancelSearch();
}
