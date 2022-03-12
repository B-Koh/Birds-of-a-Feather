package com.example.birds_of_a_feather_team_20.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DBProfile {
    @PrimaryKey(autoGenerate = true)
    //@ColumnInfo(name = "dbProfile_id")
    public int dbProfileId;

    //@ColumnInfo(name = "profile_session_id")
    public int profileSessionId; //The session to which the profile is associated.

    //@ColumnInfo(name = "profile_id")
    public String profileId; //Device ID from associated profile.
    //@ColumnInfo(name = "name")
    public String name;
    //@ColumnInfo(name = "url")
    public String photoURL;

    public boolean isFavorite;
}

