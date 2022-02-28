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
}
