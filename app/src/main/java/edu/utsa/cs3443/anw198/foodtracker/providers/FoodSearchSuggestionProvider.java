package edu.utsa.cs3443.anw198.foodtracker.providers;

import android.content.SearchRecentSuggestionsProvider;

public class FoodSearchSuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "edu.utsa.cs3443.anw198.foodtracker.providers.FoodSearchSuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public FoodSearchSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
