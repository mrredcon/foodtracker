package edu.utsa.cs3443.anw198.foodtracker.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import edu.utsa.cs3443.anw198.foodtracker.model.units.MassUnit;
import edu.utsa.cs3443.anw198.foodtracker.model.units.Unit;
import edu.utsa.cs3443.anw198.foodtracker.model.units.VolumeUnit;

@Entity
public class Food {
    public static final double DEFAULT_QUANTITY = 100.0;

    @PrimaryKey(autoGenerate = true)
    public long id;
    private String name;

    // Calories in kcal
    private double calories;

    private final boolean measuredInVolume;

    private int onlineId;

    private DataOrigin origin;

    public Food(boolean measuredInVolume)
    {
        this.measuredInVolume = measuredInVolume;
        origin = DataOrigin.UNKNOWN;
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

    public int getOnlineId() {
        return onlineId;
    }

    public void setOnlineId(int onlineId) {
        this.onlineId = onlineId;
    }

    public DataOrigin getOrigin() {
        return origin;
    }

    public void setOrigin(DataOrigin origin) {
        this.origin = origin;
    }
}