package edu.utsa.cs3443.anw198.foodtracker.providers.usda;

import edu.utsa.cs3443.anw198.foodtracker.APIListener;
import edu.utsa.cs3443.anw198.foodtracker.model.FoodSearchResult;
import edu.utsa.cs3443.anw198.foodtracker.model.usda.UsdaSearchResult;
import edu.utsa.cs3443.anw198.foodtracker.model.usda.UsdaSearchResultFood;
import edu.utsa.cs3443.anw198.foodtracker.model.usda.UsdaSearchResultFoodNutrient;
import edu.utsa.cs3443.anw198.foodtracker.providers.FoodSearchProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsdaFoodSearchProvider implements FoodSearchProvider {

    private Call<UsdaSearchResult> callAsync;

    public UsdaFoodSearchProvider() {
        callAsync = null;
    }

    //@Override
    public void searchFoods(String query, APIListener<FoodSearchResult[]> listener) {
        UsdaService service = UsdaServiceGenerator.createService(UsdaService.class);
        callAsync = service.searchFoods(query);

        callAsync.enqueue(new Callback<UsdaSearchResult>() {
            @Override
            public void onResponse(Call<UsdaSearchResult> call, Response<UsdaSearchResult> response) {
                UsdaSearchResult usdaResult = response.body();
                UsdaSearchResultFood[] usdaResults = usdaResult.getFoods();
                FoodSearchResult[] results = new FoodSearchResult[usdaResults.length];

                for (int i = 0; i < usdaResults.length; i++) {
                    UsdaSearchResultFood usdaFood = usdaResults[i];
                    String name = usdaFood.getDescription();
                    String brand = usdaFood.getBrandName();

                    String id = String.valueOf(usdaFood.getFdcId());
                    Double fat = null, carbs = null, protein = null, calories = null;

                    for (UsdaSearchResultFoodNutrient nutrient : usdaFood.getFoodNutrients()) {
                        fat = nutrient.getNutrientName().equals("Carbohydrate, by difference") ? nutrient.getValue() : fat;
                        carbs = nutrient.getNutrientName().equals("Total lipid (fat)") ? nutrient.getValue() : carbs;
                        protein = nutrient.getNutrientName().equals("Protein") ? nutrient.getValue() : protein;
                        calories = nutrient.getNutrientName().equals("Energy") && nutrient.getUnitName().equals("KCAL") ? nutrient.getValue() : calories;
                    }

                    results[i] = new FoodSearchResult(name, brand, id, fat, carbs, protein, calories);
                }
                listener.onResponse(results);
            }

            @Override
            public void onFailure(Call<UsdaSearchResult> call, Throwable throwable) {
                listener.onFailure(throwable);
            }
        });
    }

    //@Override
    public void cancelSearch() {
        if (callAsync != null) {
            callAsync.cancel();
        }
    }
}
