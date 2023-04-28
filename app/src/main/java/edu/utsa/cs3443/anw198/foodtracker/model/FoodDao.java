package edu.utsa.cs3443.anw198.foodtracker.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FoodDao {
    @Query("SELECT * FROM food")
    List<Food> getAll();

    @Query("SELECT * FROM food WHERE id = :foodId")
    Food getFood(long foodId);

    @Query("SELECT * FROM food WHERE onlineId = :onlineIdInput")
    Food getFoodByOnlineId(int onlineIdInput);

    @Query("SELECT * FROM servingsize WHERE foodId = :foodIdInput")
    List<ServingSize> getServingSizesFromFood(long foodIdInput);

    @Query("SELECT * FROM nutrient WHERE foodId = :foodIdInput")
    List<Nutrient> getNutrientsFromFood(long foodIdInput);

    @Query("SELECT * FROM trackedfood")
    List<TrackedFood> getAllTrackedFoods();

    @Transaction
    @Query("SELECT * FROM food WHERE id = :foodId")
    CompleteFood getCompleteFood(long foodId);

    @Transaction
    @Query("SELECT * FROM food WHERE id IN (:foodIds)")
    List<CompleteFood> getCompleteFoods(List<Long> foodIds);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertFood(Food food);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTrackedFood(TrackedFood trackedFood);

    //@Insert
    //void insertFood(Food food, List<ServingSize> servingSizes);

    @Insert
    long insertServingSize(ServingSize servingSizes);

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
    int deleteFood(Food food);

    @Delete
    void deleteServingSizes(List<ServingSize> servingSizes);
}
