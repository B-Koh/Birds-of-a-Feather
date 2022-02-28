package com.example.birds_of_a_feather_team_20;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.birds_of_a_feather_team_20.model.db.Course;
import com.example.birds_of_a_feather_team_20.model.db.DBSession;
import com.example.birds_of_a_feather_team_20.model.db.SessionDao;
import com.example.birds_of_a_feather_team_20.model.db.SessionDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class SessionDatabaseBackendTest {
    private SessionDao sessionDao;
    private SessionDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        SessionDatabase.useTestSingleton(context);
        db = SessionDatabase.singleton(context);
        sessionDao = db.sessionDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.birds_of_a_feather_team_20", appContext.getPackageName());
    }

    @Test
    public void addSession(){
        DBSession testSession = new DBSession("testSession");
        sessionDao.insert(testSession);

        assertEquals(1, sessionDao.getAll().size());
        assertNotEquals(null, sessionDao.getSession("testSession"));
        assertEquals(null, sessionDao.getSession("notSession"));
    }

    @Test
    public void addProfiles(){
        DBSession testSession = new DBSession("testSession");

        Profile billClinton = new Profile("Bill Clinton",
                "https://upload.wikimedia.org/wikipedia/commons/d/d3/Bill_Clinton.jpg",
                "billId");

        sessionDao.insert(testSession);
        sessionDao.insertProfile("testSession", billClinton);

        assertEquals(1, sessionDao.getProfilesInSession("testSession").size());
        assertEquals("Bill Clinton", sessionDao.getProfilesInSession("testSession").get(0).getName());
    }

    @Test
    public void addCourses(){
        DBSession testSession = new DBSession("testSession");

        Profile billClinton = new Profile("Bill Clinton",
                "https://upload.wikimedia.org/wikipedia/commons/d/d3/Bill_Clinton.jpg",
                "billId");

        sessionDao.insert(testSession);
        sessionDao.insertProfile("testSession", billClinton);

        assertEquals(0, sessionDao.getCoursesInProfile("testSession", "billId").size());
        assertEquals(null, sessionDao.getCoursesInProfile("testSession", "notId"));
        assertEquals(null, sessionDao.getCoursesInProfile("notSession", "notId"));

        Course ECE45 = new Course(2022, "WI", "ECE", "45");
        Course CSE110 = new Course(2022, "WI", "CSE", "110");
        Course MATH20D = new Course(2021, "SP", "MATH", "20D");

        sessionDao.insertCourse("testSession", "billId", ECE45);

        assertEquals(1, sessionDao.getCoursesInProfile("testSession", "billId").size());

        sessionDao.insertCourse("testSession", "billId", CSE110);
        sessionDao.insertCourse("testSession", "billId", MATH20D);

        assertEquals(3, sessionDao.getCoursesInProfile("testSession", "billId").size());
    }

    @Test
    public void deleteSession(){
        DBSession testSession1 = new DBSession("testSession1");
        DBSession testSession2 = new DBSession("testSession2");

        sessionDao.insert(testSession1);
        sessionDao.insert(testSession2);
        assertEquals(2, sessionDao.getAll().size());

        sessionDao.delete("testSession1");
        assertEquals(1, sessionDao.getAll().size());
    }

    @Test
    public void clearSession(){
        DBSession testSession = new DBSession("testSession");

        Profile billClinton = new Profile("Bill Clinton",
                "https://upload.wikimedia.org/wikipedia/commons/d/d3/Bill_Clinton.jpg",
                "billId");
        Profile barackObama = new Profile("Barack Obama",
                "https://upload.wikimedia.org/wikipedia/commons/8/8d/President_Barack_Obama.jpg",
                "barackId");
        Profile georgeWBush = new Profile("George W. Bush",
                "https://upload.wikimedia.org/wikipedia/commons/d/d4/George-W-Bush.jpeg",
                "georgeId");

        sessionDao.insert(testSession);
        sessionDao.insertProfile("testSession", billClinton);
        sessionDao.insertProfile("testSession", barackObama);
        sessionDao.insertProfile("testSession", georgeWBush);

        assertEquals(3, sessionDao.getProfilesInSession("testSession").size());

        sessionDao.clearSession("testSession");
        assertEquals(0, sessionDao.getProfilesInSession("testSession").size());
    }
}
