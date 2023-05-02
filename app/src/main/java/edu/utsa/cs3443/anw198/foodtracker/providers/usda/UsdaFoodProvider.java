package edu.utsa.cs3443.anw198.foodtracker.providers.usda;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import edu.utsa.cs3443.anw198.foodtracker.APIListener;
import edu.utsa.cs3443.anw198.foodtracker.model.CompleteFood;
import edu.utsa.cs3443.anw198.foodtracker.model.DataOrigin;
import edu.utsa.cs3443.anw198.foodtracker.model.Food;
import edu.utsa.cs3443.anw198.foodtracker.model.FoodDao;
import edu.utsa.cs3443.anw198.foodtracker.model.Nutrient;
import edu.utsa.cs3443.anw198.foodtracker.model.NutrientType;
import edu.utsa.cs3443.anw198.foodtracker.model.ServingSize;
import edu.utsa.cs3443.anw198.foodtracker.model.units.MassUnit;
import edu.utsa.cs3443.anw198.foodtracker.model.usda.UsdaFood;
import edu.utsa.cs3443.anw198.foodtracker.model.usda.UsdaFoodNutrient;
import edu.utsa.cs3443.anw198.foodtracker.model.usda.UsdaFoodPortion;
import edu.utsa.cs3443.anw198.foodtracker.model.usda.UsdaNutrient;
import edu.utsa.cs3443.anw198.foodtracker.providers.DbProvider;
import edu.utsa.cs3443.anw198.foodtracker.providers.FoodProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsdaFoodProvider implements FoodProvider {
    private Call<UsdaFood> callAsync;
    private Context context;

    private static final Map<Integer, NutrientType> fdcIdToNutrient = createFdcNutrientIds();

    private static Map<Integer, NutrientType> createFdcNutrientIds() {
        HashMap<Integer, NutrientType> map = new HashMap<>();
        map.put(1003, NutrientType.PROTEIN);
        map.put(1004, NutrientType.FAT);
        map.put(1005, NutrientType.CARBOHYDRATES);

        map.put(1018, NutrientType.ETHYL_ALCOHOL);
        map.put(1057, NutrientType.CAFFEINE);
        map.put(1058, NutrientType.THEOBROMINE);
        // "Sugars, total" intentionally has two FDC IDs
        map.put(2000, NutrientType.SUGAR);
        map.put(1063, NutrientType.SUGAR);
        map.put(1079, NutrientType.FIBER);
        map.put(1087, NutrientType.CALCIUM);
        map.put(1089, NutrientType.IRON);
        map.put(1090, NutrientType.MAGNESIUM);
        map.put(1091, NutrientType.PHOSPHORUS);
        map.put(1093, NutrientType.SODIUM);
        map.put(1096, NutrientType.CHROMIUM);
        map.put(1095, NutrientType.ZINC);
        map.put(1098, NutrientType.COPPER);
        map.put(1100, NutrientType.IODINE);
        map.put(1101, NutrientType.MANGANESE);
        map.put(1102, NutrientType.MOLYBDENUM);
        map.put(1103, NutrientType.SELENIUM);
        map.put(1105, NutrientType.RETINOL);
        map.put(1106, NutrientType.VITAMIN_A);
        map.put(1107, NutrientType.CAROTENE_BETA);
        map.put(1108, NutrientType.CAROTENE_ALPHA);
        map.put(1109, NutrientType.VITAMIN_E);
        map.put(1114, NutrientType.VITAMIN_D);
        map.put(1162, NutrientType.VITAMIN_C);
        map.put(1165, NutrientType.THIAMIN);
        map.put(1166, NutrientType.RIBOFLAVIN);
        map.put(1170, NutrientType.PANTOTHENIC_ACID);
        map.put(1175, NutrientType.VITAMIN_B6);
        map.put(1176, NutrientType.BIOTIN);
        map.put(1177, NutrientType.FOLATE);
        map.put(1178, NutrientType.VITAMIN_B12);
        map.put(1180, NutrientType.CHOLINE);
        map.put(1185, NutrientType.VITAMIN_K);
        map.put(1186, NutrientType.FOLIC_ACID);
        map.put(1253, NutrientType.CHOLESTEROL);

        return map;
    }

    public UsdaFoodProvider(Context context) {
        callAsync = null;
        this.context = context;
    }

    private MassUnit abbreviationToMassUnit(String abbreviation) {
        switch (abbreviation) {
            case "g":
                return MassUnit.GRAMS;
            case "mg":
                return MassUnit.MILLIGRAMS;
            // µg: Micro Sign (U+00B5)
            case "\u00B5g":
            // μg: Greek Small Letter Mu (U+03BC)
            case "\u03BCg":
                return MassUnit.MICROGRAMS;
        }

        throw new RuntimeException("UsdaFoodProvider: Failed to convert unit abbreviation: " + abbreviation);
    }

    @Override
    public void loadFood(String id, APIListener<CompleteFood> listener) {
        UsdaService service = UsdaServiceGenerator.createService(UsdaService.class);
        callAsync = service.loadFood(id);

        callAsync.enqueue(new Callback<UsdaFood>() {
            @Override
            public void onResponse(@NonNull Call<UsdaFood> call, @NonNull Response<UsdaFood> response) {
                UsdaFood usdaFood = response.body();

                if (usdaFood == null || usdaFood.getDataType() == null || usdaFood.getDataType().isEmpty()) {
                    onFailure(call, new RuntimeException("USDA returned an unknown object."));
                    return;
                }

                boolean isBranded = usdaFood.getDataType().equals("Branded");
                Food food;

                if (isBranded && usdaFood.getServingSizeUnit().equals("ml")) {
                    food = new Food(true);
                } else {
                    food = new Food(false);
                }

                if (isBranded) {
                    food.setName(usdaFood.getBrandName() + " " + usdaFood.getDescription());
                } else {
                    food.setName(usdaFood.getDescription());
                }

                food.setOnlineId(usdaFood.getFdcId());
                food.setOrigin(DataOrigin.USDA);

                List<Nutrient> nutrients = new ArrayList<>();
                for (UsdaFoodNutrient foodNutrient : usdaFood.getFoodNutrients()) {
                    UsdaNutrient usdaNutrient = foodNutrient.getNutrient();
                    int fdcNutrientId = usdaNutrient.getId();

                    // Special case for "Energy" nutrient
                    if ((fdcNutrientId == 1008 || fdcNutrientId == 2047) &&
                            usdaNutrient.getUnitName().equals("kcal")) {
                        food.setCalories(foodNutrient.getAmount());
                    } else {
                        NutrientType nutrientType = fdcIdToNutrient.get(fdcNutrientId);
                        // Nutrient exists in USDA database but not our model, ignore it
                        if (nutrientType == null) continue;

                        MassUnit massUnit = abbreviationToMassUnit(usdaNutrient.getUnitName());
                        nutrients.add(new Nutrient(nutrientType, massUnit, foodNutrient.getAmount()));
                    }
                }

                List<ServingSize> servingSizes = new ArrayList<>();
                if (isBranded) {
                    String servingTitle = usdaFood.getHouseholdServingFullText();
                    if (servingTitle == null)
                        servingTitle = "Manufacturer set size";

                    Double servingSizeAmount = usdaFood.getServingSize();
                    if (servingSizeAmount != null) {
                        servingSizes.add(new ServingSize(servingTitle, servingSizeAmount));
                    } else {
                        String baseUnitName = context.getString(food.getBaseUnit().getTitleResource());
                        servingSizes.add(new ServingSize(baseUnitName, 1.0));
                    }
                } else {
                    for (UsdaFoodPortion portion : usdaFood.getFoodPortions()) {
                        String portionDescription = portion.getPortionDescription();

                        if (portionDescription != null) {
                            servingSizes.add(new ServingSize(portionDescription, portion.getGramWeight()));
                        } else {
                            String servingSizeName = String.format(Locale.getDefault(), "%.1f", portion.getAmount()) + " " + portion.getModifier();
                            servingSizes.add(new ServingSize(servingSizeName, portion.getGramWeight()));
                        }
                    }
                }

                Thread thread = new Thread() {
                    public void run() {
                        FoodDao dao = DbProvider.getInstance().foodDao();

                        // If the food is already in the DB, return the old data.
                        Food existing = dao.getFoodByOnlineId(food.getOnlineId());
                        if (existing != null) {
                            List<ServingSize> existingServings = dao.getServingSizesFromFood(existing.id);
                            List<Nutrient> existingNutrients = dao.getNutrientsFromFood(existing.id);
                            listener.onResponse(new CompleteFood(existing, existingServings, existingNutrients));
                            return;
                        }

                        long foodId = dao.insertFood(food);
                        food.id = foodId;

                        for (ServingSize servingSize : servingSizes) {
                            servingSize.foodId = foodId;
                            servingSize.id = dao.insertServingSize(servingSize);
                        }

                        for (Nutrient nutrient : nutrients)
                            nutrient.foodId = foodId;

                        dao.insertNutrients(nutrients);
                        listener.onResponse(new CompleteFood(food, servingSizes, nutrients));
                    }
                };
                thread.start();
            }

            @Override
            public void onFailure(@NonNull Call<UsdaFood> call, @NonNull Throwable throwable) {
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
