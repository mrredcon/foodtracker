package edu.utsa.cs3443.anw198.foodtracker.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import edu.utsa.cs3443.anw198.foodtracker.converters.DateConverter;

@Database(entities = { Food.class, ServingSize.class, Nutrient.class, TrackedFood.class }, version = 1, exportSchema = false)
@TypeConverters({ DateConverter.class })
public abstract class NomDatabase extends RoomDatabase {
    public abstract FoodDao foodDao();
}
