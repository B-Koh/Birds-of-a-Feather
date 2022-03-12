package com.example.birds_of_a_feather_team_20;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    int courseSize;
    Integer[] yearList = {2022, 2021, 2020, 2019, 2018};
    String quarter;
//    String[] quarterList = {"Fall", "Winter", "Spring", "Summer Session I", "Summer Session II"};
    String[] quarterList = {"FA", "WI", "SP", "SS1", "SS2", "SSS"};
    String subject;
    String courseNumber;
    CourseDatabase db;
    MyProfile mp;
    List<Course> myCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        setTitle("Adding a course");

        Spinner year_dropdown = findViewById(R.id.year_dropdown);
        ArrayAdapter<Integer> year_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yearList);
        year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        year_dropdown.setAdapter(year_adapter);


        Spinner quarter_dropdown = findViewById(R.id.quarter_dropdown);
        ArrayAdapter<String> quarter_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, quarterList);
        quarter_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        quarter_dropdown.setAdapter(quarter_adapter);


        Spinner size_dropdown = findViewById(R.id.size_dropdown);
        ArrayAdapter<String> size_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Course.courseSizeList);
        size_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        size_dropdown.setAdapter(size_adapter);

        db = CourseDatabase.singleton(this);
        mp = MyProfile.singleton(this);

//        int newCourseNumber = db.courseDao().count() + 1;
//        TextView title = findViewById(R.id.adding_course_title);
//        title.setText("Enter Course #" + newCourseNumber);
    }


    public void onSaveExitClicked(View view) {
        Course newCourse = getCourseInfo();
        addCourse(newCourse);

//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
        Log.d("Courses", "Course Saved");
        finish();
    }

    public void onDeleteClicked(View view) {
        Course newCourse = getCourseInfo();
//        deleteCourse(newCourse);
//        Toast.makeText(this, "Course Canceled & Removed", Toast.LENGTH_SHORT).show();
        Log.d("Courses", "Course Canceled & Removed");
        finish();
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
    }

    public void onSaveAddClicked(View view) {
        Course newCourse = getCourseInfo();
        addCourse(newCourse);
        Log.d("Courses", "Course Saved, Continue Adding");
        // reload intent with blank template
//        Intent intent = new Intent(this, EditCourses.class);
//        startActivity(intent);
//        finish();
    }

    /**
     * Helper Method: Helps us save all the information entered by the User.
     */
    private Course getCourseInfo() {
        TextView subjectView = findViewById(R.id.subject_edit_text);
        subject = subjectView.getText().toString();

        TextView courseNumberView = findViewById(R.id.course_num_edit_text);
        courseNumber = courseNumberView.getText().toString();

        Spinner year_spinner = findViewById(R.id.year_dropdown);
        year = Integer.parseInt(year_spinner.getSelectedItem().toString());

        Spinner quarter_spinner = findViewById(R.id.quarter_dropdown);
        quarter = quarter_spinner.getSelectedItem().toString();

        Spinner size_spinner = findViewById(R.id.size_dropdown);
        courseSize = size_spinner.getSelectedItemPosition();

        Course currCourse = new Course(year, quarter, subject, courseNumber, courseSize);

        return currCourse;
    }

    private void addCourse(Course newCourse) {
        if (newCourse == null || newCourse.getCourseNumber() == null
                || newCourse.getCourseNumber().equals("") || newCourse.getDepartment() == null
                || newCourse.getDepartment().equals("")) {

            Toast.makeText(this, "Missing course info", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isCourseUnique(newCourse)) {
            Toast.makeText(this, "Course already added", Toast.LENGTH_SHORT).show();
            return;
        }
        db.courseDao().insert(newCourse);
        myCourses = db.courseDao().getAll();
        mp.setMyCourses(myCourses);
        Toast.makeText(this, "Course added", Toast.LENGTH_SHORT).show();
    }

    private boolean isCourseUnique(Course course) {
        return !db.courseDao().getAll().contains(course);
//        return db.courseDao().get(course.getYear(), course.getSession(), course.getDepartment(), course.getCourseNumber()) == null;
    }

    private void deleteCourse(Course newCourse) {
        if(db.courseDao().get(year, quarter, subject, courseNumber) != null) {
            db.courseDao().delete(db.courseDao().get(year, quarter, subject, courseNumber));
        }
        myCourses = db.courseDao().getAll();
        mp.setMyCourses(myCourses);
    }

}
