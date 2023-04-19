package edu.utsa.cs3443.anw198.foodtracker.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = { Food.class, ServingSize.class, Nutrient.class }, version = 1, exportSchema = false)
public abstract class NomDatabase extends RoomDatabase {
    public abstract FoodDao foodDao();
}
