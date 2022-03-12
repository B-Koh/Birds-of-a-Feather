package com.example.birds_of_a_feather_team_20;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import com.example.birds_of_a_feather_team_20.model.db.Course;
import com.example.birds_of_a_feather_team_20.model.db.CourseDao;
import com.example.birds_of_a_feather_team_20.model.db.CourseDatabase;
import com.example.birds_of_a_feather_team_20.wave.WaveManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class StopSessionTest {

    private CourseDao courseDao;
    private CourseDatabase db;
    private MyProfile mp;
    String[] currCourses;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        mp = MyProfile.singleton(context);
        db = Room.inMemoryDatabaseBuilder(context, CourseDatabase.class)
                .allowMainThreadQueries()
                .build();
        courseDao = db.courseDao();
    }

    @After
    public void closeDb() {db.close();}

    @Test
    public void testCurrentCourse() {

        Course firstCourse = new Course(2021, "FA", "CSE", "100");
        courseDao.insert(firstCourse);

        Course secondCourse = new Course(2022, "WI", "CSE", "110");
        courseDao.insert(secondCourse);

        List<Course> courses = courseDao.getAll();
        mp.setMyCourses(courses);

        ActivityScenario<StopSessionActivity> scenario = ActivityScenario.launch(StopSessionActivity.class);
        scenario.onActivity(activity -> {

            currCourses = activity.current_classes;

            assertEquals(currCourses[1], "CSE 110");
        });

    }
}