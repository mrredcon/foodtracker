package edu.utsa.cs3443.anw198.foodtracker.providers;

import java.util.List;

import edu.utsa.cs3443.anw198.foodtracker.APIListener;
import edu.utsa.cs3443.anw198.foodtracker.model.FoodSearchResult;

public interface FoodSearchProvider {
    void searchFoods(String query, APIListener<List<FoodSearchResult>> listener);
    void cancelSearch();
}
