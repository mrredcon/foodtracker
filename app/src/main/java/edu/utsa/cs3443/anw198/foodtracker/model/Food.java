package edu.utsa.cs3443.anw198.foodtracker.model;

import android.util.Log;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import edu.utsa.cs3443.anw198.foodtracker.units.MassUnit;
import edu.utsa.cs3443.anw198.foodtracker.units.Unit;

public class Food {
    private String name;

    // Name of serving size (e.g. "1 cup") mapped to grams
    private Map<String, Double> servingSizes;

    // Calories in kcal
    private double calories;

    private Unit baseUnit = MassUnit.GRAMS;
    
    private Map<Nutrient, Double> nutrients;

    public static final Map<Nutrient, MassUnit> NUTRIENT_UNITS = createNutrientUnits();

    public Food() {
        this.nutrients = new HashMap<>();
        this.servingSizes = new HashMap<>();
    }

    private static Map<Nutrient, MassUnit> createNutrientUnits() {
        Map<Nutrient, MassUnit> map = new HashMap<>();

        map.put(Nutrient.FAT, MassUnit.GRAMS);
        map.put(Nutrient.CARBOHYDRATES, MassUnit.GRAMS);
        map.put(Nutrient.PROTEIN, MassUnit.GRAMS);
        map.put(Nutrient.FAT_SATURATED, MassUnit.GRAMS);
        map.put(Nutrient.FAT_TRANS, MassUnit.GRAMS);
        map.put(Nutrient.FIBER, MassUnit.GRAMS);
        map.put(Nutrient.SUGAR, MassUnit.GRAMS);
        map.put(Nutrient.SUGAR_ADDED, MassUnit.GRAMS);
        map.put(Nutrient.SUGAR_ALCOHOL, MassUnit.GRAMS);
        map.put(Nutrient.ETHYL_ALCOHOL, MassUnit.GRAMS);

        map.put(Nutrient.CALCIUM, MassUnit.MILLIGRAMS);
        map.put(Nutrient.CHOLESTEROL, MassUnit.MILLIGRAMS);
        map.put(Nutrient.SODIUM, MassUnit.MILLIGRAMS);
        map.put(Nutrient.IRON, MassUnit.MILLIGRAMS);
        map.put(Nutrient.MAGNESIUM, MassUnit.MILLIGRAMS);
        map.put(Nutrient.PHOSPHORUS, MassUnit.MILLIGRAMS);
        map.put(Nutrient.POTASSIUM, MassUnit.MILLIGRAMS);
        map.put(Nutrient.ZINC, MassUnit.MILLIGRAMS);
        map.put(Nutrient.COPPER, MassUnit.MILLIGRAMS);
        map.put(Nutrient.THIAMIN, MassUnit.MILLIGRAMS);
        map.put(Nutrient.RIBOFLAVIN, MassUnit.MILLIGRAMS);
        map.put(Nutrient.NIACIN, MassUnit.MILLIGRAMS);
        map.put(Nutrient.CHOLINE, MassUnit.MILLIGRAMS);
        map.put(Nutrient.VITAMIN_B6, MassUnit.MILLIGRAMS);
        map.put(Nutrient.VITAMIN_C, MassUnit.MILLIGRAMS);
        map.put(Nutrient.VITAMIN_E, MassUnit.MILLIGRAMS);
        map.put(Nutrient.CAFFEINE, MassUnit.MILLIGRAMS);
        map.put(Nutrient.THEOBROMINE, MassUnit.MILLIGRAMS);

        map.put(Nutrient.SELENIUM, MassUnit.MICROGRAMS);
        map.put(Nutrient.FOLATE, MassUnit.MICROGRAMS);
        map.put(Nutrient.FOLIC_ACID, MassUnit.MICROGRAMS);
        map.put(Nutrient.VITAMIN_A, MassUnit.MICROGRAMS);
        map.put(Nutrient.RETINOL, MassUnit.MICROGRAMS);
        map.put(Nutrient.VITAMIN_B12, MassUnit.MICROGRAMS);
        map.put(Nutrient.VITAMIN_D, MassUnit.MICROGRAMS);
        map.put(Nutrient.VITAMIN_K, MassUnit.MICROGRAMS);
        map.put(Nutrient.CAROTENE_ALPHA, MassUnit.MICROGRAMS);
        map.put(Nutrient.CAROTENE_BETA, MassUnit.MICROGRAMS);

        return map;
    }

    public Map<Nutrient, Double> getNutrients() {
        return Collections.unmodifiableMap(nutrients);
    }

    public void setNutrient(Nutrient nutrient, MassUnit inputUnit, double inputAmount) {
        Log.i("Nom", "Nutrient: " + nutrient + " inputUnit: " + inputUnit + " inputAmount: " + inputAmount);
        MassUnit destinationUnit = NUTRIENT_UNITS.get(nutrient);
        double converted = inputUnit.convertTo(destinationUnit, inputAmount);
        nutrients.put(nutrient, converted);
    }

    public Map<String, Double> getServingSizes() {
        return servingSizes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public Unit getBaseUnit() {
        return baseUnit;
    }

    public void setBaseUnit(Unit baseUnit) {
        this.baseUnit = baseUnit;
    }
}