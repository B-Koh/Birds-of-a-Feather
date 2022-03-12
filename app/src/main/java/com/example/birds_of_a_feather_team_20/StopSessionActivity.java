package com.example.birds_of_a_feather_team_20;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.birds_of_a_feather_team_20.model.db.Course;
import com.example.birds_of_a_feather_team_20.model.db.CourseDatabase;
import com.example.birds_of_a_feather_team_20.model.db.DBSession;
import com.example.birds_of_a_feather_team_20.model.db.SessionDatabase;

import java.util.ArrayList;
import java.util.List;

public class StopSessionActivity extends AppCompatActivity {
    String[] current_classes;
    List<Course> myCourses;
    List<String> currCourses;
    CourseDatabase db;
    MyProfile mp;
    SessionDatabase sd;
    String newSession = "";
    String oldSession;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_session);

        setTitle("Save Your Session");

        Intent intent = getIntent();
        oldSession = intent.getStringExtra("autosave_session");

        db = CourseDatabase.singleton(this);
        mp = MyProfile.singleton(this);
        sd = SessionDatabase.singleton(this);
        myCourses = mp.getMyCourses();
        currCourses =  new ArrayList<String>();

        current_classes =  getCurrCourses(myCourses);


        Spinner curr_courses = findViewById(R.id.curr_courses_dropdown);
        ArrayAdapter<String> curr_course_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, current_classes);
        curr_course_adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        curr_courses.setAdapter(curr_course_adapter);

    }

    public void onClickSaveSession(View view) {
        if(validateName()){
            saveSession();
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        }
    }

    public String[] getCurrCourses(List<Course> myCourses) {
        currCourses.add("");
        for( int i = 0; i < myCourses.size(); i++){
            String quarter = myCourses.get(i).getSession();
            String year = String.valueOf(myCourses.get(i).getYear());
            String department = myCourses.get(i).getDepartment();
            String courseNum = myCourses.get(i).getCourseNumber();
            if(quarter.equals("WI") && year.equals("2022")) {
                String courseName = department + " " + courseNum;
                currCourses.add(courseName);
                Log.i(this.getClass().getSimpleName(), "Current Course Added");
            }
        }
        String[] current_courses = new String[currCourses.size()];
//        for(int i = 0; i < currCourses.size(); i++) {
//            current_classes[i] = currCourses.get(i);
//        }
        current_courses = currCourses.toArray(current_courses);

        return current_courses;
    }

    private void saveSession() {
        sd.sessionDao().renameSession(oldSession, newSession);
//        DBSession oldDB = sd.sessionDao().getSession(oldSession);
//        DBSession newDB = sd.sessionDao().getSession(newSession);
    }

    public boolean validateName() {
        TextView sessionTextView = findViewById(R.id.enter_session);
        String sessionText = sessionTextView.getText().toString();

        Spinner courseSpinner = findViewById(R.id.curr_courses_dropdown);
        String courseSelected = courseSpinner.getSelectedItem().toString();

        if(sessionText.equals("") && courseSelected.equals("")){
            Toast.makeText(this, "Please Enter or Select a Name for Your Session", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!sessionText.equals("") && !courseSelected.equals("")) {
            Toast.makeText(this, "Only Choose One: Enter or Select Name", Toast.LENGTH_SHORT).show();
            return false;
        }


        if(!courseSelected.equals("")){
            if(sd.sessionDao().getSession(courseSelected) != null){
                Toast.makeText(this, "Session name already exists: Select Different Name", Toast.LENGTH_SHORT).show();
                return false;
            }
            newSession = courseSelected;
        } else if(!sessionText.equals("")) {
            if(sd.sessionDao().getSession(sessionText) != null){
                Toast.makeText(this, "Session name already exists: Select Different Name", Toast.LENGTH_SHORT).show();
                return false;
            }
            newSession = sessionText;
        }

        Log.i("Session Added", newSession);
        return true;
    }

    public void onClickCancelSession(View view) {
        sd.sessionDao().delete(oldSession);
        finish();
    }
}