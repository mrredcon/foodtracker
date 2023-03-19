package edu.utsa.cs3443.anw198.foodtracker.units;

import edu.utsa.cs3443.anw198.foodtracker.R;

public enum VolumeUnit implements Unit {
    LITERS(1.0, R.string.unit_liters, R.string.unit_liters_abv),
    MILLILITERS(1.0e-3, R.string.unit_milliliters, R.string.unit_milliliters_abv),
    MICROLITERS(1.0e-6, R.string.unit_microliters, R.string.unit_microliters_abv),
    TEASPOONS(0.00492892159375, R.string.unit_teaspoons, R.string.unit_teaspoons_abv),
    TABLESPOONS(0.0147867647813, R.string.unit_tablespoons, R.string.unit_tablespoons_abv),
    FLUID_OUNCES(0.0295735295625, R.string.unit_fluid_ounces, R.string.unit_fluid_ounces_abv),
    SHOTS(0.0443602943438, R.string.unit_shots, R.string.unit_shots_abv),
    CUPS(0.2365882365, R.string.unit_cups, R.string.unit_cups_abv),
    PINTS(0.473176473, R.string.unit_pints, R.string.unit_pints_abv),
    QUARTS(0.946352946, R.string.unit_quarts, R.string.unit_quarts_abv),
    GALLONS(3.785411784, R.string.unit_gallons, R.string.unit_gallons_abv);

    private double unitToLiter;
    private int titleResource;
    private int abbreviationResource;

    VolumeUnit(double unitToLiters, int titleResource, int abbreviationResource) {
        this.unitToLiter = unitToLiters;
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

    public double convertTo(VolumeUnit destinationUnit, double input) {
        double liters = input * this.unitToLiter;
        return (1.0 / destinationUnit.unitToLiter) * liters;
    }
}
