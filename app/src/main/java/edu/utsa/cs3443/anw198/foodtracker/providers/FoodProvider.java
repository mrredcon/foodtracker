package edu.utsa.cs3443.anw198.foodtracker.providers;

import edu.utsa.cs3443.anw198.foodtracker.APIListener;
import edu.utsa.cs3443.anw198.foodtracker.model.CompleteFood;

public interface FoodProvider {
    void loadFood(String id, APIListener<CompleteFood> listener);
    void cancelLoad();
}
