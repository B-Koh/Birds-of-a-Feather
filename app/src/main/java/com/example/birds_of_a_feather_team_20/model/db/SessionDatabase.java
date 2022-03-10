package com.example.birds_of_a_feather_team_20.model.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {DBSession.class, DBProfile.class, DBCourse.class, DBCourseProfileCrossRef.class}, version = 1)
public abstract class SessionDatabase extends RoomDatabase {
    private static SessionDatabase singletonInstance;

    public static SessionDatabase singleton(Context context) {
        if (singletonInstance == null) {
            singletonInstance = Room.databaseBuilder(context, SessionDatabase.class, "sessions.db")
                    .allowMainThreadQueries()
                    .build();
        }

        return singletonInstance;
    }

    public static void useTestSingleton(Context context) {
        singletonInstance = Room.inMemoryDatabaseBuilder(context, SessionDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    public abstract SessionDao sessionDao();
}
