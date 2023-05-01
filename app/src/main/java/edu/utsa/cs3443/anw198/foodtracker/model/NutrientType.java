package edu.utsa.cs3443.anw198.foodtracker.model;

import android.content.Context;
import android.content.res.Resources;

import java.util.HashMap;
import java.util.Map;

import edu.utsa.cs3443.anw198.foodtracker.R;

public enum NutrientType {
    FAT(R.string.nutrient_fat, R.dimen.nutrient_dv_fat),
    FAT_SATURATED(R.string.nutrient_fat_saturated, R.dimen.nutrient_dv_fat_saturated),
    FAT_TRANS(R.string.nutrient_fat_trans, R.dimen.nutrient_dv_fat_trans),

    CARBOHYDRATES(R.string.nutrient_carbohydrates, R.dimen.nutrient_dv_carbohydrates),
    FIBER(R.string.nutrient_fiber, R.dimen.nutrient_dv_fiber),
    SUGAR(R.string.nutrient_sugar, R.dimen.nutrient_dv_sugar),
    SUGAR_ADDED(R.string.nutrient_sugar_added, R.dimen.nutrient_dv_sugar_added),
    SUGAR_ALCOHOL(R.string.nutrient_sugar_alcohol, R.dimen.nutrient_dv_sugar_alcohol),
    ETHYL_ALCOHOL(R.string.nutrient_ethyl_alcohol, R.dimen.nutrient_dv_ethyl_alcohol),

    PROTEIN(R.string.nutrient_protein, R.dimen.nutrient_dv_protein),

    CALCIUM(R.string.nutrient_calcium, R.dimen.nutrient_dv_calcium),
    CHOLESTEROL(R.string.nutrient_cholesterol, R.dimen.nutrient_dv_cholesterol),
    SODIUM(R.string.nutrient_sodium, R.dimen.nutrient_dv_sodium),
    IRON(R.string.nutrient_iron, R.dimen.nutrient_dv_iron),
    MAGNESIUM(R.string.nutrient_magnesium, R.dimen.nutrient_dv_magnesium),
    PHOSPHORUS(R.string.nutrient_phosphorus, R.dimen.nutrient_dv_phosphorus),
    POTASSIUM(R.string.nutrient_potassium, R.dimen.nutrient_dv_potassium),
    ZINC(R.string.nutrient_zinc, R.dimen.nutrient_dv_zinc),
    COPPER(R.string.nutrient_copper, R.dimen.nutrient_dv_copper),
    THIAMIN(R.string.nutrient_thiamin, R.dimen.nutrient_dv_thiamin),
    RIBOFLAVIN(R.string.nutrient_riboflavin, R.dimen.nutrient_dv_riboflavin),
    NIACIN(R.string.nutrient_niacin, R.dimen.nutrient_dv_niacin),
    CHOLINE(R.string.nutrient_choline, R.dimen.nutrient_dv_choline),
    THEOBROMINE(R.string.nutrient_theobromine, R.dimen.nutrient_dv_theobromine),
    SELENIUM(R.string.nutrient_selenium, R.dimen.nutrient_dv_selenium),
    RETINOL(R.string.nutrient_retinol, R.dimen.nutrient_dv_retinol),
    CAROTENE_ALPHA(R.string.nutrient_carotene_alpha, R.dimen.nutrient_dv_carotene_alpha),
    CAROTENE_BETA(R.string.nutrient_carotene_beta, R.dimen.nutrient_dv_carotene_beta),
    FOLIC_ACID(R.string.nutrient_folic_acid, R.dimen.nutrient_dv_folic_acid),
    FOLATE(R.string.nutrient_folate, R.dimen.nutrient_dv_folate),
    CAFFEINE(R.string.nutrient_caffeine, R.dimen.nutrient_dv_caffeine),
    MANGANESE(R.string.nutrient_manganese, R.dimen.nutrient_dv_manganese),
    BIOTIN(R.string.nutrient_biotin, R.dimen.nutrient_dv_manganese),
    CHLORIDE(R.string.nutrient_chloride, R.dimen.nutrient_dv_chloride),
    CHROMIUM(R.string.nutrient_chromium, R.dimen.nutrient_dv_chromium),
    MOLYBDENUM(R.string.nutrient_molybdenum, R.dimen.nutrient_dv_molybdenum),
    PANTOTHENIC_ACID(R.string.nutrient_pantothenic_acid, R.dimen.nutrient_dv_pantothenic_acid),
    IODINE(R.string.nutrient_iodine, R.dimen.nutrient_dv_iodine),


    VITAMIN_A(R.string.nutrient_vitamin_a, R.dimen.nutrient_dv_vitamin_a),
    VITAMIN_B6(R.string.nutrient_vitamin_b6, R.dimen.nutrient_dv_vitamin_b6),
    VITAMIN_B12(R.string.nutrient_vitamin_b12, R.dimen.nutrient_dv_vitamin_b12),
    VITAMIN_C(R.string.nutrient_vitamin_c, R.dimen.nutrient_dv_vitamin_c),
    VITAMIN_D(R.string.nutrient_vitamin_d, R.dimen.nutrient_dv_vitamin_d),
    VITAMIN_E(R.string.nutrient_vitamin_e, R.dimen.nutrient_dv_vitamin_e),
    VITAMIN_K(R.string.nutrient_vitamin_k, R.dimen.nutrient_dv_vitamin_k);


    public final int stringResource;
    public final int dailyValueResource;
    private static Map<Integer, NutrientType> nutrientOrder = null;

    NutrientType(int stringResource, int dailyValueResource) {
        this.stringResource = stringResource;
        this.dailyValueResource = dailyValueResource;
    }

    public static Map<Integer, NutrientType> getNutrientOrder(Context context) {
        if (nutrientOrder != null) {
            return nutrientOrder;
        }

        Map<Integer, NutrientType> map = new HashMap<>();
        Resources res = context.getResources();;

        map.put(res.getInteger(R.integer.nutrient_order_fat), NutrientType.FAT);
        map.put(res.getInteger(R.integer.nutrient_order_fat_saturated), NutrientType.FAT_SATURATED);
        map.put(res.getInteger(R.integer.nutrient_order_fat_trans), NutrientType.FAT_TRANS);
        map.put(res.getInteger(R.integer.nutrient_order_cholesterol), NutrientType.CHOLESTEROL);
        map.put(res.getInteger(R.integer.nutrient_order_sodium), NutrientType.SODIUM);
        map.put(res.getInteger(R.integer.nutrient_order_carbohydrates), NutrientType.CARBOHYDRATES);
        map.put(res.getInteger(R.integer.nutrient_order_fiber), NutrientType.FIBER);
        map.put(res.getInteger(R.integer.nutrient_order_sugar), NutrientType.SUGAR);
        map.put(res.getInteger(R.integer.nutrient_order_sugar_added), NutrientType.SUGAR_ADDED);
        map.put(res.getInteger(R.integer.nutrient_order_sugar_alcohol), NutrientType.SUGAR_ALCOHOL);
        map.put(res.getInteger(R.integer.nutrient_order_protein), NutrientType.PROTEIN);

        map.put(res.getInteger(R.integer.nutrient_order_vitamin_a), NutrientType.VITAMIN_A);
        map.put(res.getInteger(R.integer.nutrient_order_vitamin_b6), NutrientType.VITAMIN_B6);
        map.put(res.getInteger(R.integer.nutrient_order_vitamin_b12), NutrientType.VITAMIN_B12);
        map.put(res.getInteger(R.integer.nutrient_order_vitamin_c), NutrientType.VITAMIN_C);
        map.put(res.getInteger(R.integer.nutrient_order_vitamin_d), NutrientType.VITAMIN_D);
        map.put(res.getInteger(R.integer.nutrient_order_vitamin_e), NutrientType.VITAMIN_E);
        map.put(res.getInteger(R.integer.nutrient_order_vitamin_k), NutrientType.VITAMIN_K);

        map.put(res.getInteger(R.integer.nutrient_order_calcium), NutrientType.CALCIUM);
        map.put(res.getInteger(R.integer.nutrient_order_iron), NutrientType.IRON);
        map.put(res.getInteger(R.integer.nutrient_order_magnesium), NutrientType.MAGNESIUM);
        map.put(res.getInteger(R.integer.nutrient_order_phosphorus), NutrientType.PHOSPHORUS);
        map.put(res.getInteger(R.integer.nutrient_order_potassium), NutrientType.POTASSIUM);
        map.put(res.getInteger(R.integer.nutrient_order_zinc), NutrientType.ZINC);
        map.put(res.getInteger(R.integer.nutrient_order_copper), NutrientType.COPPER);
        map.put(res.getInteger(R.integer.nutrient_order_thiamin), NutrientType.THIAMIN);
        map.put(res.getInteger(R.integer.nutrient_order_riboflavin), NutrientType.RIBOFLAVIN);
        map.put(res.getInteger(R.integer.nutrient_order_niacin), NutrientType.NIACIN);
        map.put(res.getInteger(R.integer.nutrient_order_choline), NutrientType.CHOLINE);
        map.put(res.getInteger(R.integer.nutrient_order_theobromine), NutrientType.THEOBROMINE);
        map.put(res.getInteger(R.integer.nutrient_order_selenium), NutrientType.SELENIUM);
        map.put(res.getInteger(R.integer.nutrient_order_retinol), NutrientType.RETINOL);
        map.put(res.getInteger(R.integer.nutrient_order_carotene_alpha), NutrientType.CAROTENE_ALPHA);
        map.put(res.getInteger(R.integer.nutrient_order_carotene_beta), NutrientType.CAROTENE_BETA);
        map.put(res.getInteger(R.integer.nutrient_order_folic_acid), NutrientType.FOLIC_ACID);
        map.put(res.getInteger(R.integer.nutrient_order_folate), NutrientType.FOLATE);
        map.put(res.getInteger(R.integer.nutrient_order_caffeine), NutrientType.CAFFEINE);
        map.put(res.getInteger(R.integer.nutrient_order_ethyl_alcohol), NutrientType.ETHYL_ALCOHOL);

        nutrientOrder = map;
        return map;
    }

    public static NutrientType getNutrientTypeFromOrder(Context context, int order) {
        return getNutrientOrder(context).get(order);
    }
}
