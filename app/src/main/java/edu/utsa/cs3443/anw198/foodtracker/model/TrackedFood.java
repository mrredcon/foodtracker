package edu.utsa.cs3443.anw198.foodtracker.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.time.Instant;

@Entity
public class TrackedFood {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long foodId;
    public long servingSizeId;

    public double amount;
    public Date dateConsumed;
}
