package edu.utsa.cs3443.anw198.foodtracker.model;

import edu.utsa.cs3443.anw198.foodtracker.R;

public enum Nutrient {
    FAT(R.string.nutrient_fat),
    FAT_SATURATED(R.string.nutrient_fat_saturated),
    FAT_TRANS(R.string.nutrient_fat_trans),

    CARBOHYDRATES(R.string.nutrient_carbohydrates),
    FIBER(R.string.nutrient_fiber),
    SUGAR(R.string.nutrient_sugar),
    SUGAR_ADDED(R.string.nutrient_sugar_added),
    SUGAR_ALCOHOL(R.string.nutrient_sugar_alcohol),
    ETHYL_ALCOHOL(R.string.nutrient_ethyl_alcohol),

    PROTEIN(R.string.nutrient_protein),

    CALCIUM(R.string.nutrient_calcium),
    CHOLESTEROL(R.string.nutrient_cholesterol),
    SODIUM(R.string.nutrient_sodium),
    IRON(R.string.nutrient_iron),
    MAGNESIUM(R.string.nutrient_magnesium),
    PHOSPHORUS(R.string.nutrient_phosphorus),
    POTASSIUM(R.string.nutrient_potassium),
    ZINC(R.string.nutrient_zinc),
    COPPER(R.string.nutrient_copper),
    THIAMIN(R.string.nutrient_thiamin),
    RIBOFLAVIN(R.string.nutrient_riboflavin),
    NIACIN(R.string.nutrient_niacin),
    CHOLINE(R.string.nutrient_choline),
    THEOBROMINE(R.string.nutrient_theobromine),
    SELENIUM(R.string.nutrient_selenium),
    RETINOL(R.string.nutrient_retinol),
    CAROTENE_ALPHA(R.string.nutrient_carotene_alpha),
    CAROTENE_BETA(R.string.nutrient_carotene_beta),
    FOLIC_ACID(R.string.nutrient_folic_acid),
    FOLATE(R.string.nutrient_folate),
    CAFFEINE(R.string.nutrient_caffeine),

    VITAMIN_A(R.string.nutrient_vitamin_a),
    VITAMIN_B6(R.string.nutrient_vitamin_b6),
    VITAMIN_B12(R.string.nutrient_vitamin_b12),
    VITAMIN_C(R.string.nutrient_vitamin_c),
    VITAMIN_D(R.string.nutrient_vitamin_d),
    VITAMIN_E(R.string.nutrient_vitamin_e),
    VITAMIN_K(R.string.nutrient_vitamin_k);

    public final int stringResource;

    Nutrient(int stringResource) {
        this.stringResource = stringResource;
    }
}
