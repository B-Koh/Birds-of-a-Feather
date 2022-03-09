package com.example.birds_of_a_feather_team_20;

import com.example.birds_of_a_feather_team_20.model.db.Course;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

import android.util.JsonWriter;

import java.io.IOException;
import java.io.StringWriter;

@RunWith(RobolectricTestRunner.class)
public class CourseBackendTest {
    @Test
    public void courseEquals(){
        Course ECE45 = new Course(2022, "WI", "ECE", "45");
        Course AnotherECE45 = new Course(2022, "WI", "ECE", "45");
        Course CSE110 = new Course(2022, "WI", "CSE", "110");

        assertEquals(true, ECE45.equals(AnotherECE45));
        assertNotEquals(true, ECE45.equals(CSE110));
    }

    @Test
    public void caseInsensitiveEquals(){
        Course ECE45 = new Course(2022, "WI", "ECE", "45");
        Course ECE45Lowercase = new Course(2022, "wi", "ece", "45");

        assertEquals(true, ECE45.equals(ECE45Lowercase));
    }

    @Test
    public void spaceInsensitiveEquals(){
        Course ECE45 = new Course(2022, "WI", "ECE", "45");
        Course ECE45WithSpaces = new Course(2022, "W I", "E C E", "  45  ");

        assertEquals(true, ECE45.equals(ECE45WithSpaces));
    }

    @Test
    public void testGetters() {
        Course course1 = new Course(2020, "WI", "CSE", "100", 1);
        assertEquals(course1.getYear(), 2020);
        assertEquals(course1.getSession(), "WI");
        assertEquals(course1.getDepartment(), "CSE");
        assertEquals(course1.getCourseNumber(), "100");
        assertEquals(course1.getClassSize(), 1);
    }

    @Test
    public void serialize() throws IOException {
//        Course course1 = new Course(2020, "WI", "CSE", "100", 1);
//
//        StringWriter out = new StringWriter();
//        JsonWriter writer = new JsonWriter(out);
//
//        course1.writeCourse(writer);
//
//        writer.close();
//        String serialized = out.toString();
        Profile profile = new Profile("my name", "photo", "74891");
        profile.serialize();


    }
}
