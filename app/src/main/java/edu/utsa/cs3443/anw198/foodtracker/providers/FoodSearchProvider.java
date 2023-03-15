package edu.utsa.cs3443.anw198.foodtracker.providers;

import edu.utsa.cs3443.anw198.foodtracker.FoodSearchListener;
import edu.utsa.cs3443.anw198.foodtracker.model.FoodSearchResult;
import retrofit2.Callback;

public interface FoodSearchProvider {
    void searchFoods(String query, FoodSearchListener listener);
    void cancelSearch();
}
