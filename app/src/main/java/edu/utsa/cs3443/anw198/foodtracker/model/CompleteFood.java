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

    public Nutrient getNutrient(NutrientType nutrientType) {
        for (Nutrient nutrient : nutrients) {
            if (nutrient.nutrientType == nutrientType) {
                return nutrient;
            }
        }

        return null;
    }

//    public String[] getServingSizeNames() {
//        String[] names = new String[servingSizes.size()];
//        for (int i = 0; i < names.length; i++) {
//            names[i] = servingSizes.get(i).name;
//        }
//
//        return names;
//    }

    public ServingSize getServingSizeFromName(String name) {
        if (name == null) {
            return null;
        }
        for (ServingSize ss : servingSizes) {
            if (ss.name.equals(name)) {
                return ss;
            }
        }
        return null;
    }
}
