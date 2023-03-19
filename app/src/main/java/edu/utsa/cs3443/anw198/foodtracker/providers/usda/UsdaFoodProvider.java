package edu.utsa.cs3443.anw198.foodtracker.providers.usda;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import edu.utsa.cs3443.anw198.foodtracker.APIListener;
import edu.utsa.cs3443.anw198.foodtracker.model.Food;
import edu.utsa.cs3443.anw198.foodtracker.model.Nutrient;
import edu.utsa.cs3443.anw198.foodtracker.model.usda.UsdaFood;
import edu.utsa.cs3443.anw198.foodtracker.model.usda.UsdaFoodNutrient;
import edu.utsa.cs3443.anw198.foodtracker.model.usda.UsdaFoodPortion;
import edu.utsa.cs3443.anw198.foodtracker.model.usda.UsdaNutrient;
import edu.utsa.cs3443.anw198.foodtracker.providers.FoodProvider;
import edu.utsa.cs3443.anw198.foodtracker.units.MassUnit;
import edu.utsa.cs3443.anw198.foodtracker.units.VolumeUnit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsdaFoodProvider implements FoodProvider {
    private Call<UsdaFood> callAsync;

    private static final Map<Integer, Nutrient> fdcIdToNutrient = createFdcNutrientIds();

    private static Map<Integer, Nutrient> createFdcNutrientIds() {
        HashMap<Integer, Nutrient> map = new HashMap<>();
        map.put(1003, Nutrient.PROTEIN);
        map.put(1004, Nutrient.FAT);
        map.put(1005, Nutrient.CARBOHYDRATES);

        map.put(1018, Nutrient.ETHYL_ALCOHOL);
        map.put(1057, Nutrient.CAFFEINE);
        map.put(1058, Nutrient.THEOBROMINE);
        // "Sugars, total" intentionally has two FDC IDs
        map.put(2000, Nutrient.SUGAR);
        map.put(1063, Nutrient.SUGAR);
        map.put(1079, Nutrient.FIBER);
        map.put(1087, Nutrient.CALCIUM);
        map.put(1089, Nutrient.IRON);
        map.put(1090, Nutrient.MAGNESIUM);
        map.put(1091, Nutrient.PHOSPHORUS);
        map.put(1093, Nutrient.SODIUM);
        map.put(1095, Nutrient.ZINC);
        map.put(1098, Nutrient.COPPER);
        map.put(1103, Nutrient.SELENIUM);
        map.put(1105, Nutrient.RETINOL);
        map.put(1106, Nutrient.VITAMIN_A);
        map.put(1107, Nutrient.CAROTENE_BETA);
        map.put(1108, Nutrient.CAROTENE_ALPHA);
        map.put(1109, Nutrient.VITAMIN_E);
        map.put(1114, Nutrient.VITAMIN_D);
        map.put(1162, Nutrient.VITAMIN_C);
        map.put(1165, Nutrient.THIAMIN);
        map.put(1166, Nutrient.RIBOFLAVIN);
        map.put(1175, Nutrient.VITAMIN_B6);
        map.put(1177, Nutrient.FOLATE);
        map.put(1178, Nutrient.VITAMIN_B12);
        map.put(1180, Nutrient.CHOLINE);
        map.put(1185, Nutrient.VITAMIN_K);
        map.put(1186, Nutrient.FOLIC_ACID);
        map.put(1253, Nutrient.CHOLESTEROL);

        return map;
    }

    public UsdaFoodProvider() {
        callAsync = null;
    }

    private MassUnit abbreviationToMassUnit(String abbreviation) {
        switch(abbreviation) {
            case "g": return MassUnit.GRAMS;
            case "mg": return MassUnit.MILLIGRAMS;
            // Micro Sign (U+00B5)
            case "µg":
            // Greek Small Letter Mu (U+03BC)
            case "μg": return MassUnit.MICROGRAMS;
        }
        //Log.e("Nom", "Returning null for input: " + abbreviation);
        return null;
    }

    @Override
    public void loadFood(String id, APIListener<Food> listener) {
        UsdaService service = UsdaServiceGenerator.createService(UsdaService.class);
        callAsync = service.loadFood(id);

        callAsync.enqueue(new Callback<UsdaFood>() {
            @Override
            public void onResponse(@NonNull Call<UsdaFood> call, @NonNull Response<UsdaFood> response) {
                UsdaFood usdaFood = response.body();
                Food result = new Food();
                result.setName(usdaFood.getDescription());

                if (usdaFood.getDataType().equals("Branded") &&
                        usdaFood.getServingSizeUnit().equals("ml")) {
                    result.setBaseUnit(VolumeUnit.MILLILITERS);
                } else {
                    result.setBaseUnit(MassUnit.GRAMS);
                }

                for (UsdaFoodNutrient foodNutrient : usdaFood.getFoodNutrients()) {
                    UsdaNutrient usdaNutrient = foodNutrient.getNutrient();
                    int fdcNutrientId = usdaNutrient.getId();

                    // Special case for "Energy" nutrient
                    if ((fdcNutrientId == 1008 || fdcNutrientId == 2047) &&
                            usdaNutrient.getUnitName().equals("kcal")) {
                        result.setCalories(foodNutrient.getAmount());
                    } else {
                        Nutrient nutrient = fdcIdToNutrient.get(fdcNutrientId);
                        // Nutrient exists in USDA database but not our model, ignore it
                        if (nutrient == null) continue;

                        MassUnit massUnit = abbreviationToMassUnit(usdaNutrient.getUnitName());
                        result.setNutrient(nutrient, massUnit, foodNutrient.getAmount());
                    }
                }

                Map<String, Double> servingSizes = result.getServingSizes();
                if (usdaFood.getDataType().equals("Branded")) {
                    String servingTitle = usdaFood.getHouseholdServingFullText();
                    if (servingTitle == null)
                        servingTitle = "Manufacturer set size";

                    servingSizes.put(servingTitle, usdaFood.getServingSize());
                } else {
                    for (UsdaFoodPortion portion : usdaFood.getFoodPortions()) {
                        String portionDescription = portion.getPortionDescription();

                        if (portionDescription != null) {
                            servingSizes.put(portionDescription, portion.getGramWeight());
                        } else {
                            String servingSizeName = String.format("%.1f", portion.getAmount()) + " " + portion.getModifier();
                            servingSizes.put(servingSizeName, portion.getGramWeight());
                        }
                    }
                }

                listener.onResponse(result);
            }

            @Override
            public void onFailure(Call<UsdaFood> call, Throwable throwable) {
                listener.onFailure(throwable);
            }
        });
    }

    @Override
    public void cancelLoad() {
        if (callAsync != null) {
            callAsync.cancel();
        }
    }
}
