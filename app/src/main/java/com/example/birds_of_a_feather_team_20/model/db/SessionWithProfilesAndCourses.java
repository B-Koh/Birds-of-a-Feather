package com.example.birds_of_a_feather_team_20.model.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class SessionWithProfilesAndCourses {
    @Embedded public DBSession session;
    @Relation(
            entity = DBProfile.class,
            parentColumn = "dbSessionId",
            entityColumn = "profileSessionId"
    )
    public List<DBProfileWithCourses> profiles;
}
