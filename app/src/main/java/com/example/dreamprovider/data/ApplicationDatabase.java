package com.example.dreamprovider.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.dreamprovider.DreamDao;
import com.example.dreamprovider.model.Dream;

@Database(entities = {Dream.class}, version = 1)
abstract class ApplicationDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "dream_db";
    private static ApplicationDatabase INSTANCE;

    static ApplicationDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context,
                            ApplicationDatabase.class, DATABASE_NAME)
                    .build();
        }
        return INSTANCE;
    }

    public abstract DreamDao getDreamDao();

}
