package com.example.birds_of_a_feather_team_20;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.birds_of_a_feather_team_20.model.db.Course;
import com.example.birds_of_a_feather_team_20.model.db.CourseDao;
import com.example.birds_of_a_feather_team_20.model.db.CourseDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class DatabaseBackendTest {
    private CourseDao courseDao;
    private CourseDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        CourseDatabase.useTestSingleton(context);
        db = CourseDatabase.singleton(context);
        courseDao = db.courseDao();
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
    public void addCourses() {
        Course ECE45 = new Course(2022, "WI", "ECE", "45");
        Course CSE110 = new Course(2022, "WI", "CSE", "110");
        Course MATH20D = new Course(2021, "SP", "MATH", "20D");

        courseDao.insert(ECE45);
        courseDao.insert(CSE110);
        courseDao.insert(MATH20D);

        assertEquals(3, courseDao.count());


        assertNotEquals(null, courseDao.get(2021, "SP", "MATH", "20D"));
        assertEquals(null, courseDao.get(2021, "WI", "WCWP", "10A"));
    }

    @Test
    public void removeCourse() {
        Course ECE45 = new Course(2022, "WI", "ECE", "45");
        Course CSE110 = new Course(2022, "WI", "CSE", "110");
        Course MATH20D = new Course(2021, "SP", "MATH", "20D");

        courseDao.insert(ECE45);
        courseDao.insert(CSE110);
        courseDao.insert(MATH20D);

        courseDao.delete(courseDao.get(2021, "SP", "MATH", "20D"));

        assertEquals(2, courseDao.count());
    }
}
