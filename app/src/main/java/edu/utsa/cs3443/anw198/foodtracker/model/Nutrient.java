package edu.utsa.cs3443.anw198.foodtracker.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.Map;

import edu.utsa.cs3443.anw198.foodtracker.model.units.MassUnit;

@Entity(/*foreignKeys = {
        @ForeignKey(onDelete = ForeignKey.CASCADE,entity = Food.class,
                parentColumns = "id",childColumns = "foodId")}*/)
public class Nutrient {
    public static final Map<NutrientType, MassUnit> NUTRIENT_UNITS = createNutrientUnits();

    public Nutrient() { }

    @Ignore
    public Nutrient(NutrientType nutrientType, MassUnit inputUnit, double inputAmount) {
        this(nutrientType, inputUnit, inputAmount, 0);
    }

    @Ignore
    public Nutrient(NutrientType nutrientType, MassUnit inputUnit, double inputAmount, long foodId) {
        this.nutrientType = nutrientType;
        this.foodId = foodId;
        MassUnit destinationUnit = NUTRIENT_UNITS.get(nutrientType);
        this.amount = inputUnit.convertTo(destinationUnit, inputAmount);
    }

    @PrimaryKey(autoGenerate = true)
    public long id;
    public NutrientType nutrientType;
    public double amount;
    public long foodId;

    private static Map<NutrientType, MassUnit> createNutrientUnits() {
        Map<NutrientType, MassUnit> map = new HashMap<>();

        map.put(NutrientType.FAT, MassUnit.GRAMS);
        map.put(NutrientType.CARBOHYDRATES, MassUnit.GRAMS);
        map.put(NutrientType.PROTEIN, MassUnit.GRAMS);
        map.put(NutrientType.FAT_SATURATED, MassUnit.GRAMS);
        map.put(NutrientType.FAT_TRANS, MassUnit.GRAMS);
        map.put(NutrientType.FIBER, MassUnit.GRAMS);
        map.put(NutrientType.SUGAR, MassUnit.GRAMS);
        map.put(NutrientType.SUGAR_ADDED, MassUnit.GRAMS);
        map.put(NutrientType.SUGAR_ALCOHOL, MassUnit.GRAMS);
        map.put(NutrientType.ETHYL_ALCOHOL, MassUnit.GRAMS);

        map.put(NutrientType.CALCIUM, MassUnit.MILLIGRAMS);
        map.put(NutrientType.CHOLESTEROL, MassUnit.MILLIGRAMS);
        map.put(NutrientType.SODIUM, MassUnit.MILLIGRAMS);
        map.put(NutrientType.IRON, MassUnit.MILLIGRAMS);
        map.put(NutrientType.MAGNESIUM, MassUnit.MILLIGRAMS);
        map.put(NutrientType.PHOSPHORUS, MassUnit.MILLIGRAMS);
        map.put(NutrientType.POTASSIUM, MassUnit.MILLIGRAMS);
        map.put(NutrientType.ZINC, MassUnit.MILLIGRAMS);
        map.put(NutrientType.COPPER, MassUnit.MILLIGRAMS);
        map.put(NutrientType.THIAMIN, MassUnit.MILLIGRAMS);
        map.put(NutrientType.RIBOFLAVIN, MassUnit.MILLIGRAMS);
        map.put(NutrientType.NIACIN, MassUnit.MILLIGRAMS);
        map.put(NutrientType.CHOLINE, MassUnit.MILLIGRAMS);
        map.put(NutrientType.VITAMIN_B6, MassUnit.MILLIGRAMS);
        map.put(NutrientType.VITAMIN_C, MassUnit.MILLIGRAMS);
        map.put(NutrientType.VITAMIN_E, MassUnit.MILLIGRAMS);
        map.put(NutrientType.CAFFEINE, MassUnit.MILLIGRAMS);
        map.put(NutrientType.THEOBROMINE, MassUnit.MILLIGRAMS);
        map.put(NutrientType.MANGANESE, MassUnit.MILLIGRAMS);
        map.put(NutrientType.PANTOTHENIC_ACID, MassUnit.MILLIGRAMS);
        map.put(NutrientType.CHLORIDE, MassUnit.MILLIGRAMS);

        map.put(NutrientType.SELENIUM, MassUnit.MICROGRAMS);
        map.put(NutrientType.FOLATE, MassUnit.MICROGRAMS);
        map.put(NutrientType.FOLIC_ACID, MassUnit.MICROGRAMS);
        map.put(NutrientType.VITAMIN_A, MassUnit.MICROGRAMS);
        map.put(NutrientType.RETINOL, MassUnit.MICROGRAMS);
        map.put(NutrientType.VITAMIN_B12, MassUnit.MICROGRAMS);
        map.put(NutrientType.VITAMIN_D, MassUnit.MICROGRAMS);
        map.put(NutrientType.VITAMIN_K, MassUnit.MICROGRAMS);
        map.put(NutrientType.CAROTENE_ALPHA, MassUnit.MICROGRAMS);
        map.put(NutrientType.CAROTENE_BETA, MassUnit.MICROGRAMS);
        map.put(NutrientType.BIOTIN, MassUnit.MICROGRAMS);
        map.put(NutrientType.CHROMIUM, MassUnit.MICROGRAMS);
        map.put(NutrientType.MOLYBDENUM, MassUnit.MICROGRAMS);
        map.put(NutrientType.IODINE, MassUnit.MICROGRAMS);

        return map;
    }
}
