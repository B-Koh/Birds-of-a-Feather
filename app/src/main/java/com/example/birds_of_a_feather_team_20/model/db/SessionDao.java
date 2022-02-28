package com.example.birds_of_a_feather_team_20.model.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.birds_of_a_feather_team_20.Profile;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class SessionDao {
    @Transaction
    @Query("SELECT * FROM DBSession")
    public abstract List<DBSession> getAll();

    @Transaction
    @Query("SELECT * FROM DBSession WHERE sessionName=:sessionName")
    abstract SessionWithProfilesAndCourses getSessionWithProfilesAndCourses(String sessionName);

    @Transaction
    @Query("SELECT * FROM DBProfile WHERE profileSessionId=:sessionId AND profileId=:profileId")
    abstract DBProfileWithCourses getDBProfileWithCourses(int sessionId, String profileId);

    @Transaction
    @Query("SELECT * FROM DBCourse WHERE year=:year AND session=:session AND department=:department AND courseNumber=:courseNumber")
    abstract DBCourse getDBCourse(int year, String session, String department, String courseNumber);

    @Transaction
    public DBSession getSession(String sessionName){
        SessionWithProfilesAndCourses targetSession = getSessionWithProfilesAndCourses(sessionName);
        if(targetSession == null) return null;
        return targetSession.session;
    }

    @Transaction
    public List<Profile> getProfilesInSession(String sessionName){
        List<DBProfileWithCourses> dbProfiles = new ArrayList<DBProfileWithCourses>();
        List<Profile> profiles = new ArrayList<Profile>();
        SessionWithProfilesAndCourses targetSession = getSessionWithProfilesAndCourses(sessionName);

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
    public List<Course> getCoursesInProfile(String sessionName, String profileId){
        SessionWithProfilesAndCourses targetSession = getSessionWithProfilesAndCourses(sessionName);
        if(targetSession == null) return null;

        DBProfileWithCourses targetProfile = getDBProfileWithCourses(targetSession.session.dbSessionId, profileId);
        if(targetProfile == null) return null;

        List<DBCourse> targetList = targetProfile.courses;
        List<Course> returnList = new ArrayList<Course>();
        for(DBCourse dbCourse:targetList) returnList.add(dbCourse.toCourse());

        return returnList;
    }

    @Insert
    public abstract long insert(DBSession session);

    @Insert
    abstract long insertProfile(DBProfile dbProfile);

    @Insert
    abstract long insertCourse(DBCourse dbCourse);

    @Insert
    abstract long insertReference(DBCourseProfileCrossRef reference);

    @Delete
    public abstract void delete(DBSession session);

    @Transaction
    public void insertProfile(String sessionName, Profile profile){
        SessionWithProfilesAndCourses targetSession = getSessionWithProfilesAndCourses(sessionName);
        if(targetSession == null) return;
        //Log.e("insertProfile", "Target Session id is " + targetSession.session.sessionId);

        DBProfileWithCourses targetProfile = new DBProfileWithCourses(profile);
        targetProfile.dbProfile.profileSessionId = targetSession.session.dbSessionId;
        insertProfile(targetProfile.dbProfile);

        //Log.e("insertProfile", "Target Session profile size is " + targetSession.profiles.size());
        //Log.e("insertProfile", "Target profile has session id " + targetProfile.dbProfile.profileSessionId);
    }

    @Transaction
    public void insertCourse(String sessionName, String profileId, Course course){
        SessionWithProfilesAndCourses targetSession = getSessionWithProfilesAndCourses(sessionName);
        if(targetSession == null) return;

        DBProfileWithCourses targetProfile = getDBProfileWithCourses(targetSession.session.dbSessionId, profileId);
        if(targetProfile == null) return;

        DBCourse targetCourse = getDBCourse(course.getYear(), course.getSession(), course.getDepartment(), course.getCourseNumber());
        if(targetCourse == null) targetCourse = new DBCourse(course);

        int targetCourseId = (int)insertCourse(targetCourse);

        insertReference(new DBCourseProfileCrossRef(targetProfile.dbProfile.dbProfileId, targetCourseId));
    }
}
