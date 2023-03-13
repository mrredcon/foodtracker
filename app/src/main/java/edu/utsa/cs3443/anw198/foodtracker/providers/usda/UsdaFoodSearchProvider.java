package edu.utsa.cs3443.anw198.foodtracker.providers.usda;

import edu.utsa.cs3443.anw198.foodtracker.model.FoodSearchResult;
import edu.utsa.cs3443.anw198.foodtracker.model.usda.UsdaSearchResult;
import edu.utsa.cs3443.anw198.foodtracker.providers.FoodSearchProvider;
import retrofit2.Call;

public class UsdaFoodSearchProvider implements FoodSearchProvider {
    @Override
    public FoodSearchResult[] searchFoods(String query) {
        UsdaSearchService service = UsdaServiceGenerator.createService(UsdaSearchService.class);
        Call<UsdaSearchResult> callAsync = service.searchFoods(query);
        return new FoodSearchResult[0];
    }

    @Override
    public void cancelSearch() {

    }
}
