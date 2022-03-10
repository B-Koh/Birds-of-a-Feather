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

//    public int getDBProfileId() { return dbProfileId; }
//
//    public void setDBProfileId(int dbProfileId) { this.dbProfileId = dbProfileId; }
//
//    public int getProfileSessionId() { return profileSessionId; }
//
//    public void setProfileSessionId(int sessionId) { this.profileSessionId = sessionId; }
//
//    public int getProfileId() { return profileId; }
//
//    public void setProfileId(int profileId) { this.profileId = profileId; }
//
//    public String getName() { return name; }
//
//    public void setName(String name) { this.name = name; }
//
//    public String getPhotoURL() { return photoURL; }
//
//    public void setPhotoURL(String photoURL) { this.photoURL = photoURL; }
}

