package edu.utsa.cs3443.anw198.foodtracker.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import edu.utsa.cs3443.anw198.foodtracker.units.MassUnit;
import edu.utsa.cs3443.anw198.foodtracker.units.Unit;
import edu.utsa.cs3443.anw198.foodtracker.units.VolumeUnit;

@Entity
public class Food {
    @PrimaryKey(autoGenerate = true)
    public long id;
    private String name;

    // Name of serving size (e.g. "1 cup") mapped to grams
    //private Map<String, Double> servingSizes;

    // Calories in kcal
    private double calories;

    public static final double DEFAULT_QUANTITY = 100.0;

    private final boolean measuredInVolume;

    public Food(boolean measuredInVolume)
    {
        this.measuredInVolume = measuredInVolume;
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
        if (measuredInVolume) {
            return VolumeUnit.MILLILITERS;
        }

        return MassUnit.GRAMS;
    }

    public boolean getMeasuredInVolume() {
        return measuredInVolume;
    }
}