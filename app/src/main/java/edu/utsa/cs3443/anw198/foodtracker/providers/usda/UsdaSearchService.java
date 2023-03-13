package edu.utsa.cs3443.anw198.foodtracker.providers.usda;

import java.util.List;

import edu.utsa.cs3443.anw198.foodtracker.model.usda.UsdaSearchResult;
import edu.utsa.cs3443.anw198.foodtracker.model.usda.UsdaSearchResultFood;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface UsdaSearchService {
    @Headers({ "X-Api-Key: Ge2axZ3FNcUFvL8TCwJObK4FHXmYhWdQA5YEdF5W" })
    @GET("foods/search")
    Call<UsdaSearchResult> searchFoods(@Query("query") String query);
}
