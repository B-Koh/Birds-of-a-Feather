package com.example.birds_of_a_feather_team_20;


import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.birds_of_a_feather_team_20.model.db.Course;
import com.example.birds_of_a_feather_team_20.model.db.CourseDao;
import com.example.birds_of_a_feather_team_20.model.db.CourseDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.manipulation.Ordering;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class AddingCoursesTest {
    private CourseDao courseDao;

    private CourseDatabase db;
    private MyProfile mp;

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
    public void testInsertCourse() {
        Course firstCourse = new Course(2021, "Fall", "CSE", "100");
        courseDao.insert(firstCourse);

        Course secondCourse = new Course(2022, "Winter", "CSE", "110");
        courseDao.insert(secondCourse);

        List<Course> currCourses = courseDao.getAll();
        mp.setMyCourses(currCourses);

        assertEquals(mp.getMyCourses().size(), 2);
    }

    @Test
    public void deleteNonExistentCourse() {
        Course firstCourse = new Course(2021, "Fall", "CSE", "100");
        courseDao.insert(firstCourse);

        Course secondCourse = new Course(2022, "Winter", "CSE", "110");
        courseDao.insert(secondCourse);

        Course nonExistentCourse = new Course(2022, "Winter", "CSE", "120");
        courseDao.delete(nonExistentCourse);

        List<Course> currCourses = courseDao.getAll();
        mp.setMyCourses(currCourses);

        assertEquals(mp.getMyCourses().size(), 2);
    }

    @Test
    public void deleteExistingCourse() {
        Course firstCourse = new Course(2021, "Fall", "CSE", "100");
        Course secondCourse = new Course(2022, "Winter", "CSE", "110");

        courseDao.insert(firstCourse);
        courseDao.insert(secondCourse);

        courseDao.delete(courseDao.get(2022, "Winter", "CSE", "110"));

        List<Course> currCourses = courseDao.getAll();
        mp.setMyCourses(currCourses);

        assertEquals(mp.getMyCourses().size(), 1);
    }
}
