package com.example.birds_of_a_feather_team_20.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"dbProfileId", "dbCourseId"})
public class DBCourseProfileCrossRef {
    public int dbProfileId;
    public int dbCourseId;

    public DBCourseProfileCrossRef(int dbProfileId, int dbCourseId) {
        this.dbProfileId = dbProfileId;
        this.dbCourseId = dbCourseId;
    }
}
