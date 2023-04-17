package edu.utsa.cs3443.anw198.foodtracker.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ServingSize {
    @PrimaryKey
    public int id;
    public double amount;
    public String name;
    public int foodId;
}
