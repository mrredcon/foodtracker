package edu.utsa.cs3443.anw198.foodtracker.providers;

import android.content.Context;

import androidx.room.Room;

import edu.utsa.cs3443.anw198.foodtracker.model.NomDatabase;

public class DbProvider {
    private static NomDatabase database = null;

    public static NomDatabase createInstance(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(),
                    NomDatabase.class, "nom.db").build();
        }

        return database;
    }

    public static NomDatabase getInstance() {
        return database;
    }
}
