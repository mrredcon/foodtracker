package edu.utsa.cs3443.anw198.foodtracker.units;

import edu.utsa.cs3443.anw198.foodtracker.R;

public enum MassUnit implements Unit {
    GRAMS(1.0, R.string.unit_grams, R.string.unit_grams_abv),
    KILOGRAMS(1000.0, R.string.unit_kilograms, R.string.unit_kilograms_abv),
    MILLIGRAMS(1.0e-3, R.string.unit_milligrams, R.string.unit_milligrams_abv),
    MICROGRAMS(1.0e-6, R.string.unit_micrograms, R.string.unit_micrograms_abv),
    OUNCES(28.349523125, R.string.unit_ounces, R.string.unit_ounces_abv),
    POUNDS(453.59237, R.string.unit_pounds, R.string.unit_pounds_abv);

    private double unitToGram;
    private int titleResource;
    private int abbreviationResource;

    MassUnit(double unitToGrams, int titleResource, int abbreviationResource) {
        this.unitToGram = unitToGrams;
        this.titleResource = titleResource;
        this.abbreviationResource = abbreviationResource;
    }

    @Override
    public int getTitleResource() {
        return titleResource;
    }

    @Override
    public int getAbbreviationResource() {
        return abbreviationResource;
    }

    public double convertTo(MassUnit destinationUnit, double input) {
        double grams = input * this.unitToGram;
        return (1.0 / destinationUnit.unitToGram) * grams;
    }
}
