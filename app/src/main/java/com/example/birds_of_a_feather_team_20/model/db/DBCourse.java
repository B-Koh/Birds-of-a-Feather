package com.example.birds_of_a_feather_team_20.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Locale;

@Entity
public class DBCourse {
    @PrimaryKey(autoGenerate = true)
    //@ColumnInfo(name = "dbCourse_id")
    public int dbCourseId;

    //@ColumnInfo(name = "year")
    public int year;
    //@ColumnInfo(name = "session")
    public String session;
    //@ColumnInfo(name = "department")
    public String department;
    //@ColumnInfo(name = "course_number")
    public String courseNumber; //Must be a string due to A/B courses

    public int classSize;

    public DBCourse(int year, String session, String department, String courseNumber, int classSize) {
        this.year = year;
        this.session = session.toUpperCase(Locale.ROOT).replaceAll(" ", "");
        this.department = department.toUpperCase(Locale.ROOT).replaceAll(" ", "");
        this.courseNumber = courseNumber.toUpperCase(Locale.ROOT).replaceAll(" ", "");
        this.classSize = classSize;
    }

    public DBCourse(Course course){
        year = course.getYear();
        session = course.getSession();
        department = course.getDepartment();
        courseNumber = course.getCourseNumber();
        classSize = course.getClassSize();
    }

    public Course toCourse(){
        return new Course(year, session, department, courseNumber, classSize);
    }
}
