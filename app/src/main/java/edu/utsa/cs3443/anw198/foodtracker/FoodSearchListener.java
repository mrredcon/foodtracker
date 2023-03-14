package edu.utsa.cs3443.anw198.foodtracker;

import edu.utsa.cs3443.anw198.foodtracker.model.FoodSearchResult;

public interface FoodSearchListener {
    void onResponse(FoodSearchResult[] results);
    void onFailure(Throwable error);
}
