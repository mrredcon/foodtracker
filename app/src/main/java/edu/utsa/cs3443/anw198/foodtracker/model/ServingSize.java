package edu.utsa.cs3443.anw198.foodtracker.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(/*foreignKeys ={
        @ForeignKey(onDelete = ForeignKey.CASCADE,entity = Food.class,
                parentColumns = "id",childColumns = "foodId")}*/)
public class ServingSize {

    @Ignore
    public ServingSize(String name, double amount) {
        this(name, amount, 0);
    }

    public ServingSize(String name, double amount, long foodId) {
        this.name = name;
        this.amount = amount;
        this.foodId = foodId;
    }

    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public double amount;
    public long foodId;
}
