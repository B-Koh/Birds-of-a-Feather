package com.example.birds_of_a_feather_team_20.model.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.birds_of_a_feather_team_20.model.db.Course;

import java.util.List;

@Database(entities = {Course.class}, version = 1)
public abstract class CourseDatabase extends RoomDatabase{
    private static CourseDatabase singletonInstance;

    public static CourseDatabase singleton(Context context) {
        if (singletonInstance == null) {
            singletonInstance = Room.databaseBuilder(context, CourseDatabase.class, "courses.db")
                    .allowMainThreadQueries()
                    .build();
        }

        return singletonInstance;
    }

    public static void useTestSingleton(Context context) {
        singletonInstance = Room.inMemoryDatabaseBuilder(context, CourseDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    public abstract CourseDao courseDao();
}
