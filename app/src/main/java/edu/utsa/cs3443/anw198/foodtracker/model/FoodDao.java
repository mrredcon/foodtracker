package edu.utsa.cs3443.anw198.foodtracker.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FoodDao {
    @Query("SELECT * FROM food")
    List<Food> getAll();

    @Query("SELECT * FROM food WHERE id = :foodId")
    Food getFood(int foodId);

    @Query("SELECT * FROM servingsize WHERE id = :foodId")
    List<ServingSize> getServingSizesFromFood(int foodId);

    @Query("SELECT * FROM nutrient WHERE id = :foodId")
    List<Nutrient> getNutrientsFromFood(int foodId);

    @Insert
    long insertFood(Food food);

    //@Insert
    //void insertFood(Food food, List<ServingSize> servingSizes);

    @Insert
    long[] insertServingSizes(List<ServingSize> servingSizes);

    @Insert
    long[] insertNutrients(List<Nutrient> nutrients);

    @Update
    void updateFood(Food food);

    @Update
    void updateServingSizes(List<ServingSize> servingSizes);

    @Update
    void updateNutrients(List<Nutrient> nutrients);

    @Delete
    void deleteFood(Food food);

    @Delete
    void deleteServingSizes(List<ServingSize> servingSizes);
}
