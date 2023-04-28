package edu.utsa.cs3443.anw198.foodtracker.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CompleteFood {
    public CompleteFood(Food food, List<ServingSize> servingSizes, List<Nutrient> nutrients) {
        this.food = food;
        this.servingSizes = servingSizes;
        this.nutrients = nutrients;
    }

    @Embedded
    public Food food;

    @Relation(parentColumn = "id", entityColumn = "foodId")
    public List<ServingSize> servingSizes;

    @Relation(parentColumn = "id", entityColumn = "foodId")
    public List<Nutrient> nutrients;
}
