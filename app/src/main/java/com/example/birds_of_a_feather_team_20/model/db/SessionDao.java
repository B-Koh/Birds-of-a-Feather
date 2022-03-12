package com.example.birds_of_a_feather_team_20.model.db;

import android.util.Log;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.birds_of_a_feather_team_20.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Dao
public abstract class SessionDao {
    @Transaction
    @Query("SELECT * FROM DBSession")
    public abstract List<DBSession> getAll();

    @Transaction
    @Query("SELECT * FROM DBProfile")
    public abstract List<DBProfileWithCourses> getAllProfiles();

    @Transaction
    @Query("SELECT * FROM DBSession WHERE sessionName=:sessionName")
    abstract DBSessionWithProfilesAndCourses getSessionWithProfilesAndCourses(String sessionName);

    @Transaction
    @Query("SELECT * FROM DBProfile WHERE profileSessionId=:sessionId AND profileId=:profileId")
    abstract DBProfileWithCourses getDBProfileWithCourses(int sessionId, String profileId);

    @Transaction
    @Query("SELECT * FROM DBCourse WHERE year=:year AND session=:session AND department=:department AND courseNumber=:courseNumber")
    abstract DBCourse getDBCourse(int year, String session, String department, String courseNumber);

    @Transaction
    @Query("SELECT * FROM DBCourseProfileCrossRef WHERE dbProfileId=:dbProfileId AND dbCourseId=:dbCourseId")
    abstract DBCourseProfileCrossRef getReference(int dbProfileId, int dbCourseId);

    @Transaction
    @Query("SELECT * FROM DBCourseProfileCrossRef WHERE dbProfileId=:dbProfileId")
    abstract List<DBCourseProfileCrossRef> getRefsInProfile(int dbProfileId);

    @Transaction
    public DBSession getSession(String sessionName){
        DBSessionWithProfilesAndCourses targetSession = getSessionWithProfilesAndCourses(sessionName);
        if(targetSession == null) return null;
        return targetSession.session;
    }

    @Transaction
    public List<Profile> getProfilesInSession(String sessionName){
        List<DBProfileWithCourses> dbProfiles = new ArrayList<DBProfileWithCourses>();
        List<Profile> profiles = new ArrayList<Profile>();
        DBSessionWithProfilesAndCourses targetSession = getSessionWithProfilesAndCourses(sessionName);

        if(targetSession == null){
            //Log.e("getProfiles error", "Target session not found");
            return profiles;
        }
        //Log.e("getProfiles no error", "Target session found");
        //Log.e("getProfiles", "Target Session profile size is " + targetSession.profiles.size());
        for(DBProfileWithCourses profile:targetSession.profiles) profiles.add(profile.toProfile());

        return profiles;
    }

    @Transaction
    public List<Profile> getFavoritesInSession(String sessionName){
        List<DBProfileWithCourses> dbProfiles = new ArrayList<DBProfileWithCourses>();
        List<Profile> profiles = new ArrayList<Profile>();
        DBSessionWithProfilesAndCourses targetSession = getSessionWithProfilesAndCourses(sessionName);

        if(targetSession == null){
            //Log.e("getProfiles error", "Target session not found");
            return profiles;
        }
        //Log.e("getProfiles no error", "Target session found");
        //Log.e("getProfiles", "Target Session profile size is " + targetSession.profiles.size());
        for(DBProfileWithCourses profile:targetSession.profiles){
            if(profile.dbProfile.isFavorite) profiles.add(profile.toProfile());
        }

        return profiles;
    }

    @Transaction
    public List<Profile> getAllFavorites(){
        List<Profile> profiles = new ArrayList<Profile>();

        for(DBProfileWithCourses profile:getAllProfiles()){
            if(profile.dbProfile.isFavorite) profiles.add(profile.toProfile());
        }

        return profiles;
    }

    @Transaction
    public List<Course> getCoursesInProfile(String sessionName, String profileId){
        DBSessionWithProfilesAndCourses targetSession = getSessionWithProfilesAndCourses(sessionName);
        if(targetSession == null) return null;

        DBProfileWithCourses targetProfile = getDBProfileWithCourses(targetSession.session.dbSessionId, profileId);
        if(targetProfile == null) return null;

        List<DBCourse> targetList = targetProfile.courses;
        List<Course> returnList = new ArrayList<Course>();
        for(DBCourse dbCourse:targetList) returnList.add(dbCourse.toCourse());

        return returnList;
    }

    @Transaction
    public long insert(DBSession session){
        DBSessionWithProfilesAndCourses possibleDuplicateSession = getSessionWithProfilesAndCourses(session.sessionName);
        if(possibleDuplicateSession != null) return -1;

        return insertSession(session);
    }

    @Insert
    abstract long insertSession(DBSession session);

    @Insert
    abstract long insertProfile(DBProfile dbProfile);

    @Insert
    abstract long insertCourse(DBCourse dbCourse);

    @Insert
    abstract long insertReference(DBCourseProfileCrossRef reference);

    @Transaction
    public void insertProfile(String sessionName, Profile profile) {
        DBSessionWithProfilesAndCourses targetSession = getSessionWithProfilesAndCourses(sessionName);
        if (targetSession == null) return;
        //Log.e("insertProfile", "Target Session id is " + targetSession.session.sessionId);

        DBProfileWithCourses targetProfile = getDBProfileWithCourses(targetSession.session.dbSessionId, profile.getId());
        if (targetProfile != null){
            deleteProfile(targetProfile.dbProfile);
            for(DBCourseProfileCrossRef reference:getRefsInProfile(targetProfile.dbProfile.dbProfileId)) deleteReference(reference);
        }

        targetProfile = new DBProfileWithCourses(profile);
        targetProfile.dbProfile.profileSessionId = targetSession.session.dbSessionId;

        insertProfile(targetProfile.dbProfile);
        Log.e("insertProfile", "Profile courses size " + profile.getCourses().size());
        for(Course course:profile.getCourses()){
            insertCourse(sessionName, profile.getId(), course);
        }



        //Log.e("insertProfile", "Target Session profile size is " + targetSession.profiles.size());
        //Log.e("insertProfile", "Target profile has session id " + targetProfile.dbProfile.profileSessionId);
    }

    @Transaction
    public void insertCourse(String sessionName, String profileId, Course course){
        //Log.e("insertCourse", "Is running with " + course.getCourseNumber());
        DBSessionWithProfilesAndCourses targetSession = getSessionWithProfilesAndCourses(sessionName);
        if(targetSession == null) return;
        //Log.e("insertCourse", "Found the session");

        DBProfileWithCourses targetProfile = getDBProfileWithCourses(targetSession.session.dbSessionId, profileId);
        if(targetProfile == null) return;
        //Log.e("insertCourse", "Found the profile");

        DBCourse targetCourse = getDBCourse(course.getYear(),
                course.getSession().toUpperCase(Locale.ROOT).replaceAll(" ", ""),
                course.getDepartment().toUpperCase(Locale.ROOT).replaceAll(" ", ""),
                course.getCourseNumber().toUpperCase(Locale.ROOT).replaceAll(" ", ""));
        int targetCourseId;
        if(targetCourse == null) {
            targetCourse = new DBCourse(course);
            targetCourseId = (int) insertCourse(targetCourse);
        } else {
            targetCourseId = targetCourse.dbCourseId;
            //Log.e("insertCourse", "Found a course " + course.getCourseNumber() + " to profile " + profileId);
        }

        //Log.e("insertCourse", "Is trying inserting " + course.getCourseNumber() + " to profile " + profileId);
        if(getReference(targetProfile.dbProfile.dbProfileId, targetCourseId) != null) return;
        //Log.e("insertCourse", "Is now inserting " + course.getCourseNumber() + " to profile " + profileId);
        insertReference(new DBCourseProfileCrossRef(targetProfile.dbProfile.dbProfileId, targetCourseId));
    }

    @Transaction
    public void updateSession(String sessionName, List<Profile> profiles){
        DBSessionWithProfilesAndCourses targetSession = getSessionWithProfilesAndCourses(sessionName);
        if(targetSession == null) {
            targetSession = new DBSessionWithProfilesAndCourses();
            targetSession.session.sessionName = sessionName;
            insert(targetSession.session);
        }

        for(Profile profile:profiles){
            insertProfile(sessionName, profile);
        }
    }

    @Delete
    abstract void deleteProfile(DBProfile dbProfile);

    @Delete
    abstract void deleteSession(DBSession dbSession);

    @Delete
    abstract void deleteReference(DBCourseProfileCrossRef ref);

    @Transaction
    public void clearSession(String sessionName){
        DBSessionWithProfilesAndCourses targetSession = getSessionWithProfilesAndCourses(sessionName);
        if(targetSession == null) return;

        for(DBProfileWithCourses profile:targetSession.profiles) deleteProfile(profile.dbProfile);
    }

    @Transaction
    public void renameSession(String sessionName, String newName){
        DBSessionWithProfilesAndCourses targetSession = getSessionWithProfilesAndCourses(sessionName);
        DBSessionWithProfilesAndCourses possibleDuplicateSession = getSessionWithProfilesAndCourses(newName);

        if(possibleDuplicateSession != null) return;

        Log.e("rename", "getting to the rename");
        deleteSession(targetSession.session);

        targetSession.session.sessionName = newName;
        targetSession.session.setSessionName(newName);

        insertSession(targetSession.session);
    }

    @Transaction
    public void delete(String sessionName){
        clearSession(sessionName);
        if(getSession(sessionName) != null) deleteSession(getSession(sessionName));
    }

    @Transaction
    public void deleteProfile(Profile profile){
        DBProfileWithCourses dbCopy = new DBProfileWithCourses(profile);
        deleteProfile(profile);
    }
}
