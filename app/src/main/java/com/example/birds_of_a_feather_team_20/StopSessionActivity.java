package com.example.birds_of_a_feather_team_20;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.birds_of_a_feather_team_20.model.db.Course;
import com.example.birds_of_a_feather_team_20.model.db.CourseDatabase;

import java.util.List;

public class StopSessionActivity extends AppCompatActivity {
    String[] current_classes;
    List<Course> myCourses;
    List<String> currCourses;
    CourseDatabase db;
    MyProfile mp;
    //SessionDatabase sd;
    String sessionName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_session);


        db = CourseDatabase.singleton(this);
        mp = MyProfile.singleton(this);
        //sd = SessionDatabase.singleton(this);
        myCourses = db.courseDao().getAll();

        getCurrCourses(myCourses);


        Spinner year_dropdown = findViewById(R.id.curr_courses_dropdown);
        ArrayAdapter<String> curr_course_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, current_classes);
        curr_course_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        year_dropdown.setAdapter(curr_course_adapter);

    }

    public void onClickSaveSession(View view) {
        if(validateName()){
            saveSession();
            finish();
        }
    }

    private void getCurrCourses(List<Course> myCourses) {
        for( int i = 0; i < myCourses.size(); i++){
            String quarter = myCourses.get(i).getSession();
            String year = String.valueOf(myCourses.get(i).getYear());
            String department = myCourses.get(i).getDepartment();
            String courseNum = myCourses.get(i).getCourseNumber();
            if(quarter.equals("WI") && year.equals("2022")) {
                String courseName = department + " " + courseNum;
                currCourses.add(courseName);
            }
        }
        current_classes = new String[currCourses.size()];
        for(int i = 0; i < currCourses.size(); i++) {
            current_classes[i] = currCourses.get(i);
        }
    }

    private void saveSession() {
        //save session using Session Database insert method
        //need to pass in SessionId with intent
        //use SessionId to setName of already existing session (with date/time)
        //DBSession currSession = DBSession(sessionName);
        //sd.sessionDao().insert(sessionName);
        //OR
        //sd.sessionDao().setSessionName(sessionName);
    }

    private boolean validateName() {
        TextView sessionTextView = findViewById(R.id.enter_session);
        String sessionText = sessionTextView.getText().toString();

        Spinner courseSpinner = findViewById(R.id.quarter_dropdown);
        String courseSelected = courseSpinner.getSelectedItem().toString();

        if(sessionText.equals("") && courseSelected.equals("")){
            Toast.makeText(this, "Please Enter or Select a Name for Your Session", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!sessionText.equals("") && !courseSelected.equals("")) {
            Toast.makeText(this, "Only Choose One: Enter or Select Name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!courseSelected.equals("")){
            sessionName = courseSelected;
        } else if(!sessionText.equals("")) {
            sessionName = sessionText;
        }

        return true;
    }
}
