package com.example.birds_of_a_feather_team_20.model.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CourseDao {
    @Query("SELECT * FROM courses")
    List<Course> getAll();

    @Query("SELECT * FROM courses WHERE course_id=:id")
    Course get(int id);

    @Insert
    void insert(Course course);

    @Delete
    void delete(Course course);

    @Query("SELECT COUNT(*) FROM courses")
    int count();
}
