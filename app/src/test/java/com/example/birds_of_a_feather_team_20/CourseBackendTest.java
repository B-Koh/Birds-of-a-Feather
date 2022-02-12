package com.example.birds_of_a_feather_team_20;

import com.example.birds_of_a_feather_team_20.model.db.Course;

import org.junit.Test;
import static org.junit.Assert.*;

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
}
