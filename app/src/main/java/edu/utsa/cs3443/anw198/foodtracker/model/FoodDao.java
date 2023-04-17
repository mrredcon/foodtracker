package edu.utsa.cs3443.anw198.foodtracker.model;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FoodDao {
    @Query("SELECT * FROM food")
    List<Food> getAll();

    @Query("SELECT * FROM servingsize WHERE id = :foodId")
    List<ServingSize> getServingSizesFromFood(int foodId);
}
