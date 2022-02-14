package com.example.birds_of_a_feather_team_20;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.birds_of_a_feather_team_20.model.db.Course;
import com.example.birds_of_a_feather_team_20.model.db.CourseDao;
import com.example.birds_of_a_feather_team_20.model.db.CourseDatabase;

import java.util.List;

/**
 * This class consists of the UI necessary for adding, editing, and deleting courses.
 * It stores the course's subject, course number, year, and quarter.
 */
public class EditCourses extends AppCompatActivity {
    int year;
    Integer[] yearList = {2022, 2021, 2020, 2019, 2018};
    String quarter;
    String[] quarterList = {"Fall", "Winter", "Spring", "Summer Session I", "Summer Session II"};
    String subject;
    String courseNumber;
    CourseDatabase db;
    MyProfile mp;
    List<Course> myCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        db = CourseDatabase.singleton(this);
        mp = MyProfile.singleton(this);
//        myCourses = db.courseDao().getAll();
//        myCourses = mp.getMyCourses();

        Spinner year_dropdown = findViewById(R.id.year_dropdown);
        ArrayAdapter<Integer> year_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yearList);
        year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        year_dropdown.setAdapter(year_adapter);


        Spinner quarter_dropdown = findViewById(R.id.quarter_dropdown);
        ArrayAdapter<String> quarter_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, quarterList);
        quarter_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        quarter_dropdown.setAdapter(quarter_adapter);
    }


    public void onSaveExitClicked(View view) {
        Course newCourse = getCourseInfo();
        addCourse(newCourse);

        // goes to Personal Profile Page
//        Intent intent = new Intent(this, __.class);
//        startActivity(intent);
    }

    public void onDeleteClicked(View view) {
        Course newCourse = getCourseInfo();
        deleteCourse(newCourse);
        // goes to Personal Profile Page
//        Intent intent = new Intent(this, __.class);
//        startActivity(intent);
    }

    public void onSaveAddClicked(View view) {
        Course newCourse = getCourseInfo();
        addCourse(newCourse);
        // reload intent with blank template
        Intent intent = new Intent(this, EditCourses.class);
        startActivity(intent);
    }

    /**
     * Helper Method: Helps us save all the information entered by the User.
     */
    private Course getCourseInfo() {
        TextView subjectView = findViewById(R.id.subject_edit_text);
        subject = subjectView.getText().toString();

        TextView courseNumberView = findViewById(R.id.course_num_edit_text);
        courseNumber = subjectView.getText().toString();

        Spinner year_spinner = findViewById(R.id.year_dropdown);
        year = Integer.parseInt(year_spinner.getSelectedItem().toString());

        Spinner quarter_spinner = findViewById(R.id.quarter_dropdown);
        quarter = quarter_spinner.getSelectedItem().toString();

        Course currCourse = new Course(year, quarter, subject, courseNumber);

        return currCourse;
    }

    private void addCourse(Course newCourse) {
        db.courseDao().insert(newCourse);
        myCourses = db.courseDao().getAll();
        mp.setMyCourses(myCourses);
    }

    private void deleteCourse(Course newCourse) {
        if(db.courseDao().get(year, quarter, subject, courseNumber) != null) {
            db.courseDao().delete(db.courseDao().get(year, quarter, subject, courseNumber));
        }
        myCourses = db.courseDao().getAll();
        mp.setMyCourses(myCourses);
    }

}
