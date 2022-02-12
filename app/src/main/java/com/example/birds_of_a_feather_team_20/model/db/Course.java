package com.example.birds_of_a_feather_team_20.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")
public class Course {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "course_id")
    private int courseId = 0;

    @ColumnInfo(name = "year")
    private int year;

    @ColumnInfo(name = "session")
    private String session;

    @ColumnInfo(name = "department")
    private String department;

    @ColumnInfo(name = "course_number")
    private String courseNumber; //Must be a string due to A/B courses

    public Course(int year, String session, String department, String courseNumber){
        this.year = year;
        this.session = session;
        this.department = department;
        this.courseNumber = courseNumber;
    }

    public int getCourseId(){ return courseId; }

    public void setCourseId(int courseId){ this.courseId = courseId; }

    public int getYear() { return year; }

    public void setYear(int year) { this.year = year; }

    public String getSession() { return session; }

    public void setSession(String session) { this.session = session; }

    public String getDepartment() { return department; }

    public void setDepartment(String department) { this.department = department; }

    public String getCourseNumber() { return courseNumber; }

    public void setCourseNumber(String courseNumber) { this.courseNumber = courseNumber; }


}
