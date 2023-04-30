package edu.utsa.cs3443.anw198.foodtracker.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class TrackedFood {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long foodId;
    public long servingSizeId;

    public double amount;
    public Date dateConsumed;

    public ServingSize findServingSize(CompleteFood completeFood) {
        for (ServingSize ss : completeFood.servingSizes) {
            if (ss.id == this.servingSizeId) {
                return ss;
            }
        }

        return null;
    }

    public double getAmountConsumed(CompleteFood completeFood) {
        ServingSize servingSize = findServingSize(completeFood);
        return amount * servingSize.amount;
    }

    public double getMultiplier(CompleteFood completeFood) {
        return getAmountConsumed(completeFood) / Food.DEFAULT_QUANTITY;
    }
}
