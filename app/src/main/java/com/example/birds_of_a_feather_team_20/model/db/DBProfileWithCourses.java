package com.example.birds_of_a_feather_team_20.model.db;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.birds_of_a_feather_team_20.Profile;

import java.util.ArrayList;
import java.util.List;

public class DBProfileWithCourses {
    @Embedded public DBProfile dbProfile;
    @Relation(
            parentColumn = "dbProfileId",
            entityColumn = "dbCourseId",
            associateBy = @Junction(DBCourseProfileCrossRef.class)
    )
    public List<DBCourse> courses;

    public DBProfileWithCourses(DBProfile dbProfile, List<DBCourse> courses) {
        this.dbProfile = dbProfile;
        this.courses = courses;
    }

    public DBProfileWithCourses(Profile profile){
        dbProfile = new DBProfile();

        dbProfile.name = profile.getName();
        dbProfile.profileId = profile.getId();
        dbProfile.photoURL = profile.getPhotoURL();
        dbProfile.isFavorite = profile.getIsFavorite();

        if(profile.getSessionId() != -1){
            dbProfile.profileSessionId = profile.getSessionId();
        }
    }

    public Profile toProfile(){
        Profile newProfile = new Profile(dbProfile.name, dbProfile.photoURL, dbProfile.profileId);
        newProfile.setSessionId(this.dbProfile.profileSessionId);
        if(dbProfile.isFavorite) newProfile.setFavorite();
        for(DBCourse dbCourse:courses)newProfile.addCourse(dbCourse.toCourse());
        return newProfile;
    }
}
